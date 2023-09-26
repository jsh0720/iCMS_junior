/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.cmm.web;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.diam.biz.model.board.Dm_board_vo;
import egovframework.diam.biz.model.config.Dm_domain_list_vo;
import egovframework.diam.biz.model.display.Dm_layout_vo;
import egovframework.diam.cmm.model.Dm_common_code_vo;
import egovframework.diam.cmm.service.CommonService;
import egovframework.diam.cmm.util.CommonUtil;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;

/**
 * @Class Name : CommonController.java
 * @Description : CMS 기능별 공통으로 사용하는 조회기능구현 Controller클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
 */

@Controller
@Log4j2
public class CommonController {
	
	@Resource(name="commonService")
	private CommonService commonService;
	
	/**
	 * select_code
	 * request 객체로 전달받은 공통코드 조회조건으로 조건에 해당하는 공통코드 리스트 조회 후 JSONArray로 가공하여 화면에 전달
	 * @param selected 콤보박스에서 선택될 공통코드 값을 전달받는 문자열 
	 * @param search 콤보박스 최상단에 전체 선택값 추가여부를 전달받는 문자열
	 * @param start_index 특정 데이터만 가져올 시 시작인덱스를 전달받는 문자열
	 * @param cut_index 특정 데이터만 가져올 시 종료인덱스를 전달받는 문자열
	 * @param commonCodeVO 공통코드 조회조건 중 그룹코드를 전달받는 vo객체
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping(value = "/adm/select_code.do", produces = "application/json; charset=utf8")
	public ResponseEntity<Object> select_code(
			@RequestParam(value="selected", required=false, defaultValue="") String selected,
			@RequestParam(value="search", required=false, defaultValue="") String search,
			@RequestParam(value="start_index", required=false, defaultValue="") String start_index,
			@RequestParam(value="cut_index", required=false, defaultValue="") String cut_index,
			@RequestParam(value="dm_code_group", required=true) String code_group) {
		
		CommonUtil commonUtil = new CommonUtil();
		List<Dm_common_code_vo> resultList = new ArrayList<>();
		
		if (commonUtil.isNullOrEmpty(code_group)) {
			log.error(MessageCode.CMM_REQUEST_BADREQUEST.getLog());
			return new ResponseEntity<>(new HashMap<>().put("notice", MessageCode.CMM_REQUEST_BADREQUEST.getMessage()), HttpStatus.BAD_REQUEST);
		}
		
		try {
			List<Dm_common_code_vo> commonCodeList = commonService.selectDm_common_code(Dm_common_code_vo
					.builder()
					.dm_code_group(code_group)
					.build());
			
			if (commonCodeList.size() > 0) {
				
				for (int i=0 ; i < commonCodeList.size() ; i++) {
					
					if (!commonUtil.isNullOrEmpty(selected)) {
						if (commonCodeList.get(i).getDm_code_value().equals(selected)) {
							commonCodeList.get(i).setSelected(true);
						}
					}
					
					if (!commonUtil.isNullOrEmpty(start_index)) {
					    
						if (i < Integer.parseInt(start_index)) continue;
						
						else resultList.add(commonCodeList.get(i));
						
					} else resultList.add(commonCodeList.get(i));
					
					if (!commonUtil.isNullOrEmpty(cut_index)) {
						if (i > Integer.parseInt(cut_index)) {
							resultList.remove(resultList.size()-1);
							break;
						}
					}
				}
				
				if ("1".equals(search)) {					
					resultList.add(0, Dm_common_code_vo.builder()
							.dm_code_value("")
							.dm_code_name("전체")
							.build());
				}
				
				if (commonUtil.isNullOrEmpty(selected)) {
					resultList.get(0).setSelected(true);
				}
				
			} else {
				resultList.add(Dm_common_code_vo.builder()
						.dm_code_value("")
						.dm_code_name("등록된 공통코드가 없습니다.")
						.selected(true)
						.build());
			}
		} catch(DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			return new ResponseEntity<>("공통 코드 리스트를 불러오던 중 " + MessageCode.CMM_DATA_ERROR.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch(Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			return new ResponseEntity<>("공통 코드 리스트를 불러오던 중 " + MessageCode.CMM_SYSTEM_ERROR.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(resultList, HttpStatus.OK);
	}
		
	/**
	 * select_board
	 * 전체 게시판리스트 조회 후 JSONArray 데이터로 가공하여 화면에 전달
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping(value = "/adm/select_board.do", produces = "application/json; charset=utf8")
	public ResponseEntity<Object> select_board(
			@RequestParam(value="selected", required=false, defaultValue="") String selected,
			@RequestParam(value="domain", required=false, defaultValue="") String domain,
			@RequestParam(value="search", required=false, defaultValue="") String search) {
		
		CommonUtil commonUtil = new CommonUtil();
		List<Dm_board_vo> list = new ArrayList<>();
		
		try {
			
			list = commonService.selectBoardListAll(Dm_board_vo.builder().dm_domain(domain).build());

			if(list.size() > 0) {
				for(int i = 0; i < list.size() ; i++) {
					
					if (!commonUtil.isNullOrEmpty(selected)) {
						if (list.get(i).getDm_id().equals(selected)) {
							list.get(i).setSelected(true);
						}
					}
				}
				
				if (!commonUtil.isNullOrEmpty(search)) {
					list.add(0,Dm_board_vo.builder()
							.dm_subject("전체")
							.dm_id("")
							.build());
				}
				
				if (commonUtil.isNullOrEmpty(selected)) {
					list.get(0).setSelected(true);
				}
				
			} else {
				list.add(0,Dm_board_vo.builder()
						.dm_subject("등록된 게시판이 없습니다.")
						.dm_id("")
						.selected(true)
						.build());
			}
		} catch(DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			return new ResponseEntity<>("게시판 리스트를 불러오던 중 " + MessageCode.CMM_DATA_ERROR.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch(Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			return new ResponseEntity<>("게시판 리스트를 불러오던 중 " + MessageCode.CMM_SYSTEM_ERROR.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
		
	/**
	 * select_board_skin
	 * 게시판 스킨경로에 설치된 스킨폴더 조회 후 JSONArray 데이터로 가공하여 화면에 전달
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping(value = "/adm/select_board_skin.do", produces = "application/json; charset=utf8")
	public ResponseEntity<Object> select_board_skin (
			@RequestParam(value="selected", required=false, defaultValue="") String selected, HttpServletRequest request) {
		
		CommonUtil commonUtil = new CommonUtil();
		String FILE_PATH = request.getServletContext().getRealPath("/WEB-INF/jsp/egovframework/") + "/diam/web/base/board/";
		List<Dm_common_code_vo> resultList = new ArrayList<>();
		
		try {
			File dir = new File (FILE_PATH);
			if (!dir.exists()) {
				dir.mkdir();
			}
			
			File[] fileList = dir.listFiles();
			
			if (fileList.length > 0) {
				
				for (int i = 0; i < fileList.length; i++) {
					if (fileList[i].isDirectory()) {
						
						Dm_common_code_vo vo = Dm_common_code_vo.builder()
								.dm_code_value(fileList[i].getName())
								.dm_code_name(fileList[i].getName())
								.build();
						
						if (!commonUtil.isNullOrEmpty(selected)) {
							if (vo.getDm_code_name().equals(selected)) {
								vo.setSelected(true);
							}
						}
						
						resultList.add(vo);
					}
				}
				
				if (commonUtil.isNullOrEmpty(selected)) {
					resultList.get(0).setSelected(true);
				}
				
			} else {
				resultList.add(Dm_common_code_vo.builder()
						.dm_code_value("")
						.dm_code_name("게시판 스킨이 없습니다. 관리자에게 문의주세요.")
						.build());
			}
		} catch(Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			return new ResponseEntity<>("게시판 스킨을 불러오던 중 " + MessageCode.CMM_SYSTEM_ERROR.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
		return new ResponseEntity<>(resultList, HttpStatus.OK);
	}
	
	/**
	 * select_domain_id
	 * 사용중인 도메인 전체 조회 후 JSONArray 데이터로 가공하여 화면에 전달
	 * @param selected 콤보박스에서 선택될 도메인 값을 전달받는 문자열 
	 * @param search 콤보박스 최상단에 전체 선택값 추가여부를 전달받는 문자열
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping(value= "/adm/select_domain_id.do", produces = "application/json; charset=utf8")
	public ResponseEntity<Object> select_domain_id (
			@RequestParam(value="selected", required=false, defaultValue="") String selected,
			@RequestParam(value="search", required=false, defaultValue="") String search ) {
		CommonUtil commonUtil = new CommonUtil();
		List<Dm_domain_list_vo> domainList = new ArrayList<>();
		
		try {			
			Dm_domain_list_vo vo = new Dm_domain_list_vo();
			domainList = commonService.selectDomainList(vo);
			if (domainList.size() > 0) {
				if ("1".equals(search)) {
					vo.setDm_id("");
					vo.setDm_domain_nm("전체");
					domainList.add(0, vo);
				}
				
				for (int i = 0; i < domainList.size(); i++) {
					if (commonUtil.isNullOrEmpty(selected)) {
						domainList.get(0).setSelected(true);
					} else {
						if (domainList.get(i).getDm_id().equals(selected)) {
							domainList.get(i).setSelected(true);
						}
					}
				}
			} else {
				domainList.add(Dm_domain_list_vo.builder()
						.dm_id("")
						.dm_domain_nm("등록된 도메인이 없습니다.")
						.selected(true)
						.build());
			}
		} catch(DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			String result = "도메인 리스트를 불러오던 중 " + MessageCode.CMM_DATA_ERROR.getMessage();
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch(Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			String result = "도메인 리스트를 불러오던 중 " + MessageCode.CMM_SYSTEM_ERROR.getMessage();
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(domainList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/adm/select_layout_id.do", produces = "application/json; charset=utf8")
	public ResponseEntity<Object> select_layout_id(@RequestParam(value="selected", required=false, defaultValue="") String selected) {
		
		CommonUtil commonUtil = new CommonUtil();
		List<Dm_layout_vo> layoutList = new ArrayList<>();
		
		try {
			layoutList = commonService.selectLayoutList(new Dm_layout_vo());

			if(layoutList.size() > 0) {
				for(int i = 0; i < layoutList.size() ; i++) {
					
					if (!commonUtil.isNullOrEmpty(selected)) {
						if (layoutList.get(i).getDm_id().equals(selected)) {
							layoutList.get(i).setSelected(true);
						}
					}
				}
				
				if (commonUtil.isNullOrEmpty(selected)) {
					layoutList.get(0).setSelected(true);
				}
				
			} else {
				layoutList.add(0, Dm_layout_vo.builder()
						.dm_layout_nm("등록된 레이아웃이 없습니다.")
						.dm_id("")
						.selected(true)
						.build());
			}
		} catch (DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			String result = "레이아웃 리스트를 불러오던 중 " + MessageCode.CMM_DATA_ERROR.getMessage();
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			String result = "레이아웃 리스트를 불러오던 중 " + MessageCode.CMM_SYSTEM_ERROR.getMessage();
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(layoutList, HttpStatus.OK);
	}
}
