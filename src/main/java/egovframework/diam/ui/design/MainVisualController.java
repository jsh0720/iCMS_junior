package egovframework.diam.ui.design;

import java.io.File;
import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.diam.biz.model.config.Dm_domain_list_vo;
import egovframework.diam.biz.model.design.Dm_main_visual_vo;
import egovframework.diam.biz.service.config.DomainService;
import egovframework.diam.biz.service.design.MainVisualService;
import egovframework.diam.cmm.model.LoginVO;
import egovframework.diam.cmm.service.CommonService;
import egovframework.diam.cmm.util.CommonUtil;
import egovframework.diam.cmm.util.EgovUserDetailsHelper;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;


/**
 * @className : MainVisualController.java
 * @author : (주)디자인아이엠
 * @Date : 2023. 3. 21.
 * @Description : CMS 비주얼 관리 
 */
@Controller
@Log4j2
public class MainVisualController {
	
	@Resource(name="mainVisualService")
	private MainVisualService mainVisualService;
	
	@Resource(name="commonService")
	private CommonService commonService;
	
	@Resource(name="domainService")
	private DomainService domainService;
	

	/**
	 * @Method : get_main_visual_list
	 * @Description : CMS 비주얼 리스틑 조회
	 * @param mainVisualVO
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/get_main_visual_list.do")
	public ResponseEntity<Object> get_main_visual_list (Dm_main_visual_vo mainVisualVO) {
		Map<String, Object> resultMap = new HashMap<>();
		
		int row = mainVisualVO.getRows() != 0 ? mainVisualVO.getRows() : 50;
		int page = mainVisualVO.getPage() != 0 ? mainVisualVO.getPage() : 1;
		
		if (row < 1 || page < 0) {
			log.error(MessageCode.CMM_REQUEST_BADREQUEST.getLog());
			resultMap.put("notice", MessageCode.CMM_REQUEST_BADREQUEST.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
		} else {
			mainVisualVO.setRows(row);
			mainVisualVO.setPage(row * (page -1));
		}
		
		try {
			int mainVisualListCnt = mainVisualService.selectMainVisualListCnt(mainVisualVO);
			List<Dm_main_visual_vo> mainVisualList = mainVisualService.selectMainVisualList(mainVisualVO);
			if (mainVisualList.size() > 0) {
				mainVisualList.forEach(item -> {
					item.setDm_infinite_text(item.getDm_is_infinite().equals("1") ? "사용" : "사용안함");
				});
			}
			resultMap.put("result", "success");
			resultMap.put("total", mainVisualListCnt);
			resultMap.put("rows", mainVisualList);
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
	 * @Method : get_main_visual
	 * @Description : CMS 비주얼 상세
	 * @param mainVisualVO
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/get_main_visual.do")
	public ResponseEntity<Object> get_main_visual(Dm_main_visual_vo mainVisualVO) {
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		
		try {
			if (!commonUtil.isNullOrEmpty(mainVisualVO.getDm_id())) {
				
				mainVisualVO = mainVisualService.selectMainVisualByDmid(mainVisualVO);
				
				if (mainVisualVO == null) {
					resultMap.put("result", "fail");
					resultMap.put("notice", MessageCode.CMS_SELECT_NODATA.getMessage());
				} else {
					resultMap.put("result", "success");
					resultMap.put("rows", mainVisualVO);
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
	 * @Method : delete_main_visual
	 * @Description : CMS 비주얼 삭제
	 * @param ids
	 * @return
	 */
	@RequestMapping("/adm/delete_main_visual.do")
	public ResponseEntity<Object> delete_main_visual (@RequestParam("dm_id[]") String ids[]){
		
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
			
			List<Dm_main_visual_vo> list = new ArrayList<>();
			Arrays.asList(ids).forEach(item -> {
				Dm_main_visual_vo vo = Dm_main_visual_vo.builder()
						.dm_delete_id(loginVO.getId())
						.dm_id(item)
						.build();
				list.add(vo);
			});
			
			mainVisualService.deleteMainVisual(list);
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
	 * @Method : set_main_visual
	 * @Description : CMS 비주얼 등록및 수정
	 * @param mainVisualVO
	 * @param br
	 * @param request
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/set_main_visual.do")
	public ResponseEntity<Object> set_main_visual (@Valid Dm_main_visual_vo mainVisualVO, 
			BindingResult br, HttpServletRequest request) {
		
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String FILE_PATH = request.getServletContext().getRealPath("/") + "resources/main/";
		
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
			domainVO.setDm_id(mainVisualVO.getDm_domain());
			domainVO = domainService.selectDomainByDmid(domainVO);
			
			if (domainVO != null) {
				File folder = new File(FILE_PATH);
				if (!folder.exists()) {
					folder.mkdirs();
				}
				
				boolean chkImageExt = commonUtil.imageExtCheck(mainVisualVO.getMultifile1());
				if (!chkImageExt) {
					resultMap.put("result", "fail");
					resultMap.put("notice", "jpg,jpeg,gif,png 확장자 파일만 업로드 가능합니다.");
					return new ResponseEntity<>(resultMap, HttpStatus.OK);
				}
				
				int result = 0;
				
				if (commonUtil.isNullOrEmpty(mainVisualVO.getDm_id())) {
					
					if (mainVisualVO.getMultifile1().getSize() > 0) {
						uploadMainVisualFile(mainVisualVO, null, FILE_PATH);
						
						mainVisualVO.setDm_create_id(loginVO.getId());
						result = mainVisualService.insertMainVisual(mainVisualVO);
						if (result > 0) {
							resultMap.put("result", "success");
							resultMap.put("notice", MessageCode.CMS_INSERT_SUCCESS.getMessage());							
						} else {
							resultMap.put("result", "fail");
							resultMap.put("notice", MessageCode.CMS_INSERT_FAIL.getMessage());
						}
					} else {
						resultMap.put("result", "fail");
						resultMap.put("notice", "메인비주얼 이미지를 등록해주세요.");
					}
					
				} else {
					
					Dm_main_visual_vo checkVO = new Dm_main_visual_vo();
					checkVO.setDm_id(mainVisualVO.getDm_id());
					checkVO = mainVisualService.selectMainVisualByDmid(checkVO);
					
					if (checkVO != null) {
						if (!commonUtil.isNullOrEmpty(mainVisualVO.getDm_del_image())) {
				        	if (mainVisualVO.getMultifile1().getSize() > 0) {
				        		File file = new File(FILE_PATH+mainVisualVO.getDm_del_image()); 
								if( file.exists() ){
									FileDelete(file);
								}
				        	} else {
				        		resultMap.put("result", "fail");
								resultMap.put("notice", "메인비주얼 이미지를 등록해주세요.");
								return new ResponseEntity<>(resultMap, HttpStatus.OK);
				        	}							
						}
						uploadMainVisualFile(mainVisualVO, checkVO, FILE_PATH);		
						
						mainVisualVO.setDm_modify_id(loginVO.getId());
						result = mainVisualService.updateMainVisual(mainVisualVO);
						if (result > 0) {
							resultMap.put("result", "success");
							resultMap.put("notice", MessageCode.CMS_UPDATE_SUCCESS.getMessage());							
						} else {
							resultMap.put("result", "success");
							resultMap.put("notice", MessageCode.CMS_UPDATE_FAIL.getMessage());
						}
					} else {
						resultMap.put("result", "fail");
						resultMap.put("notice", "수정하고자 하는 메인비주얼 정보가 없습니다.");
					}
				}
				
			} else {
				resultMap.put("result", "fail");
				resultMap.put("notice", "등록하고자 하는 도메인 정보가 없습니다.");
			}			
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
	 * @Method : uploadMainVisualFile
	 * @Description : CMS 비주얼 파일업로드
	 * @param mainVisualVO
	 * @param checkVO
	 * @param file_path
	 * @throws Exception
	 */
	private void uploadMainVisualFile(Dm_main_visual_vo mainVisualVO, Dm_main_visual_vo checkVO, String file_path) throws Exception {
		CommonUtil commonUtil = new CommonUtil();
		String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")); 
		
    	if (mainVisualVO.getMultifile1().getSize() > 0 ) {
    		mainVisualVO.setDm_visual_name_ori(mainVisualVO.getMultifile1().getOriginalFilename());
    		String ext = mainVisualVO.getMultifile1().getOriginalFilename().substring(mainVisualVO.getMultifile1().getOriginalFilename().indexOf(".") + 1);
			String upload_visual = today + "_" + commonUtil.convertSHA256(mainVisualVO.getMultifile1().getOriginalFilename()) + "." + ext;
			mainVisualVO.getMultifile1().transferTo(new File(file_path + upload_visual));
			mainVisualVO.setDm_visual_name(upload_visual);
		} else {
			if (checkVO != null && (mainVisualVO.getDm_del_image() == null || mainVisualVO.getDm_del_image().isEmpty())) {
				mainVisualVO.setDm_visual_name(checkVO.getDm_visual_name());
				mainVisualVO.setDm_visual_name_ori(checkVO.getDm_visual_name_ori());
			}
		}
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
