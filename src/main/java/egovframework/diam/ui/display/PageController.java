/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.ui.display;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.diam.biz.model.board.Dm_board_vo;
import egovframework.diam.biz.model.config.Dm_domain_list_vo;
import egovframework.diam.biz.model.display.Dm_pages_vo;
import egovframework.diam.biz.service.board.BoardService;
import egovframework.diam.biz.service.config.DomainService;
import egovframework.diam.biz.service.display.LayoutService;
import egovframework.diam.biz.service.display.PageService;
import egovframework.diam.cmm.model.LoginVO;
import egovframework.diam.cmm.service.CommonService;
import egovframework.diam.cmm.util.CommonUtil;
import egovframework.diam.cmm.util.EgovUserDetailsHelper;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;

/**
 * @Class Name : PageController.java
 * @Description : 관리자페이지 페이지 관리 CRUD기능구현 Controller클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
 */

@Controller
@Log4j2
public class PageController {
	
	@Resource(name="pageService")
	private PageService pageService;
	
	@Resource(name="commonService")
	private CommonService commonService;
	
	@Resource(name="domainService")
	private DomainService domainService;
	
	@Resource(name="boardService")
	private BoardService boardService;
	
	@Resource(name="layoutService")
	private LayoutService layoutService;
	
	/**
	 * get_page_list
	 * 전달받은 검색조건 데이터로 조회한 페이지리스트 데이터를 화면에 전달
	 * @param pageVO 페이지데이터 검색조건을 전달받는 vo객체
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/get_page_list.do")
	public ResponseEntity<Object> get_page_list(Dm_pages_vo pageVO) {
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		
		int row = pageVO.getRows() != 0 ? pageVO.getRows() : 50;
		int page = pageVO.getPage() != 0 ? pageVO.getPage() : 1;
		
		if (row < 1 || page < 0) {
			log.error(MessageCode.CMM_REQUEST_BADREQUEST.getLog());
			resultMap.put("notice", MessageCode.CMM_REQUEST_BADREQUEST.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
		} else {
			pageVO.setRows(row);
			pageVO.setPage(row * (page -1));
		}
				
		try {			
			int pageListCnt = pageService.selectPageListCnt(pageVO);
			List<Dm_pages_vo> pageList = pageService.selectPageList(pageVO);
			if (pageList.size() > 0) {
				pageList.forEach(item -> {
					item.setDm_main_content(commonUtil.isNullOrEmpty(item.getDm_main_content()) ? "N" : "Y");
				});
			}
			
			resultMap.put("result", "success");
			resultMap.put("total", pageListCnt);
			resultMap.put("rows", pageList);
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
	 * get_page
	 * 전달받은 페이지PK 데이터로 조회한 1건의 페이지데이터를 화면에 전달
	 * @param pageVO 페이지데이터 PK데이터를 전달받는 vo객체
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/get_page.do")
	public ResponseEntity<Object> get_page(Dm_pages_vo pageVO) {
		
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		
		try {
			if (!commonUtil.isNullOrEmpty(pageVO.getDm_id())) {
				pageVO = pageService.selectPage(pageVO);
				
				if (pageVO != null) {
					resultMap.put("result", "success");
					resultMap.put("rows", pageVO);
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
	 * get_page_history
	 * 전달받은 페이지PK로 조회 후 Uid에 해당하는 버전이력 리스트 데이터를 화면에 전달
	 * @param pageVO 페이지PK값을 전달받는 vo객체
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/get_page_history.do")
	public ResponseEntity<Object> get_page_history(Dm_pages_vo pageVO) {
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		
		try {
			if (!commonUtil.isNullOrEmpty(pageVO.getDm_id())) {
				pageVO = pageService.selectPage(pageVO);
				
				if (pageVO != null) {
					List<Dm_pages_vo> historyList = pageService.selectPageHistory(pageVO);
					if (historyList.size() > 0) {
						historyList.forEach(item -> {
							item.setDm_main_content(commonUtil.isNullOrEmpty(item.getDm_main_content()) ? "N" : "Y");
						});
						
					} 
					resultMap.put("result", "success");
					resultMap.put("rows", historyList);
					resultMap.put("notice", MessageCode.CMS_SELECT_SUCCESS.getMessage());
				} else {
					resultMap.put("result", "fail");
					resultMap.put("notice", MessageCode.CMS_SELECT_NODATA.getMessage());
				}
				
			} else {
				log.error(MessageCode.CMM_REQUEST_BADREQUEST.getLog());
				resultMap.put("notice", MessageCode.CMM_REQUEST_BADREQUEST);
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
	 * change_page_version
	 * 전달받은 페이지PK로 조회 후 메인페이지 여부와 중복여부 확인 후 페이지 버전 업데이트
	 * @param pageVO 페이지PK값을 전달받는 vo객체
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/change_page_version.do")
	public ResponseEntity<Object> change_page_version(Dm_pages_vo pageVO) {
		
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
			pageVO = pageService.selectPage(pageVO);
			if (pageVO != null) {
				if ("PAGE".equals(pageVO.getDm_page_type())) {
					int pageMainCheck = pageService.selectPageMainCnt(pageVO);
					if (pageMainCheck > 0 && "1".equals(pageVO.getDm_main_content())) {
						resultMap.put("result", "fail");
						resultMap.put("notice", "사용중인 페이지 중 해당하는 도메인의 메인페이지로 설정된 페이지가 있습니다.");
						return new ResponseEntity<>(resultMap, HttpStatus.OK);
					}				
				} else if ("BOARD".equals(pageVO.getDm_page_type())) {
					int pageBoardCheck = pageService.selectPageBoardChk(pageVO);
					if (pageBoardCheck > 0) {
						resultMap.put("result", "fail");
						resultMap.put("notice", "사용중인 페이지 중 버전변경될 페이지와 동일한 게시판페이지가 있습니다.");
						return new ResponseEntity<>(resultMap, HttpStatus.OK);
					}
				} else {
					int pageUtilCheck = pageService.selectPageUtilCnt(pageVO);
					if (pageUtilCheck > 0) {
						resultMap.put("result", "fail");
						resultMap.put("notice", "사용중인 페이지 중 해당하는 도메인의 "+pageVO.getDm_page_type()+" 페이지로 설정된 페이지가 있습니다.");
						return new ResponseEntity<>(resultMap, HttpStatus.OK);
					}
				}
				
				pageService.updatePageDmStatusByDmuid(pageVO);
				pageService.updatePageDmStatusByDmid(pageVO);
				
				resultMap.put("result", "success");
				resultMap.put("notice", "페이지 버전을 변경하였습니다.");				
			} else {
				resultMap.put("result", "fail");
				resultMap.put("notice", "페이지 정보가 없습니다.");
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
	 * delete_page
	 * request 객체를 통하여 전달받은 페이지PK,도메인PK 배열에 해당하는 페이지데이터 삭제
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return 
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/delete_page.do")
	public ResponseEntity<Object> delete_page(@RequestParam("dm_id[]") String ids[]) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		CommonUtil commonUtil = new CommonUtil();
		
		if (commonUtil.isNullOrEmpty(loginVO.getId())) {
			resultMap.put("notice", MessageCode.CMM_SESSION_EXPIRED.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
		}
		
		try {
			if (ids.length > 0) {
				List<Dm_pages_vo> list = new ArrayList<>();
				for (String item : ids) {
					Dm_pages_vo check = pageService.selectPage(Dm_pages_vo.builder().dm_id(item).build());
					if (check.getDm_main_content() != null && check.getDm_main_content().equals("1")) {
						resultMap.put("result", "fail");
						resultMap.put("notice", "메인페이지로 등록된 페이지는 삭제할 수 없습니다.");
						return new ResponseEntity<>(resultMap, HttpStatus.OK);
					}
					list.addAll(pageService.selectPageHistory(check));
				}
				if (list.size() > 0) {
					list.forEach(item -> {
						item.setDm_delete_id(loginVO.getId());
					});
					pageService.deletePage(list);
					resultMap.put("result", "success");
					resultMap.put("notice", MessageCode.CMS_DELETE_SUCCESS.getMessage());
				} else {
					resultMap.put("result", "fail");
					resultMap.put("notice", MessageCode.CMS_SELECT_NODATA.getMessage());
				}
			} else {
				resultMap.put("result", "fail");
				resultMap.put("notice", MessageCode.CMS_SELECT_NODATA.getMessage());
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
	 * get_page_combo
	 * 사용자메뉴 등록 시 페이지 선택 콤보박스에 현재 사용중이고 메인페이지가 아닌 페이지리스트 데이터를 전달
	 * @param pageVO 도메인PK값을 전달받는 vo객체
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/get_page_combo.do")
	public ResponseEntity<Object> get_page_combo(Dm_pages_vo pageVO) {
		List<Dm_pages_vo> list = new ArrayList<>();
		
		try {
			list = pageService.selectPageListCombo(pageVO);
		} catch (DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			String result = "페이지 리스트를 불러오던 중 " + MessageCode.CMM_DATA_ERROR.getMessage();
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			String result = "페이지 리스트를 불러오던 중 " + MessageCode.CMM_SYSTEM_ERROR.getMessage();
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
		
	/**
	 * set_page
	 * 사용자가 입력한 페이지데이터의 insert/update 동작 수행
	 * @param pageVO 사용자가 입력한 게시판데이터를 전달받는 vo객체
	 * @param br vo객체의 hibernate validator 검증결과를 return 하기위한 객체
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 BindingResult객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/set_page.do")
	public ResponseEntity<Object> set_page(@Valid Dm_pages_vo pageVO, BindingResult br, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		CommonUtil commonUtil = new CommonUtil();
		
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
		
		try {		
			Dm_domain_list_vo domainVO = new Dm_domain_list_vo();
			domainVO.setDm_id(pageVO.getDm_domain());
			domainVO = domainService.selectDomainByDmid(domainVO);
			
			if (domainVO != null) {
				
				String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
				String page_type = pageVO.getDm_page_type().toLowerCase();
				String path = "egovframework/diam/web/";
				String name = "";
				
				if ("1".equals(pageVO.getDm_main_content())) {
					if ("page".equals(page_type)) {
						if (pageService.selectPageMainCnt(pageVO) > 0) {
							resultMap.put("result", "fail");
							resultMap.put("notice", "사용중인 페이지 중 해당하는 도메인의 메인페이지로 설정된 페이지가 있습니다.");
							return new ResponseEntity<>(resultMap, HttpStatus.OK);
						}
					} else {
						resultMap.put("result", "fail");
						resultMap.put("notice", "메인페이지를 적용할 수 없는 페이지입니다. 메인페이지 체크를 해제해주세요.");
						return new ResponseEntity<>(resultMap, HttpStatus.OK);
					}
				}
				
				if (commonUtil.isNullOrEmpty(pageVO.getDm_id())) {
					switch (page_type) {
					case "page":
						pageVO.setDm_board_id("");
						path = path + "page/" + domainVO.getDm_domain_root() + "/";
						String dir = request.getServletContext().getRealPath("/WEB-INF/jsp/egovframework/") + "/diam/web/page/" + domainVO.getDm_domain_root() + "/";
						
						if ("1".equals(pageVO.getDm_main_content())) name = "main_" + today;
						else name = "sub_" + today;
						
						if ("1".equals(pageVO.getDm_main_content())) {
							if (!createMainPage(request, dir, name)) throw new IOException();
						} else {
							if (!createPage(dir, name)) throw new IOException();							
						}
						
						break;
					case "board":
						if (!checkBoardType(pageVO)) {
							resultMap.put("result", "fail");
							resultMap.put("notice", "게시판이 중복되거나 존재하지 않습니다.");
							return new ResponseEntity<>(resultMap, HttpStatus.OK);
						}
						path = path + "base/" +page_type + "/";
						name = "list";
						break;
					default:
						pageVO.setDm_board_id("");
						if (!checkOtherType(pageVO)) {
							resultMap.put("result", "fail");
							resultMap.put("notice", "동일한 유형의 페이지가 존재합니다.");
							return new ResponseEntity<>(resultMap, HttpStatus.OK);
						}
						
						path = path + "base/" + page_type + "/";
						name = page_type;
						break;
					}
					String sha256Result = commonUtil.convertSHA256(today);
					pageVO.setDm_uid(sha256Result);
					pageVO.setDm_file_name(name);
					pageVO.setDm_file_src(path);
					pageVO.setDm_version("0.1");
					pageVO.setDm_status("1");
					pageVO.setDm_create_id(loginVO.getId());
					int result = pageService.insertPage(pageVO);
					if (result > 0) {
						resultMap.put("result", "success");
						resultMap.put("notice", MessageCode.CMS_INSERT_SUCCESS.getMessage());
					} else {
						resultMap.put("result", "fail");
						resultMap.put("notice", MessageCode.CMS_INSERT_FAIL.getMessage());
					}
					
				} else {
					if (!commonUtil.isNullOrEmpty(pageVO.getDm_uid())) {
						if (pageService.selectPageDmUidCnt(pageVO) < 1) {
							resultMap.put("result", "fail");
							resultMap.put("notice", "유효하지 않는 uid입니다.");
							return new ResponseEntity<>(resultMap, HttpStatus.OK);
						}
					} else {
						resultMap.put("result", "fail");
						resultMap.put("notice", "유효하지 않는 uid입니다.");
						return new ResponseEntity<>(resultMap, HttpStatus.OK);
					}
					if (!pageVO.getDm_page_type().equals("BOARD")) {
						pageVO.setDm_board_id("");
					}
					pageVO.setDm_modify_id(loginVO.getId());
					int result = pageService.updatePage(pageVO);
					if (result > 0) {
						resultMap.put("result", "success");
						resultMap.put("notice", MessageCode.CMS_UPDATE_SUCCESS.getMessage());
					} else {
						resultMap.put("result", "fail");
						resultMap.put("notice", MessageCode.CMS_UPDATE_FAIL.getMessage());
					}
 				}

				
			} else {
				resultMap.put("result", "fail");
				resultMap.put("notice", "도메인 정보가 없습니다.");
				return new ResponseEntity<>(resultMap, HttpStatus.OK);
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
	
	@PostMapping("/adm/insertNewVersion.do")
	public ResponseEntity<Object> insertNewVersion(Dm_pages_vo pageVO, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		CommonUtil commonUtil = new CommonUtil();
		
		if (commonUtil.isNullOrEmpty(loginVO.getId())) {
			resultMap.put("notice", MessageCode.CMM_SESSION_EXPIRED.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
		}
		
		try {
			pageVO = pageService.selectPage(pageVO);
			
			if (pageVO != null) {
				String version = pageService.selectPageVersion(pageVO);
				BigDecimal dm_version;
				BigDecimal add_version = new BigDecimal("0.1");
				
				if (version != null) {
					dm_version = new BigDecimal(version);
					dm_version = dm_version.add(add_version);
				} else {
					dm_version = new BigDecimal("0.1");
				}
				
				String dir = request.getServletContext().getRealPath("/WEB-INF/jsp/"+pageVO.getDm_file_src());
				String name = "";
				String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
				
				if ("1".equals(pageVO.getDm_main_content())) name = "main_" + today;
				else name = "sub_" + today;
				
				if (!createPage(dir, name)) throw new IOException();
				Path oriFile = Paths.get(dir+pageVO.getDm_file_name()+".jsp");
				Path newFile = Paths.get(dir+name+".jsp");
				
				if (!Files.exists(newFile.getParent())) {
					Files.createDirectories(newFile.getParent());
				}
				Files.copy(oriFile, newFile, StandardCopyOption.REPLACE_EXISTING);
				
				pageVO.setDm_file_name(name);
				pageVO.setDm_create_id(loginVO.getId());
				pageVO.setDm_version(String.valueOf(dm_version));
				pageVO.setDm_status("0");
				
				int result = pageService.insertPage(pageVO);
				if (result > 0) {
					resultMap.put("result", "success");
					resultMap.put("notice", MessageCode.CMS_INSERT_SUCCESS.getMessage());
				} else {
					resultMap.put("result", "fail");
					resultMap.put("notice", MessageCode.CMS_INSERT_FAIL.getMessage());
				}
			} else {
				resultMap.put("result", "fail");
				resultMap.put("notice", MessageCode.CMS_SELECT_NODATA.getMessage());
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
	
	@PostMapping("/adm/deleteVersion.do")
	public ResponseEntity<Object> deleteVersion(Dm_pages_vo pageVO) {
		Map<String, Object> resultMap = new HashMap<>();
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		CommonUtil commonUtil = new CommonUtil();
		
		if (commonUtil.isNullOrEmpty(loginVO.getId())) {
			resultMap.put("notice", MessageCode.CMM_SESSION_EXPIRED.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
		}
		try {
			pageVO = pageService.selectPage(pageVO);
			if (pageVO != null) {
				if (pageVO.getDm_status().equals("1")) {
					resultMap.put("result", "fail");
					resultMap.put("notice", "적용 중인 버전은 삭제할 수 없습니다.");
					return new ResponseEntity<>(resultMap, HttpStatus.OK);
				}
				pageVO.setDm_delete_id(loginVO.getId());
				List<Dm_pages_vo> list = Arrays.asList(pageVO);
				pageService.deletePage(list);
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
			log.error(MessageCode.CMS_DELETE_FAIL.getLog());
			resultMap.put("notice", MessageCode.CMS_DELETE_FAIL.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);			
		} catch(Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			resultMap.put("notice", MessageCode.CMM_SYSTEM_ERROR.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(resultMap, HttpStatus.OK);
	}
	
	
	
	public boolean createPage(String dir, String name) throws Exception {
		boolean result = false;
		
		File folder = new File(dir);
		if (!folder.exists()) folder.mkdirs();
		
		File page_file = new File(dir + "/" + name +".jsp");
		
		if (page_file.createNewFile()) {
			FileWriter fw = new FileWriter(dir + "/" + name + ".jsp");
			fw.write("<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\" pageEncoding=\"UTF-8\"%>");
			fw.write("<div style=\"text-align: center;\"><img style=\"width: 100%; max-width: 950px; margin: 0 auto;\" src=\"/images/under_construction.jpg\" class=\"under_construction\" alt=\"준비중 입니다.\"/></div>");
			fw.flush();
			fw.close();
			result = true;
		}
		
		return result;
	}
	
	public boolean createMainPage(HttpServletRequest request, String dir, String name) throws Exception {
		try {
			String cpPath = request.getServletContext().getRealPath("/WEB-INF/jsp/egovframework/diam/web/page/main_cp.jsp");
			Path defaultPage = Paths.get(cpPath);
			Path newFile = Paths.get(dir+name+".jsp");
			Files.copy(defaultPage, newFile, StandardCopyOption.REPLACE_EXISTING);
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	// board 타입의 페이지 게시판 중복 여부 검사
	public boolean checkBoardType(Dm_pages_vo pageVO) throws Exception {
		boolean result = true;
		
		int dupBoardChk = pageService.selectPageBoardChk(pageVO);
		
		if (dupBoardChk > 0) result = false;
		
		else {
			Dm_board_vo boardVO = new Dm_board_vo();
			boardVO.setDm_id(pageVO.getDm_board_id());
			boardVO = boardService.selectBoard(boardVO);
			
			if (boardVO == null) result = false;
		}
			
		return result;
	}
	
	//board, page 타입이 아닌 유형 중복 검사
	public boolean checkOtherType(Dm_pages_vo pageVO) throws Exception {
		boolean result = true;
		if (pageService.selectPageUtilCnt(pageVO) > 0) result = false;
		
		return result;
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
