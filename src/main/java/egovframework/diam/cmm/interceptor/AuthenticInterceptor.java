/**
 * Description Date(Format: 2011/07/01)
 */
package egovframework.diam.cmm.interceptor;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.dao.DataAccessException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

import egovframework.diam.biz.model.config.Dm_access_ip_vo;
import egovframework.diam.biz.service.config.AccessIpService;
import egovframework.diam.cmm.model.LoginVO;
import egovframework.diam.cmm.util.CommonUtil;
import egovframework.diam.cmm.util.EgovUserDetailsHelper;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;


/**
 * @className : AuthenticInterceptor.java
 * @author : (주)디자인아이엠
 * @Date : 2023. 3. 21.
 * @Description : 인증여부 체크 인터셉터
 */

@Log4j2
public class AuthenticInterceptor extends WebContentInterceptor {
	
	@Resource(name="accessIpService")
	private AccessIpService accessIpService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {		
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		CommonUtil commonUtil = new CommonUtil();
		String userIp = commonUtil.getUserIp(request);
        		 
        boolean ipChkResult = false;
        String pattern1 = "(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])";
		String pattern2 = "(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.\\*";
		
		if (userIp.matches(pattern1)) {
			String[] userIpArr = userIp.split("\\.");
			long userIpLong = (Long.parseLong(userIpArr[0]) << 24) + (Long.parseLong(userIpArr[1]) << 16) + (Long.parseLong(userIpArr[2]) << 8) + (Long.parseLong(userIpArr[3]));
			if (userIpLong < 0) {
				ModelAndView mv = new ModelAndView("egovframework/diam/web/error");
				mv.addObject("message", "IP길이 변환값이 올바르지 않습니다.");
				throw new ModelAndViewDefiningException(mv);
			} else {
				try {
					List<Dm_access_ip_vo> ipList = accessIpService.selectAccessIpAll();
					for (int i = 0 ; i < ipList.size() ; i++) {
						boolean patternChk1 = ipList.get(i).getDm_ip().matches(pattern1);
						boolean patternChk2 = ipList.get(i).getDm_ip().matches(pattern2);
						if (patternChk1) {
							if (userIp.equals(ipList.get(i).getDm_ip())) {
								ipChkResult = true;
								break;
							}
						}
						if (patternChk2) {
							String chkIp = ipList.get(i).getDm_ip().replaceAll("\\*", "");
							String startIp =  chkIp + "0";
							String endIp =  chkIp + "255";
							String[] startIpArr = startIp.split("\\.");
							String[] endIpArr = endIp.split("\\.");
							
							long startIpLong = (Long.parseLong(startIpArr[0]) << 24) + (Long.parseLong(startIpArr[1]) << 16) + (Long.parseLong(startIpArr[2]) << 8) + (Long.parseLong(startIpArr[3]));
							long endIpLong = (Long.parseLong(endIpArr[0]) << 24) + (Long.parseLong(endIpArr[1]) << 16) + (Long.parseLong(endIpArr[2]) << 8) + (Long.parseLong(endIpArr[3]));
													
							if (startIpLong <= userIpLong && endIpLong >= userIpLong) {
								ipChkResult = true;
								break;
							}
						}
					}
				} catch(DataAccessException dae) {
					log.error(MessageCode.CMM_DATA_ERROR.getLog());
				} catch(Exception e) {
					log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
				}
			}
		} else {			
			ModelAndView mv = new ModelAndView("egovframework/diam/web/error");
			mv.addObject("message", "IPv4 형식에 맞지않는 아이피주소입니다. - " + userIp);
			throw new ModelAndViewDefiningException(mv);			
		}
		
		if (!ipChkResult) {
			ModelAndView mv = new ModelAndView("egovframework/diam/web/error");
			mv.addObject("message", "접근 허용하지 않는 아이피입니다.");
			throw new ModelAndViewDefiningException(mv);
		}
		
		//인증체크 스킵할 URL 리스트, 아래 해당하는 URL들이 포함된 것들은 인증 스킵
		List<String> passUrlList = new ArrayList<String>();
		passUrlList.add("adm/login.do");
		passUrlList.add("adm/member_login.do");
		passUrlList.add("adm/member_logout.do");
		passUrlList.add("adm/se2_single_uploader.do");
		passUrlList.add("adm/se2_multi_uploader.do");
		passUrlList.add("adm/get_write_file_download.do");
		
		for (int i = 0 ; i < passUrlList.size() ; i++) {
			if (request.getRequestURI().indexOf(passUrlList.get(i)) > -1) {
				return true;
			}
		}
				
		if (loginVO.getId() != null) {
			if (Integer.parseInt(loginVO.getDm_level()) < 6) {
				ModelAndView mv = new ModelAndView("egovframework/diam/web/error");
				mv.addObject("message", "접근 불가능한 계정입니다.");
				throw new ModelAndViewDefiningException(mv);				
			}
			HttpSession session = request.getSession();
			int timeout = session.getMaxInactiveInterval();
			loginVO.setExpired(timeout);
			
			request.setAttribute("DiamLoginVO", loginVO);
			
			return true;
		} else {
			if (request.getHeader("x-requested-with") != null) {
				try {
					response.sendError(303);
				} catch (IOException e) {
					log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
				}
				return false;
			} else {
				ModelAndView modelAndView = new ModelAndView("redirect:/adm/login.do");
				throw new ModelAndViewDefiningException(modelAndView);
			}
		}
	}
}
