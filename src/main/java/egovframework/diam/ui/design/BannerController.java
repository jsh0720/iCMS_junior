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
import egovframework.diam.biz.model.design.Dm_banners_vo;
import egovframework.diam.biz.service.config.DomainService;
import egovframework.diam.biz.service.design.BannerService;
import egovframework.diam.cmm.model.LoginVO;
import egovframework.diam.cmm.util.CommonUtil;
import egovframework.diam.cmm.util.EgovUserDetailsHelper;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;


/**
 * @className : BannerController.java
 * @author : (주)디자인아이엠
 * @Date : 2023. 3. 21.
 * @Description : CMS 배너관리
 */
@Controller
@Log4j2
public class BannerController {
	
	@Resource(name="bannerService")
	private BannerService bannerService;
	
	@Resource(name="domainService")
	private DomainService domainService;
	

	
	/**
	 * @Method : get_banner_list
	 * @Description : CMS 배너리스트 조회
	 * @param bannerVO
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/get_banner_list.do")
	public ResponseEntity<Object> get_banner_list(Dm_banners_vo bannerVO) {
		Map<String, Object> resultMap = new HashMap<>();
		
		int row = bannerVO.getRows() != 0 ? bannerVO.getRows() : 50;
		int page = bannerVO.getPage() != 0 ? bannerVO.getPage() : 1;
		
		if (row < 1 || page < 0) {
			log.error(MessageCode.CMM_REQUEST_BADREQUEST.getLog());
			resultMap.put("notice", MessageCode.CMM_REQUEST_BADREQUEST.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
		} else {
			bannerVO.setRows(row);
			bannerVO.setPage(row * (page -1));
		}
		
		
		try {		
			int bannerListCnt = bannerService.selectBannerListCnt(bannerVO);
			List<Dm_banners_vo> bannerList = bannerService.selectBannerList(bannerVO);
			
			if (bannerList.size() > 0) {
				bannerList.forEach(item -> {
					item.setDm_infinite_text(item.getDm_is_infinite().equals("1") ? "사용" : "사용안함");
				});
			}
			
			resultMap.put("result", "success");
			resultMap.put("total", bannerListCnt);
			resultMap.put("rows", bannerList);
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
	 * @Method : get_banner
	 * @Description : CMS 배너 상세
	 * @param bannerVO
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/get_banner.do")
	public ResponseEntity<Object> get_banner(Dm_banners_vo bannerVO) {
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		
		try {
			if (!commonUtil.isNullOrEmpty(bannerVO.getDm_id())) {
				bannerVO = bannerService.selectBanner(bannerVO);
				
				if (bannerVO == null) {
					resultMap.put("result", "fail");
					resultMap.put("notice", MessageCode.CMS_SELECT_NODATA.getMessage());
				} else {
					resultMap.put("result", "success");
					resultMap.put("rows", bannerVO);
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
	 * @Method : delete_banner
	 * @Description : CMS 배너 삭제
	 * @param ids
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/delete_banner.do")
	public ResponseEntity<Object> delete_banner(@RequestParam("dm_id[]") String ids[]) {
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
			
			List<Dm_banners_vo> list = new ArrayList<>();
			Arrays.asList(ids).forEach(item -> {
				Dm_banners_vo vo = Dm_banners_vo.builder()
						.dm_delete_id(loginVO.getId())
						.dm_id(item)
						.build();
				list.add(vo);
			});
			bannerService.deleteBanner(list);
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
	 * @Method : set_banner
	 * @Description : CMS 배너 등록및 수정
	 * @param bannerVO
	 * @param br
	 * @param request
	 * @return ResponseEntity
	 */
	@RequestMapping("/adm/set_banner.do")
	public ResponseEntity<Object> set_banner(@Valid Dm_banners_vo bannerVO, BindingResult br, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String FILE_PATH = request.getServletContext().getRealPath("/") + "resources/banner/";
		
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
			domainVO.setDm_id(bannerVO.getDm_domain());
			domainVO = domainService.selectDomainByDmid(domainVO);
			
			if (domainVO != null) {
				File folder = new File(FILE_PATH);
				if (!folder.exists()) {
					folder.mkdirs();
				}
								
				boolean chkImageExt = commonUtil.imageExtCheck(bannerVO.getMultiFile());
				if (!chkImageExt) {
					resultMap.put("result", "fail");
					resultMap.put("notice", "jpg,jpeg,gif,png 확장자 파일만 업로드 가능합니다.");
					return new ResponseEntity<>(resultMap, HttpStatus.OK);
				}
				
				int result = 0;
				
				if (commonUtil.isNullOrEmpty(bannerVO.getDm_id())) {
					
					if (bannerVO.getMultiFile().getSize() > 0) {
						uploadBannerFile(bannerVO, null, FILE_PATH);
						
						bannerVO.setDm_create_id(loginVO.getId());
						result = bannerService.insertBanner(bannerVO);
						
						if (result > 0) {
							resultMap.put("result", "success");
							resultMap.put("notice", MessageCode.CMS_INSERT_SUCCESS.getMessage());							
						} else {
							resultMap.put("result", "fail");
							resultMap.put("notice", MessageCode.CMS_INSERT_FAIL.getMessage());
						}
						
					} else {
						resultMap.put("result", "fail");
						resultMap.put("notice", "배너 이미지를 등록해주세요.");
					}					
				} else {
					Dm_banners_vo checkVO = new Dm_banners_vo();
					checkVO.setDm_id(bannerVO.getDm_id());
					checkVO = bannerService.selectBanner(checkVO);
					
					if (checkVO != null) {
						if (!commonUtil.isNullOrEmpty(bannerVO.getDm_del_image())) {
				        	if (bannerVO.getMultiFile().getSize() > 0) {
				        		File file = new File(FILE_PATH+bannerVO.getDm_del_image()); 
								if( file.exists() ){
									FileDelete(file);
								}
				        	} else {
								resultMap.put("result", "fail");
								resultMap.put("notice", "배너 이미지를 등록해주세요.");
								return new ResponseEntity<>(resultMap, HttpStatus.OK);
				        	}		
						}
						uploadBannerFile(bannerVO, checkVO, FILE_PATH);
						
						bannerVO.setDm_modify_id(loginVO.getId());
						result = bannerService.updateBanner(bannerVO);
						
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
	 * @Method : uploadBannerFile
	 * @Description : CMS 배너 파일 업로드
	 * @param bannerVO
	 * @param checkVO
	 * @param file_path
	 * @throws Exception
	 */
	private void uploadBannerFile(Dm_banners_vo bannerVO, Dm_banners_vo checkVO, String file_path) throws Exception {
		CommonUtil commonUtil = new CommonUtil();
		String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		
        if (bannerVO.getMultiFile().getSize() > 0 ) {
        	bannerVO.setDm_banner_img_ori(bannerVO.getMultiFile().getOriginalFilename());
    		String ext = bannerVO.getMultiFile().getOriginalFilename().substring(bannerVO.getMultiFile().getOriginalFilename().indexOf(".") + 1);
			String upload_visual = today + "_" + commonUtil.convertSHA256(bannerVO.getMultiFile().getOriginalFilename()) + "." + ext;
			bannerVO.getMultiFile().transferTo(new File(file_path + upload_visual));
			bannerVO.setDm_banner_img(upload_visual);
		} else {
			if (checkVO != null && (bannerVO.getDm_del_image() == null || bannerVO.getDm_del_image().isEmpty())) {
				bannerVO.setDm_banner_img(checkVO.getDm_banner_img());
				bannerVO.setDm_banner_img_ori(checkVO.getDm_banner_img_ori());
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
