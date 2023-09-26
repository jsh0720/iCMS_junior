/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.ui.config;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import egovframework.diam.biz.model.config.Dm_config_vo;
import egovframework.diam.biz.model.config.Dm_config_vo.validGroupConfig;
import egovframework.diam.biz.model.config.Dm_domain_list_vo;
import egovframework.diam.biz.service.config.ConfigService;
import egovframework.diam.biz.service.config.DomainService;
import egovframework.diam.cmm.util.CommonUtil;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;

/**
 * @className : ConfigController.java
 * @author : (주)디자인아이엠
 * @Date : 2023. 3. 21.
 * @Description : CMS 도메인 환경설정
 */
@Log4j2
@Controller
public class ConfigController {
	
	@Resource(name="configService")
	private ConfigService configService;
	
	@Resource(name="domainService")
	private DomainService domainService;
		
	/**
	 * get_env
	 * 전달받은 도메인PK 데이터로 조회한 1건의 도메인 기본설정 데이터를 화면에 전달
	 * @param configVO 도메인데이터 PK데이터를 전달받는 vo객체
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	/**
	 * @Method : get_env
	 * @Description : CMS 도메인 환경설정 상세
	 * @param configVO
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/get_env.do")
	public ResponseEntity<Object> get_env (Dm_config_vo configVO) {
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		try {
			if (!commonUtil.isNullOrEmpty(configVO.getDm_domain_id())) {
				configVO = configService.selectDmConfig(configVO);
				
				if (configVO != null) {
					resultMap.put("result", "success");
					resultMap.put("rows", configVO);
				} else {
					resultMap.put("result", "success");
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
		} catch (Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			resultMap.put("notice", MessageCode.CMM_SYSTEM_ERROR.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(resultMap, HttpStatus.OK);
	}
	

	/**
	 * @Method : set_env
	 * @Description : CMS 도메인 환경설정 저장
	 * @param configVO
	 * @param br
	 * @param request
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/set_config_env.do")
	public ResponseEntity<Object> set_env (@Validated(validGroupConfig.class) Dm_config_vo configVO, BindingResult br, HttpServletRequest request) {
		CommonUtil commonUtil = new CommonUtil();
		Map<String, Object> resultMap = new HashMap<>();
		
		if(br.hasErrors()) {
			List<FieldError> errors = br.getFieldErrors();
			String msg = errors.get(0).getDefaultMessage();
			
			resultMap.put("result", "fail");
			resultMap.put("notice", msg);
			return new ResponseEntity<>(resultMap, HttpStatus.OK);
		}
		int slashCount = configVO.getDm_url().length() - configVO.getDm_url().replaceAll("/", "").length();
		if (slashCount > 1) {
			resultMap.put("result", "fail");
			resultMap.put("notice", "슬래시 기호(/)는 한번만 입력 가능합니다.");
			return new ResponseEntity<>(resultMap, HttpStatus.OK);
		}
		
		if (configVO.getDm_url().contains("/adm")) {
			resultMap.put("result", "fail");
			resultMap.put("notice", "/adm은 사용 불가능합니다.");
			return new ResponseEntity<>(resultMap, HttpStatus.OK);			
		}
		
		String upload_top_path = "";
		String upload_bottom_path = "";
		String upload_personal_path = "";
					
		MultipartFile top_logo = configVO.getDm_top_logo_file();
		MultipartFile bottom_logo = configVO.getDm_bottom_logo_file();
		MultipartFile personal_image = configVO.getDm_personal_image_file();
				
		String dm_company_number = configVO.getDm_company_number1() + "-" + configVO.getDm_company_number2() + "-" + configVO.getDm_company_number3();
		String dm_ceo_email = configVO.getDm_ceo_email1() + "@" + configVO.getDm_ceo_email2();

		try {
			String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));	        
			
			Dm_domain_list_vo domainVO = domainService.selectDomainByDmid(Dm_domain_list_vo
					.builder()
					.dm_id(configVO.getDm_domain_id())
					.build());
			
			if (domainVO != null) {
				String FILE_PATH = request.getServletContext().getRealPath("/resources/config/"+domainVO.getDm_domain_root()) + "/";
				String BASIC_PATH = request.getServletContext().getRealPath("");
				File folder = new File(FILE_PATH);
				
				if (!folder.exists()) {
					folder.mkdirs();
				}
				
				String dm_del_top_logo = request.getParameter("dm_del_top_logo");
				String dm_del_bottom_logo = request.getParameter("dm_del_bottom_logo");
				String dm_del_personal_image = request.getParameter("dm_del_personal_image");
				
				if (dm_del_top_logo != null) {
					if (dm_del_top_logo.equals("1")) {
						File file = new File(BASIC_PATH+configVO.getDm_top_logo());
						if( file.exists() ){
							FileDelete(file);
						}
						configVO.setDm_top_logo("");
						configVO.setDm_top_logo_name("");
					}
				}
				
				if (dm_del_bottom_logo != null) {
					if (dm_del_bottom_logo.equals("1")) {
						File file = new File(BASIC_PATH+configVO.getDm_bottom_logo()); 
						if( file.exists() ){
							FileDelete(file);
						}
						configVO.setDm_bottom_logo("");
						configVO.setDm_bottom_logo_name("");
					}		
				}
				
				if (dm_del_personal_image != null) {
					if(dm_del_personal_image.equals("1")) {
						File file = new File(BASIC_PATH+configVO.getDm_personal_image());
						if(file.exists()) {
							FileDelete(file);
						}
						configVO.setDm_personal_image("");
						configVO.setDm_personal_image_original_name("");
					}
				}
				
				if (!top_logo.isEmpty()) {
					boolean chkImageExt = commonUtil.imageExtCheck(configVO.getDm_top_logo_file());
					if (!chkImageExt) {
						resultMap.put("result", "fail");
						resultMap.put("notice", "상단 로고는 jpg,jpeg,gif,png 확장자 파일만 업로드 가능합니다.");
						return new ResponseEntity<>(resultMap, HttpStatus.OK);
					}
					
					String upload_top_logo = today + "_" + commonUtil.uploadFileNameFiltering(top_logo.getOriginalFilename());
					upload_top_path = FILE_PATH + upload_top_logo;
					
					top_logo.transferTo(new File(upload_top_path));
					configVO.setDm_top_logo("/resources/config/"+ domainVO.getDm_domain_root() + "/" + upload_top_logo);
					configVO.setDm_top_logo_name(top_logo.getOriginalFilename());
				}
				
				if (!bottom_logo.isEmpty()) {
					boolean chkImageExt2 = commonUtil.imageExtCheck(configVO.getDm_bottom_logo_file());
					if (!chkImageExt2) {
						resultMap.put("result", "fail");
						resultMap.put("notice", "하단 로고는 jpg,jpeg,gif,png 확장자 파일만 업로드 가능합니다.");
						return new ResponseEntity<>(resultMap, HttpStatus.OK);
					}
					String upload_bottom_logo = today + "_" + commonUtil.uploadFileNameFiltering(bottom_logo.getOriginalFilename());
					upload_bottom_path = FILE_PATH + upload_bottom_logo;
					
					bottom_logo.transferTo(new File(upload_bottom_path));
					configVO.setDm_bottom_logo("/resources/config/"+ domainVO.getDm_domain_root() + "/" + upload_bottom_logo);
					configVO.setDm_bottom_logo_name(bottom_logo.getOriginalFilename());
				}
				
				if (!personal_image.isEmpty()) {
					boolean chkImageExt3 = commonUtil.imageExtCheck(configVO.getDm_personal_image_file());
					if(!chkImageExt3) {
						resultMap.put("result", "fail");
						resultMap.put("notice", "대표 이미지는 jpg,jpeg,gif,png 확장자 파일만 업로드 가능합니다.");
						return new ResponseEntity<>(resultMap, HttpStatus.OK);
					}
					String upload_personal_image = today + "_" + commonUtil.uploadFileNameFiltering(personal_image.getOriginalFilename());
					upload_personal_path = FILE_PATH + upload_personal_image;
					
					personal_image.transferTo(new File(upload_personal_path));
					configVO.setDm_personal_image("/resources/config/" + domainVO.getDm_domain_root() + "/" + upload_personal_image);
					configVO.setDm_personal_image_original_name(personal_image.getOriginalFilename());
				}
				
				configVO.setDm_site_name(domainVO.getDm_domain_nm());
				configVO.setDm_company_number(dm_company_number);
				configVO.setDm_ceo_email(dm_ceo_email);
				
				int urlDupCount = configService.selectDmConfigDuplicateUrl(configVO);
				if (urlDupCount > 0) {
					resultMap.put("result", "fail");
					resultMap.put("notice", "도메인주소가 중복됩니다. 다른 도메인주소를 입력해주세요.");
					return new ResponseEntity<>(resultMap, HttpStatus.OK);
				}
				
				int result = 0;
				if (commonUtil.isNullOrEmpty(configVO.getDm_id())) {
					Dm_config_vo checkVO = new Dm_config_vo();
					checkVO.setDm_domain_id(domainVO.getDm_id());
					int chk_cnt = configService.selectDmConfigDuplicate(checkVO);					
					
					if (chk_cnt > 0) {
						resultMap.put("result", "fail");
						resultMap.put("notice", "중복된 도메인설정값이 있습니다.");
					} else {
						result = configService.insertDmConfig(configVO);
						
						if (result > 0) {
							resultMap.put("result", "success");
							resultMap.put("notice", MessageCode.CMS_INSERT_SUCCESS.getMessage());
						} else {
							resultMap.put("result", "fail");
							resultMap.put("notice", MessageCode.CMS_INSERT_FAIL.getMessage());
						}
					}
					
				} else {
					result = configService.updateDmConfig(configVO);
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
				resultMap.put("notice", "수정하고자 하는 도메인 정보가 없습니다.");
			}
		} catch(IOException ioe) {
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
	 * @Method : set_page_env
	 * @Description : CMS 도메인 이용약관 상세
	 * @param configVO
	 * @param br
	 * @param request
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/set_page_env.do")
	public ResponseEntity<Object> set_page_env (@Validated(Dm_config_vo.validGroupPage.class) Dm_config_vo configVO, BindingResult br,
			HttpServletRequest request) {
		CommonUtil commonUtil = new CommonUtil();
		Map<String, Object> resultMap = new HashMap<>();
		
		if(br.hasErrors()) {
			List<FieldError> errors = br.getFieldErrors();
			String msg = errors.get(0).getDefaultMessage();
			
			resultMap.put("result", "fail");
			resultMap.put("notice", msg);
			return new ResponseEntity<>(resultMap, HttpStatus.OK);
		}
		
		try {
			Dm_config_vo checkVO = configService.selectDmConfig(configVO);
			if (checkVO != null) {
				configVO.setDm_private_text(commonUtil.xssSaxFiltering(configVO.getDm_private_text()));
				configVO.setDm_policy_text(commonUtil.xssSaxFiltering(configVO.getDm_policy_text()));
				configVO.setDm_reject_text(commonUtil.xssSaxFiltering(configVO.getDm_reject_text()));
				
				int result = configService.updateDmPage(configVO);
				
				if (result > 0) {
					resultMap.put("result", "success");
					resultMap.put("notice", MessageCode.CMS_UPDATE_SUCCESS.getMessage());
				} else {
					resultMap.put("result", "fail");
					resultMap.put("notice", MessageCode.CMS_UPDATE_FAIL.getMessage());
				}
			} else {
				resultMap.put("result", "fail");
				resultMap.put("notice", "기본 환경 설정 후 등록 가능합니다.");
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
	 * @Method : dup_domain_url
	 * @Description : CMS 도메인 URL 중복체크
	 * @param configVO
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/dup_domain_url.do")
	public ResponseEntity<Object> dup_domain_url(@Validated(Dm_config_vo.validDup.class) Dm_config_vo configVO, BindingResult br) {
		Map<String, Object> resultMap = new HashMap<>();
		if(br.hasErrors()) {
			List<FieldError> errors = br.getFieldErrors();
			String msg = errors.get(0).getDefaultMessage();
			
			resultMap.put("result", "fail");
			resultMap.put("notice", msg);
			return new ResponseEntity<>(resultMap, HttpStatus.OK);
		}
		
		int slashCount = configVO.getDm_url().length() - configVO.getDm_url().replaceAll("/", "").length();
		if (slashCount > 1) {
			resultMap.put("result", "fail");
			resultMap.put("notice", "슬래시 기호(/)는 한번만 입력 가능합니다.");
			return new ResponseEntity<>(resultMap, HttpStatus.OK);
		}
		if (configVO.getDm_url().contains("/adm")) {
			resultMap.put("result", "fail");
			resultMap.put("notice", "/adm은 사용 불가능합니다.");
			return new ResponseEntity<>(resultMap, HttpStatus.OK);			
		}
		try {
			int urlDupCount = configService.selectDmConfigDuplicateUrl(configVO);
			if (urlDupCount > 0) {
				resultMap.put("result", "fail");
				resultMap.put("notice", "도메인주소가 중복됩니다. 다른 도메인주소를 입력해주세요.");
			} else {
				resultMap.put("result", "success");
				resultMap.put("notice", configVO.getDm_url() + " 도메인주소는 사용가능합니다.");
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
	 * @Method : FileDelete
	 * @Description : 파일삭제
	 * @param file
	 */
	private synchronized void FileDelete(File file) {
		file.delete();
	}
}

