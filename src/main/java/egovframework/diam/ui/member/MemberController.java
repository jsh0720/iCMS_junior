/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.ui.member;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.jam.mutable.MSourcePosition;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.diam.biz.db.member.MemberMapper;
import egovframework.diam.biz.model.member.Dm_member_vo;
import egovframework.diam.biz.model.member.Dm_member_vo.MemberAdminGroup;
import egovframework.diam.biz.service.member.MemberService;
import egovframework.diam.cmm.model.LoginVO;
import egovframework.diam.cmm.service.CommonService;
import egovframework.diam.cmm.util.CommonUtil;
import egovframework.diam.cmm.util.EgovUserDetailsHelper;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;

/**
 * @Class Name : MemberController.java
 * @Description : 관리자페이지 일반회원 관리 CRUD기능구현 Controller클래스
 * @author 디자인아이엠 개발팀 김민성
 * @since 2022.07.13
 * @version 1.0
 */

@Log4j2
@Controller
public class MemberController {
	
	@Resource(name="memberService")
	private MemberService memberService;
	
	@Resource(name="commonService")
	private CommonService commonService;
		
	/**
	 * get_member_list
	 * 전달받은 검색조건 데이터로 조회한 일반회원 판리스트 데이터를 화면에 전달
	 * @param memberVO 일반회원 데이터 검색조건을 전달받는 vo객체
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/get_member_list.do")
	public ResponseEntity<Object> get_member_list(Dm_member_vo memberVO) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		
		int row = memberVO.getRows() != 0 ? memberVO.getRows() : 50;
		int page = memberVO.getPage() != 0 ? memberVO.getPage() : 1;
		
		if (row < 1 || page < 0) {
			log.error(MessageCode.CMM_REQUEST_BADREQUEST.getLog());
			resultMap.put("notice", MessageCode.CMM_REQUEST_BADREQUEST.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
		} else {
			memberVO.setRows(row);
			memberVO.setPage(row * (page -1));
		}

		try {
			int memberCnt = memberService.selectMemberCnt(memberVO);
			List<Dm_member_vo> memberList = memberService.selectMemberList(memberVO);
			
			resultMap.put("result", "success");
			resultMap.put("total", memberCnt);
			resultMap.put("rows", memberList);
			resultMap.put("notice", MessageCode.CMS_SELECT_SUCCESS.getMessage());				

		} catch(DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			resultMap.put("notice", MessageCode.CMM_DATA_ERROR.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch(Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			resultMap.put("notice", MessageCode.CMM_SYSTEM_ERROR.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(resultMap, HttpStatus.OK);
	}		
	
	/**
	 * get_member
	 * 전달받은 일반회원PK 데이터로 조회한 1건의 일반회원 데이터를 화면에 전달
	 * @param memberVO 일반회원 데이터 PK데이터를 전달받는 vo객체
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/get_member.do")
	public ResponseEntity<Object> get_member(Dm_member_vo memberVO) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		
		try {
			if (!commonUtil.isNullOrEmpty(memberVO.getDm_no())) {
				memberVO = memberService.selectMember(memberVO);
				if (memberVO != null) {
					resultMap.put("result", "success");
					resultMap.put("rows", memberVO);
				} else {
					resultMap.put("result", "fail");
					resultMap.put("notice", MessageCode.CMS_SELECT_NODATA.getMessage());
				}				
				
			} else {
				log.error(MessageCode.CMM_REQUEST_BADREQUEST.getLog());
				resultMap.put("notice", MessageCode.CMM_REQUEST_BADREQUEST.getMessage());
				return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
			}
			
		} catch(DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			resultMap.put("notice", MessageCode.CMM_DATA_ERROR.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch(Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			resultMap.put("notice", MessageCode.CMM_SYSTEM_ERROR.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(resultMap, HttpStatus.OK);
	}
	
	/**
	 * delete_member
	 * request 객체를 통하여 전달받은 일반회원 PK배열에 해당하는 일반회원 데이터 삭제
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/delete_member.do")
	public ResponseEntity<Object> delete_member(@RequestParam("dm_no[]") String ids[]) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		
		try {
			if (commonUtil.isNullOrEmpty(loginVO.getId())) {
				resultMap.put("notice", MessageCode.CMM_SESSION_EXPIRED.getMessage());
				return new ResponseEntity<>(resultMap, HttpStatus.UNAUTHORIZED);
			}
			
			if (ids.length < 1) {
				resultMap.put("notice", MessageCode.CMM_REQUEST_BADREQUEST.getMessage());
				return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);				
			}
			List<Dm_member_vo> list = new ArrayList<>();
			Arrays.asList(ids).forEach(item -> {
				Dm_member_vo vo = Dm_member_vo.builder()
						.dm_no(item)
						.build();
				list.add(vo);
			});
			
			memberService.deleteMember(list);
			
			resultMap.put("result", "success");
			resultMap.put("notice", MessageCode.CMS_DELETE_SUCCESS.getMessage());	
		} catch (DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			resultMap.put("notice", MessageCode.CMM_DATA_ERROR.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (RuntimeException rte) {
			log.error(MessageCode.CMM_TRANSACTION_FAIL.getLog());
			resultMap.put("notice", MessageCode.CMM_TRANSACTION_FAIL.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);			
		} catch (Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			resultMap.put("notice", MessageCode.CMM_SYSTEM_ERROR.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(resultMap, HttpStatus.OK);
		
	}
	
	/**
	 * kick_member
	 * request 객체를 통하여 전달받은 일반회원 PK배열에 해당하는 일반회원 데이터 탈퇴처리
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/kick_member.do")
	public ResponseEntity<Object> kick_member(@RequestParam("dm_no[]") String ids[]) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		
		try {
			if (commonUtil.isNullOrEmpty(loginVO.getId())) {
				resultMap.put("notice", MessageCode.CMM_SESSION_EXPIRED.getMessage());
				return new ResponseEntity<>(resultMap, HttpStatus.UNAUTHORIZED);
			}
			
			if (ids.length < 1) {
				resultMap.put("notice", MessageCode.CMM_REQUEST_BADREQUEST.getMessage());
				return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);				
			}
			List<Dm_member_vo> list = new ArrayList<>();
			Arrays.asList(ids).forEach(item -> {
				Dm_member_vo vo = Dm_member_vo.builder()
						.dm_modify_id(loginVO.getId())
						.dm_no(item)
						.build();
				list.add(vo);
			});
			memberService.kickMember(list);
			resultMap.put("result", "success");
			resultMap.put("notice", MessageCode.CMS_UPDATE_SUCCESS.getMessage());
		} catch (DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			resultMap.put("notice", MessageCode.CMM_DATA_ERROR.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (RuntimeException rte) {
			log.error(MessageCode.CMM_TRANSACTION_FAIL.getLog());
			resultMap.put("notice", MessageCode.CMM_TRANSACTION_FAIL.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);			
		} catch (Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			resultMap.put("notice", MessageCode.CMM_SYSTEM_ERROR.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(resultMap, HttpStatus.OK);			
	}
	
	@RequestMapping("/adm/chage_member_status.do")
	public ResponseEntity<Object> chage_member_status(Dm_member_vo memberVO) {
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		
		try {
			if (commonUtil.isNullOrEmpty(loginVO.getId())) {
				resultMap.put("notice", MessageCode.CMM_SESSION_EXPIRED.getMessage());
				return new ResponseEntity<>(resultMap, HttpStatus.UNAUTHORIZED);
			}
			if(memberVO != null) {
				if (memberVO.getDm_no().equals(loginVO.getDm_no()) && memberVO.getDm_status().equals("J")) {
					resultMap.put("result", "fail");
					resultMap.put("notice", "현재 로그인중인 계정은 탈퇴시킬 수 없습니다.");
					return ResponseEntity.ok(resultMap);
				}
				
				memberVO.setDm_modify_id(loginVO.getId());
				int result = memberService.chageMemberStatus(memberVO);
				if(result > 0) {
					resultMap.put("result", "success");
					resultMap.put("notice", MessageCode.CMS_UPDATE_SUCCESS.getMessage());
				} else {
					resultMap.put("result", "fail");
					resultMap.put("notice", MessageCode.CMS_UPDATE_FAIL.getMessage());
				}
			} else {
				resultMap.put("result", "fail");
				resultMap.put("notice", MessageCode.CMS_SELECT_NODATA.getMessage());
			}
		} catch (DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			resultMap.put("notice", MessageCode.CMM_DATA_ERROR.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			resultMap.put("notice", MessageCode.CMM_SYSTEM_ERROR.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(resultMap, HttpStatus.OK);
	}

	/**
	 * get_member_excel
	 * 일반/탈퇴회원을 구분하는 mode 값에따라 일반/회원 데이터 전체조회 후 엑셀로 다운로드
	 * @param mode 일반/탈퇴회원을 구분값을 전달받는 문자열
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/get_member_excel.do")
	public ResponseEntity<?> get_member_excel(@RequestBody Dm_member_vo memberVO, HttpServletRequest request) {
		CommonUtil commonUtil = new CommonUtil();
		List<Dm_member_vo> memberList = new ArrayList<Dm_member_vo>();
		Map<String, String> resultMap = new HashMap<>();
		String[] headerList = {"번호","아이디","이름","이메일","권한","전화번호","휴대전화","우편번호","주소","가입일","접속아이피","탈퇴일"};
		String excel_title = "";
		try {
			memberList = memberService.selectMemberList(memberVO);
			excel_title = "회원";

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(excel_title + "리스트");
			XSSFRow row = sheet.createRow(0);
			XSSFCell cell = null;
			CellStyle style = workbook.createCellStyle();
			style.setBorderRight(XSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			style.setBorderTop(XSSFCellStyle.BORDER_THIN);
			style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
						
			for (int i = 0 ; i < headerList.length ; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(style);
				cell.setCellValue(headerList[i]);
				//sheet.autoSizeColumn(i);
			}
			
			for (int i = 0 ; i < memberList.size() ; i++) {
				row = sheet.createRow(i+1);
				
				int cellCnt = 0 ;
				cell = row.createCell(cellCnt);
				cell.setCellStyle(style);
				cellCnt++;
				cell.setCellValue(i+1);
				
				cell = row.createCell(cellCnt);
				cell.setCellStyle(style);
				cellCnt++;
				cell.setCellValue(memberList.get(i).getDm_id());
				
				cell = row.createCell(cellCnt);
				cell.setCellStyle(style);
				cellCnt++;
				cell.setCellValue(memberList.get(i).getDm_name());
											
				cell = row.createCell(cellCnt);
				cell.setCellStyle(style);
				cellCnt++;
				cell.setCellValue(memberList.get(i).getDm_email());
										
				cell = row.createCell(cellCnt);
				cell.setCellStyle(style);
				cellCnt++;
				cell.setCellValue(memberList.get(i).getDm_level_text());
							
				cell = row.createCell(cellCnt);
				cell.setCellStyle(style);
				cellCnt++;
				cell.setCellValue(memberList.get(i).getDm_tel());
				
				cell = row.createCell(cellCnt);
				cell.setCellStyle(style);
				cellCnt++;
				cell.setCellValue(memberList.get(i).getDm_hp());
								
				cell = row.createCell(cellCnt);
				cell.setCellStyle(style);
				cellCnt++;
				cell.setCellValue(memberList.get(i).getDm_zip());
						
				cell = row.createCell(cellCnt);
				cell.setCellStyle(style);
				cellCnt++;
				String value = commonUtil.isNullOrEmpty(memberList.get(i).getDm_addr1()) ? "" : memberList.get(i).getDm_addr1() + " ";
				value += commonUtil.isNullOrEmpty(memberList.get(i).getDm_addr2()) ? "" : memberList.get(i).getDm_addr2() + " ";
				value += commonUtil.isNullOrEmpty(memberList.get(i).getDm_addr3()) ? "" : memberList.get(i).getDm_addr3();
				cell.setCellValue(value);
								
				cell = row.createCell(cellCnt);
				cell.setCellStyle(style);
				cellCnt++;
				cell.setCellValue(memberList.get(i).getDm_datetime());
				
				cell = row.createCell(cellCnt);
				cell.setCellStyle(style);
				cellCnt++;
				cell.setCellValue(memberList.get(i).getDm_ip());
							
				cell = row.createCell(cellCnt);
				cell.setCellStyle(style);
				cellCnt++;
				cell.setCellValue(memberList.get(i).getDm_leave_date());					
			}
						
			for (int i = 0 ; i < headerList.length ; i++) {
				sheet.autoSizeColumn(i);
				sheet.setColumnWidth(i, (sheet.getColumnWidth(i)+1024));
			}
			
			String path = request.getServletContext().getRealPath("/resources/excel/");
			String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss"));
			String filename = excel_title+"목록_"+time + ".xlsx";
			File folder = new File(path);
			if (!folder.exists()) {
				folder.mkdirs();			
			}
			File save = new File(path + filename);
			FileOutputStream fos = new FileOutputStream(save);
			workbook.write(fos);
			if (save.exists()) {
				resultMap.put("result", "success");
				resultMap.put("rows", "/resources/excel/" + filename);
			} else {
				throw new FileNotFoundException();
			}
			
		} catch(FileNotFoundException e) {
			log.error("File not found Exception");
			resultMap.put("notice", "엑셀 파일을 찾을 수 없습니다.");
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch(IOException ioe) {
			log.error(MessageCode.CMM_FILE_ERROR.getLog());
			resultMap.put("notice", MessageCode.CMM_FILE_ERROR.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch(DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			resultMap.put("notice", MessageCode.CMM_DATA_ERROR.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch(Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			resultMap.put("notice", MessageCode.CMM_SYSTEM_ERROR.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(resultMap, HttpStatus.OK);
	}	
	
	/**
	 * set_member
	 * 사용자가 입력한 일반회원 데이터의 insert/update 동작 수행
	 * @param type 입력 폼에서 전달받는 insert/update 수행명령값
	 * @param memberVO 사용자가 입력한 일반회원 데이터를 전달받는 vo객체
	 * @param br vo객체의 hibernate validator 검증결과를 return 하기위한 객체
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 BindingResult객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/set_member.do")
	public ResponseEntity<Object> set_member(@Validated(MemberAdminGroup.class) Dm_member_vo memberVO, BindingResult br,
			HttpServletRequest request) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		HttpSession session = request.getSession();
		
		if (commonUtil.isNullOrEmpty(loginVO.getId())) {
			resultMap.put("notice", MessageCode.CMM_SESSION_EXPIRED.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
		}
		
		if(br.hasErrors()) {
			List<FieldError> errors = br.getFieldErrors();
			String msg = errors.get(0).getDefaultMessage();
			
			resultMap.put("result", "fail");
			resultMap.put("notice", msg);
			return new ResponseEntity<>(resultMap, HttpStatus.OK);
		}		
		String dm_id = memberVO.getDm_id();		
		String dm_password = memberVO.getDm_password();			
			
		try {
			Dm_member_vo checkVO = new Dm_member_vo();
						
			if (dm_password != null && !"".equals(dm_password)) {
				PrivateKey privateKey = (PrivateKey) session.getAttribute("DIAM_MEMBER_RSA_KEY");
				dm_password = commonUtil.decryptRsa(privateKey, dm_password);		
				
				String password_pattern = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,}$";
				Pattern pattern_chk = Pattern.compile(password_pattern);
				Matcher matcher = pattern_chk.matcher(dm_password);
				boolean pattern_result = matcher.find();
				
				if (!pattern_result) {
					resultMap.put("result", "fail");
					resultMap.put("notice", "패스워드 형식에 맞지않습니다. 공백(스페이스바)을 제외한 영문자,숫자,특수문자를 1개이상 포함하여 8자 이상 입력해주세요.");
					return new ResponseEntity<>(resultMap, HttpStatus.OK);
				} else {
					memberVO.setDm_password(commonUtil.encryptPassword(dm_password, dm_id));
				}
			} else {
				if (commonUtil.isNullOrEmpty(memberVO.getDm_no())) {
					resultMap.put("result", "fail");
					resultMap.put("notice", "패스워드를 입력해주세요.");
					return new ResponseEntity<>(resultMap, HttpStatus.OK);
				}
			}
					
			memberVO.setDm_status("J");
			memberVO.setUser_se("USR");
			memberVO.setDm_ip(request.getRemoteAddr());
			memberVO.setDm_about_me(commonUtil.xssSaxFiltering(memberVO.getDm_about_me()));
			
			if (commonUtil.isNullOrEmpty(memberVO.getDm_no())) {
				checkVO.setDm_id(memberVO.getDm_id());
				checkVO = memberService.selectMemberIdDupChk(checkVO);
				if (checkVO != null) {
					resultMap.put("result", "fail");
					resultMap.put("notice", "중복된 아이디입니다. 다른 아이디를 입력해주세요.");	
				} else {
					memberVO.setDm_create_id(loginVO.getId());
					memberService.insertMember(memberVO);
					session.removeAttribute("DIAM_MEMBER_RSA_KEY");
					resultMap.put("result", "success");
					resultMap.put("notice", MessageCode.CMS_INSERT_SUCCESS.getMessage());
				}				
			} else if (!commonUtil.isNullOrEmpty(memberVO.getDm_no())) {
				checkVO.setDm_no(memberVO.getDm_no());
				checkVO = memberService.selectMember(checkVO);
				
				if (checkVO != null) {
					if (Integer.parseInt(checkVO.getDm_level()) > 5) {
						resultMap.put("result", "fail");
						resultMap.put("notice", "일반회원 정보만 수정가능합니다.");	
					} else {
						if (checkVO.getDm_password().equals(memberVO.getDm_password())) {
							resultMap.put("result", "fail");
							resultMap.put("notice", "이전과 동일한 비밀번호로는 변경불가합니다. 이전 비밀번호와 다른 비밀번호를 입력해주세요.");
						} else {
							memberVO.setDm_modify_id(loginVO.getId());
							memberService.updateMember(memberVO);					
							session.removeAttribute("DIAM_MEMBER_RSA_KEY");
							resultMap.put("result", "success");
							resultMap.put("notice", MessageCode.CMS_UPDATE_SUCCESS.getMessage());
						}
					}				
				} else {
					resultMap.put("result", "fail");
					resultMap.put("notice", MessageCode.CMS_SELECT_NODATA.getMessage());	
				}		
			} else {
				resultMap.put("result", "fail");
				resultMap.put("notice", MessageCode.CMM_REQUEST_BADREQUEST.getMessage());
			}			
		} catch(InvalidKeyException ike) {
			log.error(MessageCode.CMM_ENCRYPT_EXPIRED.getLog());
			resultMap.put("notice", MessageCode.CMM_ENCRYPT_EXPIRED.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch(BadPaddingException bpe) {
			log.error(MessageCode.CMM_ENCRYPT_EXPIRED.getLog());
			resultMap.put("notice", MessageCode.CMM_ENCRYPT_EXPIRED.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			resultMap.put("notice", MessageCode.CMM_DATA_ERROR.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			resultMap.put("notice", MessageCode.CMM_SYSTEM_ERROR.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(resultMap, HttpStatus.OK);
	}
}
