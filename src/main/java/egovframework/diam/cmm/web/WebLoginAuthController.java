/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.cmm.web;

import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import egovframework.diam.biz.model.config.Dm_config_vo;
import egovframework.diam.cmm.model.LoginVO;
import egovframework.diam.cmm.service.LoginAuthService;
import egovframework.diam.cmm.util.CommonUtil;
import egovframework.diam.cmm.util.EgovUserDetailsHelper;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;

/**
 * @Class Name : WebLoginAuthController.java
 * @Description : 사용자페이지 로그인/로그아웃 기능 구현 Controller클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
 */

@Controller
@Log4j2
public class WebLoginAuthController {
	
	@Resource(name="loginAuthService")
	private LoginAuthService loginAuthService;
	
	/**
	 * login
	 * 사용자가 입력한 계정정보 검증 후 로그인 처리 수행
	 * @param loginVO 사용자가 입력한 회원계정정보 데이터를 전달받는 vo객체
	 * @param br vo객체의 hibernate validator 검증결과를 return 하기위한 객체
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 BindingResult객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @param session 로그인 처리 시 조회에 성공한 계정정보를 session에 담기위해 사용하는 HttpSession 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping(value= {"/{domain}/web/login.do", "/web/login.do"}, method=RequestMethod.POST)
	public @ResponseBody void login(@Valid LoginVO loginVO, BindingResult br,
			HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		CommonUtil commonUtil = new CommonUtil();	
		JSONObject result = new JSONObject();
		if (EgovUserDetailsHelper.isAuthenticated()) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('이미 로그인 중입니다.'); location.href='"+commonUtil.getDomain(request)+"/index.do'; </script>");
			out.flush();
			return;
		}
		
		if(br.hasErrors()) {
			List<FieldError> errors = br.getFieldErrors();
			String msg = errors.get(0).getDefaultMessage();
								
			result.put("result", "fail");
			result.put("notice", msg);
			response.setContentType("application/json");
			response.getWriter().write(result.toString());
			return;
		}
		
		try {
			PrivateKey privateKey = (PrivateKey) session.getAttribute("DIAM_WEB_LOGIN_RSA_KEY");
			String password = commonUtil.decryptRsa(privateKey, loginVO.getPassword());
			
			String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			String user_ip = commonUtil.getUserIp(request);
			String id = loginVO.getId();
			
			LoginVO logVO = new LoginVO();
			logVO.setId(loginVO.getId());
			logVO.setIp(user_ip);
			logVO.setDm_fn_code("login");
			logVO.setDm_agent_info(request.getHeader("User-Agent"));
			logVO.setDm_fn_url(request.getHeader("referer"));
			logVO.setDm_type("사용자페이지 로그인");
			
			loginVO.setPassword(commonUtil.encryptPassword(password, loginVO.getId()));
			loginVO = loginAuthService.actionLogin(loginVO);
			
			if (loginVO != null) {
				
				if ("L".equals(loginVO.getDm_status())) {
					logVO.setDm_fn_result("leave");
					loginAuthService.insertLoginLog(logVO);
					result.put("result", "fail");
					result.put("notice", "탈퇴 처리되어 이용할 수 없는 계정입니다.");
					response.setContentType("application/json");
					response.getWriter().write(result.toString());
					return;
				}
				
				if ("D".equals(loginVO.getDm_status())) {
					logVO.setDm_fn_result("lock");
					loginAuthService.insertLoginLog(logVO);
					result.put("result", "fail");
					result.put("notice", "로그인 5회이상 실패로  잠김처리된 계정입니다. 관리자에게 문의주세요.");
					response.setContentType("application/json");
					response.getWriter().write(result.toString());
					return;
				}
				
				if (Integer.parseInt(loginVO.getDm_level()) >= 6) {
					loginVO.setIs_admin(true);
				} else {
					loginVO.setIs_admin(false);
				}
				
				loginAuthService.updateMemberUnlock(loginVO);
				
				loginVO.setLogin_date(time);
				session.setAttribute("LoginVO", loginVO);
				logVO.setDm_fn_result("success");
				loginAuthService.insertLoginLog(logVO);
				session.removeAttribute("DIAM_LOGIN_RSA_KEY");				
				
				result.put("result", "success");
				result.put("notice", "로그인 하였습니다.");
			} else {
				logVO.setDm_fn_result("fail");
				loginAuthService.insertLoginLog(logVO);
				
				LoginVO checkVO = new LoginVO();
				checkVO.setId(id);
				checkVO = loginAuthService.selectLoginInfo(checkVO);
				if (checkVO != null) {
					int fail_cnt = loginAuthService.selectLoginFailCnt(checkVO);
					if (fail_cnt < 4) {
						loginAuthService.updateLoginFailCnt(checkVO);
						result.put("result", "fail");
						result.put("notice", "로그인 " + (fail_cnt+1) + "회 실패하였습니다. 5회 이상 실패시 30분간 계정이 잠김처리됩니다.");
					} else if (fail_cnt == 4) {
						loginAuthService.updateLoginFailCntLock(checkVO);
						result.put("result", "fail");
						result.put("notice", "로그인 " + (fail_cnt+1) + "회 실패하였습니다. 30분간 계정이 잠김처리 됩니다.");
					} else {
						loginAuthService.updateLoginFailCntLock(checkVO);
						result.put("result", "lock");
						result.put("notice", "로그인 5회이상 실패로 잠김처리된 계정입니다. 30분 뒤 다시 시도해주세요.");
					}					
				} else {
					result.put("result", "fail");
					result.put("notice", "아이디 또는 패스워드가 존재하지 않습니다.");
				}
			}
		} catch(BadPaddingException bpe) {
			log.error(MessageCode.CMM_ENCRYPT_EXPIRED.getLog());
			result.put("notice", MessageCode.CMM_ENCRYPT_EXPIRED.getMessage());
			response.setStatus(500);
		} catch(InvalidKeyException ike) {
			log.error(MessageCode.CMM_ENCRYPT_EXPIRED.getLog());
			result.put("notice", MessageCode.CMM_ENCRYPT_EXPIRED.getMessage());
			response.setStatus(500);
		} catch (DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			result.put("notice", MessageCode.CMM_DATA_ERROR.getMessage());
			response.setStatus(500);
		} catch (Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			result.put("notice", MessageCode.CMM_SYSTEM_ERROR.getMessage());
			response.setStatus(500);
		}	
		response.setContentType("application/json");
		response.getWriter().write(result.toString());
	}
	
	/**
	 * logout
	 * 로그인한 회원정보 로그아웃 처리 수행
	 * @param model 로그아웃 후 결과페이지에 메시지를 전달하기 위해 사용하는 ModelMap 객체
	 * @param session 로그아웃 후 로그인 세션을 초기화하기 위해 사용하는 HttpSession 객체
	 * @return String 로그아웃 후 화면에 전달하는 메시지에 따라 완료메시지를 출력해주는 결과페이지 경로를 문자열로 전달
	*/
	@RequestMapping(value= {"/web/logout.do", "/{domain}/web/logout.do"})
	public String logout(ModelMap model, HttpSession session, HttpServletRequest request) {
		CommonUtil commonUtil = new CommonUtil();
		model.addAttribute("root", commonUtil.getDomain(request));
		
		if (!EgovUserDetailsHelper.isAuthenticated()) {
			model.addAttribute("message", "member.login.notlogin");
			return "egovframework/diam/web/base/login/result";
		}
		
		try {
			session.setAttribute("LoginVO", null);
			RequestContextHolder.getRequestAttributes().removeAttribute("LoginVO", RequestAttributes.SCOPE_SESSION);
			model.addAttribute("message", "member.logout.success");
		} catch(NullPointerException npe) {
			log.error(MessageCode.CMM_NULL_ERROR.getLog());
			model.addAttribute("message", "member.login.notlogin");
		} catch(Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			model.addAttribute("message", "member.logout.fail");
		}
		
		return "egovframework/diam/web/base/login/result";
	}	
}
