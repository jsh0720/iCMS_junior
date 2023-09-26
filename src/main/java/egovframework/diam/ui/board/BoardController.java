/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.ui.board;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import egovframework.diam.biz.model.board.Dm_board_vo;
import egovframework.diam.biz.model.config.Dm_domain_list_vo;
import egovframework.diam.biz.service.board.BoardService;
import egovframework.diam.biz.service.board.WriteService;
import egovframework.diam.biz.service.config.DomainService;
import egovframework.diam.cmm.model.LoginVO;
import egovframework.diam.cmm.service.CommonService;
import egovframework.diam.cmm.util.CommonUtil;
import egovframework.diam.cmm.util.EgovUserDetailsHelper;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;

/**
 * @Class Name : WriteController.java
 * @Description : 관리자페이지 게시판 관리 CRUD기능구현 Controller클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
 */

@Log4j2
@Controller
public class BoardController {
	@Resource(name="boardService") private BoardService boardService;
	@Resource(name="commonService") private CommonService commonService;
	@Resource(name="writeService") private WriteService writeService;	
	@Resource(name="domainService") private DomainService domainService;
	
	/**
	 * getBoardList
	 * 전달받은 검색조건 데이터로 조회한 게시판리스트 데이터를 화면에 전달
	 * @param boardVO 게시판데이터 검색조건을 전달받는 vo객체
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/get_board_list.do")
	public ResponseEntity<Object> getBoardList(Dm_board_vo boardVO) {
		Map<String, Object> resultMap = new HashMap<>();
		
		int row = boardVO.getRows() != 0 ? boardVO.getRows() : 50;
		int page = boardVO.getPage() != 0 ? boardVO.getPage() : 1;
		
		if (row < 1 || page < 0) {
			log.error(MessageCode.CMM_REQUEST_BADREQUEST.getLog());
			resultMap.put("notice", MessageCode.CMM_REQUEST_BADREQUEST.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
		} else {
			boardVO.setRows(row);
			boardVO.setPage(row * (page -1));
		}
		
		try {
			int boardListCnt = boardService.selectBoardListCnt(boardVO);
			List<Dm_board_vo> boardList = boardService.selectBoardList(boardVO);
			
			if (boardList.size() > 0) {
				boardList.forEach(item -> {
					item.setDm_main_use(item.getDm_main_use().equals("1") ? "사용" : "미사용");
				});
			}
			
			resultMap.put("result", "success");
			resultMap.put("total", boardListCnt);
			resultMap.put("rows", boardList);
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
	 * getBoard
	 * 전달받은 게시판PK 데이터로 조회한 1건의 게시판데이터를 화면에 전달
	 * @param boardVO 게시판데이터 PK데이터를 전달받는 vo객체
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/get_board.do")
	public ResponseEntity<Object> getBoard(Dm_board_vo boardVO) {
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
			boardVO = boardService.selectBoard(boardVO);
			if (boardVO != null) {
				boardVO.setDm_list_group_arr(groupStringToArr(boardVO.getDm_list_group()));
				boardVO.setDm_write_group_arr(groupStringToArr(boardVO.getDm_write_group()));
				boardVO.setDm_read_group_arr(groupStringToArr(boardVO.getDm_read_group()));
				boardVO.setDm_comment_group_arr(groupStringToArr(boardVO.getDm_comment_group()));
				boardVO.setDm_reply_group_arr(groupStringToArr(boardVO.getDm_reply_group()));
				boardVO.setDm_link_group_arr(groupStringToArr(boardVO.getDm_link_group()));
				boardVO.setDm_upload_group_arr(groupStringToArr(boardVO.getDm_upload_group()));
				
				resultMap.put("result", "success");
				resultMap.put("rows", boardVO);
			} else {
				resultMap.put("result", "fail");
				resultMap.put("notice", MessageCode.CMS_SELECT_NODATA.getMessage());
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
	 * setBoard
	 * 사용자가 입력한 게시판데이터의 insert/update 동작 수행
	 * @param type 입력 폼에서 전달받는 insert/update 수행명령값
	 * @param boardVO 사용자가 입력한 게시판데이터를 전달받는 vo객체
	 * @param br vo객체의 hibernate validator 검증결과를 return 하기위한 객체
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/set_board.do")
	public ResponseEntity<Object> setBoard(@Valid Dm_board_vo boardVO, BindingResult br, HttpServletRequest request) {
		
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		
		if(br.hasErrors()) {
			List<FieldError> errors = br.getFieldErrors();
			String msg = errors.get(0).getDefaultMessage();
			
			resultMap.put("result", "fail");
			resultMap.put("notice", msg);
			return new ResponseEntity<>(resultMap, HttpStatus.OK);
		}
		
		String icon_path = request.getServletContext().getRealPath("/resources/board/") + boardVO.getDm_table() + "/icon/" ;
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

		if (commonUtil.isNullOrEmpty(loginVO.getId())) {
			resultMap.put("notice", MessageCode.CMM_SESSION_EXPIRED.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.UNAUTHORIZED);
		}
				
		Dm_board_vo checkVO = new Dm_board_vo();
		try {
			File folder = new File(icon_path);
			if (!folder.exists()) {
				folder.mkdirs();					
			}
			
			boardVO.setDm_list_group(groupArrToString(boardVO.getDm_list_group_arr()));
			boardVO.setDm_read_group(groupArrToString(boardVO.getDm_read_group_arr()));
			boardVO.setDm_write_group(groupArrToString(boardVO.getDm_write_group_arr()));
			boardVO.setDm_reply_group(groupArrToString(boardVO.getDm_reply_group_arr()));
			boardVO.setDm_comment_group(groupArrToString(boardVO.getDm_comment_group_arr()));
			boardVO.setDm_link_group(groupArrToString(boardVO.getDm_link_group_arr()));
			boardVO.setDm_upload_group(groupArrToString(boardVO.getDm_upload_group_arr()));
			
			boardVO.setDm_basic_content(commonUtil.xssSaxFiltering(boardVO.getDm_basic_content()));
			boardVO.setDm_header_content(commonUtil.xssSaxFiltering(boardVO.getDm_header_content()));
			boardVO.setDm_footer_content(commonUtil.xssSaxFiltering(boardVO.getDm_footer_content()));
			
			String today_str = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
					
			Dm_domain_list_vo domainVO = new Dm_domain_list_vo();
			domainVO.setDm_id(boardVO.getDm_domain());
			domainVO = domainService.selectDomainByDmid(domainVO);
			
			if (domainVO != null) {
				
				if (!commonUtil.isNullOrEmpty(boardVO.getDm_id())) {
					checkVO.setDm_id(boardVO.getDm_id());
					checkVO = boardService.selectBoard(checkVO);
					if (checkVO != null) {
						boolean hit_icon_ext_chk = commonUtil.imageExtCheck(boardVO.getDm_hit_icon_file());
						boolean new_icon_ext_chk = commonUtil.imageExtCheck(boardVO.getDm_new_icon_file());
						
						if (hit_icon_ext_chk && new_icon_ext_chk) {
							boardVO.setDm_hit_icon(upload_icon_file(request, checkVO, boardVO.getDm_del_hit(), boardVO.getDm_hit_icon_file(), icon_path, today_str, "hit"));
							boardVO.setDm_new_icon(upload_icon_file(request, checkVO, boardVO.getDm_del_new(), boardVO.getDm_new_icon_file(), icon_path, today_str, "new"));
							boardVO.setDm_modify_id(loginVO.getId());
							int result = boardService.updateBoard(boardVO);
							if (result > 0) {
								resultMap.put("result", "success");
								resultMap.put("notice", MessageCode.CMS_UPDATE_SUCCESS.getMessage());									
							} else {
								resultMap.put("result", "fail");
								resultMap.put("notice", MessageCode.CMS_UPDATE_FAIL.getMessage());
							}
						} else {
							resultMap.put("result", "fail");
							resultMap.put("notice", "hit/new 아이콘은 jpg,jpeg,gif,png 확장자 파일만 업로드 가능합니다.");
						}
					} else {
						resultMap.put("result", "fail");
						resultMap.put("notice", MessageCode.CMS_SELECT_NODATA.getMessage());
					}
				} else {
					boolean hit_icon_ext_chk = commonUtil.imageExtCheck(boardVO.getDm_hit_icon_file());
					boolean new_icon_ext_chk = commonUtil.imageExtCheck(boardVO.getDm_new_icon_file());
					
					if (hit_icon_ext_chk && new_icon_ext_chk) {
						boardVO.setDm_hit_icon(upload_icon_file(request, checkVO, boardVO.getDm_del_hit(), boardVO.getDm_hit_icon_file(), icon_path, today_str, "hit"));
						boardVO.setDm_new_icon(upload_icon_file(request, checkVO, boardVO.getDm_del_new(), boardVO.getDm_new_icon_file(), icon_path, today_str, "new"));
						
						Dm_board_vo dupVO = boardService.selectBoardByDmtable(boardVO);
						
						if (dupVO != null) {
							resultMap.put("result",  "duplicate");
							resultMap.put("notice", "동일한 게시판 아이디가 존재합니다.");
						} else {
							boardVO.setDm_create_id(loginVO.getId());
							int result = boardService.insertBoard(boardVO);
							if (result > 0) {
								resultMap.put("result", "success");
								resultMap.put("notice", MessageCode.CMS_INSERT_SUCCESS.getMessage());
							} else {
								resultMap.put("result", "success");
								resultMap.put("notice", MessageCode.CMS_INSERT_FAIL.getMessage());								
							}
						}
					} else {
						resultMap.put("result", "fail");
						resultMap.put("notice", "hit/new 아이콘은 jpg,jpeg,gif,png 확장자 파일만 업로드 가능합니다.");
					}
				}
			} else {
				resultMap.put("result", "fail");
				resultMap.put("notice", "도메인 정보가 없습니다.");
			}								
		} catch (IOException ioe) {
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
	 * delete_board
	 * request 객체를 통하여 전달받은 게시판 PK배열에 해당하는 게시판데이터 삭제
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/delete_board.do")
	public ResponseEntity<Object> delete_board(@RequestParam("dm_id[]") String ids[]) {
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
			List<Dm_board_vo> list = new ArrayList<>();
			Arrays.asList(ids).forEach(item -> {
				Dm_board_vo vo = new Dm_board_vo();
				vo.setDm_id(item);
				vo.setDm_delete_id(loginVO.getId());
				list.add(vo);
			});
			
			boardService.deleteBoard(list);
			resultMap.put("result", "success");
			resultMap.put("notice", MessageCode.CMS_DELETE_SUCCESS.getMessage());

		} catch (DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			resultMap.put("notice", MessageCode.CMM_DATA_ERROR.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (RuntimeException rte) {
			log.error(MessageCode.CMS_DELETE_FAIL.getLog());
			resultMap.put("notice", MessageCode.CMS_DELETE_FAIL.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			resultMap.put("notice", MessageCode.CMM_SYSTEM_ERROR.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(resultMap, HttpStatus.OK);
	}
	
	/**
	 * get_board_icon_download
	 * 게시물에 첨부한 첨부파일 다운로드 수행
	 * @param file_name 파일다운로드 시 참조할 첨부파일의 임시파일명을 전달받는 문자열
	 * @param ori_file_name 파일다운로드 시 다운로드파일명이 될 원파일명을 전달받는 문자열
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	*/
	@RequestMapping("/adm/get_board_icon_download.do")
	public void get_board_icon_download(
			@RequestParam(value="file_name", required=true) String file_name, @RequestParam(value="ori_file_name", required=true) String ori_file_name,
			HttpServletRequest request, HttpServletResponse response) {
		CommonUtil commonUtil = new CommonUtil();
		String file_path = request.getServletContext().getRealPath("/") + file_name;
		File file = new File(file_path);
		try {
			if (file.exists() && file.isFile()) {
				response.setContentType("application/octet-stream; charset=utf-8");
				response.setContentLength((int) file.length()); 
				String browser = commonUtil.getBrowser(request);
				String disposition = commonUtil.getDisposition(ori_file_name, browser);
				response.setHeader("Content-Disposition", disposition);
				response.setHeader("Content-Transfer-Encoding", "binary");
				
				OutputStream out = response.getOutputStream();
				FileInputStream fis = null;
				fis = new FileInputStream(file);
				FileCopyUtils.copy(fis, out);
				if (fis != null)
					fis.close();
				out.flush();
				out.close();
			} else {
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter print_out = response.getWriter();
				print_out.println("<script>alert('해당하는 파일이 없습니다.'); history.go(-1); </script>");
				print_out.flush();
				print_out.close();
			}
		} catch(IOException ioe) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter print_out = null;
			try {
				print_out = response.getWriter();
				print_out.println("<script>alert('해당하는 파일이 없거나 다운로드 중 오류가 발생하였습니다.'); history.go(-1); </script>");
				print_out.flush();
				print_out.close();
			} catch (IOException e) {
				log.error(MessageCode.CMM_FILE_ERROR.getLog());
			} finally {
				print_out.close();
			}
		} catch(Exception e) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter print_out = null;
			try {
				print_out = response.getWriter();
				print_out.println("<script>alert('오류가 발생하였습니다. 관리자에게 문의주세요.'); history.go(-1); </script>");
				print_out.flush();
				print_out.close();
			} catch (IOException e1) {
				log.error(MessageCode.CMM_FILE_ERROR.getLog());
			} finally {
				print_out.close();
			}
		}	
	}	
	
	/**
	 * upload_icon_file
	 * insert/update 시 사용자가 등록한 아이콘 파일 업로드 수행
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param checkVO 아이콘 업로드를 수행하기 위해 필요한 게시판정보를 전달받는 vo객체 
	 * @param del_yn 아이콘의 삭제여부를 전달받는 문자열
	 * @param icon_file 업로드한 아이콘파일을 전달받는 MultipartFile 객체
	 * @param icon_path 아이콘이 저장된 경로를 전달받는 문자열
	 * @param today 금일 날짜를 전달받는 문자열
	 * @param type 아이콘의 종류를 전달받는 문자열
	 * @return String 파일 업로드 프로세스 후 경로+파일명을 연결하여 문자열로 return
	*/
	private String upload_icon_file(HttpServletRequest request,	Dm_board_vo checkVO, String del_yn, MultipartFile icon_file, String icon_path, String today, String type) throws Exception{
		CommonUtil commonUtil = new CommonUtil();
		String result = "";
		if ("1".equals(del_yn)) {
			File del_icon_file = null;
			if ("new".equals(type)) {
				del_icon_file = new File(request.getServletContext().getRealPath("") + checkVO.getDm_new_icon());
			} else {
				del_icon_file = new File(request.getServletContext().getRealPath("") + checkVO.getDm_hit_icon());
			}
			
			if (del_icon_file.exists() && del_icon_file.isFile()) {
				FileDelete(del_icon_file);
			}
			
			if (icon_file != null && icon_file.getSize() > 0) {
				String original_file_name = commonUtil.uploadFileNameFiltering(icon_file.getOriginalFilename());
				String upload_name = today + "_" + original_file_name;
				String upload_path = icon_path + upload_name;
				icon_file.transferTo(new File(upload_path));
				result = "/resources/board/" + checkVO.getDm_table() + "/icon/" + upload_name;	
			}
		} else {
			if (icon_file != null && icon_file.getSize() > 0) {
				String original_file_name = commonUtil.uploadFileNameFiltering(icon_file.getOriginalFilename());
				String upload_name = today + "_" + original_file_name;
				String upload_path = icon_path + upload_name;
				icon_file.transferTo(new File(upload_path));
				result = "/resources/board/" + checkVO.getDm_table() + "/icon/" + upload_name;	
			} else {
				if ("new".equals(type)) {
					result = checkVO.getDm_new_icon();
				} else {
					result = checkVO.getDm_hit_icon();
				}
			}
		}				
		return result;
	}
	
	/**
	 * groupArrToString
	 * List 자료형에 담긴 게시판 그룹권한 정보를 문자열로 변환
	 * @param str_arr 그릅권한을 문자열로 변환하기 위해 그룹권한 정보를 List 자료형으로 전달
	 * @return String 게시판 그룹권한 정보를 문자열로 return
	*/
	private String groupArrToString(List<String> arr) {
		String returnStr = "";
		if (arr != null && arr.size() > 0) {
			returnStr = String.join("|", arr);
		}
		return returnStr;
	}
	
	/**
	 * groupStringToArr
	 * String 문자열에 담긴 그룹권한 정보를 List 자료형으로 변환
	 * @param str 그릅권한을 List 자료형으로 변환하기 위해 그룹권한 정보를 String 자료형으로 전달
	 * @return List<String> 게시판 그룹권한 정보를  List 자료형으로 return
	*/
	private List<String> groupStringToArr(String str) {
		List<String> returnStr = new ArrayList<>();
		if (str != null && !"".equals(str)) {
			returnStr = Arrays.asList(str.split("\\|"));			
		}		
		return returnStr;
	}
	
	/**
	 * FileDelete
	 * File 객체에 담긴 파일을 삭제
	 * @param file 삭제할 파일정보를 File 객체로 전달
	 * @return void 전달받은 File 객체의 파일데이터 삭제만 수행
	*/
	private synchronized void FileDelete(File file) {
		file.delete();
	}
}
