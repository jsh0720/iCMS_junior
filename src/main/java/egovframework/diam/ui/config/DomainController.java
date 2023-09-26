package egovframework.diam.ui.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.diam.biz.model.config.Dm_domain_list_vo;
import egovframework.diam.biz.service.config.DomainService;
import egovframework.diam.cmm.model.LoginVO;
import egovframework.diam.cmm.util.CommonUtil;
import egovframework.diam.cmm.util.EgovUserDetailsHelper;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;



/**
 * @className : DomainController.java
 * @author : (주)디자인아이엠
 * @Date : 2023. 3. 21.
 * @Description : CMS 도메인관리
 */
@Controller
@Log4j2
public class DomainController {
	
	@Resource(name="domainService")
	private DomainService domainService;
	


	/**
	 * @Method : get_domain_list
	 * @Description : CMS 도메인 리스트 조회
	 * @param domainVO
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/get_domain_list.do")
	public ResponseEntity<Object> get_domain_list(Dm_domain_list_vo domainVO) {
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		
		int row = domainVO.getRows() != 0 ? domainVO.getRows() : 50;
		int page = domainVO.getPage() != 0 ? domainVO.getPage() : 1;
		
		if (row < 1 || page < 0) {
			log.error(MessageCode.CMM_REQUEST_BADREQUEST.getLog());
			resultMap.put("notice", MessageCode.CMM_REQUEST_BADREQUEST.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
		} else {
			domainVO.setRows(row);
			domainVO.setPage(row * (page -1));
		}
			
		try {
			int domainCnt = domainService.selectDomainListCnt(domainVO);
			List<Dm_domain_list_vo> domainList = domainService.selectDomainList(domainVO);
			if (domainList.size() > 0) {
				domainList.forEach(item -> {
					item.setDm_domain_main(commonUtil.isNullOrEmpty(item.getDm_domain_main()) ? "N" : "Y");
				});
				
			}
			resultMap.put("result", "success");
			resultMap.put("total", domainCnt);
			resultMap.put("rows", domainList);
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
	 * @Method : get_domain
	 * @Description : CMS 도메인 상세
	 * @param domainVO
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/get_domain.do")
	public ResponseEntity<Object> get_domain(Dm_domain_list_vo domainVO) {
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		
		try {
			if (!commonUtil.isNullOrEmpty(domainVO.getDm_id())) {
				domainVO = domainService.selectDomainByDmid(domainVO);
				
				if (domainVO != null) {
					resultMap.put("result", "success");
					resultMap.put("rows", domainVO);
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
	 * @Method : get_domain_main_cnt
	 * @Description : CMS에 등록된 메인도메인 수 체크
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/get_domain_main_cnt.do")
	public ResponseEntity<Object> get_domain_main_cnt() {
		Map<String , Object> resultMap = new HashMap<>();
		try {
			int mainChk = domainService.selectDomainMainCnt();
			resultMap.put("result", "success");
			resultMap.put("count", mainChk);
			
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
	 * @Method : delete_domain
	 * @Description : CMS 도메인 삭제
	 * @param domainVO
	 * @param request
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/delete_domain.do")
	public ResponseEntity<Object> delete_domain(Dm_domain_list_vo domainVO, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();	
		CommonUtil commonUtil = new CommonUtil();
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		try {
			if (commonUtil.isNullOrEmpty(loginVO.getId())) {
				resultMap.put("notice", MessageCode.CMM_SESSION_EXPIRED.getMessage());
				return new ResponseEntity<>(resultMap, HttpStatus.UNAUTHORIZED);
			}
			
			if (commonUtil.isNullOrEmpty(domainVO.getDm_id())) {
				resultMap.put("notice", MessageCode.CMM_REQUEST_BADREQUEST.getMessage());
				return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
			}
			
			domainVO = domainService.selectDomainByDmid(domainVO);
			if (domainVO != null) {
				
				String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
				String path = request.getServletContext().getRealPath("/WEB-INF/jsp/egovframework/") + "/diam/web/page/";
				Path oldPath = Paths.get(path + domainVO.getDm_domain_root() + "/");
				Path newPath = Paths.get(path + domainVO.getDm_domain_root() + "_" +today +"/");
				
				domainVO.setDm_delete_id(loginVO.getId());
				domainVO.setDm_domain_root(domainVO.getDm_domain_root() + "_" +today);
				int result = domainService.deleteDomain(domainVO);
				
				if (result > 0) {
					Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
					resultMap.put("result", "success");
					resultMap.put("notice", MessageCode.CMS_DELETE_SUCCESS.getMessage());
				} else {
					resultMap.put("result", "fail");
					resultMap.put("notice", MessageCode.CMS_DELETE_FAIL.getMessage());
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
		

	/**
	 * @Method : set_domain
	 * @Description : CMS 도메인 등록 및 수정
	 * @param domainVO
	 * @param br
	 * @param request
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/set_domain.do")
	public ResponseEntity<Object> set_domain(@Valid Dm_domain_list_vo domainVO, BindingResult br, HttpServletRequest request) {
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
			
			if (!commonUtil.isNullOrEmpty(domainVO.getDm_id())) {
				domainVO.setDm_modify_id(loginVO.getId());
				result = domainService.updateDomain(domainVO);
				if (result > 0) {
					resultMap.put("result", "success");
					resultMap.put("notice", MessageCode.CMS_UPDATE_SUCCESS.getMessage());									
				} else {
					resultMap.put("result", "fail");
					resultMap.put("notice", MessageCode.CMS_UPDATE_FAIL.getMessage());														
				}
				
			} else {
				int rootFolderChk = domainService.selectDomainRootDupCnt(domainVO);
				if (rootFolderChk > 0) {
					resultMap.put("result", "fail");
					resultMap.put("notice", "디렉토리가 중복됩니다. 다른 폴더명을 입력해주세요.");
					return new ResponseEntity<>(resultMap, HttpStatus.OK);
				}
				domainVO.setDm_create_id(loginVO.getId());
				result = domainService.insertDomain(domainVO);
				if (result > 0) {
					
					String file_path = request.getServletContext().getRealPath("/WEB-INF/jsp/egovframework/") + "/diam/web/page/" + domainVO.getDm_domain_root() + "/";
					File folder = new File(file_path);
					if (!folder.exists()) {
						folder.mkdirs();
					} else {
						resultMap.put("result", "fail");
						resultMap.put("notice", "동일한 디렉토리가 존재하여 폴더를 생성할 수 없습니다.");
						return new ResponseEntity<>(resultMap, HttpStatus.OK);
					}
					
					resultMap.put("result", "success");
					resultMap.put("notice", MessageCode.CMS_INSERT_SUCCESS.getMessage());
				} else {
					resultMap.put("result", "fail");
					resultMap.put("notice", MessageCode.CMS_INSERT_FAIL.getMessage());
				}
			}
		} catch (IOException ioe) {
			log.error(MessageCode.CMM_FILE_ERROR.getLog());
			resultMap.put("notice", MessageCode.CMM_FILE_ERROR.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
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
	 * @Method : dup_domain_directory
	 * @Description : CMS 도메인 ROOT 중복체크
	 * @param domainVO
	 * @param request
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/dup_domain_directory.do")
	public ResponseEntity<Object> dup_domain_directory(Dm_domain_list_vo domainVO, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
			int rootFolderChk = domainService.selectDomainRootDupCnt(domainVO);
			if (rootFolderChk > 0) {
				resultMap.put("result", "fail");
				resultMap.put("notice", "디렉토리가 중복됩니다. 다른 디렉토리를 입력해주세요.");
			} else {
				String file_path = request.getServletContext().getRealPath("/WEB-INF/jsp/egovframework/") + "/diam/web/page/" + domainVO.getDm_domain_root() + "/";
				File folder = new File(file_path);
				if (folder.exists()) {
					resultMap.put("result", "fail");
					resultMap.put("notice", "디렉토리가 중복됩니다. 다른 디렉토리를 입력해주세요.");
				} else {
					resultMap.put("result", "success");
					resultMap.put("notice", domainVO.getDm_domain_root() + " 디렉토리는 사용가능합니다.");					
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
