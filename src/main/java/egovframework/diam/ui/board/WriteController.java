/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.ui.board;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.util.JSONPObject;

import egovframework.diam.biz.model.board.Dm_board_vo;
import egovframework.diam.biz.model.board.Dm_write_vo;
import egovframework.diam.biz.service.board.BoardService;
import egovframework.diam.biz.service.board.WriteService;
import egovframework.diam.biz.service.config.ConfigService;
import egovframework.diam.biz.service.display.PageService;
import egovframework.diam.biz.service.member.MemberService;
import egovframework.diam.cmm.model.Dm_common_code_vo;
import egovframework.diam.cmm.model.LoginVO;
import egovframework.diam.cmm.util.CommonUtil;
import egovframework.diam.cmm.util.EgovUserDetailsHelper;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;

/**
 * @Class Name : WriteController.java
 * @Description : 관리자페이지 게시물 관리 CRUD기능구현 Controller클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
 */

@Log4j2
@Controller
public class WriteController {
	@Resource(name="writeService") private WriteService writeService;
	@Resource(name="boardService") private BoardService boardService;
	@Resource(name="configService") private ConfigService configService;
	@Resource(name="memberService") private MemberService memberService;
	@Resource(name="pageService") private PageService pageService;

		
	/**
	 * get_write_list
	 * 전달받은 검색조건 데이터로 조회한 게시물리스트 데이터를 화면에 전달
	 * @param writeVO 게시물데이터 검색조건을 전달받는 vo객체
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/get_write_list.do")
	public ResponseEntity<Object> get_write_list (Dm_write_vo writeVO) {
		Map<String, Object> resultMap = new HashMap<>();
		
		int row = writeVO.getRows() != 0 ? writeVO.getRows() : 50;
		int page = writeVO.getPage() != 0 ? writeVO.getPage() : 1;
		
		if (row < 1 || page < 0) {
			log.error(MessageCode.CMM_REQUEST_BADREQUEST.getLog());
			resultMap.put("notice", MessageCode.CMM_REQUEST_BADREQUEST.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
		} else {
			writeVO.setRows(row);
			writeVO.setPage(row * (page -1));
		}
		try {		
			List<Dm_write_vo> writeList = writeService.selectWriteList(writeVO);
			int writeCnt = writeService.selectWriteListCnt(writeVO);
			if (writeList.size() > 0) {
				writeList.forEach(item -> {
					if (item.getWr_is_notice() != null && item.getWr_is_notice().equals("1")) {
						item.setWr_is_notice("Y");
					} else {
						item.setWr_is_notice("N");						
					}
				});
			}
			resultMap.put("result", "success");
			resultMap.put("total", writeCnt);
			resultMap.put("rows", writeList);
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
	 * move_write
	 * request 객체를 통하여 전달받은 기존 게시판아이디/게시물PK, 이동할 게시판아이디 배열에 해당하는 게시물데이터 이동
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/move_write.do")
	public ResponseEntity<Object> move_write (@RequestBody Map<String, Object> move) {
		Map<String, Object> resultMap = new HashMap<>();
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		CommonUtil commonUtil = new CommonUtil();
		
		List<?> ids = commonUtil.isNullOrEmpty(move.get("wr_id").toString()) ? new ArrayList<>() : (List<?>) move.get("wr_id");
		String target = commonUtil.isNullOrEmpty(move.get("target").toString()) ? "" : (String) move.get("target");

		try {
			
			if (commonUtil.isNullOrEmpty(loginVO.getId())) {
				resultMap.put("notice", MessageCode.CMM_SESSION_EXPIRED.getMessage());
				return new ResponseEntity<>(resultMap, HttpStatus.UNAUTHORIZED);
			}
			
			List<Dm_write_vo> list = new ArrayList<>();
			if (ids.size() > 0 && !commonUtil.isNullOrEmpty(target)) {
				for(Object item : ids) {
					
					int check = writeService.selectWriteCommentReplyCnt(Dm_write_vo.builder().wr_id(item.toString()).build());
					
					if (check > 0) {
						resultMap.put("result", "fail");
						resultMap.put("notice", "답변/댓글이 있는 게시물은 이동할 수 없습니다.");
						return new ResponseEntity<>(resultMap, HttpStatus.OK);
					}
					
					list.add(
							Dm_write_vo.builder().wr_id(item.toString()).wr_board(target).dm_modify_id(loginVO.getId()).build()
					);
				}
				writeService.moveWrite(list);
				resultMap.put("result", "success");
				resultMap.put("notice", MessageCode.CMS_UPDATE_SUCCESS.getMessage());
			} else {
				resultMap.put("notice", MessageCode.CMM_REQUEST_BADREQUEST.getMessage());
				return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
			}
			
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
	 * delete_write
	 * request 객체를 통하여 전달받은 게시판아이디/게시물PK 배열에 해당하는 게시물데이터 삭제
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/delete_write.do")
	public ResponseEntity<Object> delete_write (@RequestParam("wr_id[]") String ids[]) {
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		
		try {
			
			if (commonUtil.isNullOrEmpty(loginVO.getId())) {
				resultMap.put("notice", MessageCode.CMM_SESSION_EXPIRED.getMessage());
				return new ResponseEntity<>(resultMap, HttpStatus.UNAUTHORIZED);
			}
			
			if (ids.length > 0) {
				List<Dm_write_vo> list = new ArrayList<>();
				for(String item : ids) {
					Dm_write_vo vo = new Dm_write_vo();
					vo.setWr_id(item);
					vo = writeService.selectWrite(vo);
					if (vo == null) {
						resultMap.put("result", "fail");
						resultMap.put("notice", "삭제하고자 하는 게시글정보가 없습니다.");
						return new ResponseEntity<>(resultMap, HttpStatus.OK);
					}
					vo.setDm_delete_id(loginVO.getId());
					list.add(vo);
				}
				writeService.deleteWrite(list);
				resultMap.put("result", "success");
				resultMap.put("notice", MessageCode.CMS_DELETE_SUCCESS.getMessage());
			} else {
				resultMap.put("notice", MessageCode.CMM_REQUEST_BADREQUEST.getMessage());
				return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
			}
			
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
	 * get_write
	 * 전달받은 게시물PK 데이터로 조회한 1건의 게시판데이터를 화면에 전달
	 * @param writeVO 게시판데이터 PK데이터를 전달받는 vo객체
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/get_write.do")
	public ResponseEntity<Object> get_write (Dm_write_vo writeVO) {
		
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
			Dm_write_vo write = writeService.selectWrite(writeVO);
								
			if (write != null) {
				List<String> file_list = new ArrayList<String>(Arrays.asList(write.getWr_file().split("\\|")));
				List<String> file_ori_list = new ArrayList<String>(Arrays.asList(write.getWr_ori_file_name().split("\\|")));
				
				resultMap.put("result", "success");
				resultMap.put("wr_file_array", file_list);
				resultMap.put("wr_file_ori_array", file_ori_list);
				resultMap.put("rows", write);
				
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
	 * set_write
	 * 사용자가 입력한 게시물데이터의 insert/update 동작 수행
	 * @param type 입력 폼에서 전달받는 insert/update 수행명령값
	 * @param writeVO 사용자가 입력한 게시물데이터를 전달받는 vo객체
	 * @param br vo객체의 hibernate validator 검증결과를 return 하기위한 객체
	 * @param session 게시글 등록/수정 후 RSA암호화 세션을 삭제할 때 사용하는 HttpSession 객체 
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 BindingResult객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return 
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/set_write.do")
	public ResponseEntity<Object> set_write (@Valid Dm_write_vo writeVO, BindingResult br, 
			HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		
		if(br.hasErrors()) {
			List<FieldError> errors = br.getFieldErrors();
			String msg = errors.get(0).getDefaultMessage();
			
			resultMap.put("result", "fail");
			resultMap.put("notice", msg);
			return new ResponseEntity<>(resultMap, HttpStatus.OK);
		}
		
		if (commonUtil.isNullOrEmpty(loginVO.getId())) {
			resultMap.put("notice", MessageCode.CMM_SESSION_EXPIRED.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.UNAUTHORIZED);
		}
		
		try {
			Dm_board_vo boardVO = new Dm_board_vo();
			boardVO.setDm_id(writeVO.getWr_board());
			boardVO = boardService.selectBoard(boardVO);
			
			if (boardVO != null) {
				if (writeVO.getFile().size() > 0) {
					for (int i=0 ; i < writeVO.getFile().size() ; i++) {
						boolean chkExt = commonUtil.prohibitExtCheck(writeVO.getFile().get(i));
						if (chkExt) {
							resultMap.put("result", "fail");
							resultMap.put("notice", "금지된 업로드 확장자입니다.");
							return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
						}
					}
				}
					
				if (!commonUtil.isNullOrEmpty(writeVO.getWr_password())) {
					String wr_password = writeVO.getWr_password();
					PrivateKey privateKey = (PrivateKey) session.getAttribute("DIAM_WRITE_RSA_KEY");
					wr_password = commonUtil.decryptRsa(privateKey, wr_password);
					writeVO.setWr_password(commonUtil.convertSHA256(wr_password));
				}
				
				writeVO.setMb_id(loginVO.getId());
				writeVO.setWr_ip(commonUtil.getUserIp(request));
				//XSS필터링을 위한 신규추가
				writeVO.setWr_content(commonUtil.xssSaxFiltering(writeVO.getWr_content()));
				
				String FILE_PATH = request.getServletContext().getRealPath("/resources/board/"+boardVO.getDm_table())+"/";
				String basic_path = request.getServletContext().getRealPath("/");
				File folder = new File(FILE_PATH);
				if (!folder.exists()) {
					folder.mkdirs();
				}			
				
				if (commonUtil.isNullOrEmpty(writeVO.getWr_id())) {
					List<String> file_array = new ArrayList<String>();
					List<String> file_ori_array = new ArrayList<String>();
					
					for (int i=0; i<Integer.parseInt(boardVO.getDm_upload_count()); i++) {
						file_array.add("");
						file_ori_array.add("");
					}
					uploadWriteFile(boardVO, writeVO, file_array, file_ori_array, FILE_PATH, basic_path);
					
					writeVO.setWr_file(String.join("|", file_array));
					writeVO.setWr_ori_file_name(String.join("|", file_ori_array));
					
					
					
					int result = writeService.insertWrite(writeVO);
					
					session.removeAttribute("DIAM_WRITE_RSA_KEY");
					
					if (result > 0) {
						resultMap.put("result",  "success");
						resultMap.put("notice", MessageCode.CMS_INSERT_SUCCESS.getMessage());
					} else {
						resultMap.put("result",  "fail");
						resultMap.put("notice", MessageCode.CMS_INSERT_FAIL.getMessage());
					}
								
				} else {
					Dm_write_vo seWriteVo = writeService.selectWrite(writeVO);
					if (seWriteVo != null) {
						
						if (!commonUtil.isNullOrEmpty(writeVO.getWr_is_notice()) && writeVO.getWr_is_notice().equals("1")) {
							int replyCount = writeService.selectWriteReplyCount(seWriteVo);
							if (replyCount > 0) {
								resultMap.put("result",  "fail");
								resultMap.put("notice", "답글이 있는 게시글은 공지로 등록할 수 없습니다.");
								return new ResponseEntity<>(resultMap, HttpStatus.OK);
							}
						}
						
						List<String> file_array = new ArrayList<String>(Arrays.asList(seWriteVo.getWr_file().split("\\|")));
						List<String> file_ori_array = new ArrayList<String>(Arrays.asList(seWriteVo.getWr_ori_file_name().split("\\|")));
						
						int tmp_size = file_array.size();
						if (Integer.parseInt(boardVO.getDm_upload_count()) > file_array.size()) {
				        	for (int i = Integer.parseInt(boardVO.getDm_upload_count()) ;  i > tmp_size ; i--) {
				        		file_array.add("");
					        	file_ori_array.add("");
				        	}
				        } else if (Integer.parseInt(boardVO.getDm_upload_count()) < file_array.size()) {
				        	for (int i=tmp_size ; i < Integer.parseInt(boardVO.getDm_upload_count()) ; i--) {
				        		file_array.remove(i);
					        	file_ori_array.remove(i);
				        	}
				        }
				        
				        for (int i=0; i<Integer.parseInt(boardVO.getDm_upload_count()); i++) {
				        	String del_file = request.getParameter("del_file_"+i);
							if (del_file != null && !del_file.isEmpty()) {
								if (del_file.equals("1")) {
									File file = new File(basic_path+file_array.get(i));
									if( file.exists() ){
										FileDelete(file);
									}
									file_array.set(i, "");
									file_ori_array.set(i, "");
									
									String wr_file = file_array.get(i);
									/*섬네일 삭제*/
									File t_gallery = new File(FILE_PATH + "gallery" + "/t-" + wr_file.substring(wr_file.lastIndexOf("/")+1, wr_file.length()));
									if( t_gallery.exists() ){
										FileDelete(t_gallery);
									}
								}
							}
				        }
				        uploadWriteFile(boardVO, writeVO, file_array, file_ori_array, FILE_PATH, basic_path);
				        
				        writeVO.setWr_file(String.join("|", file_array));
						writeVO.setWr_ori_file_name(String.join("|", file_ori_array));
						
				        int result = writeService.updateWrite(writeVO);
				        
				        session.removeAttribute("DIAM_WRITE_RSA_KEY");
				        
				        if (result > 0) {
							resultMap.put("result",  "success");
							resultMap.put("notice", MessageCode.CMS_UPDATE_SUCCESS.getMessage());
						} else {
							resultMap.put("result",  "fail");
							resultMap.put("notice", MessageCode.CMS_UPDATE_FAIL.getMessage());
						}
				        
					} else {
						resultMap.put("result",  "fail");
						resultMap.put("notice", MessageCode.CMS_SELECT_NODATA.getMessage());
					}				
				}	
			} else {
				resultMap.put("result", "fail");
				resultMap.put("notice", "게시판 정보가 없습니다.");
			}			
		} catch(InvalidKeyException ike) {
			log.error(MessageCode.CMM_ENCRYPT_EXPIRED.getLog());
			resultMap.put("notice", MessageCode.CMM_ENCRYPT_EXPIRED.getMessage());
			return new ResponseEntity<>(resultMap,HttpStatus.INTERNAL_SERVER_ERROR);
		} catch(BadPaddingException bpe) {
			log.error(MessageCode.CMM_ENCRYPT_EXPIRED.getLog());
			resultMap.put("notice", MessageCode.CMM_ENCRYPT_EXPIRED.getMessage());
			return new ResponseEntity<>(resultMap,HttpStatus.INTERNAL_SERVER_ERROR);
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
	 * get_category
	 * request 객체를 통하여 전달받은 게시판PK에 해당하는 게시판카테고리 리스트 조회
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/get_category.do")
	public ResponseEntity<Object> get_category (@RequestParam(value="selected", required=false, defaultValue="") String selected,
			@RequestParam(value="wr_board", required=true) String wr_board) {
		CommonUtil commonUtil = new CommonUtil();
		List<Dm_common_code_vo> result = new ArrayList<>();

		try {
			Dm_board_vo boardVO = new Dm_board_vo();
			boardVO.setDm_id(wr_board);
			boardVO = boardService.selectBoard(boardVO);
			if (boardVO != null) {
				List<String> category_list = new ArrayList<String>();
				if (boardVO.getDm_category_list() != null && !boardVO.getDm_category_list().replaceAll(" ","").isEmpty()) {
					category_list = new ArrayList<String>(Arrays.asList(boardVO.getDm_category_list().split(",")));
				}
				if (category_list.size() > 0) {					
					
					for (int i=0 ; i < category_list.size() ; i++) {
						Dm_common_code_vo vo = new Dm_common_code_vo();
						vo.setDm_code_value(category_list.get(i));
						vo.setDm_code_name(category_list.get(i));
						
						if (!commonUtil.isNullOrEmpty(selected)) {
							if (selected.equals(category_list.get(i))) {
								vo.setSelected(true);
							}
						}
						result.add(vo);
					}
					
					result.add(0, Dm_common_code_vo.builder()
							.dm_code_value("")
							.dm_code_name("선택안함")
							.build());
					
					if (commonUtil.isNullOrEmpty(selected)) {
						result.get(0).setSelected(true);
					}
					
				} else {
					result.add(0, Dm_common_code_vo.builder()
							.dm_code_value("")
							.dm_code_name("등록된 카테고리가 없습니다")
							.selected(true)
							.build());
				}
			} else {
				result.add(0,Dm_common_code_vo.builder()
						.dm_code_name("등록된 게시판이 없습니다.")
						.dm_code_value("")
						.selected(true)
						.build());
			}		
		} catch(DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			return new ResponseEntity<>("카테고리 리스트를 불러오던 중 " + MessageCode.CMM_DATA_ERROR.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch(Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			return new ResponseEntity<>("카테고리 리스트를 불러오던 중 " + MessageCode.CMM_SYSTEM_ERROR.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	/**
	 * get_write_file_download
	 * 게시물에 첨부한 첨부파일 다운로드 수행
	 * @param file_name 파일다운로드 시 참조할 첨부파일의 임시파일명을 전달받는 문자열
	 * @param ori_file_name 파일다운로드 시 다운로드파일명이 될 원파일명을 전달받는 문자열
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/get_write_file_download.do")
	public @ResponseBody void get_write_file_download(@RequestParam(value="file_name", required=true) String file_name,
			@RequestParam(value="ori_file_name", required=true) String ori_file_name,
			@RequestParam(value="wr_id", required=true) String wr_id,
			@RequestParam(value="wr_board", required=true) String wr_board,
			HttpServletRequest request, HttpServletResponse response) {
		CommonUtil commonUtil = new CommonUtil();
		
		try {
			Dm_board_vo boardVO = new Dm_board_vo();
			boardVO.setDm_id(wr_board);
			boardVO = boardService.selectBoard(boardVO);
			
			if (boardVO != null) {
				Dm_write_vo writeVO = new Dm_write_vo();
				writeVO.setWr_id(wr_id);
				writeVO = writeService.selectWrite(writeVO);
				
				if (writeVO != null) {
					String file_path = request.getServletContext().getRealPath("/") + file_name;
					
					List<String> file_list = Arrays.asList(writeVO.getWr_file().split("\\|"));
					if (file_list.size() > 0) {
						boolean file_check = false;
						for (int i=0 ; i < file_list.size() ; i++) {
							if (file_name.equals(file_list.get(i))) {
								file_check = true;
								break;
							}
						}
						
						if (file_check) {
							File file = new File(file_path);
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
						} else {
							response.setContentType("text/html; charset=UTF-8");
							PrintWriter print_out = response.getWriter();
							print_out.println("<script>alert('잘못된 접근입니다.'); history.go(-1); </script>");
							print_out.flush();
							print_out.close();
						}						
					} else {
						response.setContentType("text/html; charset=UTF-8");
						PrintWriter print_out = response.getWriter();
						print_out.println("<script>alert('등록된 파일이 없습니다.'); history.go(-1); </script>");
						print_out.flush();
						print_out.close();
					}
				} else {
					response.setContentType("text/html; charset=UTF-8");
					PrintWriter print_out = response.getWriter();
					print_out.println("<script>alert('게시글 정보가 없습니다.'); history.go(-1); </script>");
					print_out.flush();
					print_out.close();
				}
				
			} else {
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter print_out = response.getWriter();
				print_out.println("<script>alert('게시판 정보가 없습니다.'); history.go(-1); </script>");
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
	
	@RequestMapping("/adm/selectCommentList.do")
	public ResponseEntity<Object> selectCommentList(Dm_write_vo writeVO) {
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		
		if (commonUtil.isNullOrEmpty(writeVO.getWr_id())) {
			resultMap.put("notice", MessageCode.CMM_REQUEST_BADREQUEST.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
		}
		
		int wr_num = Integer.parseInt(writeVO.getWr_id()) * -1;
		writeVO.setWr_num(wr_num);
		
		try {
			List<Dm_write_vo> list = writeService.selectParentComment(writeVO);
			resultMap.put("rows", list);
			
			
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
	
	@RequestMapping("/adm/deleteComment.do")
	public ResponseEntity<Object> deleteComment(Dm_write_vo writeVO) {
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		
		try {
			if (commonUtil.isNullOrEmpty(loginVO.getId())) {
				resultMap.put("notice", MessageCode.CMM_SESSION_EXPIRED.getMessage());
				return new ResponseEntity<>(resultMap, HttpStatus.UNAUTHORIZED);
			}
			
			if (commonUtil.isNullOrEmpty(writeVO.getWr_id())) {
				resultMap.put("notice", MessageCode.CMM_REQUEST_BADREQUEST.getMessage());
				return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
			}
			
			List<Dm_write_vo> list = writeService.selectCommentChildrenAll(writeVO);
			if (list.size() > 0) {
				list.forEach(item -> {
					item.setDm_delete_id(loginVO.getId());
				});
				
				writeService.deleteComment(list);
				resultMap.put("result", "success");
				resultMap.put("notice", MessageCode.CMS_DELETE_SUCCESS.getMessage());
			} else {
				resultMap.put("result", "fail");
				resultMap.put("notice", MessageCode.CMS_SELECT_NODATA.getMessage());
			}
			
		} catch(DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			resultMap.put("notice", MessageCode.CMM_DATA_ERROR.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (RuntimeException rte) {
			log.error(MessageCode.CMM_TRANSACTION_FAIL.getLog());
			resultMap.put("result", MessageCode.CMM_TRANSACTION_FAIL.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch(Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			resultMap.put("notice", MessageCode.CMM_SYSTEM_ERROR.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(resultMap, HttpStatus.OK);
	}
	
	@RequestMapping("/adm/upsertComment.do")
	public ResponseEntity<Object> upsertComment(Dm_write_vo writeVO, BindingResult br, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		if (commonUtil.isNullOrEmpty(loginVO.getId())) {
			resultMap.put("notice", MessageCode.CMM_SESSION_EXPIRED.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.UNAUTHORIZED);
		}
		
		if(br.hasErrors()) {
			List<FieldError> errors = br.getFieldErrors();
			String msg = errors.get(0).getDefaultMessage();
			
			resultMap.put("result", "fail");
			resultMap.put("notice", msg);
			return new ResponseEntity<>(resultMap, HttpStatus.OK);
		}
		
		if (commonUtil.isNullOrEmpty(writeVO.getWr_board())) {
			resultMap.put("result", "fail");
			resultMap.put("notice", "게시판 정보가 없습니다.");
			return new ResponseEntity<>(resultMap, HttpStatus.OK);
		}
		try {
			
			if (commonUtil.isNullOrEmpty(writeVO.getWr_id())) {
				int max = writeService.selectMaxCommentReply(writeVO);
				String parent = writeService.selectWrite(Dm_write_vo.builder().wr_id(writeVO.getWr_parent()).build()).getMb_id();
				if (Integer.parseInt(writeVO.getWr_parent()) * -1 == writeVO.getWr_num()) {
					writeVO.setWr_comment(1);
				} else {
					writeVO.setWr_comment(2);
				}
				writeVO.setWr_ip(commonUtil.getUserIp(request));
				writeVO.setMb_id(loginVO.getId());
				writeVO.setWr_is_comment(1);
				writeVO.setWr_comment_reply(max+1);
				writeVO.setReply_mb_id(parent);
				writeVO.setWr_name(loginVO.getName());
				writeVO.setWr_subject("관리자 페이지 댓글");
				int result = writeService.insertComment(writeVO);
				if (result > 0) {
					resultMap.put("result", "success");
					resultMap.put("notice", MessageCode.CMS_INSERT_SUCCESS.getMessage());
				} else {
					resultMap.put("result", "fail");
					resultMap.put("notice", MessageCode.CMS_INSERT_FAIL.getMessage());
				}
			} else {
				writeVO.setDm_modify_id(loginVO.getId());
				int result = writeService.updateComment(writeVO);
				if (result > 0) {
					resultMap.put("result", "success");
					resultMap.put("notice", MessageCode.CMS_UPDATE_SUCCESS.getMessage());
				} else {
					resultMap.put("result", "fail");
					resultMap.put("notice", MessageCode.CMS_UPDATE_FAIL.getMessage());
				}
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
	
	@RequestMapping("/adm/selectReplyList.do")
	public ResponseEntity<Object> selectReplyList(Dm_write_vo writeVO) {
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		
		if (commonUtil.isNullOrEmpty(writeVO.getWr_id())) {
			resultMap.put("notice", MessageCode.CMM_REQUEST_BADREQUEST.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
		}
		
		try {
			List<Dm_write_vo> list = writeService.selectReplyList(writeVO);
			if (list.size() > 1) {
				list.remove(0);
				list.forEach(item -> {
					if (!commonUtil.isNullOrEmpty(item.getWr_content())) {
						item.setWr_content(commonUtil.removeHtml(item.getWr_content()));
					}
					if (item.getDm_delete_yn().equals("Y")) {
						item.setWr_subject("삭제된 게시글입니다.");
						item.setWr_content("삭제된 게시글입니다.");
					}
				});
				List<Dm_write_vo> removeList = new ArrayList<>();
				for (int i = 0; i < list.size(); i++) {
					for (Dm_write_vo vo : list) {
						if (list.get(i).getWr_parent().equals(vo.getWr_id())) {
							vo.getChildren().add(list.get(i));
							removeList.add(list.get(i));
						}
					}
				}
				list.removeAll(removeList);
				resultMap.put("rows", list);
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
	
	@RequestMapping("/adm/set_reply.do")
	public String set_reply(Dm_write_vo writeVO, RedirectAttributes rttr, HttpServletResponse response) {
		CommonUtil commonUtil = new CommonUtil();
		try {
			Dm_write_vo parentVO = writeService.selectWrite(Dm_write_vo.builder()
					.wr_id(writeVO.getWr_parent())
					.build());
			if (parentVO != null) {
				if (!commonUtil.isNullOrEmpty(parentVO.getWr_is_notice()) && parentVO.getWr_is_notice().equals("1")) {
					exceptionHandle(response, 400, "공지 게시글에는 답변을 작성할 수 없습니다.");
					return null;
				}
				
				int maxReply = writeService.selectMaxReplyReply(writeVO);
				writeVO.setWr_reply(parentVO.getWr_reply() +1);
				writeVO.setWr_reply_reply(maxReply +1);
				writeVO.setWr_num(parentVO.getWr_num());
				writeVO.setReply_mb_id(parentVO.getMb_id());
				writeVO.setCa_name(parentVO.getCa_name());
				rttr.addFlashAttribute(writeVO);
				return "redirect:/adm/set_write.do";
			} else {
				exceptionHandle(response, 400, "부모 글의 정보를 찾을 수 없습니다.");
				return null;
			}
			
		} catch(DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			exceptionHandle(response, 500 ,MessageCode.CMM_DATA_ERROR.getMessage());
			return null;
		} catch(Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			exceptionHandle(response, 500,MessageCode.CMM_SYSTEM_ERROR.getMessage());
			return null;
		}
	}
	
	public void exceptionHandle(HttpServletResponse response, int status, String message) {
		try {
			response.setStatus(status);
			response.getWriter().write(message);
			response.getWriter().flush();
		} catch (Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
		}
	}
	
	/**
	 * uploadWriteFile
	 * 게시물 등록/수정시 업로드한 첨부파일을 서버에 업로드
	 * @param boardVO 첨부파일과 관련된 게시판 설정값을 전달받는 vo객체
	 * @param writeVO 사용자가 업로드한 게시물 정보를 전달받는 vo객체
	 * @param file_array 첨부파일 임시파일명을 전달받는 List 자료형
	 * @param file_ori_array 첨부파일 원파일명을 전달받는 List 자료형
	 * @param file_path 첨부파일이 업로드될 서버의 폴더경로를 전달받는 문자열
	 * @param basic_path 서버의 web루트 경로를 전달받는 문자열
	 * @return String 파일 업로드 프로세스 후 경로+파일명을 연결하여 문자열로 return
	*/
	private void uploadWriteFile(Dm_board_vo boardVO, Dm_write_vo writeVO, List<String> file_array, List<String> file_ori_array, 
			String file_path, String basic_path) throws Exception {
		CommonUtil commonUtil = new CommonUtil();
				       
		for (int i=0; i < Integer.parseInt(boardVO.getDm_upload_count()); i++) {
			if (writeVO.getFile().size() > 0) {
				if (writeVO.getFile().get(i).getSize() > 0 ) {
					if (!file_array.get(i).equals("")) {
						File file = new File(basic_path+file_array.get(i));
						if( file.exists() ){
							FileDelete(file);
							//file.delete(); 시큐어코딩 결함으로 주석처리, 후에 제거필요
							file_array.set(i, "");
							file_ori_array.set(i, "");
						}
					}
					
					String original_file_name = commonUtil.uploadFileNameFiltering(writeVO.getFile().get(i).getOriginalFilename());
					String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
					String upload_name = today + "_" + original_file_name;
					String upload_path = file_path + upload_name;
										
					writeVO.getFile().get(i).transferTo(new File(upload_path));
					file_array.set(i, "/resources/board/"+boardVO.getDm_table()+"/"+upload_name);
					file_ori_array.set(i, original_file_name); 
										
					/*섬네일 생성*/
					String[] extlist = {"jpg","gif","png","JPG","GIF","PNG","jpeg","JPEG"};
					String ext = upload_name.substring(upload_name.indexOf(".") + 1);
					boolean extchk = false;
					extchk = Arrays.asList(extlist).contains(ext);
					if(extchk) {
						if (Integer.parseInt(boardVO.getDm_gallery_width()) > 0 && Integer.parseInt(boardVO.getDm_gallery_height()) > 0) {
							commonUtil.makeThumbnail(file_path, upload_name, boardVO.getDm_gallery_width(), boardVO.getDm_gallery_height(), "gallery");
						}
					}
				}
			}
		}
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