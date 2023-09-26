/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.cmm.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.diam.biz.model.member.Dm_member_vo;
import egovframework.diam.cmm.service.LoginLockService;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;

/**
 * @Class Name : LoginLockController.java
 * @Description : 관리자 페이지 로그인 실패 시 잠금기능 구현 Controller클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
 */

@Controller
@Log4j2
public class LoginLockController {
	
	@Resource(name="loginLockService")
	private LoginLockService loginLockService;
	
	/**
	 * get_lock_list
	 * 전달받은 검색조건 데이터로 조회한 잠김계정 리스트 데이터를 화면에 전달
	 * @param memberVO 잠김계정 데이터 검색조건을 전달받는 vo객체
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/get_lock_list.do")
	public ResponseEntity<?> get_lock_list(Dm_member_vo memberVO) {
		Map<String, Object> resultMap = new HashMap<>();
		
		int row = memberVO.getRows() != 0 ? memberVO.getRows() : 50;
		int page = memberVO.getPage() != 0 ? memberVO.getPage() : 1;
		
		if (row < 1 || page < 0) {
			log.error(MessageCode.CMM_REQUEST_BADREQUEST.getLog());
			resultMap.put("notice", MessageCode.CMM_REQUEST_BADREQUEST.getMessage());
			return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
		} else {
			memberVO.setRows(row);
			memberVO.setPage(row * (page -1));
		}
		
		try {
			int lockCnt = loginLockService.selectLoginLockListCnt(memberVO);
			List<Dm_member_vo> lockList = loginLockService.selectLoginLockList(memberVO);
			
			resultMap.put("result", "success");
			resultMap.put("total", lockCnt);
			resultMap.put("rows", lockList);
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
	 * set_unlock
	 * request 객체를 통하여 전달받은 잠김계정 PK배열에 해당하는 잠김계정 데이터 잠김해제 처리
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/set_unlock.do")
	public ResponseEntity<?> set_unlock(@RequestParam("dm_no[]") String nos[]) {
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
			if (nos.length > 0) {
				List<Dm_member_vo> list = new ArrayList<>();
				Arrays.asList(nos).forEach(item -> {
					Dm_member_vo vo = Dm_member_vo.builder().dm_no(item).build();
					list.add(vo);
				});
				
				loginLockService.unlockMember(list);
				resultMap.put("result", "success");
				resultMap.put("notice", "잠김 해제 되었습니다.");
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
