package egovframework.diam.ui.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.diam.biz.model.admin.Dm_access_admin_menu_vo;
import egovframework.diam.biz.service.admin.AdminMenuService;
import egovframework.diam.cmm.model.LoginVO;
import egovframework.diam.cmm.util.CommonUtil;
import egovframework.diam.cmm.util.EgovUserDetailsHelper;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;


/**
 * @className : AdminMenuController.java
 * @author : (주)디자인아이엠
 * @Date : 2023. 3. 21.
 * @Description : CMS 관리자메뉴 관리
 */
@Controller
@Log4j2
public class AdminMenuController {
		
	@Resource(name="adminMenuService")
	private AdminMenuService adminMenuService;

	/**
	 * @Method : getParentNode
	 * @Description : 전체메뉴를 조회한 데이터에서 root메뉴의 하위메뉴만 다시 조회 후 하위메뉴를 조회하는 getChildNode 메소드를 호출
	 * @param treesList
	 * @return String
	 * @throws Exception
	 */
	public String getParentNode(List<Dm_access_admin_menu_vo> treesList) throws Exception {
		Dm_access_admin_menu_vo vo = new Dm_access_admin_menu_vo();
		JSONArray newTrees = new JSONArray();
		if (treesList.size() > 0) {
			for (Dm_access_admin_menu_vo admin_menus_vo : treesList) {
				if ("0".equals(admin_menus_vo.getDm_parent_id())) {
					JSONObject jsonItem = new JSONObject();
					List<Dm_access_admin_menu_vo> treesLists = adminMenuService.selectAdminMenuRootChild(vo);
					jsonItem.put("children", getChildNode(admin_menus_vo.getDm_id(), treesLists));
					jsonItem.put("data", "");
					jsonItem.put("id", admin_menus_vo.getDm_id());
					jsonItem.put("text", admin_menus_vo.getDm_title());
					jsonItem.put("dm_parent_id", admin_menus_vo.getDm_parent_id());
					jsonItem.put("dm_parent_text", admin_menus_vo.getDm_parent_text());
					jsonItem.put("dm_link_url", admin_menus_vo.getDm_link_url());
					jsonItem.put("dm_access_level", admin_menus_vo.getDm_access_level());
					jsonItem.put("dm_status", admin_menus_vo.getDm_status());
					jsonItem.put("dm_view_order", admin_menus_vo.getDm_view_order() != null ? admin_menus_vo.getDm_view_order() : "");
					jsonItem.put("dm_create_id", admin_menus_vo.getDm_create_id());
					jsonItem.put("dm_create_dt", admin_menus_vo.getDm_create_dt());
					jsonItem.put("dm_modify_id", admin_menus_vo.getDm_modify_id());
					jsonItem.put("dm_modify_dt", admin_menus_vo.getDm_modify_dt());
					newTrees.put(jsonItem);
				}
			}
			return newTrees.toString();
		} else {
			return "";
		}
	}

	/**
	 * @Method : getChildNode
	 * @Description : getParentNode 함수에서 조회한 root메뉴의 하위메뉴들을 트리구조로 가공하기위해 재귀호출을 통하여 데이터 가공
	 * @param id
	 * @param treesLists
	 * @return JSONArray
	 * @throws Exception
	 */
	public JSONArray getChildNode(String id, List<Dm_access_admin_menu_vo> treesLists) throws Exception {
		JSONArray newTrees = new JSONArray();
		if (treesLists.size() > 0) {
			for (Dm_access_admin_menu_vo admin_menus_vo : treesLists) {
				if (admin_menus_vo.getDm_parent_id() != null && !"".equals(admin_menus_vo.getDm_parent_id())) {
					if (admin_menus_vo.getDm_parent_id().equals(id)) {
						JSONObject jsonItem = new JSONObject();
						jsonItem.put("children", getChildNode(admin_menus_vo.getDm_id(), treesLists));
						jsonItem.put("data", "");
						jsonItem.put("id", admin_menus_vo.getDm_id());
						jsonItem.put("text", admin_menus_vo.getDm_title());
						jsonItem.put("dm_parent_id", admin_menus_vo.getDm_parent_id());
						jsonItem.put("dm_parent_text", admin_menus_vo.getDm_parent_text());
						jsonItem.put("dm_link_url", admin_menus_vo.getDm_link_url());
						jsonItem.put("dm_access_level", admin_menus_vo.getDm_access_level());
						jsonItem.put("dm_status", admin_menus_vo.getDm_status());
						jsonItem.put("dm_view_order", admin_menus_vo.getDm_view_order());
						jsonItem.put("dm_create_id", admin_menus_vo.getDm_create_id());
						jsonItem.put("dm_create_dt", admin_menus_vo.getDm_create_dt());
						jsonItem.put("dm_modify_id", admin_menus_vo.getDm_modify_id());
						jsonItem.put("dm_modify_dt", admin_menus_vo.getDm_modify_dt());
						newTrees.put(jsonItem);
					}
				}
			}
		}
		return newTrees;
	}
	
	/**
	 * @Method : get_admin_menu_list
	 * @Description : 관리자페이지 관리자메뉴관리 페이지 tree구조 메뉴리스트에 사용할 등록된 모든 관리자메뉴 리스트데이터 조회
	 * @param adminMenuVO
	 * @return ResponseEntity
	 */
	@RequestMapping(value = "/adm/get_admin_menu_list.do", produces = "application/json; charset=utf8")
	public ResponseEntity<String> get_admin_menu_list(Dm_access_admin_menu_vo adminMenuVO) {
		
		String result = "";
		
		try {
			List<Dm_access_admin_menu_vo> adminMenuList = adminMenuService.selectAdminMenuList(adminMenuVO);
			result = getParentNode(adminMenuList);
		} catch (DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			return new ResponseEntity<>(MessageCode.CMM_DATA_ERROR.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (JSONException je) {
			log.error(MessageCode.CMM_JSON_ERROR.getLog());
			return new ResponseEntity<>(MessageCode.CMM_JSON_ERROR.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			return new ResponseEntity<>(MessageCode.CMM_SYSTEM_ERROR.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	/**
	 * @Method : get_admin_menu_table
	 * @Description : 관리자페이지 관리자메뉴관리 페이지 easyui-datagrid에서 사용할 등록된 모든 관리자메뉴 리스트데이터 조회 후 JSON형식으로 변환
	 * @param adminMenuVO
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/get_admin_menu_table.do")
	public ResponseEntity<Object> get_admin_menu_table(Dm_access_admin_menu_vo adminMenuVO){
		
		List<Dm_access_admin_menu_vo> adminMenuList = new ArrayList<>();
		try {
			adminMenuList = adminMenuService.selectAdminMenuList(adminMenuVO);
			
			if (adminMenuList.size() > 0) {
				adminMenuList.forEach(item -> {
					item.setDm_view_order(item.getDm_view_order() != null ? item.getDm_view_order() : "");
				});
			}
			
		} catch (DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			return new ResponseEntity<>(MessageCode.CMM_DATA_ERROR.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			return new ResponseEntity<>(MessageCode.CMM_SYSTEM_ERROR.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(adminMenuList, HttpStatus.OK);
	}
	
	/**
	 * @Method : delete_admin_menu
	 * @Description : 파라미터로 전달받은 관리자메뉴 PK값에 해당하는 관리자메뉴 및 하위메뉴 데이터 삭제
	 * @param vo
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/delete_admin_menu.do")
	public ResponseEntity<Object> delete_admin_menu(Dm_access_admin_menu_vo vo) {
		
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		
		if (commonUtil.isNullOrEmpty(vo.getDm_id())) {
			resultMap.put("notice", MessageCode.CMM_REQUEST_BADREQUEST.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
		}
		
		if (commonUtil.isNullOrEmpty(loginVO.getId())) {
			resultMap.put("notice", MessageCode.CMM_SESSION_EXPIRED.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.UNAUTHORIZED);
		}
		
		try {
			vo = adminMenuService.selectAdminMenuByDmId(vo);
			if (vo != null) {
				if (!"1".equals(vo.getDm_depth())) {
					List<Dm_access_admin_menu_vo> list = adminMenuService.selectAdminTreeMenu(vo);
					list.forEach(item -> {
						item.setDm_delete_id(loginVO.getId());
					});
					adminMenuService.deleteAdminMenuByDmid(list);
					
					resultMap.put("result", "success");
					resultMap.put("notice", MessageCode.CMS_DELETE_SUCCESS.getMessage());
				} else {
					resultMap.put("result", "fail");
					resultMap.put("notice", "최상위 메뉴는 삭제할 수 없습니다.");
				}
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
			resultMap.put("notice", MessageCode.CMM_TRANSACTION_FAIL.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch(Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			resultMap.put("notice", MessageCode.CMM_SYSTEM_ERROR.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(resultMap, HttpStatus.OK);
	}
			
	/**
	 * @Method : set_admin_menu
	 * @Description : CMS 관리자 메뉴 등록및 수정
	 * @param adminMenuVO
	 * @param br
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/set_admin_menu.do")
	public ResponseEntity<Object> set_admin_menu(@Valid Dm_access_admin_menu_vo adminMenuVO, BindingResult br) {
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		
		if (br.hasErrors()) {
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
		
		if (!"ROOT".equals(adminMenuVO.getDm_title())) {
			try {
				Dm_access_admin_menu_vo checkVO = new Dm_access_admin_menu_vo();
				checkVO.setDm_parent_id(adminMenuVO.getDm_parent_id());
				checkVO = adminMenuService.selectAdminMenuByParentId(checkVO);
				
				if (checkVO != null) {
					
					int result = 0;
					
					if (Integer.parseInt(checkVO.getDm_depth()) > 2) {
						resultMap.put("result", "fail");
						resultMap.put("notice", "3depth를 초과하여 메뉴를 등록할 수 없습니다.");
					} else {
						if (Integer.parseInt(checkVO.getDm_depth()) != 1) {
							int dupCount = adminMenuService.selectAdminMenuUrlDupCheck(adminMenuVO);
							if (dupCount > 0) {
								resultMap.put("result", "fail");
								resultMap.put("notice", "중복된 link url 데이터가 있습니다.");
								return new ResponseEntity<>(resultMap,HttpStatus.OK);
							}
						} 
						
						if (commonUtil.isNullOrEmpty(adminMenuVO.getDm_id())) {
							adminMenuVO.setDm_create_id(loginVO.getId());
							int depth = Integer.parseInt(checkVO.getDm_depth()) + 1;
							adminMenuVO.setDm_depth(Integer.toString(depth));
							result = adminMenuService.insertAdminMenu(adminMenuVO);
							if (result > 0) {
								resultMap.put("result", "success");
								resultMap.put("notice", MessageCode.CMS_INSERT_SUCCESS.getMessage());
							} else {
								resultMap.put("result", "fail");
								resultMap.put("notice", MessageCode.CMS_INSERT_FAIL.getMessage());
							}
							
						} else {
							adminMenuVO.setDm_modify_id(loginVO.getId());
							result = adminMenuService.updateAdminMenu(adminMenuVO);
							if (result > 0) {
								resultMap.put("result", "success");
								resultMap.put("notice", MessageCode.CMS_UPDATE_SUCCESS.getMessage());
							} else {
								resultMap.put("result", "success");
								resultMap.put("notice", MessageCode.CMS_UPDATE_FAIL.getMessage());
							}
						}
					}
				} else {
					resultMap.put("result", "fail");
					resultMap.put("notice", "상위메뉴 정보를 찾을 수 없습니다. 메뉴 선택 후 수정 혹은 신규생성을 진행해주세요.");
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
		} else {
			resultMap.put("result", "fail");
			resultMap.put("notice", "ROOT폴더는 등록/수정할 수 없습니다.");
		}

		return new ResponseEntity<>(resultMap, HttpStatus.OK);
	}
}
