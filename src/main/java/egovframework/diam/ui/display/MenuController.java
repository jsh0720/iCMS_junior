/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.ui.display;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import egovframework.diam.biz.model.config.Dm_domain_list_vo;
import egovframework.diam.biz.model.display.Dm_menus_vo;
import egovframework.diam.biz.service.config.DomainService;
import egovframework.diam.biz.service.display.MenuService;
import egovframework.diam.cmm.model.LoginVO;
import egovframework.diam.cmm.util.CommonUtil;
import egovframework.diam.cmm.util.EgovUserDetailsHelper;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;

/**
 * @Class Name : MenuController.java
 * @Description : 관리자페이지 메뉴관리 CRUD기능구현 Controller클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
 */

@Controller
@Log4j2
public class MenuController {

	@Resource(name="menuService")
	private MenuService menuService;		
	
	@Resource(name="domainService")
	private DomainService domainService;	
	
	private int depth_i = 1;
	
	/**
	 * getParentNode
	 * 도메인PK에 해당하는 메뉴를 조회한 데이터에서 root메뉴의 하위메뉴만 다시 조회 후 하위메뉴를 조회하는 getChildNode 메소드를 호출
	 * @param treesList 도메인PK에 해당하는 메뉴를 조회한 리스트데이터를 전달받는 List 자료형
	 * @param site_id 도메인 PK값을 전달받는 문자열
	 * @return String getChildNode 메소드 재귀호출로 가공된 데이터를 문자열로 변환하여 return
	*/
	public String getParentNode(List<Dm_menus_vo> treesList, String site_id) throws Exception {
		Dm_menus_vo vo = new Dm_menus_vo();
		vo.setDm_domain(site_id);
		JSONArray newTrees = new JSONArray();
		if (treesList.size() > 0) {
			for (Dm_menus_vo dm_menus_vo : treesList) {
				if ("0".equals(dm_menus_vo.getDm_parent_id())) {
					JSONObject jsonItem = new JSONObject();
					List<Dm_menus_vo> treesLists = menuService.selectMenuRootChild(vo);
					jsonItem.put("children", getChildNode(dm_menus_vo.getDm_id(), treesLists));
					jsonItem.put("data", "");
					jsonItem.put("dm_create_dt", dm_menus_vo.getDm_create_dt());
					jsonItem.put("dm_create_id", dm_menus_vo.getDm_create_id());
					jsonItem.put("dm_link_data", dm_menus_vo.getDm_link_data());
					jsonItem.put("dm_link_type", dm_menus_vo.getDm_link_type());
					jsonItem.put("dm_menu_view", dm_menus_vo.getDm_menu_view());
					jsonItem.put("dm_menu_hidden", dm_menus_vo.getDm_menu_hidden());
					jsonItem.put("dm_modify_dt", dm_menus_vo.getDm_modify_dt());
					jsonItem.put("dm_modify_id", dm_menus_vo.getDm_modify_id());
					jsonItem.put("dm_parent_id", dm_menus_vo.getDm_parent_id());
					jsonItem.put("dm_parent_text", dm_menus_vo.getDm_parent_text());
					jsonItem.put("id", dm_menus_vo.getDm_id());
					jsonItem.put("text", dm_menus_vo.getDm_menu_text());
					newTrees.put(jsonItem);
				}
			}
		}
		return newTrees.toString();
	}
	
	/**
	 * getChildNode
	 * getParentNode 함수에서 조회한 root메뉴의 하위메뉴들을 트리구조로 가공하기위해 재귀호출을 통하여 데이터 가공
	 * @param id 상위메뉴 PK값을 전달받는 문자열
	 * @param treesLists getParentNode 함수에서 조회한 root메뉴의 하위메뉴 리스트를 전달받는 List 자료형
	 * @return JSONArray 하위메뉴 데이터 가공 후 JSONArray 객체로 return
	*/
	public JSONArray getChildNode(String id, List<Dm_menus_vo> treesLists) throws Exception{
		JSONArray newTrees = new JSONArray();
		if (treesLists.size() > 0) {
			for (Dm_menus_vo dm_menus_vo : treesLists) {
				if (dm_menus_vo.getDm_parent_id() != null && !"".equals(dm_menus_vo.getDm_parent_id())) {
					if (dm_menus_vo.getDm_parent_id().equals(id)) {
						JSONObject jsonItem = new JSONObject();
						jsonItem.put("children", getChildNode(dm_menus_vo.getDm_id(), treesLists));
						jsonItem.put("data", "");
						jsonItem.put("dm_create_dt", dm_menus_vo.getDm_create_dt());
						jsonItem.put("dm_create_id", dm_menus_vo.getDm_create_id());
						jsonItem.put("dm_link_data", dm_menus_vo.getDm_link_data());
						jsonItem.put("dm_link_type", dm_menus_vo.getDm_link_type());
						jsonItem.put("dm_menu_view", dm_menus_vo.getDm_menu_view());
						jsonItem.put("dm_menu_hidden", dm_menus_vo.getDm_menu_hidden());
						jsonItem.put("dm_modify_dt", dm_menus_vo.getDm_modify_dt());
						jsonItem.put("dm_modify_id", dm_menus_vo.getDm_modify_id());
						jsonItem.put("dm_parent_id", dm_menus_vo.getDm_parent_id());
						jsonItem.put("dm_parent_text", dm_menus_vo.getDm_parent_text());
						jsonItem.put("id", dm_menus_vo.getDm_id());
						jsonItem.put("text", dm_menus_vo.getDm_menu_text());
						newTrees.put(jsonItem);
					}
				}
			}
		}
		return newTrees;
	}
	
	/**
	 * getDepth
	 * 메뉴 등록 시 메뉴 depth를 계산하기 위해 상위메뉴PK값으로 등록된 메뉴를 조회 후, 재귀호출을 통해서 depth값 연산
	 * @param parentId 상위메뉴 PK값을 전달받는 문자열
	 * @return int 연산된 메뉴 depth 값을 정수형으로 전달
	*/
	public int getDepth(String parentId) throws Exception {
		Dm_menus_vo vo = new Dm_menus_vo();
		vo.setDm_parent_id(parentId);
		vo = menuService.selectMenuByParentId(vo);
		if (vo != null) {
			depth_i++;
			getDepth(vo.getDm_parent_id());
		}
		return depth_i;
	}
	/**
	 * get_admin_menu_list
	 * 관리자페이지 메뉴관리 페이지 tree구조 메뉴리스트에 사용할 등록된 선택된 도메인PK에 해당하는 사용자메뉴 리스트데이터 조회
	 * @param menuVO 사용자메뉴 도메인PK값을 vo객체에 담아 전달
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping(value = "/adm/get_menu_list.do", produces = "application/json; charset=utf8")
	public ResponseEntity<String> get_menu_list(Dm_menus_vo menuVO,
			HttpServletRequest request, HttpServletResponse response) {
		
		String result = "";
		
		try {
			List<Dm_menus_vo> menuList = menuService.selectMenuList(menuVO);	
			result = getParentNode(menuList, menuVO.getDm_domain());
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
	 * get_menu_table
	 * 관리자페이지 메뉴관리 페이지 easyui-datagrid에서 사용할 등록된 모든 사용자메뉴 리스트데이터 조회 후 JSON형식으로 변환
	 * @param menuVO 사용자메뉴 도메인PK값을 vo객체에 담아 전달
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/get_menu_table.do")
	public ResponseEntity<Object> get_menu_table(Dm_menus_vo menuVO) {
		
		List<Dm_menus_vo> list = new ArrayList<>();
		
		try {
			list = menuService.selectMenuListDesc(menuVO);
		} catch (DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			return new ResponseEntity<>(MessageCode.CMM_DATA_ERROR.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			return new ResponseEntity<>(MessageCode.CMM_SYSTEM_ERROR.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	/**
	 * delete_admin_menu
	 * 파라미터로 전달받은 관리자메뉴 PK값에 해당하는 관리자메뉴 및 하위메뉴 데이터 삭제
	 * @param dm_id 선택한 관리자메뉴 PK값을 문자열에 담아 전달
	 * @param dm_domain 선택한 도메인 PK값을 문자열에 담아 전달
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/delete_menu.do")
	public ResponseEntity<Object> delete_menu(Dm_menus_vo vo) {
		
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
			vo = menuService.selectMenuByDmId(vo);
			
			if (vo != null) {
				
				if (!"1".equals(vo.getDm_depth())) {
					List<Dm_menus_vo> list = menuService.selectTreeMenu(vo);
					list.forEach(item -> {
						item.setDm_delete_id(loginVO.getId());
					});
					menuService.deleteMenuByDmid(list);
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
	 * set_menu
	 * 사용자가 입력한 사용자메뉴 데이터의 insert/update 동작 수행
	 * @param type 입력 폼에서 전달받는 insert/update 수행명령값
	 * @param vo 사용자가 입력한 관리자메뉴 데이터를 전달받는 vo객체
	 * @param br vo객체의 hibernate validator 검증결과를 return 하기위한 객체
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 BindingResult객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/set_menu.do")
	public ResponseEntity<Object> set_menu(@Valid Dm_menus_vo vo, BindingResult br) {
		
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
		
		if (!"ROOT".equals(vo.getDm_menu_text())) {
			try {
				Dm_menus_vo checkVO = new Dm_menus_vo();
				checkVO.setDm_parent_id(vo.getDm_parent_id());
				checkVO = menuService.selectMenuByParentId(checkVO);
				if (checkVO != null) {
					
					Dm_domain_list_vo domainVO = new Dm_domain_list_vo();
					domainVO.setDm_id(vo.getDm_domain());
					domainVO = domainService.selectDomainByDmid(Dm_domain_list_vo.builder()
							.dm_id(vo.getDm_domain())
							.build());
					
					if (domainVO != null) {
						
						int result = 0;
						
						if (commonUtil.isNullOrEmpty(vo.getDm_id())) {
							vo.setDm_create_id(loginVO.getId());
							
							int depth = getDepth(vo.getDm_parent_id());
							depth_i = 1;
							vo.setDm_depth(Integer.toString(depth));
							result = menuService.insertMenu(vo);
							if (result > 0) {
								resultMap.put("result", "success");
								resultMap.put("notice", MessageCode.CMS_INSERT_SUCCESS.getMessage());
							} else {
								resultMap.put("result", "fail");
								resultMap.put("notice", MessageCode.CMS_INSERT_FAIL.getMessage());
							}
							
						} else {
							vo.setDm_modify_id(loginVO.getId());
							result = menuService.updateMenu(vo);
							if (result > 0) {
								resultMap.put("result", "success");
								resultMap.put("notice", MessageCode.CMS_UPDATE_SUCCESS.getMessage());
							} else {
								resultMap.put("result", "success");
								resultMap.put("notice", MessageCode.CMS_UPDATE_FAIL.getMessage());
							}
						}
						
					} else {
						resultMap.put("result", "fail");
						resultMap.put("notice", "도메인 정보를 찾을 수 없습니다.");
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
