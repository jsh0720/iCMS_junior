/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.cmm.interceptor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

import egovframework.diam.biz.model.board.Dm_board_vo;
import egovframework.diam.biz.model.config.Dm_config_vo;
import egovframework.diam.biz.model.display.Dm_menus_vo;
import egovframework.diam.biz.model.display.Dm_pages_vo;
import egovframework.diam.biz.model.statistics.Dm_visit_env_vo;
import egovframework.diam.biz.model.statistics.Dm_visit_orgin_vo;
import egovframework.diam.biz.model.statistics.Dm_visit_vo;
import egovframework.diam.biz.model.statistics.Dm_web_log_vo;
import egovframework.diam.biz.service.board.BoardService;
import egovframework.diam.biz.service.config.ConfigService;
import egovframework.diam.biz.service.display.MenuService;
import egovframework.diam.biz.service.display.PageService;
import egovframework.diam.biz.service.statistics.VisitEnvService;
import egovframework.diam.biz.service.statistics.VisitOrginService;
import egovframework.diam.biz.service.statistics.VisitService;
import egovframework.diam.biz.service.statistics.WebLogService;
import egovframework.diam.cmm.model.LoginVO;
import egovframework.diam.cmm.util.CommonUtil;
import egovframework.diam.cmm.util.EgovUserDetailsHelper;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;

/**
 * @className : WebInterceptor.java
 * @author : (주)디자인아이엠
 * @Date : 2023. 3. 21.
 * @Description : 사용자페이지 도메인/기본설정 정보 조회를 수행하는 인터셉터
 */
@Log4j2
public class WebInterceptor extends WebContentInterceptor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebInterceptor.class);
	
	@Resource(name="configService")
	private ConfigService configService;
	
	@Resource(name="pageService")
	private PageService pageService;
	
	@Resource(name="webLogService")
	private WebLogService webLogService;
	
	@Resource(name="visitService")
	private VisitService visitService;
	
	@Resource(name="visitEnvService")
	private VisitEnvService visitEnvService;
	
	@Resource(name="visitOrginService")
	private VisitOrginService visitOrginService;
	
	@Resource(name="boardService")
	private BoardService boardService;
	
	@Autowired
	private MenuService menuService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
		String domain_site = "";
		
		String request_url = request.getRequestURL().toString();
		String request_querystring = request.getQueryString() != null ? "?" + request.getQueryString() : "";
		
		String domain = request_url.substring(request_url.indexOf("://") + 3);
		domain = domain.substring(0, domain.indexOf("/"));
		String request_uri = request.getRequestURI();
		request_uri = request_uri.replace(request.getContextPath(), "");
		if (request_url.indexOf("WEB-INF") < 0) {
			try {
				//README.3
				String mode = System.getProperty("globals.properties.mode");
				if(mode.equals("dev")) {
					domain = "icms.java.cro.kro.kr";
				}
				domain_site = domain + "/" + request_uri.substring(1, request_uri.indexOf("/", 1));
			} catch(StringIndexOutOfBoundsException sioob) {
				LOGGER.debug("==> DOMAIN URL NOT ABLE TO SUBSTRING BY StringIndexOutOfBoundsException");
			} catch(Exception e) {
				LOGGER.debug("==> DOMAIN URL NOT ABLE TO SUBSTRING BY Other Exception");
			}
			
			
			Dm_config_vo configVO = new Dm_config_vo();
			configVO.setDm_url(domain_site);
			try {
				configVO = configService.selectDmConfigByUrl(configVO);
			} catch(DataAccessException dae) {
				log.error(MessageCode.CMM_DATA_ERROR.getLog());
			} catch (Exception e) {
				log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			}
			
			if (configVO == null) {
				configVO = new Dm_config_vo();
				configVO.setDm_url(domain);
				try {
					configVO = configService.selectDmConfigByUrl(configVO);
				} catch(DataAccessException dae) {
					log.error(MessageCode.CMM_DATA_ERROR.getLog());
				} catch (Exception e) {
					log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
				}
			}
			
			if (configVO == null) {
				ModelAndView modelAndView = new ModelAndView("egovframework/diam/web/error");
				modelAndView.addObject("message", "등록된 도메인 URL이 없습니다.");
				throw new ModelAndViewDefiningException(modelAndView);
			} else {
				LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
				request.setAttribute("DiamLoginVO", loginVO);
				request.setAttribute("CONFIG_INFO", configVO);
				/*
				 * 사용자 통계
				 * 2022-07-19
				 * 김민성
				 */
				//if ("/index.do".equals(request_uri)) {
				if (request_uri.contains("/index.do")) {
					try {
						Calendar calendar = Calendar.getInstance();
						Date date = calendar.getTime();
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String today = format.format(date);
						String referer = (String)request.getHeader("referer")!=null? (String)request.getHeader("referer") : "";
						
						CommonUtil commonUtil = new CommonUtil();
						String user_ip = commonUtil.getUserIp(request);
						String date_time = today;
						String dm_domain = configVO.getDm_domain_id();
						
						Dm_web_log_vo webLogVO = new Dm_web_log_vo();

						Dm_pages_vo dmPageVO = new Dm_pages_vo();
						String dm_uid = request.getParameter("contentId") != null ? request.getParameter("contentId").trim() : "";

						if (!"".equals(dm_uid)) {
							if (isNumber(dm_uid)) {
								Dm_menus_vo menuVO = Dm_menus_vo.builder().dm_id(dm_uid).build();
								menuVO = menuService.selectMenuByDmId(menuVO);
								if (menuVO != null) {
									dm_uid = menuVO.getDm_link_data();
								}
							}
							
							dmPageVO.setDm_uid(dm_uid);
							dmPageVO = pageService.selectPageDmUid(dmPageVO);
						} else {
							dmPageVO.setDm_domain(configVO.getDm_domain_id());
							dmPageVO = pageService.selectPageMainContent(dmPageVO);
						}
						
						webLogVO.setDm_fn_url(request_url + request_querystring);
						webLogVO.setDm_agent_info(request.getHeader("User-Agent"));
						webLogVO.setDm_ip(user_ip);
						webLogVO.setDm_domain(dm_domain);
						webLogVO.setDm_type(dmPageVO.getDm_page_name());
						webLogVO.setDm_fn_code("page");
						if(!"".equals(loginVO.getId()) && loginVO.getId() != null) {
							webLogVO.setDm_login_id(loginVO.getId());
						} else {
							webLogVO.setDm_login_id("not login");
						}
						int check_ip = webLogService.selectWebLogIp(webLogVO);
						if(check_ip == 0) {
							webLogVO.setDm_re_visit("visit");
						} else {
							webLogVO.setDm_re_visit("re_visit");
						}
						
						webLogService.insertWebLog(webLogVO);
						if(request.getSession().getAttribute("visit") == null) {
							request.getSession().setAttribute("visit", "1");
							Dm_visit_vo visitVO = new Dm_visit_vo();
							visitVO.setDm_datetime(date_time);
							if("PAGE".equals(dmPageVO.getDm_page_type())) {
								visitVO.setDm_type("page");
							} else if("BOARD".equals(dmPageVO.getDm_page_type())) {
								Dm_board_vo boardVO = new Dm_board_vo();
								boardVO.setDm_id(dmPageVO.getDm_board_id());
								Dm_board_vo board = boardService.selectBoard(boardVO);
								visitVO.setDm_type(board.getDm_table());
							} else {
								visitVO.setDm_type(dmPageVO.getDm_page_type().toLowerCase());
							}
							visitVO.setDm_domain(dm_domain);
							visitVO.setDm_page_name(dmPageVO.getDm_page_name());
							int check_visit = visitService.checktVisit(visitVO);
							if(check_visit > 0) {
								visitService.updateCountVisit(visitVO);
							} else {
								visitVO.setDm_count(1);
								visitService.insertVisit(visitVO);
							}
						}

						if(request.getSession().getAttribute("visit_env") == null) {
							request.getSession().setAttribute("visit_env", "1");
							Dm_visit_env_vo visitEnvVO = new Dm_visit_env_vo();
							visitEnvVO.setDm_datetime(date_time);
							visitEnvVO.setDm_os(commonUtil.UserOsChk(request));
							visitEnvVO.setDm_brower(commonUtil.UserBrowserChk(request));
							//if(commonUtil.isMobile(request)) {
							if(commonUtil.isMobile(visitEnvVO.getDm_os())) {
								visitEnvVO.setDm_type("1");
							} else {
								visitEnvVO.setDm_type("0");
							}
							visitEnvVO.setDm_domain(dm_domain);
							int check_visit_env = visitEnvService.checkVisitEnv(visitEnvVO);
							if(check_visit_env > 0) {
								visitEnvService.updateVisitEnv(visitEnvVO);
							} else {
								visitEnvVO.setDm_count(1);
								visitEnvService.insertVisitEnv(visitEnvVO);
							}
						}

						if("1".equals(dmPageVO.getDm_main_content())) {
							Dm_visit_orgin_vo visitOrginVO = new Dm_visit_orgin_vo();
							visitOrginVO.setDm_datetime(date_time);
							visitOrginVO.setDm_orgin(referer);
							visitOrginVO.setDm_domain(dm_domain);
							int check_visit_orgin = visitOrginService.checkVisitOrgin(visitOrginVO);
							if(check_visit_orgin > 0) {
								visitOrginService.updateVisitOrgin(visitOrginVO);
							} else {
								visitOrginVO.setDm_count(1);
								visitOrginService.insertVisitOrgin(visitOrginVO);
							}
						}
					} catch(DataAccessException dae) {
						log.error(MessageCode.CMM_DATA_ERROR.getLog());
					} catch (Exception e) {
						log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
					}
				}
				return true;
			}			
		} else {
			return true;
		}		
	}
	
	private boolean isNumber(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
}
