/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.cmm.web;

import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.codehaus.jettison.json.JSONObject;
import org.jasypt.contrib.org.apache.commons.codec_1_3.binary.Base64;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import egovframework.diam.cmm.model.LoginVO;
import egovframework.diam.cmm.service.LoginAuthService;
import egovframework.diam.cmm.util.CommonUtil;
import egovframework.diam.cmm.util.EgovUserDetailsHelper;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;

/**
 * @Class Name : LoginAuthController.java
 * @Description : 관리자페이지 로그인 인증기능 구현 Controller클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
 */

@Controller
@Log4j2
public class LoginAuthController {
	
	public void test (HttpSession session) {
		LoginVO loginVO = (LoginVO) session.getAttribute("loginVO");
		String deptId = loginVO.getId();

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String dbhost = "jdbc:oracle:thin:@112.187.64.62:1521:TP";
		String dbuser = "GJTP";
		String dbpass = "gjtp!@#";
		String result = "";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(dbhost, dbuser, dbpass);
			stmt = conn.createStatement();
			String sql = "SELECT DEPT_NM FROM JMY_ORGNZT_DEPT WHERE USE_AT = 'Y' AND DEPT_ID = '" + deptId + "'";
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				result =  rs.getString(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try{
				if(rs != null) 	 rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	@Resource(name="loginAuthService")
	private LoginAuthService loginAuthService;
		
	/**
	 * login
	 * 로그인 세션 유무에 따라 로그인/메인페이지 파일경로를 문자열로 ViewResolver로 전송, 세션이 없을 경우 비밀번호 RSA암호화 전송을 위한 RSA세션 생성 및 공개키 화면에 전달
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param model 메소드의 수행결과를 화면에 전달하기 위해 사용하는 ModelMap 객체
	 * @return String 로그인 세션 유무에 따라 로그인/메인페이지 파일경로를 return
	*/
	@RequestMapping("/adm/login.do")
	public String login(HttpServletRequest request, ModelMap model) {
		
		try {
			if (EgovUserDetailsHelper.isAuthenticated()) {
				return "redirect:/adm/main.do";
			} else {
				CommonUtil commonUtil = new CommonUtil();
				JSONObject rsaObject = commonUtil.initRsa(request, "DIAM_ADMIN_LOGIN_RSA_KEY");
				
		        if ("success".equals(rsaObject.get("result"))) {
		        	model.addAttribute("RSAModulus", rsaObject.get("RSAModulus"));
		    		model.addAttribute("RSAExponent", rsaObject.get("RSAExponent"));
		    		return "egovframework/diam/ui/login";
		        } else {
		        	model.addAttribute("message", MessageCode.CMM_SYSTEM_ERROR.getMessage());
		        	return "egovframework/diam/web/error";
		        }
			}
		} catch(InvalidKeySpecException ike) {
			log.error(MessageCode.CMM_ENCRYPT_EXPIRED.getLog());
        	model.addAttribute("message", MessageCode.CMM_ENCRYPT_EXPIRED.getMessage());
        	return "egovframework/diam/web/error";
        } catch (Exception e) {
        	log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			model.addAttribute("message", MessageCode.CMM_SYSTEM_ERROR.getMessage());
			return "egovframework/diam/web/error";
		}
	}
	
	/**
	 * member_login
	 * 사용자가 로그인폼에 입력한 로그인정보로 회원데이터를 조회하여 로그인 처리
	 * @param loginVO 사용자가 입력한 로그인정보를 전달받는 vo객체
	 * @param br vo객체의 hibernate validator 검증결과를 return 하기위한 객체
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @param session 로그인한 결과값을 세션에 저장 시 사용하는 HttpSession 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping(value="/adm/member_login.do", method=RequestMethod.POST)
	public @ResponseBody void member_login(@Valid LoginVO loginVO, BindingResult br,
			HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		CommonUtil commonUtil = new CommonUtil();		
		JSONObject result = new JSONObject();
		
		if (EgovUserDetailsHelper.isAuthenticated()) {
			result.put("result", "already");
			result.put("notice", MessageCode.CMM_LOGIN_ALREADY.getMessage());
			response.setContentType("application/json");
			response.getWriter().write(result.toString());
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
			
			PrivateKey privateKey = (PrivateKey) session.getAttribute("DIAM_ADMIN_LOGIN_RSA_KEY");
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
			logVO.setDm_type("관리자페이지 로그인");
						
			loginVO.setPassword(commonUtil.encryptPassword(password, loginVO.getId()));
			loginVO = loginAuthService.actionLogin(loginVO);
			if (loginVO != null) {
				
				if (Integer.parseInt(loginVO.getDm_level()) < 6) {
					logVO.setDm_fn_result("fail");
					loginAuthService.insertLoginLog(logVO);
					result.put("result", "fail");
					result.put("notice", MessageCode.CMM_ACCESS_DENIED.getMessage());
					response.setContentType("application/json");
					response.getWriter().write(result.toString());
					return;
				}
				
				if ("D".equals(loginVO.getDm_status())) {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					LocalDateTime todaytime = LocalDateTime.now();
					LocalDateTime compareDate = LocalDateTime.parse(loginVO.getDm_fail_time(), formatter).plusMinutes(30);
									
					if (compareDate.isAfter(todaytime)) {
						logVO.setDm_fn_result("lock");
						loginAuthService.insertLoginLog(logVO);
						result.put("result", "fail");
						result.put("notice", "로그인 5회이상 실패로  잠김처리된 계정입니다. "+ compareDate.toString().replace("T", " ")  +"이후 로그인 가능합니다.");
						response.setContentType("application/json");
						response.getWriter().write(result.toString());
						return;
					} else {
						loginAuthService.updateMemberUnlock(loginVO);
						loginVO = loginAuthService.actionLogin(loginVO);
					}
				} else if ("L".equals(loginVO.getDm_status())) {
					result.put("result", "fail");
					result.put("notice", "탈퇴 처리된 관리자 계정입니다.");
					response.setContentType("application/json");
					response.getWriter().write(result.toString());
					return;
				} else {
					loginAuthService.updateMemberUnlock(loginVO);
				}
				
				if (Integer.parseInt(loginVO.getDm_level()) >= 6) {
					loginVO.setIs_admin(true);
				} else {
					loginVO.setIs_admin(false);
				}
				
				loginVO.setLogin_date(time);
				session.setAttribute("LoginVO", loginVO);
				
				logVO.setDm_fn_result("success");
				loginAuthService.insertLoginLog(logVO);
				session.removeAttribute("DIAM_ADMIN_LOGIN_RSA_KEY");
				
				result.put("result", "success");
				result.put("notice", MessageCode.CMM_LOGIN_SUCCESS.getMessage());
				
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
	 * member_logout
	 * 사용자가 로그아웃 버튼 클릭 시 로그인 세션 제거 후 로그아웃 처리
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @param session 로그아웃 시 로그인 세션 제거 시 사용하는 HttpSession 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/member_logout.do")
	public @ResponseBody void member_logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		JSONObject result = new JSONObject();
		
		if (!EgovUserDetailsHelper.isAuthenticated()) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('로그인 상태가 아닙니다.'); location.href='/index.do'; </script>");
			out.flush();
			return;
		}
		
		try {
			session.setAttribute("LoginVO", null);
			RequestContextHolder.getRequestAttributes().removeAttribute("LoginVO", RequestAttributes.SCOPE_SESSION);
		
			result.put("result", "success");
			result.put("notice", "로그아웃 하였습니다.");		
		} catch(NullPointerException npe) {
			log.error(MessageCode.CMM_NULL_ERROR.getLog());
			result.put("notice", "로그인 상태가 아닙니다.");
			response.setStatus(500);
		} catch(Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			result.put("notice", MessageCode.CMM_SYSTEM_ERROR.getMessage());
			response.setStatus(500);
		}
		response.setContentType("application/json");
		response.getWriter().write(result.toString());
	}
	
	/**
	 * change_password
	 * 초기비밀번호가 세팅된 계정일 시 비밀번호 변경페이지로 이동할 수 있도록 비밀번호 변경페이지 파일경로를 문자열로 ViewResolver로 전송, 세션이 없을 경우 비밀번호 RSA암호화 전송을 위한 RSA세션 생성 및 공개키 화면에 전달
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param model 메소드의 수행결과를 화면에 전달하기 위해 사용하는 ModelMap 객체
	 * @return String RSA암호화 생성 성공여부에 따라 비밀번호변경/에러페이지 파일경로를 return
	*/
	@RequestMapping("/adm/change_password.do")
	public String change_password(HttpServletRequest request, ModelMap model) throws Exception {
		CommonUtil commonUtil = new CommonUtil();
		JSONObject rsaObject = commonUtil.initRsa(request, "DIAM_CHANGE_PASSWORD_RSA_KEY");
		
        if ("success".equals(rsaObject.get("result"))) {
        	model.addAttribute("RSAModulus", rsaObject.get("RSAModulus"));
    		model.addAttribute("RSAExponent", rsaObject.get("RSAExponent"));
    		return "egovframework/diam/ui/change_password";
        } else {
        	model.addAttribute("message", "암호화 생성 중 오류가 발생하였습니다.");
        	return "egovframework/diam/web/error";
        }
	}
	
	/**
	 * set_change_password
	 * 비밀번호 변경 페이지에서 사용자가 입력한 비밀번호를 request 객체로 받아 비밀번호 검증 후 비밀번호 변경처리
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/set_change_password.do")
	public @ResponseBody void set_change_password(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		HttpSession session = request.getSession();
		CommonUtil commonUtil = new CommonUtil();
		JSONObject result = new JSONObject();
				
		String dm_password = request.getParameter("dm_password");
				
		if (dm_password == null || dm_password.isEmpty()) {
			result.put("result", "fail");
			result.put("notice", "패스워드를 입력해주세요.");
			response.setContentType("application/json");
			response.getWriter().write(result.toString());
			return;
		}
		
		try {
			if (loginVO.getId() != null && !loginVO.getId().isEmpty()) {
				PrivateKey privateKey = (PrivateKey) session.getAttribute("DIAM_CHANGE_PASSWORD_RSA_KEY");
				dm_password = commonUtil.decryptRsa(privateKey, dm_password);		
				
				String password_pattern = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,}$";
				Pattern pattern_chk = Pattern.compile(password_pattern);
				Matcher matcher = pattern_chk.matcher(dm_password);
				boolean pattern_result = matcher.find();
				
				if (!pattern_result) {
					result.put("result", "fail");
					result.put("notice", "패스워드 형식에 맞지않습니다. 영어 대/소문자,숫자,특수문자를 1개이상 포함하여 8자 이상 입력해주세요.");
				} else {
					LoginVO changeVO = new LoginVO();
					changeVO.setId(loginVO.getId());
					changeVO.setPassword(commonUtil.encryptPassword(dm_password, loginVO.getId()));
					
					if (loginVO.getPassword().equals(changeVO.getPassword())) {
						result.put("result", "fail");
						result.put("notice", "이전과 동일한 비밀번호로는 변경불가합니다. 이전 비밀번호와 다른 비밀번호를 입력해주세요.");
					} else {
						loginAuthService.updateMemberPasswordChange(changeVO);
						session.removeAttribute("DIAM_CHANGE_PASSWORD_RSA_KEY");
						
						session.setAttribute("LoginVO", null);
						RequestContextHolder.getRequestAttributes().removeAttribute("LoginVO", RequestAttributes.SCOPE_SESSION);
						result.put("result", "success");
						result.put("notice", "비밀번호 변경에 성공하였습니다. 새로운 비밀번호로 다시 로그인해 주세요.");
					}
				}		
			} else {
				result.put("result", "fail");
				result.put("notice", "로그인 세션 값이 없습니다.");
			}
		} catch (DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			result.put("notice", MessageCode.CMM_DATA_ERROR.getMessage());
			response.setStatus(500);
		} catch(BadPaddingException bpe) {
			log.error(MessageCode.CMM_ENCRYPT_EXPIRED.getLog());
			result.put("notice", MessageCode.CMM_ENCRYPT_EXPIRED.getMessage());
			response.setStatus(500);
		} catch(InvalidKeyException ike) {
			log.error(MessageCode.CMM_ENCRYPT_EXPIRED.getLog());
			result.put("notice", MessageCode.CMM_ENCRYPT_EXPIRED.getMessage());
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
	 * pass_change_password
	 * 초기비밀번호 세팅계정일 시 비밀번호 변경페이지에서 다음에 변경하기 버튼 클릭 시 관리자 메인페이지로 이동
	 * @param session RSA암호화 세션 제거 시 사용하는 HttpSession 객체
	 * @return String 관리자페이지 url redirect 수행
	*/
	@RequestMapping("/adm/pass_change_password.do")
	public String pass_change_password(HttpSession session) throws Exception {
		session.removeAttribute("DIAM_CHANGE_PASSWORD_RSA_KEY");
		return "redirect:/adm/main.do";
	}
}
