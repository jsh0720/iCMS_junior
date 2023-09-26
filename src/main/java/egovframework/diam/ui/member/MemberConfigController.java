package egovframework.diam.ui.member;

import java.lang.reflect.Field;
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

import egovframework.diam.biz.model.member.Dm_member_config_vo;
import egovframework.diam.biz.service.member.MemberConfigService;
import egovframework.diam.cmm.model.LoginVO;
import egovframework.diam.cmm.util.CommonUtil;
import egovframework.diam.cmm.util.EgovUserDetailsHelper;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
public class MemberConfigController {
	
	@Resource(name="memberConfigService")
	private MemberConfigService memberConfigService;
	
	@RequestMapping("/adm/get_member_config.do")
	public ResponseEntity<Object> get_member_config(Dm_member_config_vo memberConfiVO) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
			memberConfiVO = memberConfigService.selectMemberConfig();
			if(memberConfiVO != null) {
				resultMap.put("result", "success");
				resultMap.put("rows", memberConfiVO);
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

	@RequestMapping("/adm/set_member_config.do")
	public ResponseEntity<Object> set_member_config(@Valid Dm_member_config_vo memberConfigVO, BindingResult br) throws Exception {
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> resultMap = new HashMap<>();
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
			int result = 0;
			
			Field[] keySet = ((Object) memberConfigVO).getClass().getDeclaredFields();
			Map<String, String> map = new HashMap<>();
			for (Field field : keySet) {
				field.setAccessible(true);
				if (field.getName().contains("use") || field.getName().contains("require")) {
					String key = field.getName().split("_")[field.getName().split("_").length-1];
					String val = (map.get(key) == null ? "" : map.get(key)) + (field.get(memberConfigVO) == null ? "0" : field.get(memberConfigVO).toString());
					map.put(key, val);					
				}
			}
			
			for (String str : map.keySet()) {
				if (map.get(str).equals("01")) {
					resultMap.put("result", "fail");
					resultMap.put("notice", "사용하지 않는 항목은 필수여부를 사용할 수 없습니다.");
					return new ResponseEntity<>(resultMap, HttpStatus.OK);
				}
			}
			
			if (commonUtil.isNullOrEmpty(memberConfigVO.getDm_id())) {
				memberConfigVO.setDm_create_id(loginVO.getId());
				result = memberConfigService.insertMemberConfig(memberConfigVO);
				
				if (result > 0) {
					resultMap.put("result", "success");
					resultMap.put("notice", MessageCode.CMS_INSERT_SUCCESS.getMessage());
				} else {
					resultMap.put("result", "fail");
					resultMap.put("notice", MessageCode.CMS_INSERT_FAIL.getMessage());
				}
			} else {
				memberConfigVO.setDm_modify_id(loginVO.getId());
				result = memberConfigService.updateMemberConfig(memberConfigVO);
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
			e.printStackTrace();
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			resultMap.put("notice", MessageCode.CMM_SYSTEM_ERROR.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(resultMap, HttpStatus.OK);
	}
}
