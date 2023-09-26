package egovframework.diam.ui.admin;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
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
import javax.servlet.http.HttpSession;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.diam.biz.model.admin.Dm_admin_vo;
import egovframework.diam.biz.model.admin.Dm_admin_vo.AdminAdminGroup;
import egovframework.diam.biz.model.admin.Dm_admin_vo.DuplicateAdminGroup;
import egovframework.diam.biz.service.admin.AdminService;
import egovframework.diam.cmm.model.LoginVO;
import egovframework.diam.cmm.service.CommonService;
import egovframework.diam.cmm.util.CommonUtil;
import egovframework.diam.cmm.util.EgovUserDetailsHelper;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;


/**
 * @className : AdminController.java
 * @author : (주)디자인아이엠
 * @Date : 2023. 3. 21.
 * @Description : CMS 관리자 관리
 */
@Log4j2
@Controller
public class AdminController {
	
	@Resource(name="adminService")
	private AdminService adminService;
	
	@Resource(name="commonService")
	private CommonService commonService;
		

	/**
	 * @Method : get_admin_list
	 * @Description : CMS 관리자관리 리스트 조회
	 * @param adminVO
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/get_admin_list.do")
	public ResponseEntity<Object> get_admin_list(Dm_admin_vo adminVO) {
		Map<String, Object> resultMap = new HashMap<>();
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		adminVO.setDm_level(loginVO.getDm_level());
		
		int row = adminVO.getRows() != 0 ? adminVO.getRows() : 50;
		int page = adminVO.getPage() != 0 ? adminVO.getPage() : 1;
		
		if (row < 1 || page < 0) {
			log.error(MessageCode.CMM_REQUEST_BADREQUEST.getLog());
			resultMap.put("notice", MessageCode.CMM_REQUEST_BADREQUEST.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
		} else {
			adminVO.setRows(row);
			adminVO.setPage(row * (page -1));
		}		
		
		try {
			int adminCnt = adminService.selectAdminListCnt(adminVO);
			List<Dm_admin_vo> adminList = adminService.selectAdminList(adminVO);
			
			resultMap.put("result", "success");
			resultMap.put("total", adminCnt);
			resultMap.put("rows", adminList);
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
	 * @Method : get_admin
	 * @Description : CMS 관리자관리 상세
	 * @param adminVO
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/get_admin.do")
	public ResponseEntity<Object> get_admin(Dm_admin_vo adminVO) {
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		
		try {
			if (!commonUtil.isNullOrEmpty(adminVO.getDm_no())) {
				adminVO = adminService.selectAdmin(adminVO);
				if (adminVO == null) {
					resultMap.put("result", "fail");
					resultMap.put("notice", MessageCode.CMS_SELECT_NODATA.getMessage());
				} else {
					resultMap.put("result", "success");
					resultMap.put("rows", adminVO);
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
	 * @Method : delete_admin
	 * @Description : CMS 관리자관리 삭제
	 * @param ids
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/delete_admin.do")
	public ResponseEntity<Object> delete_admin(@RequestParam("dm_no[]") String ids[]) {
		Map<String, Object> resultMap = new HashMap<>();
		
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		boolean isCheck = true;
		try {
			if(ids.length > 0) {
				for (int i = 0 ; i < ids.length ; i++) {
					if (ids[i].equals(loginVO.getDm_no())) {
						resultMap.put("result", "fail");
						resultMap.put("notice", "현재 로그인중인 계정은 삭제할 수 없습니다.");
						return ResponseEntity.ok(resultMap);
					}
					Dm_admin_vo checkVO = new Dm_admin_vo();
					checkVO.setDm_no(ids[i]);
					checkVO = adminService.selectAdmin(checkVO);
					if (checkVO.getDm_id().equals("admin")) {
						isCheck = false;
					}
				}
				
				if(isCheck) {
					List<Dm_admin_vo> list = new ArrayList<>();
					Arrays.asList(ids).forEach(item -> {
						Dm_admin_vo vo = new Dm_admin_vo();
						vo.setDm_no(item);
						vo.setDm_delete_id(loginVO.getId());
						list.add(vo);
					});
					
					adminService.deleteAdmin(list);
					resultMap.put("result", "success");
					resultMap.put("notice", MessageCode.CMS_DELETE_SUCCESS.getMessage());
				} else {
					resultMap.put("result", "fail");
					resultMap.put("notice", "admin 계정은 삭제할 수 없습니다.");
				}
			} else {
				resultMap.put("result", "fail");
				resultMap.put("notice", MessageCode.CMM_REQUEST_BADREQUEST.getMessage());
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
	 * @Method : set_admin
	 * @Description : CMS 관리자관리 등록및 수정
	 * @param adminVO
	 * @param br
	 * @param request
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/set_admin.do")
	public ResponseEntity<Object> set_admin (@Validated(AdminAdminGroup.class) Dm_admin_vo adminVO, BindingResult br,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
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
			
		String dm_id = adminVO.getDm_id();		
		String dm_password = adminVO.getDm_password();				
					
		try {
			Dm_admin_vo checkVO = new Dm_admin_vo();
						
			if (dm_password != null && !"".equals(dm_password)) {
				PrivateKey privateKey = (PrivateKey) session.getAttribute("DIAM_ADMIN_RSA_KEY");
				dm_password = commonUtil.decryptRsa(privateKey, dm_password);
												
				String password_pattern = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$";
				Pattern pattern_chk = Pattern.compile(password_pattern);
				Matcher matcher = pattern_chk.matcher(dm_password);
				boolean pattern_result = matcher.find();
				
				if (!pattern_result) {
					resultMap.put("result", "fail");
					resultMap.put("notice", "패스워드 형식에 맞지않습니다. 공백(스페이스바)을 제외한 영문자,숫자,특수문자를 1개이상 포함하여 8~20자 입력해주세요.");
					return new ResponseEntity<>(resultMap, HttpStatus.OK);
				} else {
					adminVO.setDm_password(commonUtil.encryptPassword(dm_password, dm_id));
				}
			} else {
				if (commonUtil.isNullOrEmpty(adminVO.getDm_no())) {
					resultMap.put("result", "fail");
					resultMap.put("notice", "패스워드를 입력해주세요.");
					return new ResponseEntity<>(resultMap, HttpStatus.OK);
				}
			}
				
			adminVO.setDm_status("J");
			adminVO.setUser_se("USR");
			adminVO.setDm_ip(request.getRemoteAddr());
			
			if (commonUtil.isNullOrEmpty(adminVO.getDm_no())) {
				checkVO.setDm_id(adminVO.getDm_id());
				checkVO = adminService.selectAdminDupChk(checkVO);
				if (checkVO != null) {
					resultMap.put("result", "fail");
					resultMap.put("notice", "중복된 아이디입니다. 다른 아이디를 입력해주세요.");
				} else {
					adminVO.setDm_create_id(loginVO.getId());
					adminService.insertAdmin(adminVO);
					session.removeAttribute("DIAM_ADMIN_RSA_KEY");
					resultMap.put("result", "success");
					resultMap.put("notice", MessageCode.CMS_INSERT_SUCCESS.getMessage());
				}				
			} else if (!commonUtil.isNullOrEmpty(adminVO.getDm_no())) {
				checkVO.setDm_no(adminVO.getDm_no());
				checkVO = adminService.selectAdmin(checkVO);
				if (checkVO != null) {
					if (Integer.parseInt(checkVO.getDm_level()) < 6) {
						resultMap.put("result", "fail");
						resultMap.put("notice", "관리자 정보만 수정가능합니다.");	
					} else {
						if (checkVO.getDm_password().equals(adminVO.getDm_password())) {
							resultMap.put("result", "fail");
							resultMap.put("notice", "이전과 동일한 비밀번호로는 변경불가합니다. 이전 비밀번호와 다른 비밀번호를 입력해주세요.");
						} else {
							adminVO.setDm_modify_id(loginVO.getId());
							adminService.updateAdmin(adminVO);
							session.removeAttribute("DIAM_ADMIN_RSA_KEY");
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
	
	/**
	 * @Method : dup_admin_id
	 * @Description : CMS 관리자 관리 ID 중복체크
	 * @param adminVO
	 * @param br
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/dup_admin_id.do")
	public ResponseEntity<Object> dup_admin_id(@Validated(DuplicateAdminGroup.class) Dm_admin_vo adminVO, BindingResult br) {
		Map<String, Object> resultMap = new HashMap<>();
		
		if(br.hasErrors()) {
			List<FieldError> errors = br.getFieldErrors();
			String msg = errors.get(0).getDefaultMessage();
			
			resultMap.put("result", "fail");
			resultMap.put("notice", msg);
			return new ResponseEntity<>(resultMap, HttpStatus.OK);
		}
		
		try {
			Dm_admin_vo checkVO = new Dm_admin_vo();
			checkVO = adminService.selectAdminDupChk(adminVO);
			if (checkVO != null) {
				resultMap.put("result", "fail");
				resultMap.put("notice", adminVO.getDm_id()+"는 사용 불가능합니다. 다른 ID를 입력해주세요.");
				return new ResponseEntity<>(resultMap, HttpStatus.OK);
			} else {
				resultMap.put("result", "success");
				resultMap.put("notice", adminVO.getDm_id()+"는 사용 가능합니다.");
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
}
