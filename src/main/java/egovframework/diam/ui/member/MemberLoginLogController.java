/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.ui.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.diam.biz.model.member.Dm_login_log_vo;
import egovframework.diam.biz.service.member.MemberLoginLogService;
import egovframework.diam.cmm.service.CommonService;
import egovframework.diam.cmm.util.CommonUtil;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;

/**
 * @Class Name : MemberLoginLogController.java
 * @Description : 관리자페이지 로그인로그 관리 조회/검색 기능구현 Controller클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
 */

@Log4j2
@Controller
public class MemberLoginLogController {
	
	@Resource(name="memberLoginLogService")
	private MemberLoginLogService memberLoginLogService;
	
	@Resource(name="commonService")
	private CommonService commonService;
	
	/**
	 * get_member_login_log
	 * 전달받은 검색조건 데이터로 조회한 회원 로그인로그 리스트 데이터를 화면에 전달
	 * @param loginLogVO 회원 로그인로그 데이터 검색조건을 전달받는 vo객체
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/get_member_login_log.do")
	public ResponseEntity<Object> get_member_login_log (Dm_login_log_vo loginLogVO) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		
		int row = loginLogVO.getRows() != 0 ? loginLogVO.getRows() : 50;
		int page = loginLogVO.getPage() != 0 ? loginLogVO.getPage() : 1;
		
		if (row < 1 || page < 0) {
			log.error(MessageCode.CMM_REQUEST_BADREQUEST.getLog());
			resultMap.put("notice", MessageCode.CMM_REQUEST_BADREQUEST.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
		} else {
			loginLogVO.setRows(row);
			loginLogVO.setPage(row * (page -1));
		}
		
		try {
			int loginLogListCnt = memberLoginLogService.selectLoginLogListCnt(loginLogVO);
			List<Dm_login_log_vo> loginLogList = memberLoginLogService.selectLoginLogList(loginLogVO);
			if (loginLogList.size() > 0) {
				resultMap.put("result", "success");
				resultMap.put("total", loginLogListCnt);
				resultMap.put("rows", loginLogList);
				resultMap.put("notice", MessageCode.CMS_SELECT_SUCCESS.getMessage());
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
