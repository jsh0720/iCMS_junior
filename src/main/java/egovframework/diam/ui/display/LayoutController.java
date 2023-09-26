/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.ui.display;

import java.io.File;
import java.io.IOException;
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
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.diam.biz.model.config.Dm_access_ip_vo;
import egovframework.diam.biz.model.display.Dm_layout_vo;
import egovframework.diam.biz.service.config.DomainService;
import egovframework.diam.biz.service.display.LayoutService;
import egovframework.diam.cmm.model.LoginVO;
import egovframework.diam.cmm.util.CommonUtil;
import egovframework.diam.cmm.util.EgovUserDetailsHelper;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;

/**
 * @Class Name : LayoutController.java
 * @Description : 관리자페이지 레이아웃 관리 CRUD기능구현 Controller클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
 */

@Log4j2
@Controller
public class LayoutController {

	@Resource(name="layoutService")
	private LayoutService layoutService;

	/**
	 * get_layout_list
	 * 전달받은 검색조건 데이터로 조회한 레이아웃 리스트 데이터를 화면에 전달
	 * @param layoutVO 레이아웃 데이터 검색조건을 전달받는 vo객체
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/get_layout_list.do")
	public ResponseEntity<Object> get_layout_list(Dm_layout_vo layoutVO) {
		Map<String, Object> resultMap = new HashMap<>();
		
		
		int row = layoutVO.getRows() != 0 ? layoutVO.getRows() : 50;
		int page = layoutVO.getPage() != 0 ? layoutVO.getPage() : 1;
		
		if (row < 1 || page < 0) {
			log.error(MessageCode.CMM_REQUEST_BADREQUEST.getLog());
			resultMap.put("notice", MessageCode.CMM_REQUEST_BADREQUEST.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
		} else{
			layoutVO.setRows(row);
			layoutVO.setPage(row * (page -1));
		}
		
		try {
			int layoutListCnt = layoutService.selectLayoutListCnt(layoutVO);
			List<Dm_layout_vo> layoutList = layoutService.selectLayoutList(layoutVO);
			
			resultMap.put("result", "success");
			resultMap.put("total", layoutListCnt);
			resultMap.put("rows", layoutList);
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
	 * get_layout
	 * 전달받은 레이아웃PK 데이터로 조회한 1건의 레이아웃 데이터를 화면에 전달
	 * @param layoutVO 레이아웃 데이터 PK데이터를 전달받는 vo객체
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/get_layout.do")
	public ResponseEntity<Object> get_layout(Dm_layout_vo layoutVO) {
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
			if (layoutVO.getDm_id() != null && !layoutVO.getDm_id().isEmpty()) {
				
				layoutVO = layoutService.selectLayout(layoutVO);
				
				if (layoutVO == null) {
					resultMap.put("result", "fail");
					resultMap.put("notice", MessageCode.CMS_SELECT_NODATA.getMessage());
				} else {
					resultMap.put("result", "success");
					resultMap.put("rows", layoutVO);
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
	 * get_layout_combo
	 * 페이지 등록 시 레이아웃 선택 콤보박스에 사용될 현재 등록된 레이아웃 리스트데이터 화면에 전달
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/get_layout_combo.do")
	public ResponseEntity<Object> get_layout_combo(Dm_layout_vo layoutVO) {
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
			List<Dm_layout_vo> layoutList = layoutService.selectLayoutCombo(layoutVO);
			
			if (layoutList.size() > 0) {
				resultMap.put("result", "success");
				resultMap.put("rows", layoutList);
				resultMap.put("notice", MessageCode.CMS_SELECT_SUCCESS.getMessage());
			} else {
				resultMap.put("result", "success");
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
	 * delete_layout
	 * request 객체를 통하여 전달받은 게시판 PK배열에 해당하는 레이아웃 데이터 삭제
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/delete_layout.do")
	public ResponseEntity<Object> delete_layout(Dm_layout_vo layoutVO, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		
		try {
			if (commonUtil.isNullOrEmpty(loginVO.getId())) {
				resultMap.put("notice", MessageCode.CMM_SESSION_EXPIRED.getMessage());
				return new ResponseEntity<>(resultMap, HttpStatus.UNAUTHORIZED);
			}
			
			if (commonUtil.isNullOrEmpty(layoutVO.getDm_id())) {
				resultMap.put("notice", MessageCode.CMM_REQUEST_BADREQUEST.getMessage());
				return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
			}
			
			layoutVO = layoutService.selectLayout(layoutVO);
			
			if(layoutVO != null) {
				
				String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
				String layoutPath = request.getServletContext().getRealPath("/WEB-INF/jsp/egovframework/diam/web/thema/") + layoutVO.getDm_layout_folder();
				String pluginPath = request.getServletContext().getRealPath("/thema/") + layoutVO.getDm_layout_folder();
				
				Path oldLayoutPath = Paths.get(layoutPath + "/");
				Path newLayoutPath = Paths.get(layoutPath + "_" +today +"/");
				Path oldPluginPath = Paths.get(pluginPath + "/");
				Path newPluginPath = Paths.get(pluginPath + "_" +today +"/");
				
				layoutVO.setDm_delete_id(loginVO.getId());
				layoutVO.setDm_layout_folder(layoutVO.getDm_layout_folder() + "_" +today);
				int result = layoutService.deleteLayout(layoutVO);
				
				if(result > 0) {
					Files.move(oldLayoutPath, newLayoutPath, StandardCopyOption.REPLACE_EXISTING);
					Files.move(oldPluginPath, newPluginPath, StandardCopyOption.REPLACE_EXISTING);
					
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
	 * set_layout
	 * 사용자가 입력한 레이아웃 데이터의 insert/update 동작 수행
	 * @param type 입력 폼에서 전달받는 insert/update 수행명령값
	 * @param layoutVO 사용자가 입력한 레이아웃 데이터를 전달받는 vo객체
	 * @param br vo객체의 hibernate validator 검증결과를 return 하기위한 객체
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 BindingResult객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/set_layout.do")
	public ResponseEntity<Object> set_layout(@Valid Dm_layout_vo layoutVO, BindingResult br, HttpServletRequest request ) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		
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
			int result = 0;
			
			//insert
			if (commonUtil.isNullOrEmpty(layoutVO.getDm_id())) {
				String defaultLayout = request.getServletContext().getRealPath("/WEB-INF/jsp/egovframework/diam/web/thema/default/");
				String defaultPlugin = request.getServletContext().getRealPath("/thema/default/");
				String saveLayout = request.getServletContext().getRealPath("/WEB-INF/jsp/egovframework/diam/web/thema/") + layoutVO.getDm_layout_folder();
				String savePlugin = request.getServletContext().getRealPath("/thema/") + layoutVO.getDm_layout_folder();
				
				File defaultLayoutFile = new File(defaultLayout);
				File defaultPluginFile = new File(defaultPlugin);			
				File saveLayoutFile = new File(saveLayout);
				File savePluginFile = new File(savePlugin);
				
				//db 중복검사
				int dupCnt = layoutService.selectLayoutFolderDupCnt(layoutVO);
				if(dupCnt > 0) {
					resultMap.put("result", "fail");
					resultMap.put("notice", "폴더명이 중복됩니다. 다른 폴더명을 입력해주세요.");
					return new ResponseEntity<>(resultMap, HttpStatus.OK);
				}
				
				if ((saveLayoutFile.exists() && saveLayoutFile.isDirectory()) || (savePluginFile.exists() && savePluginFile.isDirectory())) {
					resultMap.put("result", "fail");
					resultMap.put("notice", MessageCode.CMM_FILE_ERROR.getMessage());
				} else {
					layoutVO.setDm_create_id(loginVO.getId());
					result = layoutService.insertLayout(layoutVO);
					
					saveLayoutFile.mkdirs();
					savePluginFile.mkdirs();
					
					boolean copyLayout = commonUtil.layoutFileCopy(defaultLayoutFile, saveLayoutFile);
					boolean copyPlugin = commonUtil.layoutFileCopy(defaultPluginFile, savePluginFile);
					
					if (copyLayout == false || copyPlugin == false) {
						resultMap.put("result", "fail");
						resultMap.put("notice", MessageCode.CMM_DATA_ERROR.getMessage());
					} else {
						resultMap.put("result",  "success");
						resultMap.put("notice",  MessageCode.CMS_INSERT_SUCCESS.getMessage());
					}					
				}
				
				
			} else {
				layoutVO.setDm_modify_id(loginVO.getId());
				result = layoutService.updateLayout(layoutVO);
				if(result > 0) {
					resultMap.put("result", "success");
					resultMap.put("notice", MessageCode.CMS_UPDATE_SUCCESS.getMessage());
				} else {
					resultMap.put("result", "fail");
					resultMap.put("notice", MessageCode.CMS_SELECT_NODATA.getMessage());
				}
			}
			
		} catch(DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			resultMap.put("notice", MessageCode.CMM_DATA_ERROR.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch(IOException ioe) {
			log.error(MessageCode.CMM_FILE_ERROR.getLog());
			resultMap.put("notice", MessageCode.CMM_FILE_ERROR.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch(Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			resultMap.put("notice", MessageCode.CMM_SYSTEM_ERROR.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(resultMap, HttpStatus.OK);
	}
	
	@RequestMapping("/adm/dup_layout_folder.do")
	public ResponseEntity<Object> dup_layout_folder(Dm_layout_vo layoutVO, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			int dupChk = layoutService.selectLayoutFolderDupCnt(layoutVO);
			
			if (dupChk > 0) {
				resultMap.put("result", "fail");
				resultMap.put("notice", "폴더명이 중복됩니다. 다른 폴더명을 입력해주세요.");
				return new ResponseEntity<>(resultMap, HttpStatus.OK);
			} else {
				String saveLayout = request.getServletContext().getRealPath("/WEB-INF/jsp/egovframework/diam/web/thema/") + layoutVO.getDm_layout_folder();
				String savePlugin = request.getServletContext().getRealPath("/thema/") + layoutVO.getDm_layout_folder();
				File layoutFolder = new File(saveLayout);
				File pluginFolder = new File(savePlugin);
				
				if (layoutFolder.exists() || pluginFolder.exists()) {
					resultMap.put("result", "fail");
					resultMap.put("notice", "폴더명이 중복됩니다. 다른 폴더명을 입력해주세요.");
				} else {
					resultMap.put("result", "success");
					resultMap.put("notice", layoutVO.getDm_layout_folder() + " 폴더는 사용가능합니다.");
				}
				
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
}