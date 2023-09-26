/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.ui.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringEscapeUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.diam.biz.model.board.Dm_faq_vo;
import egovframework.diam.biz.model.board.Dm_faq_vo.FaqAdminGroup;
import egovframework.diam.biz.model.config.Dm_access_ip_vo;
import egovframework.diam.biz.model.config.Dm_config_vo;
import egovframework.diam.biz.model.config.Dm_domain_list_vo;
import egovframework.diam.biz.service.board.FaqService;
import egovframework.diam.biz.service.config.DomainService;
import egovframework.diam.cmm.model.LoginVO;
import egovframework.diam.cmm.service.CommonService;
import egovframework.diam.cmm.util.CommonUtil;
import egovframework.diam.cmm.util.EgovUserDetailsHelper;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;

/**
 * @Class Name : FaqController.java
 * @Description : 관리자페이지 FAQ 관리 CRUD기능구현 Controller클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
 */

@Log4j2
@Controller
public class FaqController {
	
	@Resource(name="faqService")
	private FaqService faqService;
	
	@Resource(name="commonService")
	private CommonService commonService;
	
	@Resource(name="domainService")
	private DomainService domainService;
	
	/**
	 * get_faq_list
	 * 전달받은 검색조건 데이터로 조회한 FAQ리스트 데이터를 화면에 전달
	 * @param faqVO FAQ데이터 검색조건을 전달받는 vo객체
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/get_faq_list.do")
	public ResponseEntity<Object> get_faq_list(Dm_faq_vo faqVO) {
		Map<String, Object> resultMap = new HashMap<>();
		
		int row = faqVO.getRows() != 0 ? faqVO.getRows() : 50;
		int page = faqVO.getPage() != 0 ? faqVO.getPage() : 1;
		
		if (row < 1 || page < 0) {
			log.error(MessageCode.CMM_REQUEST_BADREQUEST.getLog());
			resultMap.put("notice", MessageCode.CMM_REQUEST_BADREQUEST.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
		} else {
			faqVO.setRows(row);
			faqVO.setPage(row * (page -1));
		}
		
		try {
			int faqListCnt = faqService.selectFaqListCnt(faqVO);
			List<Dm_faq_vo> faqList = faqService.selectFaqList(faqVO);
						
			resultMap.put("result", "success");
			resultMap.put("total", faqListCnt);
			resultMap.put("rows", faqList);
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
	 * get_faq_list_web
	 * 사용자페이지의 도메인에 해당하는 FAQ리스트 데이터를 화면에 전달
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
/*	@RequestMapping("/adm/get_faq_list_web.do")
	public ResponseEntity<Object> get_faq_list_web() throws Exception {
		JSONObject result = new JSONObject();
		JSONArray jsonList = new JSONArray();
		try {
			Dm_config_vo configVO = (Dm_config_vo) request.getAttribute("CONFIG_INFO");
			Dm_faq_vo faqVO = new Dm_faq_vo();
			faqVO.setDm_domain(configVO.getDm_domain_id());
			List<Dm_faq_vo> faqList = faqService.selectFaqListForWeb(faqVO);
			
			if (faqList.size() > 0) {
				for (int i=0 ; i<faqList.size() ; i++) {
					JSONObject jsonItem = new JSONObject();
					jsonItem.put("dm_question", faqList.get(i).getDm_question());
					jsonItem.put("dm_answer", faqList.get(i).getDm_answer());
					jsonItem.put("dm_order", faqList.get(i).getDm_order());
					jsonItem.put("dm_status", faqList.get(i).getDm_status());
					jsonItem.put("dm_state_text", faqList.get(i).getDm_state_txt());
					jsonItem.put("dm_create_dt", faqList.get(i).getDm_create_dt());
					jsonItem.put("dm_create_id", faqList.get(i).getDm_create_id());
					jsonItem.put("dm_modify_dt", faqList.get(i).getDm_modify_dt());
					jsonItem.put("dm_modify_id", faqList.get(i).getDm_modify_id());
					jsonList.put(jsonItem);
				}				
				result.put("result", "success");
				result.put("notice", "FAQ 리스트 정보 가져오기 성공");
				result.put("rows", jsonList);				
			} else {
				result.put("result", "fail");
				result.put("rows", jsonList);
				result.put("notice", "FAQ 정보가 없습니다.");
			}			
		} catch(DataAccessException dae) {
			dae.printStackTrace();
			result.put("result", "fail");
			result.put("rows", jsonList);
			result.put("notice", "SQL 구문오류가 발생하였습니다.");
		} catch(Exception e) {
			e.printStackTrace();
			result.put("result", "fail");
			result.put("rows", jsonList);
			result.put("notice", "오류가 발생하였습니다. 관리자에게 문의주세요.");
		}		
		response.setContentType("application/json");
		response.getWriter().write(result.toString());
	}
	*/
	/**
	 * get_faq
	 * 전달받은 FAQ PK 데이터로 조회한 1건의 FAQ데이터를 화면에 전달
	 * @param faqVO FAQ데이터 PK데이터를 전달받는 vo객체
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/get_faq.do")
	public ResponseEntity<Object> get_faq(Dm_faq_vo faqVO) {
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
			
		try {
			if (!commonUtil.isNullOrEmpty(faqVO.getDm_id())) {
				faqVO = faqService.selectFaq(faqVO);
				
				if (faqVO == null) {
					resultMap.put("result", "fail");
					resultMap.put("notice", MessageCode.CMS_SELECT_NODATA.getMessage());
				} else {
					String dm_question = faqVO.getDm_question();
					String dm_answer = faqVO.getDm_answer();
					
					if (faqVO.getDm_question() != null && !faqVO.getDm_question().isEmpty()) {
						dm_question = StringEscapeUtils.unescapeHtml4(dm_question);
						faqVO.setDm_question(dm_question.replaceAll("&apos;", "\'"));
					}
					
					if (faqVO.getDm_answer() != null && !faqVO.getDm_answer().isEmpty()) {
						dm_answer = StringEscapeUtils.unescapeHtml4(dm_answer);
						faqVO.setDm_answer(dm_answer.replaceAll("&apos;", "\'"));
					}
					
					resultMap.put("result", "success");
					resultMap.put("rows", faqVO);
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
	 * set_faq
	 * 사용자가 입력한 FAQ데이터의 insert/update 동작 수행
	 * @param type 입력 폼에서 전달받는 insert/update 수행명령값
	 * @param faqVO 사용자가 입력한 FAQ데이터를 전달받는 vo객체
	 * @param br vo객체의 hibernate validator 검증결과를 return 하기위한 객체
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 BindingResult객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/set_faq.do")
	public ResponseEntity<Object> set_faq (@Valid Dm_faq_vo faqVO, BindingResult br) {
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
			
			Dm_domain_list_vo domainVO = new Dm_domain_list_vo();
			domainVO.setDm_id(faqVO.getDm_domain());
			domainVO = domainService.selectDomainByDmid(domainVO);
			
			if (domainVO != null) {
				
				faqVO.setDm_question(commonUtil.xssSaxFiltering(faqVO.getDm_question()));
				faqVO.setDm_answer(commonUtil.xssSaxFiltering(faqVO.getDm_answer()));
				
				if (commonUtil.isNullOrEmpty(faqVO.getDm_id())) {
					faqVO.setDm_create_id(loginVO.getId());
					result = faqService.insertFaq(faqVO);
					if (result > 0) {
						resultMap.put("result", "success");
						resultMap.put("notice", MessageCode.CMS_INSERT_SUCCESS.getMessage());
					} else {
						resultMap.put("result", "fail");
						resultMap.put("notice", MessageCode.CMS_INSERT_FAIL.getMessage());
					}
				} else {
					faqVO.setDm_modify_id(loginVO.getId());
					result = faqService.updateFaq(faqVO);
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
				resultMap.put("notice", "도메인 정보가 없습니다.");
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
	 * delete_faq
	 * request 객체를 통하여 전달받은 FAQ PK배열에 해당하는 FAQ데이터 삭제
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/delete_faq.do")
	public ResponseEntity<Object> delete_faq (@RequestParam("dm_id[]") String ids[]) {
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
			
			List<Dm_faq_vo> list = new ArrayList<>();
			Arrays.asList(ids).forEach(item -> {
				Dm_faq_vo vo = new Dm_faq_vo();
				vo.setDm_id(item);
				vo.setDm_delete_id(loginVO.getId());
				list.add(vo);
			});
			
			faqService.deleteFaq(list);
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
}
