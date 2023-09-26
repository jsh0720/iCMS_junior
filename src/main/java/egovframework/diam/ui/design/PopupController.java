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
import egovframework.diam.biz.model.design.Dm_popup_vo;
import egovframework.diam.biz.service.config.DomainService;
import egovframework.diam.biz.service.design.PopupService;
import egovframework.diam.cmm.model.LoginVO;
import egovframework.diam.cmm.service.CommonService;
import egovframework.diam.cmm.util.CommonUtil;
import egovframework.diam.cmm.util.EgovUserDetailsHelper;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;


/**
 * @className : PopupController.java
 * @author : (주)디자인아이엠
 * @Date : 2023. 3. 21.
 * @Description : CMS 팝업관리
 */
@Controller
@Log4j2
public class PopupController {
	
	@Resource(name="popupService")
	private PopupService popupService;
	
	@Resource(name="commonService")
	private CommonService commonService;
	
	@Resource(name="domainService")
	private DomainService domainService;
	

	/**
	 * @Method : get_popup_list
	 * @Description : CMS 팝업 리스트 조회
	 * @param popupVO
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/get_popup_list.do")
	public ResponseEntity<Object> get_popup_list(Dm_popup_vo popupVO) {
		Map<String, Object> resultMap = new HashMap<>();
		
		int row = popupVO.getRows() != 0 ? popupVO.getRows() : 50;
		int page = popupVO.getPage() != 0 ? popupVO.getPage() : 1;
		
		if (row < 1 || page < 0) {
			log.error(MessageCode.CMM_REQUEST_BADREQUEST.getLog());
			resultMap.put("notice", MessageCode.CMM_REQUEST_BADREQUEST.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
		} else {
			popupVO.setRows(row);
			popupVO.setPage(row * (page -1));
		}
		
		try {
			int popupListCnt = popupService.selectPopupListCnt(popupVO);
			List<Dm_popup_vo> popupList = popupService.selectPopupList(popupVO);
			
			if (popupList.size() > 0) {
				popupList.forEach(item -> {
					item.setDm_infinite_text(item.getDm_is_infinite().equals("1") ? "사용" : "사용안함");
				});
			}
			
			resultMap.put("result", "success");
			resultMap.put("total", popupListCnt);
			resultMap.put("rows", popupList);
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
	 * @Method : get_popup
	 * @Description : CMS 팝업 상세
	 * @param popupVO
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/get_popup.do")
	public ResponseEntity<Object> get_popup(Dm_popup_vo popupVO) {
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		try {
			if (!commonUtil.isNullOrEmpty(popupVO.getDm_id())) {
				popupVO = popupService.selectPopup(popupVO);
				
				if (popupVO == null) {
					resultMap.put("result", "fail");
					resultMap.put("notice", MessageCode.CMS_SELECT_NODATA.getMessage());
				} else {
					resultMap.put("result", "success");
					resultMap.put("rows", popupVO);
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
	 * @Method : delete_popup
	 * @Description : CMS 팝업 삭제
	 * @param ids
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/delete_popup.do")
	public ResponseEntity<Object> delete_popup(@RequestParam("dm_id[]") String ids[]){
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
			List<Dm_popup_vo> list = new ArrayList<>();
			Arrays.asList(ids).forEach(item -> {
				Dm_popup_vo vo = Dm_popup_vo.builder()
						.dm_delete_id(loginVO.getId())
						.dm_id(item)
						.build();
				list.add(vo);
			});
			
			popupService.deletePopup(list);
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
	 * @Method : set_popup
	 * @Description : CMS 팝업 등록및 수정
	 * @param popupVO
	 * @param br
	 * @param request
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/set_popup.do")
	public ResponseEntity<Object> set_popup(@Valid Dm_popup_vo popupVO, BindingResult br, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String FILE_PATH = request.getServletContext().getRealPath("/") + "resources/popup/";
		
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
			domainVO.setDm_id(popupVO.getDm_domain());
			domainVO = domainService.selectDomainByDmid(domainVO);
			
			if (domainVO != null) {
				File folder = new File(FILE_PATH);
				if (!folder.exists()) {
					folder.mkdirs();
				}
								
				boolean chkImageExt = commonUtil.imageExtCheck(popupVO.getMultiFile());
				if (!chkImageExt) {
					resultMap.put("result", "fail");
					resultMap.put("notice", "jpg,jpeg,gif,png 확장자 파일만 업로드 가능합니다.");
					return new ResponseEntity<>(resultMap, HttpStatus.OK);
				}				
				
				int result = 0;
				
				if (commonUtil.isNullOrEmpty(popupVO.getDm_id())) {
					
					
					
					if (popupVO.getMultiFile().getSize() > 0) {
						uploadPopupFile(popupVO, null, FILE_PATH);
						
						popupVO.setDm_create_id(loginVO.getId());
						result = popupService.insertPopup(popupVO);
						
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
					Dm_popup_vo checkVO = new Dm_popup_vo();
					checkVO.setDm_id(popupVO.getDm_id());
					checkVO = popupService.selectPopup(checkVO);
					
					if (checkVO != null) {
						if (!commonUtil.isNullOrEmpty(popupVO.getDm_popup_del_img())) {
				        	if (popupVO.getMultiFile().getSize() > 0) {
				        		File file = new File(FILE_PATH+popupVO.getDm_popup_del_img()); 
								if( file.exists() ){
									FileDelete(file);
								}
				        	} else {
								resultMap.put("result", "fail");
								resultMap.put("notice", "팝업 이미지를 등록해주세요.");
								return new ResponseEntity<>(resultMap, HttpStatus.OK);
				        	}
						}
						uploadPopupFile(popupVO, checkVO, FILE_PATH);
						
						popupVO.setDm_modify_id(loginVO.getId());
						result = popupService.updatePopup(popupVO);
						
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
	 * @Method : uploadPopupFile
	 * @Description : CMS 팝업 파일 업로드
	 * @param popupVO
	 * @param checkVO
	 * @param file_path
	 * @throws Exception
	 */
	private void uploadPopupFile(Dm_popup_vo popupVO, Dm_popup_vo checkVO, String file_path) throws Exception {
		CommonUtil commonUtil = new CommonUtil();
		String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        
        if (popupVO.getMultiFile().getSize() > 0 ) {
        	popupVO.setDm_popup_img_ori(popupVO.getMultiFile().getOriginalFilename());
    		String ext = popupVO.getMultiFile().getOriginalFilename().substring(popupVO.getMultiFile().getOriginalFilename().indexOf(".") + 1);
			String upload_visual = today + "_" + commonUtil.convertSHA256(popupVO.getMultiFile().getOriginalFilename()) + "." + ext;
			popupVO.getMultiFile().transferTo(new File(file_path + upload_visual));
			popupVO.setDm_popup_img(upload_visual);
		} else {
			if (checkVO != null && (popupVO.getDm_popup_del_img() == null || popupVO.getDm_popup_del_img().isEmpty())) {
				popupVO.setDm_popup_img(checkVO.getDm_popup_img());
				popupVO.setDm_popup_img_ori(checkVO.getDm_popup_img_ori());
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
