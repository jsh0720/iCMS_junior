/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.diam.biz.model.config.Dm_config_vo;
import egovframework.diam.biz.model.config.Dm_domain_list_vo;
import egovframework.diam.biz.service.config.ConfigService;
import egovframework.diam.biz.service.config.DomainService;

/**
 * @Class Name : DiamCommIndexController.java
 * @Description : 관리자페이지 main/page iframe 내용표출을 위해 호출하는 include URL Mapping Controller클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
 */

@Controller
public class DiamCommIndexController {
	
	@Resource(name="domainService")
	private DomainService domainService;
	
	@Resource(name="configService")
	private ConfigService configService;
	
	/**
	 * main
	 * 관리자 메인페이지를 표출하기 위해 메인페이지 파일경로를 문자열로 ViewResolver로 전송
	 * @return String 관리자 메인페이지 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/main.do")
	public String main() {	
		return "egovframework/diam/ui/main";
	}
	
	/**
	 * header
	 * 관리자 메인페이지 메인프레임에서 include 되는 header 파일경로를  문자열로 ViewResolver로 전송
	 * @param model 화면에 결과값을 전달하기 위한 ModelMap 객체
	 * @return String 관리자 메인페이지 메인프레임 header 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/frame_header.do")
	public String header(ModelMap model) throws Exception {
		Dm_domain_list_vo domainVO = new Dm_domain_list_vo();
		domainVO = domainService.selectDomainMain();
		
		if (domainVO != null) {
			Dm_config_vo configVO = new Dm_config_vo();
			configVO.setDm_domain_id(domainVO.getDm_id());
			configVO = configService.selectDmConfig(configVO);			
			model.addAttribute("main_domain_url", configVO.getDm_url());
		}		
		return "egovframework/diam/lib/frame_header";
	}
	
	/**
	 * body
	 * 관리자 메인페이지 메인프레임에서 include 되는 body 파일경로를  문자열로 ViewResolver로 전송
	 * @return String 관리자 메인페이지 메인프레임 body 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/frame_body.do") 
	public String body() {
		return "egovframework/diam/lib/frame_body";
	}
	
	/**
	 * bottom
	 * 관리자 메인페이지 메인프레임에서 include 되는 bottom 파일경로를  문자열로 ViewResolver로 전송
	 * @return String 관리자 메인페이지 메인프레임 bottom 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/frame_bottom.do")
	public String bottom() {
		return "egovframework/diam/lib/frame_bottom";
	}
	
	/**
	 * page_header
	 * 관리자 메인페이지 서브프레임에서 include 되는 header 파일경로를  문자열로 ViewResolver로 전송
	 * @return String 관리자 메인페이지 서브프레임 header 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/page_header.do")
	public String page_header() {
		return "egovframework/diam/lib/page_header";
	}
	
	/**
	 * page_bottom
	 * 관리자 메인페이지 서브프레임에서 include 되는 bottom 파일경로를  문자열로 ViewResolver로 전송
	 * @return String 관리자 메인페이지 서브프레임 bottom 파일경로를 문자열로 return
	*/
	@RequestMapping("/adm/page_bottom.do")
	public String page_bottom() {
		return "egovframework/diam/lib/page_bottom";
	}
}
