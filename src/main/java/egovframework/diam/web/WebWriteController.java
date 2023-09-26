/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.web;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
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
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.diam.biz.model.board.Dm_board_vo;
import egovframework.diam.biz.model.board.Dm_write_vo;
import egovframework.diam.biz.model.display.Dm_menus_vo;
import egovframework.diam.biz.model.display.Dm_pages_vo;
import egovframework.diam.biz.service.board.BoardService;
import egovframework.diam.biz.service.board.WriteService;
import egovframework.diam.biz.service.display.MenuService;
import egovframework.diam.biz.service.display.PageService;
import egovframework.diam.cmm.model.LoginVO;
import egovframework.diam.cmm.util.CommonUtil;
import egovframework.diam.cmm.util.EgovUserDetailsHelper;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;

/**
 * @Class Name : WebWriteController.java
 * @Description : 사용자페이지 게시글 작성, 답변/댓글 작성 등 게시판 CRUD기능 구현 Controller클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
 */

@Controller
@Log4j2
public class WebWriteController {
		
	@Resource(name="writeService")
	private WriteService writeService;
	
	@Resource(name="boardService")
	private BoardService boardService;
	
	@Resource(name="pageService")
	private PageService pageService;
	
	@Autowired
	private MenuService menuService;
	
	private boolean isNumber(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
	
	/**
	 * insert
	 * 사용자가 입력한 게시물데이터의 insert/update 동작 수행
	 * @param type 입력 폼에서 전달받는 insert/update 수행명령값
	 * @param writeVO 사용자가 입력한 게시물 데이터를 전달받는 vo객체
	 * @param br vo객체의 hibernate validator 검증결과를 return 하기위한 객체
	 * @param session 게시글 등록/수정 후 RSA암호화 세션을 삭제할 때 사용하는 HttpSession 객체 
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param multiRequest 전송된 첨부파일 정보를 가져오기 위한 MultipartHttpServletRequest객체
	 * @param model 화면에 결과값을 전달하기 위한 ModelMap 객체
	 * @return String 게시물 등록 후 결과페이지를 표출할 파일경로를 문자열로 return
	 */
	@RequestMapping(value= {"/write/set_write.do", "/{domain}/write/set_write.do"}, method=RequestMethod.POST)
	public String set_write(@RequestParam(value="type", required=true) String type,
			@ModelAttribute("writeVO") @Valid Dm_write_vo writeVO, BindingResult br, HttpSession session,
			HttpServletRequest request, MultipartHttpServletRequest multiRequest, ModelMap model) {
		CommonUtil commonUtil = new CommonUtil();
		model.addAttribute("root", commonUtil.getDomain(request));
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String mb_id = loginVO.getId() != null && !loginVO.getId().isEmpty() ? loginVO.getId() : "비회원";
		
		if ("비회원".equals(mb_id) && "insert".equals(type)) {
			if (writeVO.getWr_password() == null || writeVO.getWr_password().isEmpty()) {
				model.addAttribute("message","board.invalid.password");
				return "egovframework/diam/web/base/board/result";
			}
		}
		
		if(br.hasErrors()) {
			List<FieldError> errors = br.getFieldErrors();
			String msg = errors.get(0).getDefaultMessage();
			
			model.addAttribute("message","board.fail.validate");
			model.addAttribute("notice", msg);
			return "egovframework/diam/web/base/board/result";
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
							model.addAttribute("message","board.prohibit.ext");
							return "egovframework/diam/web/base/board/result";
						}
					}
				}
				
				if (writeVO.getWr_password() != null && !writeVO.getWr_password().isEmpty()) {
					String wr_password = writeVO.getWr_password();
					PrivateKey privateKey = (PrivateKey) session.getAttribute("DIAM_WEB_WRITE_RSA_KEY");
					wr_password = commonUtil.decryptRsa(privateKey, wr_password);
					writeVO.setWr_password(commonUtil.convertSHA256(wr_password));
				}
				
				writeVO.setMb_id(mb_id);
				writeVO.setWr_board(boardVO.getDm_id());
				writeVO.setWr_ip(commonUtil.getUserIp(request));
				
				String FILE_PATH = request.getServletContext().getRealPath("/resources/board/"+writeVO.getDm_table())+"/";
				String basic_path = request.getServletContext().getRealPath("/");
				
				File folder = new File(FILE_PATH);
				if (!folder.exists()) {
					folder.mkdirs();
				}
				
				if ("insert".equals(type)) {
					List<String> file_array = new ArrayList<String>();
					List<String> file_ori_array = new ArrayList<String>();
					
					for (int i=0; i<Integer.parseInt(boardVO.getDm_upload_count()); i++) {
						file_array.add("");
						file_ori_array.add("");
					}
					
					uploadWriteFile(boardVO, writeVO, file_array, file_ori_array, FILE_PATH, basic_path);
					
					writeVO.setWr_file(String.join("|", file_array));
					writeVO.setWr_ori_file_name(String.join("|", file_ori_array));
					
					//XSS필터링을 위한 신규추가
					writeVO.setWr_content(commonUtil.xssSaxFiltering(writeVO.getWr_content()));
					
					writeService.insertWrite(writeVO);
					session.removeAttribute("DIAM_WEB_WRITE_RSA_KEY");
										
					model.addAttribute("message","board.success.write");
				} else if ("update".equals(type)) {
					Dm_write_vo seWriteVo = writeService.selectWrite(writeVO);
					if (seWriteVo != null) {
						
						if (!commonUtil.isNullOrEmpty(writeVO.getWr_is_notice()) && writeVO.getWr_is_notice().equals("1")) {
							int replyCount = writeService.selectWriteReplyCount(seWriteVo);
							if (replyCount > 0) {
								model.addAttribute("message","board.modify.notice");
								return "egovframework/diam/web/base/board/result";
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
						
						//XSS필터링을 위한 신규추가
						writeVO.setWr_content(commonUtil.xssSaxFiltering(writeVO.getWr_content()));
						
				        writeService.updateWrite(writeVO);
				        session.removeAttribute("DIAM_WEB_WRITE_RSA_KEY");
				        
				        model.addAttribute("message","board.success.modify");
					} else {
						model.addAttribute("message","board.notfound.write");
					}					
				} else {
					model.addAttribute("message","board.invalid.command");
				}				
			} else {
				model.addAttribute("message","board.notfound.info");
			}			
		} catch(InvalidKeyException ike) {
			log.error(MessageCode.CMM_ENCRYPT_EXPIRED.getLog());
			model.addAttribute("message", "board.encrypt.expired");		
		} catch(BadPaddingException bpe) {
			log.error(MessageCode.CMM_ENCRYPT_EXPIRED.getLog());
			model.addAttribute("message", "board.encrypt.expired");		
		} catch(IOException ioe) {
			log.error(MessageCode.CMM_FILE_ERROR.getLog());
			model.addAttribute("message","board.file.error");
		} catch(DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			model.addAttribute("message","board.sql.error");
		} catch(Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());			
			model.addAttribute("message","board.other.error");
		}
		return "egovframework/diam/web/base/board/result";
	}
	
	/**
	 * delete_write
	 * 게시물 상세보기에서 삭제 버튼을 클릭하여 삭제요청 시 게시물데이터 삭제 수행
	 * @param wr_id 삭제할 게시물의 PK값을 문자열로 전달
	 * @param board_id 삭제할 게시물이 소속된 게시판 PK값을 문자열로 전달
	 * @param paramMap 리스트 검색값이 포함되어 있는 파라미터를 Map으로 전달
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param model 화면에 결과값을 전달하기 위한 ModelMap 객체
	 * @param session 세션에 저장된 검증된 비밀번화 값을 꺼낼 시 사용하는 HttpSession 객체
	 * @return String 게시글 삭제 후 결과페이지를 표출할 파일경로를 문자열로 return
	*/
	@RequestMapping(value= {"/write/delete_write.do", "/{domain}/write/delete_write.do"})
	public String delete_write(@RequestParam(value="wr_id", required=true) String wr_id,
			@RequestParam(value="board_id", required=true) String board_id,
			@RequestParam Map<String, Object> paramMap,
			HttpServletRequest request, ModelMap model, HttpSession session) {
		CommonUtil commonUtil = new CommonUtil();
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		model.addAttribute("root", commonUtil.getDomain(request));
		
		String mb_id = loginVO.getId() != null && !loginVO.getId().isEmpty() ? loginVO.getId() : "비회원";
		try {
			Dm_board_vo boardVO = new Dm_board_vo();
			boardVO.setDm_id(board_id);
			boardVO = boardService.selectBoard(boardVO);
			
			if (boardVO != null) {
				Dm_write_vo checkVO = new Dm_write_vo();
				checkVO.setWr_id(wr_id);
				checkVO = writeService.selectWrite(checkVO);
				if (checkVO != null) {
					if ("비회원".equals(checkVO.getMb_id())) {
						if (!loginVO.getIs_admin()) {
							String sessionPassword = (String) session.getAttribute("DIAM_CHECKPASS_RESULT");
							if (sessionPassword != null && !sessionPassword.isEmpty()) {
								if (!sessionPassword.equals(checkVO.getWr_password())) {
									model.addAttribute("message", "board.invalid.password");
									return "egovframework/diam/web/base/board/result";
								} else {
									session.removeAttribute("DIAM_CHECKPASS_RESULT");
								}
							} else {
								model.addAttribute("message", "board.fault.access");
								return "egovframework/diam/web/base/board/result";
							}
						}
					} else {
						if (!loginVO.getIs_admin() && !checkVO.getMb_id().equals(loginVO.getId())) {
							model.addAttribute("message", "board.fault.access");
							return "egovframework/diam/web/base/board/result";
						}
					}
					
					Map<String, Object> deleteSearchMap = new HashMap<String, Object>();
					deleteSearchMap.put("search_type", paramMap.get("search_type"));
					deleteSearchMap.put("search_value", paramMap.get("search_value"));
					deleteSearchMap.put("search_cate", paramMap.get("search_cate"));
					deleteSearchMap.put("contentId", paramMap.get("contentId"));
					deleteSearchMap.put("page", paramMap.get("page"));	
					
					checkVO.setDm_delete_id(mb_id);
					writeService.deleteWrite(Arrays.asList(checkVO));
					
					model.addAttribute("deleteSearchMap", commonUtil.convertParam(deleteSearchMap));
					
					if (checkVO.getWr_is_comment() == 1) {
						model.addAttribute("wr_id", Math.abs(checkVO.getWr_num()));
						model.addAttribute("message", "board.comment.delete");
					} else {
						model.addAttribute("message", "board.success.delete");
					}
				} else {
					model.addAttribute("message","board.notfound.write");
				}
			} else {
				model.addAttribute("message", "board.notfound.info");
			}
		} catch(DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			model.addAttribute("message","board.sql.error");
		} catch(Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			model.addAttribute("message","board.other.error");
		}
		return "egovframework/diam/web/base/board/result";
	}
		
	/**
	 * check_password
	 * 비밀번호 입력팝업에서 사용자가 입력한 비밀번호 검증
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @param session 검증된 비밀번호 값을 세션에 저장 시 사용하는 HttpSession 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/write/check_password.do")
	public ResponseEntity<?> check_password(@RequestParam(value="contentId", required=true) String contentId,
			@RequestParam(value="wr_id", required=true) String wr_id,
			@RequestParam(value="chkpass", required=true) String chkpass, HttpSession session) {
		CommonUtil commonUtil = new CommonUtil();
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
			Dm_pages_vo pageVO = new Dm_pages_vo();
			if (isNumber(contentId)) {
				Dm_menus_vo menuVO = Dm_menus_vo.builder().dm_id(contentId).build();
				menuVO = menuService.selectMenuByDmId(menuVO);
				if (menuVO != null) {
					contentId = menuVO.getDm_link_data();
				}
			}
			pageVO.setDm_uid(contentId);
			pageVO = pageService.selectPageDmUid(pageVO);
			
			if (pageVO != null) {
				Dm_board_vo boardVO = new Dm_board_vo();
				boardVO.setDm_id(pageVO.getDm_board_id());
				boardVO = boardService.selectBoard(boardVO);
				
				if (boardVO != null) {
					Dm_write_vo checkVO = new Dm_write_vo();
					checkVO.setWr_id(wr_id);
					checkVO.setDm_table(boardVO.getDm_table());
					checkVO = writeService.selectWrite(checkVO);
					
					if (checkVO != null) {
						Dm_write_vo writeVO = new Dm_write_vo();
						writeVO.setWr_id(wr_id);
						
						if (chkpass != null && !chkpass.isEmpty()) {
							String wr_password = chkpass;
							PrivateKey privateKey = (PrivateKey) session.getAttribute("DIAM_WRITE_SECRET_RSA_KEY");
							wr_password = commonUtil.decryptRsa(privateKey, wr_password);
							writeVO.setWr_password(commonUtil.convertSHA256(wr_password));
						}
												
						writeVO = writeService.selectWriteCheckPassword(writeVO);
												
						if (writeVO != null) {
							session.setAttribute("DIAM_CHECKPASS_RESULT", writeVO.getWr_password());
							resultMap.put("result", "success");
							resultMap.put("notice", "비밀번호 확인 완료");
						} else {
							resultMap.put("result", "fail");
							resultMap.put("notice", "비밀번호가 올바르지 않습니다.");
						}
					} else {
						resultMap.put("result", "fail");
						resultMap.put("notice", "해당하는 게시글 정보가 없습니다.");
					}					
				} else {
					resultMap.put("result", "fail");
					resultMap.put("notice", "해당하는 게시판 정보가 없습니다.");
				}				
			} else {
				resultMap.put("result", "fail");
				resultMap.put("notice", "해당하는 UID에 해당하는 페이지가 없습니다.");
			}
		} catch(InvalidKeyException ike) {
			log.error(MessageCode.CMM_ENCRYPT_EXPIRED.getLog());
			resultMap.put("notice",MessageCode.CMM_ENCRYPT_EXPIRED.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch(BadPaddingException bpe) {
			log.error(MessageCode.CMM_ENCRYPT_EXPIRED.getLog());
			resultMap.put("notice",MessageCode.CMM_ENCRYPT_EXPIRED.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch(DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			resultMap.put("notice",MessageCode.CMM_DATA_ERROR.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch(Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			resultMap.put("notice",MessageCode.CMM_SYSTEM_ERROR.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}		
		return new ResponseEntity<>(resultMap, HttpStatus.OK);
	}
	
	/**
	 * secret
	 * 비밀글 열람 시 표출될 비밀번호 입력팝업 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param model 화면에 결과값을 전달하기 위한 ModelMap 객체
	 * @return String 비밀번호 입력팝업 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping({"/write/secret.do", "/{domain}/write/secret.do"})
	public String secret(HttpServletRequest request, ModelMap model) {		
		CommonUtil commonUtil = new CommonUtil();
		try {
			JSONObject rsaObject = commonUtil.initRsa(request, "DIAM_WRITE_SECRET_RSA_KEY");
			if ("success".equals(rsaObject.get("result"))) {
				model.addAttribute("RSAModulus", rsaObject.get("RSAModulus"));
	    		model.addAttribute("RSAExponent", rsaObject.get("RSAExponent"));
				return "egovframework/diam/web/base/board/secret";
			} else {
				model.addAttribute("message", "암호화 생성 중 오류가 발생하였습니다.");
				return "egovframework/diam/web/error";
			}	
		} catch(InvalidKeySpecException ike) {
        	log.error(MessageCode.CMM_ENCRYPT_EXPIRED.getLog());
		    return "egovframework/diam/web/error";
        } catch (Exception e) {
        	log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
        	return "egovframework/diam/web/error";
		}
		
	}
	
	/**
	 * get_comment_list
	 * 게시글 상세보기 시 해당 게시글의 댓글 리스트를 조회 후 댓글이 출력에 사용되는 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @param wr_id 게시글 PK정보를 전달받는 문자열
	 * @param dm_id 게시판 PK정보를 전달받는 문자열
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param model 화면에 결과값을 전달하기 위한 ModelMap 객체 
	 * @return String 최근게시물 리스트데이터를 표출할 스킨 파일경로를 return
	*/
	@RequestMapping("/write/get_comment_list.do")
	public String get_comment_list(@RequestParam(value="wr_id", required=true) String wr_id,
			@RequestParam(value="dm_id", required=true) String dm_id,
			HttpServletRequest request,	ModelMap model) {
		CommonUtil commonUtil = new CommonUtil();
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String result = "/egovframework/diam/web/base/board/comment";
				
		try {
			Dm_board_vo boardVO = new Dm_board_vo();
			boardVO.setDm_id(dm_id);
			boardVO = boardService.selectBoard(boardVO);
			if (boardVO != null) {
				JSONObject rsaObject = commonUtil.initRsa(request, "DIAM_WEB_COMMENT_RSA_KEY");
				if ("success".equals(rsaObject.get("result"))) {
					request.setAttribute("RSAModulus", rsaObject.get("RSAModulus"));
					request.setAttribute("RSAExponent", rsaObject.get("RSAExponent"));
					
					Dm_write_vo writeVO = new Dm_write_vo();
					writeVO.setWr_id(wr_id);
					writeVO = writeService.selectWrite(writeVO);
					if (writeVO != null) {
						List<String> commentGroupList = new ArrayList<String>(Arrays.asList(boardVO.getDm_comment_group().split("\\|")));
						boolean is_comment = commonUtil.getAuth(boardVO.getDm_auth_type(), loginVO.getDm_level(), boardVO.getDm_comment_level(), loginVO.getGroup_id(), commentGroupList, loginVO.getIs_admin());

						List<Dm_write_vo> commentList = writeService.selectParentComment(writeVO);
									
						model.addAttribute("is_comment", is_comment);
						model.addAttribute("commentList", commentList);
						model.addAttribute("boardVO", boardVO);
					} else {
						model.addAttribute("message", "게시글 정보가 없습니다.");
						result = "egovframework/diam/web/error";
					}										
				} else {
					model.addAttribute("message", "암호화 생성 중 오류가 발생하였습니다.");
					result = "egovframework/diam/web/error";
		        }					
			} else {
				model.addAttribute("message", "게시판 정보가 없습니다.");
				result = "egovframework/diam/web/error";
			}
		} catch(InvalidKeySpecException ike) {
			log.error(MessageCode.CMM_ENCRYPT_EXPIRED.getLog());
        	model.addAttribute("message", MessageCode.CMM_ENCRYPT_EXPIRED.getMessage());
        	result = "egovframework/diam/web/error";
		} catch(DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			model.addAttribute("message", MessageCode.CMM_DATA_ERROR.getMessage());
			result = "egovframework/diam/web/error";
		} catch(Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			model.addAttribute("message", MessageCode.CMM_SYSTEM_ERROR.getMessage());
			result = "egovframework/diam/web/error";	
		}
		return result;
	}
	
	/**
	 * comment_form
	 * 댓글 수정 시 선택한 댓글정보를 가져와서 화면에 전달
	 * @param type 입력 폼에서 전달받는 insert 수행명령값
	 * @param wr_id 댓글PK값을 문자열로 전달
	 * @param board_id 게시판 PK값을 문자열로 전달
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param model 화면에 결과값을 전달하기 위한 ModelMap 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/	
	@RequestMapping({"/write/comment_form.do", "/{domain}/write/comment_form.do"})
	public String comment_form(@RequestParam(value="type", required=true) String type,
			@RequestParam(value="wr_id", required=true) String wr_id,
			@RequestParam(value="board_id", required=true) String board_id, ModelMap model) {
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String result = "egovframework/diam/web/error";
		try {			
			Dm_board_vo boardVO = new Dm_board_vo();
			boardVO.setDm_id(board_id);
			boardVO = boardService.selectBoard(boardVO);
			
			if (boardVO != null) {
				Dm_write_vo commentVO = new Dm_write_vo();
				commentVO.setWr_id(wr_id);
				commentVO.setDm_table(boardVO.getDm_table());
				commentVO = writeService.selectWrite(commentVO);
				
				if (commentVO != null) {
					if ("reply".equals(type) || "modify".equals(type)) {		
						Dm_write_vo writeOriVO = new Dm_write_vo();
						writeOriVO.setWr_num(commentVO.getWr_num());
						writeOriVO = writeService.selectWriteOri(writeOriVO);
						
						model.addAttribute("DiamLoginVO", loginVO);
						model.addAttribute("boardVO", boardVO);
						model.addAttribute("writeOriVO", writeOriVO);
						model.addAttribute("commentVO", commentVO);						
						result = "egovframework/diam/web/base/board/comment_" + type;
					} else {
						model.addAttribute("message", "유효하지 않은 명령값입니다.");
					}
				} else {
					model.addAttribute("message", "댓글 정보가 없습니다.");
				}
			} else {
				model.addAttribute("message", "게시판 정보가 없습니다.");
			}			
		} catch(DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			model.addAttribute("message", MessageCode.CMM_DATA_ERROR.getMessage());
		} catch(Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			model.addAttribute("message", MessageCode.CMM_SYSTEM_ERROR.getMessage());
		}		
		return result;
	}
	
	/**
	 * set_comment
	 * 사용자가 입력한 댓글정보를 수정/등록
	 * @param type 입력 폼에서 전달받는 insert/update 수행명령값
	 * @param commentVO 사용자가 작성한 댓글정보를 vo객체로 전달
	 * @param br vo객체의 hibernate validator 검증결과를 return 하기위한 객체
	 * @param session 댓글 등록/수정 후 RSA암호화 세션 제거 시 사용하는 HttpSession 객체
	 * @param vo 게시판 정보를 vo객체로 전달
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping(value="/write/set_comment.do", method=RequestMethod.POST)
	public ResponseEntity<?> set_comment(@RequestParam(value="type", required=false) String type,
			@ModelAttribute("commentVO") @Valid Dm_write_vo commentVO, BindingResult br, HttpSession session,
			Dm_board_vo vo,	HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		CommonUtil commonUtil = new CommonUtil();
		String mb_id = loginVO.getId() != null && !loginVO.getId().isEmpty() ? loginVO.getId() : "비회원" ;
		
		if ("비회원".equals(mb_id) && "insert".equals(type)) {
			if (commonUtil.isNullOrEmpty(commentVO.getWr_password())) {
				resultMap.put("result",  "fail");
				resultMap.put("notice", "비밀번호가 누락되었습니다. 비밀번호를 입력해주세요.");
				return new ResponseEntity<>(resultMap, HttpStatus.OK);
			}
		}
		
		if(br.hasErrors()) {
			List<FieldError> errors = br.getFieldErrors();
			String msg = errors.get(0).getDefaultMessage();
			
			resultMap.put("result", "fail");
			resultMap.put("notice", msg);
			return new ResponseEntity<>(resultMap, HttpStatus.OK);
		}
		
		try {
			Dm_board_vo boardVO = boardService.selectBoard(vo);
			if (boardVO != null) {
				if (!commonUtil.isNullOrEmpty(commentVO.getWr_password())) {
					String wr_password = commentVO.getWr_password();
					PrivateKey privateKey = (PrivateKey) session.getAttribute("DIAM_WEB_COMMENT_RSA_KEY");
					wr_password = commonUtil.decryptRsa(privateKey, wr_password);
					commentVO.setWr_password(commonUtil.convertSHA256(wr_password));					
				}
				
				//XSS필터링을 위한 신규추가
				commentVO.setWr_content(commonUtil.xssSaxFiltering(commentVO.getWr_content()));
								
				if ("insert".equals(type)) {
					Dm_write_vo seCommentVO = writeService.selectWrite(commentVO);
					if (seCommentVO != null) {
						int max = writeService.selectMaxCommentReply(seCommentVO);
						if (Integer.parseInt(seCommentVO.getWr_id()) * -1 == seCommentVO.getWr_num()) {
							commentVO.setWr_comment(1);
						} else {
							commentVO.setWr_comment(2);
						}
						commentVO.setWr_board(boardVO.getDm_id());
						commentVO.setWr_ip(commonUtil.getUserIp(request));
						commentVO.setMb_id(mb_id);
						commentVO.setWr_is_comment(1);
						commentVO.setWr_num(seCommentVO.getWr_num());
						commentVO.setWr_parent(seCommentVO.getWr_id());
						commentVO.setReply_mb_id(seCommentVO.getMb_id());
						commentVO.setWr_comment_reply(max+1);
						
						int result = writeService.insertComment(commentVO);
						session.removeAttribute("DIAM_WEB_COMMENT_RSA_KEY");
						if (result > 0) {
							resultMap.put("result", "success");
							resultMap.put("notice", MessageCode.CMS_INSERT_SUCCESS.getMessage());
						} else {
							resultMap.put("result", "fail");
							resultMap.put("notice", MessageCode.CMS_INSERT_FAIL.getMessage());
						}
					} else {
						resultMap.put("result", "fail");
						resultMap.put("notice", "댓글을 작성할 게시글데이터가 없습니다.");
					}					
				} else if ("update".equals(type)) {
					Dm_write_vo checkVO = new Dm_write_vo();
					checkVO = writeService.selectWrite(commentVO);
					if (checkVO != null) {
						if ("비회원".equals(checkVO.getMb_id())) {
							if (!loginVO.getIs_admin()) {
								String sessionPassword = (String) session.getAttribute("DIAM_CHECKPASS_RESULT");
								if (sessionPassword != null && !sessionPassword.isEmpty()) {
									if (!sessionPassword.equals(checkVO.getWr_password())) {
										resultMap.put("result", "fail");
										resultMap.put("notice", "잘못된 접근입니다.");										
									}
								} else {
									resultMap.put("result", "fail");
									resultMap.put("notice", "잘못된 접근입니다.");									
								}								
							}
						} else {
							if (!loginVO.getIs_admin() && !checkVO.getMb_id().equals(loginVO.getId())) {
								resultMap.put("result", "fail");
								resultMap.put("notice", "잘못된 접근입니다.");
							}
						}
						
						writeService.updateWrite(commentVO);
						session.removeAttribute("DIAM_WEB_COMMENT_RSA_KEY");
						resultMap.put("result", "success");
						resultMap.put("notice", "댓글을 수정했습니다.");						
					} else {
						resultMap.put("result", "fail");
						resultMap.put("notice", "수정할 댓글정보가 없습니다.");
					}
				} else {
					resultMap.put("result", "fail");
					resultMap.put("notice", "유효하지 않는 명령값입니다.");
				}
			} else {
				resultMap.put("result", "fail");
				resultMap.put("notice", "게시판 정보가 없습니다.");
			}
		} catch (InvalidKeyException ike) {
			log.error(MessageCode.CMM_ENCRYPT_EXPIRED.getLog());
			resultMap.put("notice", MessageCode.CMM_ENCRYPT_EXPIRED.getMessage());
			return new ResponseEntity<>(resultMap,HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (BadPaddingException bpe) {
			log.error(MessageCode.CMM_ENCRYPT_EXPIRED.getLog());
			resultMap.put("notice", MessageCode.CMM_ENCRYPT_EXPIRED.getMessage());
			return new ResponseEntity<>(resultMap,HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (IOException ioe) {
			log.error(MessageCode.CMM_FILE_ERROR.getLog());
			resultMap.put("notice", MessageCode.CMM_FILE_ERROR.getMessage());
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
	
	/**
	 * reply
	 * 사용자가 작성한 답글정보 등록 수행
	 * @param writeVO 사용자가 작성한 답글데이터를 전달받는 vo객체
	 * @param br vo객체의 hibernate validator 검증결과를 return 하기위한 객체
	 * @param session 답변 게시글 등록 후 RSA암호화 세션을 삭제할 때 사용하는 HttpSession 객체 
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param model 화면에 결과값을 전달하기 위한 ModelMap 객체
	 * @return String 답글 등록 후 결과페이지를 표출할 파일경로를 문자열로 return
	*/
	@RequestMapping({"/write/set_reply.do", "/{domain}/write/set_reply.do"})
	public String reply(@ModelAttribute("writeVO") @Valid Dm_write_vo writeVO,  BindingResult br,
			HttpSession session, HttpServletRequest request, ModelMap model) {
		CommonUtil commonUtil = new CommonUtil();
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String mb_id = loginVO.getId() != null && !loginVO.getId().isEmpty() ? loginVO.getId() : "비회원";
		model.addAttribute("root", commonUtil.getDomain(request));
		
		if ("비회원".equals(mb_id)) {
			if (writeVO.getWr_password() == null || writeVO.getWr_password().isEmpty()) {
				model.addAttribute("message","board.invalid.password");
				return "egovframework/diam/web/base/board/result";
			}
		}
		
		if(br.hasErrors()) {
			List<FieldError> errors = br.getFieldErrors();
			String msg = errors.get(0).getDefaultMessage();
			
			model.addAttribute("message","board.fail.validate");
			model.addAttribute("notice", msg);
			return "egovframework/diam/web/base/board/result";
		}
		
		try {
			Dm_board_vo boardVO = new Dm_board_vo();
			boardVO.setDm_id(writeVO.getWr_board());
			boardVO = boardService.selectBoard(boardVO);
			
			if (boardVO != null) {
				Dm_write_vo parentVO = writeService.selectWrite(writeVO);
				
				if (parentVO != null) {
					if (!commonUtil.isNullOrEmpty(parentVO.getWr_is_notice()) && parentVO.getWr_is_notice().equals("1")) {
						model.addAttribute("message", "board.reply.notice");
						return "egovframework/diam/web/base/board/result";
					}
					
					if (writeVO.getFile().size() > 0) {
						for (int i=0 ; i < writeVO.getFile().size() ; i++) {
							boolean chkExt = commonUtil.prohibitExtCheck(writeVO.getFile().get(i));
							if (chkExt) {
								model.addAttribute("message","board.prohibit.ext");
								return "egovframework/diam/web/base/board/result";
							}
						}
					}
					if (writeVO.getWr_password() != null && !writeVO.getWr_password().isEmpty()) {
						String wr_password = writeVO.getWr_password();
						PrivateKey privateKey = (PrivateKey) session.getAttribute("DIAM_WEB_REPLY_RSA_KEY");
						wr_password = commonUtil.decryptRsa(privateKey, wr_password);
						writeVO.setWr_password(commonUtil.convertSHA256(wr_password));
					}
					writeVO.setWr_parent(parentVO.getWr_id());
					int maxReply = writeService.selectMaxReplyReply(writeVO);
					writeVO.setWr_reply(parentVO.getWr_reply() +1);
					writeVO.setWr_reply_reply(maxReply +1);
					writeVO.setWr_num(parentVO.getWr_num());
					writeVO.setReply_mb_id(parentVO.getMb_id());
					writeVO.setCa_name(parentVO.getCa_name());
					writeVO.setMb_id(mb_id);
					writeVO.setWr_ip(commonUtil.getUserIp(request));
									
					String FILE_PATH = request.getServletContext().getRealPath("/resources/board/"+boardVO.getDm_table())+"/";
					String basic_path = request.getServletContext().getRealPath("/");
					
					File folder = new File(FILE_PATH);				
					if (!folder.exists()) {
						folder.mkdirs();			
					}
					
					List<String> file_array = new ArrayList<String>();
					List<String> file_ori_array = new ArrayList<String>();
					
					for (int i=0; i<Integer.parseInt(boardVO.getDm_upload_count()); i++) {
						file_array.add("");
						file_ori_array.add("");
					}
					
					uploadWriteFile(boardVO, writeVO, file_array, file_ori_array, FILE_PATH, basic_path);
					
					writeVO.setWr_file(String.join("|", file_array));
					writeVO.setWr_ori_file_name(String.join("|", file_ori_array));
					
					//XSS필터링을 위한 신규추가
					writeVO.setWr_content(commonUtil.xssSaxFiltering(writeVO.getWr_content()));
					
					writeService.insertWrite(writeVO);
					
					session.removeAttribute("DIAM_WEB_REPLY_RSA_KEY");
					model.addAttribute("message","board.success.reply");			
				} else {
					model.addAttribute("message","board.notfound.write");
				}	
			} else {
				model.addAttribute("message","board.notfound.info");
			}			
		} catch(InvalidKeyException ike) {
			log.error(MessageCode.CMM_ENCRYPT_EXPIRED.getLog());
			model.addAttribute("message", "board.encrypt.expired");		
		} catch(BadPaddingException bpe) {
			log.error(MessageCode.CMM_ENCRYPT_EXPIRED.getLog());
			model.addAttribute("message", "board.encrypt.expired");		
		} catch(IOException ioe) {
			log.error(MessageCode.CMM_FILE_ERROR.getLog());
			model.addAttribute("message","board.file.error");
		} catch(DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			model.addAttribute("message","board.sql.error");
		} catch(Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			model.addAttribute("message","board.other.error");
		}
		return "egovframework/diam/web/base/board/result";
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
					file_array.set(i, "/resources/board/"+writeVO.getDm_table()+"/"+upload_name);
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
