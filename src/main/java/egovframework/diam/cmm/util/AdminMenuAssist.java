/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.cmm.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import egovframework.diam.biz.model.admin.Dm_access_admin_menu_vo;
import egovframework.diam.biz.service.admin.AdminMenuService;

/**
 * @Class Name : AdminMenuAssist.java
 * @Description : 관리자페이지 권한별 관리자메뉴 조회 메소드를 jsp단에서 사용할 수 있도록 bean 객체를 생성해주는 유틸 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
 */

public class AdminMenuAssist {
	private HttpServletRequest request;
	private WebApplicationContext context;
	private AdminMenuService adminMenuService;
	
	/**
	 * AdminMenuAssist
	 * jsp페이지에서 service를 직접 호출하기 위해 bean을 생성
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 BindingResult객체
	 * @return AdminMenuAssist 생성자로서 클래스 내부의 값 설정기능만 수행
	*/
	public AdminMenuAssist(HttpServletRequest request) throws Exception {
		this.request = request;
		this.context = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		this.adminMenuService = (AdminMenuService) this.context.getBean("adminMenuService");		
	}
	
	/**
	 * getMenuList
	 * 관리자페이지 메인페이지 왼쪽 메뉴리스트 출력 시 사용
	 * @param dm_parent_id 메뉴를 가져올 기준이되는 상위아이디 값을 문자열로 전달
	 * @param dm_access_level 관리자 로그인 시 로그인한 계정의 권한 값을 문자열로 전달
	 * @return AdminMenuAssist 생성자로서 클래스 내부의 값 설정기능만 수행
	*/
	public List<Dm_access_admin_menu_vo> getMenuList(String dm_parent_id, String dm_access_level) throws Exception {
		Dm_access_admin_menu_vo vo = new Dm_access_admin_menu_vo();
		vo.setDm_parent_id(dm_parent_id);
		vo.setDm_access_level(dm_access_level);
				
		return adminMenuService.selectAdminMenuListMain(vo);
	}
	
}
