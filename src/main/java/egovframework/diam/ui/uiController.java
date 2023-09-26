/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.ui;

import java.security.spec.InvalidKeySpecException;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.diam.cmm.util.CommonUtil;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;

/**
 * @Class Name : uiController.java
 * @Description : 관리자페이지 서브페이지 화면 출력 시 참조할 파일경로 매핑기능 구현 Controller 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
 */

@Controller
@Log4j2
public class uiController {

	/**
	 * menu
	 * 관리자페이지 메뉴관리 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 메뉴관리 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/menu.do")
	public String menu() {
		return "egovframework/diam/ui/menu/menu";
	}
	
	/**
	 * layout
	 * 관리자페이지 레이아웃관리 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 레이아웃관리 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/layout.do")
	public String layout() {
		return "egovframework/diam/ui/layout/layout";
	}
	
	/**
	 * layout_form
	 * 관리자페이지 레이아웃등록/수정 팝업 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 레이아웃등록/수정 팝업 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/layout_form.do")
	public String layout_form() {
		return "egovframework/diam/ui/layout/layout_form";
	}
	
	/**
	 * page
	 * 관리자페이지 페이지관리 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 페이지관리 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/page.do")
	public String page() {
		return "egovframework/diam/ui/page/page";
	}
	
	/**
	 * page_form
	 * 관리자페이지 페이지등록/수정 팝업 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 페이지등록/수정 팝업 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/page_form.do")
	public String page_form() {
		return "egovframework/diam/ui/page/page_form";
	}
	
	/**
	 * admin
	 * 관리자페이지 관리자관리 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 관리자관리 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/admin.do")
	public String admin() {
		return "egovframework/diam/ui/admin/admin";
	}
	
	/**
	 * admin_form
	 * 비밀번호 RSA암호화 전송을 위한 RSA세션 생성 및 공개키 화면에 전달 후 관리자페이지 관리자등록/수정 팝업 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param model 화면에 결과값을 전달하기 위한 ModelMap 객체
	 * @return String 관리자페이지 관리자등록/수정 팝업 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/admin_form.do")
	public String admin_form(HttpServletRequest request, ModelMap model) {
		
		try {
			CommonUtil commonUtil = new CommonUtil();
			JSONObject rsaObject = commonUtil.initRsa(request, "DIAM_ADMIN_RSA_KEY");
			
			if ("success".equals(rsaObject.get("result"))) {
	        	model.addAttribute("RSAModulus", rsaObject.get("RSAModulus"));
	    		model.addAttribute("RSAExponent", rsaObject.get("RSAExponent"));
	    		return "egovframework/diam/ui/admin/admin_form";
	        } else {
	        	model.addAttribute("message", "암호화 생성 중 오류가 발생하였습니다.");
	        	return "egovframework/diam/web/error";
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
	 * member_form
	 * 비밀번호 RSA암호화 전송을 위한 RSA세션 생성 및 공개키 화면에 전달 후 관리자페이지 회원등록/수정 팝업 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param model 화면에 결과값을 전달하기 위한 ModelMap 객체
	 * @return String 관리자페이지 회원등록/수정 팝업 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/member_form.do")
	public String member_form(HttpServletRequest request, Model model) {
		try {
			
			CommonUtil commonUtil = new CommonUtil();
			JSONObject rsaObject = commonUtil.initRsa(request, "DIAM_MEMBER_RSA_KEY");
			
			if ("success".equals(rsaObject.get("result"))) {
				model.addAttribute("RSAModulus", rsaObject.get("RSAModulus"));
				model.addAttribute("RSAExponent", rsaObject.get("RSAExponent"));
				return "egovframework/diam/ui/member/member_form";
			} else {
				model.addAttribute("message", "암호화 생성 중 오류가 발생하였습니다.");
				return "egovframework/diam/web/error";
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
	 * leave_member
	 * 관리자페이지 탈퇴회원관리 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 탈퇴회원관리 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/leave_member.do")
	public String leave_member() {
		return "egovframework/diam/ui/member/leave_member";
	}
		
	/**
	 * login_log
	 * 관리자페이지 회원로그인기록 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 회원로그인기록 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/login_log.do")
	public String login_log() {
		return "egovframework/diam/ui/member/login_log";
	}
	
	/**
	 * member
	 * 관리자페이지 회원관리 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 회원관리 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/member_list.do")
	public String member() {
		return "egovframework/diam/ui/member/member";
	}
		
	/**
	 * member_config
	 * 관리자페이지 회원 환경설정 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 회원 환경설정 페이지 파일경로를 문자열로 return
	 */
	@RequestMapping("/adm/member_config.do")
	public String member_config() {
		return "egovframework/diam/ui/member/member_config";
	}
		
	/**
	 * bbs
	 * 관리자페이지 게시판관리 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 게시판관리 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/bbs.do")
	public String bbs() {
		return "egovframework/diam/ui/bbs/bbs";
	}
	
	/**
	 * bbs_form
	 * 관리자페이지 게시판등록/수정 팝업 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 게시판등록/수정 팝업 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/bbs_form.do")
	public String bbs_form() {
		return "egovframework/diam/ui/bbs/bbs_form";
	}
	
	/**
	 * write_list
	 * 관리자페이지 게시글관리 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 게시글관리 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/write_list.do")
	public String write_list() {
		return "egovframework/diam/ui/write/write_list";
	}
	
	@RequestMapping("/adm/comment.do")
	public String comment() {
		return "egovframework/diam/ui/write/comment";
	}
	
	/**
	 * comment_list
	 * 관리자페이지 게시글관리 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 게시글관리 페이지 파일경로를 문자열로 return
	 */
	@RequestMapping("/adm/reply.do")
	public String reply() {
		return "egovframework/diam/ui/write/reply";
	}
	/**
	 * write_form
	 * 비밀번호 RSA암호화 전송을 위한 RSA세션 생성 및 공개키 화면에 전달 후 관리자페이지 게시글등록/수정 팝업 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param model 화면에 결과값을 전달하기 위한 ModelMap 객체
	 * @return String 관리자페이지  게시글등록/수정 팝업 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/write_form.do")
	public String write_form(HttpServletRequest request, ModelMap model) {
		try {
			CommonUtil commonUtil = new CommonUtil();
			JSONObject rsaObject = commonUtil.initRsa(request, "DIAM_WRITE_RSA_KEY");
			
			if ("success".equals(rsaObject.get("result"))) {
				model.addAttribute("RSAModulus", rsaObject.get("RSAModulus"));
				model.addAttribute("RSAExponent", rsaObject.get("RSAExponent"));
				return "egovframework/diam/ui/write/write_form";
			} else {
				model.addAttribute("message", "암호화 생성 중 오류가 발생하였습니다.");
				return "egovframework/diam/web/error";
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
	@RequestMapping("/adm/reply_form.do")
	public String reply_form(HttpServletRequest request, ModelMap model) {
		try {
			CommonUtil commonUtil = new CommonUtil();
			JSONObject rsaObject = commonUtil.initRsa(request, "DIAM_WRITE_RSA_KEY");
			
			if ("success".equals(rsaObject.get("result"))) {
				model.addAttribute("RSAModulus", rsaObject.get("RSAModulus"));
				model.addAttribute("RSAExponent", rsaObject.get("RSAExponent"));
				return "egovframework/diam/ui/write/reply_form";
			} else {
				model.addAttribute("message", "암호화 생성 중 오류가 발생하였습니다.");
				return "egovframework/diam/web/error";
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
	 * faq
	 * 관리자페이지 FAQ관리 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 FAQ관리 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/faq.do")
	public String faq() {
		return "egovframework/diam/ui/faq/faq";
	}
	
	/**
	 * faq_form
	 * 관리자페이지 FAQ등록/수정 팝업 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 FAQ등록/수정 팝업 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/faq_form.do")
	public String faq_form() {
		return "egovframework/diam/ui/faq/faq_form";
	}
		
	/**
	 * main_visual
	 * 관리자페이지 메인비주얼관리 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 메인비주얼관리 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/main_visual.do")
	public String main_visual() {
		return "egovframework/diam/ui/main_visual/main_visual";
	}
	
	/**
	 * main_visual_form
	 * 관리자페이지 메인비주얼등록/수정 팝업 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 메인비주얼등록/수정 팝업 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/main_visual_form.do")
	public String main_visual_form() {
		return "egovframework/diam/ui/main_visual/main_visual_form";
	}
	
	/**
	 * popup
	 * 관리자페이지 팝업관리 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 팝업관리 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/popup.do")
	public String popup() {
		return "egovframework/diam/ui/popup/popup";
	}
	
	/**
	 * popup_form
	 * 관리자페이지 팝업등록/수정 팝업 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 팝업등록/수정 팝업 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/popup_form.do")
	public String popup_form() {
		return "egovframework/diam/ui/popup/popup_form";
	}
	
	/**
	 * banner
	 * 관리자페이지 배너관리 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 배너관리 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/banner.do")
	public String banner() {
		return "egovframework/diam/ui/banner/banner";
	}
	
	/**
	 * banner_form
	 * 관리자페이지 배너등록/수정 팝업 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 배너등록/수정 팝업 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/banner_form.do")
	public String banner_form() {
		return "egovframework/diam/ui/banner/banner_form";
	}
		
	/**
	 * admin_menu
	 * 관리자페이지 관리자메뉴관리 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 관리자메뉴관리 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/admin_menu.do")
	public String admin_menu() {
		return "egovframework/diam/ui/admin_menu/admin_menu";
	}
	
	/**
	 * group
	 * 관리자페이지 그룹관리 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 그룹관리 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/group.do")
	public String group() {
		return "egovframework/diam/ui/group/group";
	}
	
	/**
	 * group_form
	 * 관리자페이지 그룹등록/수정 팝업 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 그룹등록/수정 팝업 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/group_form.do")
	public String group_form() {
		return "egovframework/diam/ui/group/group_form";
	}
	
	/**
	 * common_code
	 * 관리자페이지 공통코드관리 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 공통코드관리 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/common_code.do")
	public String common_code() {
		return "egovframework/diam/ui/common_code/common_code";
	}
	
	/**
	 * common_code_form
	 * 관리자페이지 공통코드등록/수정 팝업 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 공통코드등록/수정 팝업 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/common_code_form.do")
	public String common_code_form() {
		return "egovframework/diam/ui/common_code/common_code_form";
	}
	
	/**
	 * login_lock
	 * 관리자페이지 잠김회원관리 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 잠김회원관리 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/login_lock.do")
	public String login_lock() {
		return "egovframework/diam/ui/login_lock/login_lock";
	}
	
	/**
	 * domain
	 * 관리자페이지 도메인관리 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 도메인관리 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/domain.do")
	public String domain() {
		return "egovframework/diam/ui/domain/domain";
	}
	
	/**
	 * domain_form
	 * 관리자페이지 도메인등록/수정 팝업 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 도메인등록/수정 팝업 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/domain_form.do")
	public String domain_form() {
		return "egovframework/diam/ui/domain/domain_form";
	}
	
	/**
	 * confg_env
	 * 관리자페이지 도메인 기본설정등록/수정 팝업 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 도메인 기본설정등록/수정 팝업 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/config_form.do")
	public String confg_env() {
		return "egovframework/diam/ui/domain/config_form";
	}
	
	/**
	 * page_env
	 * 관리자페이지 도메인 기본페이지 설정(이용약관, 개인정보취급방침, 이메일수집, 개인정보관리자, 회원대상 동의항목, 비회원 대상 동의항목, 이용안내, 탈퇴안내) 페이지  파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 도메인 기본설정등록/수정 팝업 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/page_env.do")
	public String page_env() {
		return "egovframework/diam/ui/domain/page_env";
	}
	
	/**
	 * access_ip
	 * 관리자페이지 접근아이피관리 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 접근아이피관리 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/access_ip.do")
	public String access_ip() {
		return "egovframework/diam/ui/access_ip/access_ip";
	}
	
	/**
	 * access_ip_form
	 * 관리자페이지 접근아이피등록/수정 팝업 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 접근아이피등록/수정 팝업 페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/access_ip_form.do")
	public String access_ip_form() {
		return "egovframework/diam/ui/access_ip/access_ip_form";
	}
	
	/**
	 * visit_u
	 * 관리자페이지 방문자분석 방문현황 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지  방문자분석 방문현황 페이지 파일경로를 문자열로 return
	 */
	@RequestMapping("/adm/visit.do")
	public String visit_u() {
		return "egovframework/diam/ui/statistics/visit";
	}
	
	/**
	 * visit_day
	 * 관리자페이지 방문자분석 일별방문현황 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 방문자분석 일별방문현황 페이지 파일경로를 문자열로 return
	 */
	@RequestMapping("/adm/visit_day.do")
	public String visit_day() {
		return "egovframework/diam/ui/statistics/visit_day";
	}
	
	/**
	 * visit_hour
	 * 관리자페이지 방문자분석 시간대별 방문현황 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 방문자분석 시간대별 방문현황 페이지 파일경로를 문자열로 return
	 */
	@RequestMapping("/adm/visit_hour.do")
	public String visit_hour() {
		return "egovframework/diam/ui/statistics/visit_hour";
	}
	
	/**
	 * visit_week
	 * 관리자페이지 방문자분석 요일별 방문현황 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 방문자분석 요일별 방문현황 페이지 파일경로를 문자열로 return
	 */
	@RequestMapping("/adm/visit_week.do")
	public String visit_week() {
		return "egovframework/diam/ui/statistics/visit_week";
	}
	
	/**
	 * visit_month
	 * 관리자페이지 방문자분석 월별 방문현황 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 방문자분석 페이지 파일경로를 문자열로 return
	 */
	@RequestMapping("/adm/visit_month.do")
	public String visit_month() {
		return "egovframework/diam/ui/statistics/visit_month";
	}
	
	/**
	 * visitor_route_u
	 * 관리자페이지 방문자경로 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 방문자경로 페이지 파일경로를 문자열로 return
	 */
	@RequestMapping("/adm/visitor_route.do")
	public String visitor_route_u() {
		return "egovframework/diam/ui/statistics/visitor_route";
	}
	
	/**
	 * visitor_env_u
	 * 관리자페이지 방문자환경 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지  방문자환경 페이지 파일경로를 문자열로 return
	 */
	@RequestMapping("/adm/visitor_env.do")
	public String visitor_env_u() {
		return "egovframework/diam/ui/statistics/visitor_env";
	}
	
	@RequestMapping("/adm/visitor_browser.do")
	public String visitor_browser() {
		return "egovframework/diam/ui/statistics/visitor_browser";
	}
	
	/**
	 * new_member_u
	 * 관리자페이지 신규회원분석 일별신규회원 현황 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 신규회원분석 일별신규회원 현황 페이지 파일경로를 문자열로 return
	 */
	@RequestMapping("/adm/new_member.do")
	public String new_member_u() {
		return "egovframework/diam/ui/statistics/statistics_new_member";
	}
	
	/**
	 * new_member_month
	 * 관리자페이지 신규회원분석 월별 신규회원 현황 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 신규회원분석 월별 신규회원 현황 페이지 파일경로를 문자열로 return
	 */
	@RequestMapping("/adm/new_member_month.do")
	public String new_member_month() {
		return "egovframework/diam/ui/statistics/statistics_new_member_month";
	}
	
	/**
	 * all_member_u
	 * 관리자페이지 전체회원분석 일별 회원 현황 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 전체회원분석 일별 회원 현황 페이지 파일경로를 문자열로 return
	 */
	@RequestMapping("/adm/all_member.do")
	public String all_member_u() {
		return "egovframework/diam/ui/statistics/statistics_all_member";
	}
	
	/**
	 * all_member_month
	 * 관리자페이지 전회체원분석 월별 회원 현황 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 전체회원분석 월별 회원 현황 페이지 파일경로를 문자열로 return
	 */
	@RequestMapping("/adm/all_member_month.do")
	public String all_member_month() {
		return "egovframework/diam/ui/statistics/statistics_all_member_month";
	}
	
	/**
	 * page_view_u
	 * 관리자페이지 페이지뷰 분석 페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자페이지 페이지뷰 분석 페이지 파일경로를 문자열로 return
	 */
	@RequestMapping("/adm/page_view.do")
	public String page_view_u() {
		return "egovframework/diam/ui/statistics/page_view";
	}
	
}