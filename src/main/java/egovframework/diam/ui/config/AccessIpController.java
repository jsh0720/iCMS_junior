/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.ui.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.diam.biz.model.config.Dm_access_ip_vo;
import egovframework.diam.biz.service.config.AccessIpService;
import egovframework.diam.cmm.model.LoginVO;
import egovframework.diam.cmm.service.CommonService;
import egovframework.diam.cmm.util.CommonUtil;
import egovframework.diam.cmm.util.EgovUserDetailsHelper;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;

/**
 * @className : AccessIpController.java
 * @author : (주)디자인아이엠
 * @Date : 2023. 3. 21.
 * @Description : CMS 접근아이피관리 
 */
@Controller
@Log4j2
public class AccessIpController {
	
	@Resource(name="accessIpService")
	private AccessIpService accessIpService;
	
	@Resource(name="commonService")
	private CommonService commonService;

	/**
	 * @Method : get_access_ip_list
	 * @Description : CMS 접근아이피 리스트 조회
	 * @param accessIpVO
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/get_access_ip_list.do")
	public ResponseEntity<Object> get_access_ip_list(Dm_access_ip_vo accessIpVO) {
		Map<String, Object> resultMap = new HashMap<>();
		
		int row = accessIpVO.getRows() != 0 ? accessIpVO.getRows() : 50;
		int page = accessIpVO.getPage() != 0 ? accessIpVO.getPage() : 1;
		
		if (row < 1 || page < 0) {
			log.error(MessageCode.CMM_REQUEST_BADREQUEST.getLog());
			resultMap.put("notice", MessageCode.CMM_REQUEST_BADREQUEST.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
		} else {
			accessIpVO.setRows(row);
			accessIpVO.setPage(row * (page -1));
		}
		
		try {
			int accessIpListCnt = accessIpService.selectAccessIpListCnt(accessIpVO);
			List<Dm_access_ip_vo> accessIpList = accessIpService.selectAccessIpList(accessIpVO);
							
			resultMap.put("result", "success");
			resultMap.put("total", accessIpListCnt);
			resultMap.put("rows", accessIpList);
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
	 * @Method : get_access_ip
	 * @Description : CMS 접근아이피관리 상세
	 * @param accessIpVO
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/get_access_ip.do")
	public ResponseEntity<Object> get_access_ip(Dm_access_ip_vo accessIpVO){
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		
		try {
			if (!commonUtil.isNullOrEmpty(accessIpVO.getDm_id())) {
				
				accessIpVO = accessIpService.selectAccessIp(accessIpVO);
				
				if (accessIpVO == null) {
					resultMap.put("result", "fail");
					resultMap.put("notice", MessageCode.CMS_SELECT_NODATA.getMessage());
				} else {
					resultMap.put("result", "success");
					resultMap.put("rows", accessIpVO);
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
	 * @Method : delete_access_ip
	 * @Description : CMS 접근아이피 삭제
	 * @param ids
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/delete_access_ip.do")
	public ResponseEntity<Object> delete_access_ip(@RequestParam("dm_id[]") String ids[]){
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
			
			List<Dm_access_ip_vo> list = new ArrayList<>();
			Arrays.asList(ids).forEach(item -> {
				Dm_access_ip_vo vo = new Dm_access_ip_vo();
				vo.setDm_id(item);
				vo.setDm_delete_id(loginVO.getId());
				list.add(vo);
			});
			System.out.println("list : " + list);
			
			accessIpService.deleteAccessIp(list);
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
	 * @Method : set_access_ip
	 * @Description : CMS 접근아이피 등록및 수정
	 * @param accessIpVO
	 * @param br
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/set_access_ip.do")
	public ResponseEntity<Object> set_access_ip(@Valid Dm_access_ip_vo accessIpVO, BindingResult br) {
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
			
			if (commonUtil.isNullOrEmpty(accessIpVO.getDm_id())) {
				int duplication = accessIpService.selectAccessIpDuplicate(accessIpVO);
				if (duplication > 0) {
					resultMap.put("result", "fail");
					resultMap.put("notice", "동일한 아이피가 등록되어 있습니다. 다른 아이피를 입력해주세요.");
				} else {
					accessIpVO.setDm_create_id(loginVO.getId());
					result = accessIpService.insertAccessIp(accessIpVO);
					if (result > 0) {
						resultMap.put("result", "success");
						resultMap.put("notice", MessageCode.CMS_INSERT_SUCCESS.getMessage());
					} else {
						resultMap.put("result", "fail");
						resultMap.put("notice", MessageCode.CMS_INSERT_FAIL.getMessage());
					}
					
				}
				
			} else {
				accessIpVO.setDm_modify_id(loginVO.getId());
				result = accessIpService.updateAccessIp(accessIpVO);
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
}
