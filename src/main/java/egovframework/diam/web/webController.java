/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.web;

import java.io.File;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import egovframework.diam.biz.model.board.Dm_board_vo;
import egovframework.diam.biz.model.board.Dm_faq_vo;
import egovframework.diam.biz.model.board.Dm_write_vo;
import egovframework.diam.biz.model.config.Dm_config_vo;
import egovframework.diam.biz.model.design.Dm_banners_vo;
import egovframework.diam.biz.model.design.Dm_main_visual_vo;
import egovframework.diam.biz.model.design.Dm_popup_vo;
import egovframework.diam.biz.model.display.Dm_layout_vo;
import egovframework.diam.biz.model.display.Dm_menus_vo;
import egovframework.diam.biz.model.display.Dm_pages_vo;
import egovframework.diam.biz.model.member.Dm_member_config_vo;
import egovframework.diam.biz.model.member.Dm_member_vo;
import egovframework.diam.biz.service.board.BoardService;
import egovframework.diam.biz.service.board.FaqService;
import egovframework.diam.biz.service.board.WriteService;
import egovframework.diam.biz.service.config.ConfigService;
import egovframework.diam.biz.service.design.BannerService;
import egovframework.diam.biz.service.design.MainVisualService;
import egovframework.diam.biz.service.design.PopupService;
import egovframework.diam.biz.service.display.LayoutService;
import egovframework.diam.biz.service.display.MenuService;
import egovframework.diam.biz.service.display.PageService;
import egovframework.diam.biz.service.member.MemberConfigService;
import egovframework.diam.biz.service.member.MemberService;
import egovframework.diam.cmm.model.Dm_common_code_vo;
import egovframework.diam.cmm.model.LoginVO;
import egovframework.diam.cmm.service.CommonCodeService;
import egovframework.diam.cmm.service.SearchService;
import egovframework.diam.cmm.util.CommonUtil;
import egovframework.diam.cmm.util.EgovUserDetailsHelper;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;

/**
 * @Class Name : webController.java
 * @Description : 사용자페이지 레이아웃, 헤더/컨텐츠/푸터, index페이지 등 페이지별 사용자페이지 렌더링 기능 구현 Controller클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
 */

@Controller
@Log4j2
public class webController {
	
	private String ERROR_PAGE = "egovframework/diam/web/error";
	private String UNDER_CONSTRUCTION = "egovframework/diam/web/under_construction";
		
	@Resource(name="pageService") private PageService pageService;
	@Resource(name="layoutService")	private LayoutService layoutService;
	@Resource(name="configService")	private ConfigService configService;
	@Resource(name="popupService") private PopupService popupService;
	@Resource(name="menuService") private MenuService menuService;
	@Resource(name="mainVisualService") private MainVisualService mainVisualService;	
	@Resource(name="boardService") private BoardService boardService;
	@Resource(name="writeService") private WriteService writeService;
	@Resource(name="memberService") private MemberService memberService;
	@Resource(name="faqService") private FaqService faqService;
	@Resource(name="bannerService") private BannerService bannerService;
	@Resource(name="memberConfigService") private MemberConfigService memberConfigService;
	@Resource(name="searchService")	private SearchService searchService;
	@Resource(name="commonCodeService")	private CommonCodeService commonCodeService;
	
	private boolean isNumber(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
	
	
	public HashMap<String, Dm_menus_vo> selectUnderMenu(List<Dm_menus_vo> list, Dm_menus_vo vo) {
		HashMap<String, Dm_menus_vo> result = new HashMap<>();
		
		list.forEach(item -> {
			if (item.getDm_parent_id().equals(vo.getDm_id()) && item.getDm_link_data().equals(vo.getDm_link_data())) {
				result.put(item.getDm_depth(), item);
				result.putAll(selectUnderMenu(list, item));
			}
		});
		
		return result;
	}
	
	/**
	 * selectMenu
	 * 사용자페이지 header 영역에 표출할 메뉴리스트를 상위메뉴 > 하위메뉴 순으로 재귀호출을 통하여 데이터 가공
	 * @param map 재귀호출을 통하여 가공된 메뉴데이터를 전달
	 * @param menuList 사용중인 전체 메뉴리스트를 List자료형으로 전달 
	 * @param nMenu 반복문 수행 중 조건에 일치하는 메뉴데이터를 vo객체로 전달
	 * @return HashMap<String, Dm_menus_vo> 재귀호출을 통하여 가공된 메뉴데이터를 HashMap자료형으로 return
	*/
	private HashMap<String, Dm_menus_vo> selectMenu(HashMap<String, Dm_menus_vo> map, List<Dm_menus_vo> menuList,
			Dm_menus_vo nMenu) {
		for (Dm_menus_vo dm_menus_vo : menuList) {
			if (nMenu.getDm_parent_id().equals(dm_menus_vo.getDm_id())) {
				map.put(dm_menus_vo.getDm_depth(), dm_menus_vo);
				map = this.selectMenu(map, menuList, dm_menus_vo);
			}
		}
		return map;
	}
	
	/**
	 * index
	 * 사용자 메인페이지를 표출하기 위해 메인페이지 파일경로와 화면에 출력될 데이터를 ModelAndView 객체로 ViewResolver로 전송
	 * @param dm_uid 메인/서브 페이지 고유Uid 값을 전달받는 문자열
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @return ModelAndView 사용자 메인페이지 파일경로 + 화면에서 사용될 데이터 ModelAndView에 담아 return
	*/
	@RequestMapping(value = {"/{domainId}/index.do", "/index.do"})
	public ModelAndView index(@RequestParam(value="contentId", required=false, defaultValue="") String contentId,
			@RequestParam(value="command", required=false, defaultValue="") String command,
			@PathVariable(value="domainId", required=false) String domainId,
			HttpServletRequest request) {
		ModelAndView result = new ModelAndView();
		CommonUtil commonUtil = new CommonUtil();
		try {
			
			Dm_config_vo configVO = (Dm_config_vo) request.getAttribute("CONFIG_INFO");
			Dm_pages_vo pageVO = new Dm_pages_vo();
			if (!commonUtil.isNullOrEmpty(domainId)) {
				if (configVO.getDm_url().contains("/" + domainId)) {
					String[] domainArray = configVO.getDm_url().split(domainId);
					if (domainArray.length > 1) {
						result.addObject("message", "해당하는 페이지 정보가 없습니다.");
						result.setViewName(this.ERROR_PAGE);
						return result;
					}
				} else {
					result.addObject("message", "해당하는 페이지 정보가 없습니다.");
					result.setViewName(this.ERROR_PAGE);
					return result;
				}
			}
			
			if (!commonUtil.isNullOrEmpty(contentId)) {
				if (isNumber(contentId)) {
					result.addObject("isPK", true);
					Dm_menus_vo menuVO = Dm_menus_vo.builder().dm_id(contentId).build();
					menuVO = menuService.selectMenuByDmId(menuVO);
					if (menuVO != null) {
						contentId = menuVO.getDm_link_data();
					}
				}
				
				pageVO.setDm_domain(configVO.getDm_domain_id());
				pageVO.setDm_uid(contentId);					
				pageVO = pageService.selectPageDmUid(pageVO);
			} else {
				pageVO.setDm_domain(configVO.getDm_domain_id());
				pageVO = pageService.selectPageMainContent(pageVO);
			}
			
			if (pageVO != null) {
				Dm_layout_vo layoutVO = new Dm_layout_vo();
				layoutVO.setDm_id(configVO.getDm_theme());
				layoutVO = layoutService.selectLayout(layoutVO);
				if (layoutVO != null) {
					if (pageVO.getDm_page_type().equals("CLAUSE") || pageVO.getDm_page_type().equals("MEMBER")) {
						if (!commonUtil.isNullOrEmpty(command)) {
							Dm_common_code_vo codeVO= commonCodeService.selectCodeNameByValue(Dm_common_code_vo.builder()
									.dm_code_group("1000")
									.dm_code_value(command)
									.build());
							if (codeVO != null) {
								pageVO.setDm_page_name(codeVO.getDm_code_name());
							}
						}
					}
					
					request.setAttribute("pageVO", pageVO);
					request.setAttribute("layoutVO", layoutVO);
					request.setAttribute("layout_path", "/thema/" + layoutVO.getDm_layout_folder());
					
					if("1".equals(pageVO.getDm_main_content()))	{
						Dm_popup_vo popupVO = new Dm_popup_vo();
						popupVO.setDm_domain(configVO.getDm_domain_id());
						List<Dm_popup_vo> popupList = popupService.selectPopupListForWeb(popupVO);
						result.addObject("popupVO", popupList);
					}
					result.setViewName("egovframework/diam/web/index");
				} else {
					result.addObject("message", "해당하는 레이아웃 정보가 없습니다.");
					result.setViewName(this.ERROR_PAGE);
				}
			} else {
				result.addObject("message", "해당하는 페이지 정보가 없습니다.");
				result.setViewName(this.ERROR_PAGE);
			}
		} catch(DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			result.addObject("message", "SQL 구문오류가 발생하였습니다.");
			result.setViewName(this.ERROR_PAGE);
		} catch (Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			result.addObject("message", "알 수 없는 오류가 발생하였습니다. 관리자에게 문의주세요.");
			result.setViewName(this.ERROR_PAGE);
		}
		return result;
	}

	/**
	 * header
	 * 사용자 메인/서브페이지에 표출될 상단 header 파일경로와 화면에서 사용할 데이터를 ModelAndView 객체로 ViewResolver로 전송
	 * @param dm_uid 메인/서브 페이지 고유Uid 값을 전달받는 문자열
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @return ModelAndView 사용자 메인/서브페이지 header 파일경로 + 화면에서 사용될 데이터 ModelAndView에 담아 return
	 */
	@RequestMapping("/web/frame_header.do")
	public ModelAndView header(@RequestParam(value="contentId", required=false, defaultValue="") String contentId,
			HttpServletRequest request) throws Exception {
		ModelAndView result = new ModelAndView();
		try {
			Dm_layout_vo layoutVO = (Dm_layout_vo) request.getAttribute("layoutVO");
			result.setViewName("egovframework/diam/web/thema/" + layoutVO.getDm_layout_folder() + "/header");
		} catch(DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			result.addObject("message", "SQL오류가 발생하였습니다.");
			result.setViewName(this.ERROR_PAGE);
		} catch (Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			result.addObject("message", "알 수 없는 오류가 발생하였습니다. 관리자에게 문의주세요.");
			result.setViewName(this.ERROR_PAGE);
		}
		return result;
	}
	/**
	 * header
	 * 사용자 메인/서브페이지에 표출될 상단 header 파일경로와 화면에서 사용할 데이터를 ModelAndView 객체로 ViewResolver로 전송
	 * @param dm_uid 메인/서브 페이지 고유Uid 값을 전달받는 문자열
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @return ModelAndView 사용자 메인/서브페이지 header 파일경로 + 화면에서 사용될 데이터 ModelAndView에 담아 return
	*/
	@RequestMapping("/web/frame_top.do")
	public ModelAndView top(@RequestParam(value="contentId", required=false, defaultValue="") String contentId,
			HttpServletRequest request) throws Exception {
		ModelAndView result = new ModelAndView();
		try {
			Dm_config_vo configVO = (Dm_config_vo) request.getAttribute("CONFIG_INFO");
			Dm_layout_vo layoutVO = (Dm_layout_vo) request.getAttribute("layoutVO");
			
			Dm_main_visual_vo mainVisualVO = new Dm_main_visual_vo();
			mainVisualVO.setDm_domain(configVO.getDm_domain_id());
			List<Dm_main_visual_vo> mainVisualList = mainVisualService.selectMainVisualListForWeb(mainVisualVO);
			result.addObject("mainVisualList", mainVisualList);
			
			Dm_member_config_vo memberConfigVO = new Dm_member_config_vo();
			memberConfigVO = memberConfigService.selectMemberConfig();
			result.addObject("memberConfigVO", memberConfigVO);
			
			Dm_menus_vo menuVO = new Dm_menus_vo();
			menuVO.setDm_domain(configVO.getDm_domain_id());
			List<Dm_menus_vo> menuList = menuService.selectMenuList(menuVO);
			
			request.setAttribute("menuList", menuList);
						
			if (contentId != null && !contentId.isEmpty()) {
				HashMap<String, Dm_menus_vo> now_menu = new HashMap<>();
				
				if (isNumber(contentId)) {
					HashMap<String, Dm_menus_vo> childMenu = new HashMap<>();
					result.addObject("isPK", true);
					for (Dm_menus_vo dm_menus_vo : menuList) {
						if (contentId.equals(dm_menus_vo.getDm_id())) {
							now_menu.put(dm_menus_vo.getDm_depth(), dm_menus_vo);
							now_menu = selectMenu(now_menu, menuList, dm_menus_vo);
							childMenu = selectUnderMenu(menuList,dm_menus_vo);
							now_menu.putAll(childMenu);
						}
					}
				
				} else {
					for (Dm_menus_vo dm_menus_vo : menuList) {
						if (contentId.equals(dm_menus_vo.getDm_link_data())) {
							now_menu.put(dm_menus_vo.getDm_depth(), dm_menus_vo);
							now_menu = selectMenu(now_menu, menuList, dm_menus_vo);
						}
					}
				}
				
				String now_menu_navi = "";
				if (now_menu.size() < 1) {
					now_menu = selectMenu(now_menu, menuList, menuList.get(0));					
				} else {
					for (int i= 1 ; i <= now_menu.size() ; i++) {
						if (i == 1) {
							now_menu_navi += "<li><a href=\"?contentId=\">" + now_menu.get(Integer.toString(i)).getDm_menu_text() + "</a></li>";							
						} else {
							now_menu_navi += "<li><a href=\"?contentId="+now_menu.get(Integer.toString(i)).getDm_id()+"\">" + now_menu.get(Integer.toString(i)).getDm_menu_text() + "</a></li>";							
						}
					}
				}
				result.addObject("now_menu", now_menu);
				result.addObject("now_menu_navi", now_menu_navi);
			}
			
			result.addObject("login_uid", pageService.selectPageUtilUid(configVO.getDm_domain_id(), "LOGIN"));
			result.addObject("member_uid", pageService.selectPageUtilUid(configVO.getDm_domain_id(), "MEMBER"));
			result.setViewName("egovframework/diam/web/thema/" + layoutVO.getDm_layout_folder() + "/top");
		} catch(DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			result.addObject("message", "SQL오류가 발생하였습니다.");
			result.setViewName(this.ERROR_PAGE);
		} catch (Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			result.addObject("message", "알 수 없는 오류가 발생하였습니다. 관리자에게 문의주세요.");
			result.setViewName(this.ERROR_PAGE);
		}
		return result;
	}

	/**
	 * header_sub
	 * 사용자 서브페이지 서브비주얼 영역에 표출될  sub header 파일경로와 화면에서 사용할 데이터를 ModelAndView 객체로 ViewResolver로 전송
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @return ModelAndView 사용자 서브페이지 sub header 파일경로 + 화면에서 사용될 데이터 ModelAndView에 담아 return
	*/
	@RequestMapping("/web/frame_top.sub.do")
	public ModelAndView top_sub(HttpServletRequest request) throws Exception {
		ModelAndView result = new ModelAndView();
		try {
			Dm_layout_vo layoutVO = (Dm_layout_vo) request.getAttribute("layoutVO");
			result.setViewName("egovframework/diam/web/thema/" + layoutVO.getDm_layout_folder() + "/top.sub");
		} catch(NullPointerException npe) {
			log.error(MessageCode.CMM_NULL_ERROR.getLog());
			result.addObject("message", "레이아웃 정보가 없습니다.");
			result.setViewName(this.ERROR_PAGE);
		} catch (Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			result.addObject("message", "알 수 없는 오류가 발생하였습니다. 관리자에게 문의주세요.");			
			result.setViewName(this.ERROR_PAGE);
		}
		return result;
	}

	
	/**
	 * body
	 * 사용자 메인/서브페이지 컨텐츠 영역에 표출될 페이지의 타입에 따라 데이터 가공 및 파일경로를  ModelAndView 객체로 ViewResolver로 전송
	 * @param command 게시판 타입페이지 리스트/상세보기/쓰기 등의 화면 타입을 문자열로 전달
	 * @param wr_id 게시글의 PK값을 문자열로 전달, 게시글 상세보기/답변 작성 등에 사용
	 * @param dm_no 회원정보의 PK값을 문자열로 전달, 회원정보 수정 시 사용
	 * @param searchDmWriteVO 게시판 타입 페이지에서 리스트 검색 시 검색한 값들을 vo객체에 담아 전달
	 * @param paramMap 현재페이지,검색조건,컨텐츠 아이디 등의 컨텐츠페이지와 관련된 파라미터들을 Map자료형에 담아 전달
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param session 세션에 저장된 검증된 비밀번호 값을 꺼낼 시 사용하는 HttpSession 객체
	 * @return ModelAndView 사용자 메인/서브페이지 컨텐츠 파일경로 + 화면에서 사용될 데이터 ModelAndView에 담아 return
	*/
	@RequestMapping("/web/frame_body.do")
	public ModelAndView body(@RequestParam(value="command", required=false, defaultValue="list") String command,
			@RequestParam(value="wr_id", required=false, defaultValue="") String wr_id,
			@RequestParam(value="dm_no", required=false, defaultValue="") String dm_no,
			@ModelAttribute("searchDmWriteVO") Dm_write_vo searchDmWriteVO,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpSession session) {
		
		CommonUtil commonUtil = new CommonUtil();
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

		request.setAttribute("DiamLoginVO", loginVO);
		
		ModelAndView result = new ModelAndView();
		try {
			Dm_config_vo configVO = (Dm_config_vo) request.getAttribute("CONFIG_INFO");
			Dm_pages_vo pageVO = (Dm_pages_vo) request.getAttribute("pageVO");
			
			Map<String, Object> param = new HashMap<String, Object>();
			param.putAll(paramMap);
			param.put("page", null);
			switch (pageVO.getDm_page_type()) {
				case "PAGE":
					if ("1".equals(pageVO.getDm_main_content())) {
						Dm_main_visual_vo mainVisualVO = new Dm_main_visual_vo();
						mainVisualVO.setDm_domain(configVO.getDm_domain_id());
						List<Dm_main_visual_vo> mainVisualList = mainVisualService.selectMainVisualListForWeb(mainVisualVO);
						result.addObject("mainVisualList", mainVisualList);
						
						Dm_banners_vo bannerVO = new Dm_banners_vo();
						bannerVO.setDm_domain(configVO.getDm_domain_id());
						List<Dm_banners_vo> bannerList = bannerService.selectBannerListForWeb(bannerVO);
						result.addObject("bannerList", bannerList);
						
						List<Dm_board_vo> sampleBoard = boardService.selectMainBoardList(Dm_board_vo.builder()
								.dm_domain(configVO.getDm_domain_id())
								.build());
						
						if (sampleBoard.size() > 0) {
							Map<String, List<Dm_write_vo>> writeMap = getMainSampleWrite(sampleBoard);
							result.addObject("writeMap", writeMap);
						}
					}
					
					String pageFilePath = request.getServletContext().getRealPath("/WEB-INF/jsp/") +  pageVO.getDm_file_src() + pageVO.getDm_file_name() + ".jsp";
					File pageFile = new File(pageFilePath);
					if (pageFile.exists()) {
						if (!pageFile.isFile()) {
							result.setViewName(this.UNDER_CONSTRUCTION);
						} else {
							result.setViewName(pageVO.getDm_file_src() + pageVO.getDm_file_name());
						}
					} else {
						result.setViewName(this.UNDER_CONSTRUCTION);
					}
					break;
				case "SEARCH":
					String search_value = request.getParameter("search_value");
					List<Dm_board_vo> boardList = searchService.selectBoardList(pageVO);
					Dm_write_vo vo = new Dm_write_vo();
					List<Object> list = new ArrayList<>();
					if(boardList.size() > 0) {
						vo.setSearchBoardList(boardList);
						for(int i=0; i<vo.getSearchBoardList().size(); i++) {
							Map<String, Object> boardMap = new HashMap<>();
							vo.getSearchBoardList().get(i).setSearch_value(search_value);
							List<Dm_write_vo> writeList = searchService.selectWriteList(vo.getSearchBoardList().get(i));
							int cnt = searchService.selectWriteListCnt(vo.getSearchBoardList().get(i));
							ArrayList<Object> arr = new ArrayList<>();
							for(int j=0; j<writeList.size(); j++) {
								Map<String, Object> writeMap = new HashMap<>();
								writeMap.put("wr_subject", writeList.get(j).getWr_subject());
								writeMap.put("wr_content", commonUtil.removeHtml(writeList.get(j).getWr_content()));
								writeMap.put("wr_name", writeList.get(j).getWr_name());
								writeMap.put("mb_id", writeList.get(j).getMb_id());
								writeMap.put("wr_id", writeList.get(j).getWr_id());
								writeMap.put("wr_datetime", writeList.get(j).getWr_datetime());
								writeMap.put("wr_hit", writeList.get(j).getWr_hit());
								writeMap.put("wr_is_comment", writeList.get(j).getWr_is_comment());
								writeMap.put("wr_option", writeList.get(j).getWr_option());
								writeMap.put("wr_parent", writeList.get(j).getWr_parent());
								arr.add(writeMap);
							}
							boardMap.put("dm_table", vo.getSearchBoardList().get(i).getDm_table());
							boardMap.put("dm_subject", vo.getSearchBoardList().get(i).getDm_subject());
							boardMap.put("dm_board_id", vo.getSearchBoardList().get(i).getDm_id());
							boardMap.put("dm_uid", vo.getSearchBoardList().get(i).getDm_uid());
							boardMap.put("dm_writer_type", vo.getSearchBoardList().get(i).getDm_writer_type());
							boardMap.put("dm_writer_secret", vo.getSearchBoardList().get(i).getDm_writer_secret());
							boardMap.put("total", cnt);
							boardMap.put("write", arr);
							list.add(boardMap);
						}
						result.addObject("writeList", list);
						result.addObject("search_value", search_value);
						result.setViewName("egovframework/diam/web/base/search/search");
					}
					
					break;
				case "BOARD":
					Dm_board_vo boardVO = new Dm_board_vo();
					boardVO.setDm_id(pageVO.getDm_board_id());
					boardVO = boardService.selectBoard(boardVO);
									
					List<String> listGroupList = new ArrayList<String>(Arrays.asList(boardVO.getDm_list_group().split("\\|")));
					List<String> readGroupList = new ArrayList<String>(Arrays.asList(boardVO.getDm_read_group().split("\\|")));
					List<String> writeGroupList = new ArrayList<String>(Arrays.asList(boardVO.getDm_write_group().split("\\|")));
					List<String> replyGroupList = new ArrayList<String>(Arrays.asList(boardVO.getDm_reply_group().split("\\|")));
					List<String> uploadGroupList = new ArrayList<String>(Arrays.asList(boardVO.getDm_upload_group().split("\\|")));
					List<String> linkGroupList = new ArrayList<String>(Arrays.asList(boardVO.getDm_link_group().split("\\|")));
										
					boolean is_admin = loginVO.getIs_admin();
					result.addObject("is_admin", is_admin);
					
					boolean is_basic = boardVO.getDm_skin().equals("basic") ? true : false;
					boolean is_list_read = commonUtil.getAuth(boardVO.getDm_auth_type(), loginVO.getDm_level(), boardVO.getDm_list_level(), loginVO.getGroup_id(), listGroupList, is_admin);
					boolean is_read = commonUtil.getAuth(boardVO.getDm_auth_type(), loginVO.getDm_level(), boardVO.getDm_read_level(), loginVO.getGroup_id(), readGroupList, is_admin);
					boolean is_write = commonUtil.getAuth(boardVO.getDm_auth_type(), loginVO.getDm_level(), boardVO.getDm_write_level(), loginVO.getGroup_id(), writeGroupList, is_admin);
					boolean is_reply = commonUtil.getAuth(boardVO.getDm_auth_type(), loginVO.getDm_level(), boardVO.getDm_reply_level(), loginVO.getGroup_id(), replyGroupList, is_admin);
					boolean is_file = commonUtil.getAuth(boardVO.getDm_auth_type(), loginVO.getDm_level(), boardVO.getDm_upload_level(), loginVO.getGroup_id(), uploadGroupList, is_admin);					
					boolean is_link = commonUtil.getAuth(boardVO.getDm_auth_type(), loginVO.getDm_level(), boardVO.getDm_link_level(), loginVO.getGroup_id(), linkGroupList, is_admin);
					
					if (command.equals("list")) {
						if (!is_list_read) {
							result.addObject("message", "board.list.auth");
							result.setViewName("egovframework/diam/web/base/board/result");
							return result;						
						} else {
							String rowsTmp = boardVO.getDm_page_rows() != null ? boardVO.getDm_page_rows() : "10";
							String pageTmp = request.getParameter("page") != null ? request.getParameter("page") : "1";				
							if (Integer.parseInt(rowsTmp) < 0 || Integer.parseInt(pageTmp) < 1) {
								result.addObject("message", "board.paging.error");
								result.setViewName("egovframework/diam/web/base/board/result");
								return result;
							} else {
								Integer rows = Integer.parseInt(rowsTmp);
								Integer page = rows * (Integer.parseInt(pageTmp) - 1);				
															
								searchDmWriteVO.setRows(rows);
								searchDmWriteVO.setPage(page);
								searchDmWriteVO.setSearch_board(boardVO.getDm_id());
								searchDmWriteVO.setType("web");
								
								//int writeListCnt = writeService.selectWriteListCnt(searchDmWriteVO);
								int writeListCnt = writeService.selectWriteCountForWeb(searchDmWriteVO);
								List<Dm_write_vo> writeList = writeService.selectWriteListForWeb(searchDmWriteVO);
								List<Dm_write_vo> noticeList = writeService.selectWriteNoticeForWeb(searchDmWriteVO);
																
								int doubleRows = rows;
								int doubleCnt = writeListCnt;
								int total_page = (int) Math.ceil((double)doubleCnt / doubleRows);
								String pagingStr = commonUtil.getPaging(rows, Integer.parseInt(pageTmp), total_page, param, configVO.getDm_url());
								
								if (boardVO.getDm_skin().equals("gallery")) {
									setThumbnail(writeList);
								}
								
								String dm_hit_url ="";
								String dm_new_url = "";
								String dm_file_icon = "";
								
								if ("1".equals(boardVO.getDm_is_hit())) {
									if (!"".equals(boardVO.getDm_hit_icon()) && boardVO.getDm_hit_icon() != null) {
										File fileChk = new File(request.getServletContext().getRealPath("") + boardVO.getDm_hit_icon());
										if (fileChk.isFile()) {
											dm_hit_url = boardVO.getDm_hit_icon();
										} else {
											dm_hit_url = "/images/ico_hit.png";
										}
									} else {
										dm_hit_url ="/images/ico_hit.png";
									}
								}
								
								if ("1".equals(boardVO.getDm_is_new())) {
									if (!"".equals(boardVO.getDm_new_icon()) && boardVO.getDm_new_icon() != null) {
										File fileChk = new File(request.getServletContext().getRealPath("") + boardVO.getDm_new_icon());
										if (fileChk.isFile()) {
											dm_new_url = boardVO.getDm_new_icon();
										} else {
											dm_new_url = "/images/ico_new.png";
										}
									} else {
										dm_new_url = "/images/ico_new.png";
									}
								}
								
								if (boardVO.getDm_use_file_icon() != null && !boardVO.getDm_use_file_icon().isEmpty()) {
									dm_file_icon = "/images/file.png";
								}						
															
								result.addObject("is_write", is_write);
								result.addObject("page_rows", rows);
								result.addObject("writeListCnt", writeListCnt);
								result.addObject("total_page", total_page);
								result.addObject("pagingStr", pagingStr);
								result.addObject("writeList", writeList);
								result.addObject("noticeList", noticeList);
								result.addObject("dm_hit_url", dm_hit_url);
								result.addObject("dm_new_url", dm_new_url);
								result.addObject("dm_file_icon", dm_file_icon);
							}
						}
					} else if (command.equals("view")) {
						if (!is_read) {
							result.addObject("dm_uid", pageVO.getDm_uid());
							result.addObject("message", "board.read.auth");
							result.setViewName("egovframework/diam/web/base/board/result");
							return result;
						} else {
							Dm_write_vo writeViewVO = new Dm_write_vo();
							searchDmWriteVO.setWr_id(wr_id);
							searchDmWriteVO.setWr_hit(Integer.parseInt(boardVO.getDm_hit_count()));
							writeViewVO = writeService.selectWriteAndUpdateHits(searchDmWriteVO);
													
							if (writeViewVO != null) {
								if (is_basic) {
									String write_secret = writeViewVO.getWr_option();
									if ("비회원".equals(writeViewVO.getMb_id())) {
										if (!is_admin) {
											if (write_secret != null && !write_secret.isEmpty()) {
												if (write_secret.contains("secret")) {
													String sessionPassword = (String) session.getAttribute("DIAM_CHECKPASS_RESULT");
													if (sessionPassword != null && !sessionPassword.isEmpty()) {
														if (!sessionPassword.equals(writeViewVO.getWr_password())) {
															result.addObject("message", "board.invalid.password");
															result.setViewName("egovframework/diam/web/base/board/result");
															return result;
														}																								
													} else {
														result.addObject("message", "board.fault.access");
														result.setViewName("egovframework/diam/web/base/board/result");
														return result;
													}
												}
											}
										}
									} else {
										if (write_secret != null && !write_secret.isEmpty()) {
											if (write_secret.contains("secret")) {
												if (!is_admin && !writeViewVO.getMb_id().equals(loginVO.getId())) {
													if (writeViewVO.getWr_reply() > 0) {
														if (!writeViewVO.getOri_mb_id().equals(loginVO.getId()) && !writeViewVO.getReply_mb_id().equals(loginVO.getId())) {
															result.addObject("message", "board.fault.access");
															result.setViewName("egovframework/diam/web/base/board/result");
															return result;
														}													
													} else {
														result.addObject("message", "board.fault.access");
														result.setViewName("egovframework/diam/web/base/board/result");
														return result;
													}												
												}
											}
										}
									}
								}
																								
								String[] oriFileList = writeViewVO.getWr_ori_file_name().split("\\|");
								String[] fileList = writeViewVO.getWr_file().split("\\|");
															
								result.addObject("is_reply", is_reply);
								result.addObject("is_write", is_write);
								result.addObject("oriFileList", oriFileList);
								result.addObject("fileList", fileList);
								result.addObject("writeVO", writeViewVO);
								
							} else {
								result.addObject("message", "board.notfound.write");
								result.setViewName("egovframework/diam/web/base/board/result");
								return result;
							}							
						}		
					} else if (command.equals("write")) {
						if (!is_write) {
							result.addObject("dm_uid", pageVO.getDm_uid());
							result.addObject("message", "board.write.auth");
							result.setViewName("egovframework/diam/web/base/board/result");
							return result;
						} else {
							JSONObject rsaObject = commonUtil.initRsa(request, "DIAM_WEB_WRITE_RSA_KEY");
							if ("success".equals(rsaObject.get("result"))) {
								result.addObject("RSAModulus", rsaObject.get("RSAModulus"));
								result.addObject("RSAExponent", rsaObject.get("RSAExponent"));
					    		
								String basic_content_editor = boardVO.getDm_basic_content();
								String basic_content_normal = "";
														
								if (basic_content_editor != null && !basic_content_editor.isEmpty()) {
									basic_content_normal = basic_content_editor.replaceAll("<([^>]+)>", "");
								}
																
								result.addObject("dm_basic_content_editor", basic_content_editor);
								result.addObject("dm_basic_content_normal", basic_content_normal);
								result.addObject("is_file", is_file);
								result.addObject("is_link", is_link);
					        } else {
					        	result.addObject("message", "board.encrypt.error");
					        	result.setViewName("egovframework/diam/web/base/board/result");
					        	return result;
					        }
						}					
					} else if (command.equals("modify")) {
						if (!is_write) {
							result.addObject("dm_uid", pageVO.getDm_uid());
							result.addObject("message", "board.modify.auth");
							result.setViewName("egovframework/diam/web/base/board/result");
							return result;
						} else {
							JSONObject rsaObject = commonUtil.initRsa(request, "DIAM_WEB_WRITE_RSA_KEY");
							if ("success".equals(rsaObject.get("result"))) {
								result.addObject("RSAModulus", rsaObject.get("RSAModulus"));
								result.addObject("RSAExponent", rsaObject.get("RSAExponent"));
								
								Dm_write_vo writeViewVO = new Dm_write_vo();
								searchDmWriteVO.setWr_id(wr_id);
								writeViewVO = writeService.selectWrite(searchDmWriteVO);
								
								if (writeViewVO != null) {
									Dm_write_vo oriVO = new Dm_write_vo();
									oriVO.setWr_num(writeViewVO.getWr_num());
									oriVO = writeService.selectWriteOri(oriVO);
									
									if ("비회원".equals(writeViewVO.getMb_id())) {
										if (!is_admin) {
											String sessionPassword = (String) session.getAttribute("DIAM_CHECKPASS_RESULT");
											if (sessionPassword != null && !sessionPassword.isEmpty()) {
												if (!sessionPassword.equals(writeViewVO.getWr_password())) {
													result.addObject("message", "board.fault.access");
													result.setViewName("egovframework/diam/web/base/board/result");
													return result;
												}																								
											} else {
												result.addObject("message", "board.fault.access");
												result.setViewName("egovframework/diam/web/base/board/result");
												return result;
											}
										}
									} else {
										if (!is_admin && !writeViewVO.getMb_id().equals(loginVO.getId())) {
											result.addObject("message", "board.fault.access");
											result.setViewName("/egovframework/diam/web/base/board/result");
											return result;
										}
									}	
									
									String[] oriFileList = writeViewVO.getWr_ori_file_name().split("\\|");
									String[] fileList = writeViewVO.getWr_file().split("\\|");
																		
									result.addObject("oriFileList", oriFileList);
									result.addObject("fileList", fileList);									
									result.addObject("is_file", is_file);
									result.addObject("is_link", is_link);	
									result.addObject("writeOriVO", oriVO);
									result.addObject("writeVO", writeViewVO);
								} else {
									result.addObject("message", "board.notfound.write");
									result.setViewName("egovframework/diam/web/base/board/result");
									return result;
								}
							} else {
								result.addObject("message", "board.encrypt.error");
					        	result.setViewName("egovframework/diam/web/base/board/result");
					        	return result;
							}							
						}
					} else if (command.equals("reply")) {
						if (!is_reply) {
							result.addObject("dm_uid", pageVO.getDm_uid());
							result.addObject("message", "board.reply.auth");
							result.setViewName("egovframework/diam/web/base/board/result");
							return result;
						} else {
							JSONObject rsaObject = commonUtil.initRsa(request, "DIAM_WEB_REPLY_RSA_KEY");
							if ("success".equals(rsaObject.get("result"))) {
								Dm_write_vo writeViewVO = new Dm_write_vo();
								searchDmWriteVO.setWr_id(wr_id);
								writeViewVO = writeService.selectWrite(searchDmWriteVO);
								
								if (writeViewVO != null) {
									Dm_write_vo oriVO = new Dm_write_vo();
									oriVO.setWr_num(writeViewVO.getWr_num());
									oriVO = writeService.selectWriteOri(oriVO);
									result.addObject("RSAModulus", rsaObject.get("RSAModulus"));
									result.addObject("RSAExponent", rsaObject.get("RSAExponent"));
									result.addObject("is_file", is_file);
									result.addObject("is_link", is_link);
									result.addObject("writeOriVO", oriVO);
									result.addObject("writeVO", writeViewVO);
								} else {
									result.addObject("message", "board.notfound.write");
									result.setViewName("egovframework/diam/web/base/board/result");
						        	return result;
								}
							} else {
								result.addObject("message", "board.encrypt.error");
					        	result.setViewName("egovframework/diam/web/base/board/result");
					        	return result;
							}
						}
					}
					request.setAttribute("boardVO", boardVO);
					if (!commonUtil.isNullOrEmpty(command)) {
						result.setViewName(pageVO.getDm_file_src() + boardVO.getDm_skin() + "/" + command);
					} else {
						result.setViewName(pageVO.getDm_file_src() + boardVO.getDm_skin() + "/list");
					}
					break;
				case "FAQ":
					Dm_faq_vo faqVO = new Dm_faq_vo();
					faqVO.setDm_domain(configVO.getDm_domain_id());
					List<Dm_faq_vo> faqList= faqService.selectFaqListForWeb(faqVO);
					
					request.setAttribute("faqList", faqList);
					result.setViewName(pageVO.getDm_file_src() + pageVO.getDm_file_name());
					break;
				case "LOGIN":
					if (!EgovUserDetailsHelper.isAuthenticated()) {
						JSONObject rsaObject = commonUtil.initRsa(request, "DIAM_WEB_LOGIN_RSA_KEY");
						if ("success".equals(rsaObject.get("result"))) {
							result.addObject("RSAModulus", rsaObject.get("RSAModulus"));
							result.addObject("RSAExponent", rsaObject.get("RSAExponent"));
							result.addObject("member_uid", pageService.selectPageUtilUid(configVO.getDm_domain_id(), "MEMBER"));
						} else {
							result.addObject("message", "member.encrypt.error");
				        	result.setViewName("egovframework/diam/web/base/login/result");
				        	return result;
						}
					}
					result.setViewName(pageVO.getDm_file_src() + pageVO.getDm_file_name());
					break;
				case "MEMBER":
					if ("join".equals(command)) {
						if (!EgovUserDetailsHelper.isAuthenticated()) {
							JSONObject rsaObject = commonUtil.initRsa(request, "DIAM_WEB_MEMBER_RSA_KEY");
							if ("success".equals(rsaObject.get("result"))) {
								Dm_member_config_vo memberConfigVO = new Dm_member_config_vo();
								memberConfigVO = memberConfigService.selectMemberConfig();
								if(memberConfigVO != null) {
									result.addObject("RSAModulus", rsaObject.get("RSAModulus"));
									result.addObject("RSAExponent", rsaObject.get("RSAExponent"));
									result.addObject("memberConfigVO", memberConfigVO);
								} else {
									result.addObject("message", "member.fault.access");
						        	result.setViewName("egovframework/diam/web/base/member/result");
						        	return result;
								}
							} else {
								result.addObject("message", "member.encrypt.error");
					        	result.setViewName("egovframework/diam/web/base/member/result");
					        	return result;
							}
						} else {
							result.addObject("message", "member.already.login");
				        	result.setViewName("egovframework/diam/web/base/member/result");
				        	return result;
						}
						result.setViewName(pageVO.getDm_file_src() + pageVO.getDm_file_name() + "_join");
					} else if ("modify".equals(command)) {
						if (EgovUserDetailsHelper.isAuthenticated()) {
							if (loginVO.getIs_admin()) {
								result.addObject("message", "member.info.admin");
					        	result.setViewName("egovframework/diam/web/base/member/result");
					        	return result;
							} else {
								JSONObject rsaObject = commonUtil.initRsa(request, "DIAM_WEB_MEMBER_RSA_KEY");
								if ("success".equals(rsaObject.get("result"))) {
									Dm_member_vo memberVO = new Dm_member_vo();
									Dm_member_config_vo memberConfigVO = new Dm_member_config_vo();
									memberConfigVO = memberConfigService.selectMemberConfig();
									memberVO.setDm_no(dm_no);
									memberVO = memberService.selectMember(memberVO);
									
									if (memberVO != null) {
										if (memberVO.getDm_id().equals(loginVO.getId())) {
											result.addObject("RSAModulus", rsaObject.get("RSAModulus"));
											result.addObject("RSAExponent", rsaObject.get("RSAExponent"));
											result.addObject("memberVO", memberVO);
											result.addObject("memberConfigVO", memberConfigVO);
										} else {
											result.addObject("message", "member.fault.access");
								        	result.setViewName("egovframework/diam/web/base/member/result");
								        	return result;
										}									
									} else {
										result.addObject("message", "member.info.notfound");
							        	result.setViewName("egovframework/diam/web/base/member/result");
							        	return result;
									}
								} else {
									result.addObject("message", "member.encrypt.error");
						        	result.setViewName("egovframework/diam/web/base/member/result");
						        	return result;
								}
							}
						} else {
							result.addObject("message", "member.not.login");
							result.setViewName("egovframework/diam/web/base/member/result");
				        	return result;
						}
						result.setViewName(pageVO.getDm_file_src() + pageVO.getDm_file_name() + "_modify");
					} else if ("terms".equals(command)) {
						if (EgovUserDetailsHelper.isAuthenticated()) {
							result.addObject("message", "member.already.login");
							result.setViewName("egovframework/diam/web/base/member/result");
							return result;
						}
						result.setViewName(pageVO.getDm_file_src() + pageVO.getDm_file_name() + "_terms");
					} else if (command.contains("forgot") && (command.contains("id"))||command.contains("pw")){
						if (EgovUserDetailsHelper.isAuthenticated()) {
							result.addObject("message", "member.already.login");
							result.setViewName("egovframework/diam/web/base/member/result");
							return result;
						}
						result.addObject("login_uid", pageService.selectPageUtilUid(configVO.getDm_domain_id(), "LOGIN"));
						result.addObject("member_uid", pageService.selectPageUtilUid(configVO.getDm_domain_id(), "MEMBER"));
						result.setViewName(pageVO.getDm_file_src() + pageVO.getDm_file_name() + "_" + command);
					} else {
						result.addObject("message", "명령 유형을 알 수 없습니다.");
						result.setViewName(this.ERROR_PAGE);
					}
					
					break;
				case "CLAUSE" :
					if ("policy".equals(command) || "privacy".equals(command) || "reject".equals(command)) {
						result.setViewName(pageVO.getDm_file_src() + command);
					} else {
						result.setViewName(pageVO.getDm_file_src() + "policy");
					}
					break;
				default:
					result.addObject("message", "페이지 타입을 알 수 없습니다.");
					result.setViewName(this.ERROR_PAGE);
					break;
			}			
		} catch(InvalidKeySpecException ike) {
			log.error(MessageCode.CMM_ENCRYPT_EXPIRED.getLog());
			result.addObject("message", MessageCode.CMM_ENCRYPT_EXPIRED.getMessage());
			result.setViewName(this.ERROR_PAGE);
		} catch (IOException ioe) {
			log.error("WEB Body: {}", MessageCode.CMM_FILE_ERROR.getLog());
			result.addObject("message", MessageCode.CMM_FILE_ERROR.getMessage());
			result.setViewName(this.ERROR_PAGE);
		} catch(DataAccessException dae) {
			log.error("WEB Body: {}", MessageCode.CMM_DATA_ERROR.getLog());
			result.addObject("message", MessageCode.CMM_DATA_ERROR.getMessage());
			result.setViewName(this.ERROR_PAGE);
		} catch (Exception e) {
			log.error("WEB Body: {}", MessageCode.CMM_SYSTEM_ERROR.getLog());
			result.addObject("message", MessageCode.CMM_SYSTEM_ERROR.getMessage());
			result.setViewName(this.ERROR_PAGE);
		}
		return result;
	}

	/**
	 * bottom
	 * 사용자 메인/서브페이지에 표출될 하단 bottom 파일경로와 화면에서 사용할 데이터를 ModelAndView 객체로 ViewResolver로 전송
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @return ModelAndView 사용자 메인/서브페이지 bottom 파일경로 + 화면에서 사용될 데이터 ModelAndView에 담아 return
	*/
	@RequestMapping("/web/frame_bottom.do")
	public ModelAndView bottom(HttpServletRequest request) throws Exception {
		ModelAndView result = new ModelAndView();
		try {
			Dm_layout_vo layoutVO = (Dm_layout_vo) request.getAttribute("layoutVO");
			Dm_config_vo configVO = (Dm_config_vo) request.getAttribute("CONFIG_INFO");
			result.setViewName("egovframework/diam/web/thema/" + layoutVO.getDm_layout_folder() + "/bottom");
			
			Dm_menus_vo menuVO = new Dm_menus_vo();
			menuVO.setDm_domain(configVO.getDm_domain_id());
			List<Dm_menus_vo> menuList = menuService.selectMenuList(menuVO);
			request.setAttribute("menuList", menuList);
			result.addObject("clause_uid", pageService.selectPageUtilUid(configVO.getDm_domain_id(), "CLAUSE"));
		} catch(NullPointerException npe) {
			log.error("WEB Bootom: {}", MessageCode.CMM_NULL_ERROR.getLog());
			result.addObject("message", "bottom페이지 정보가 없습니다.");
			result.setViewName(this.ERROR_PAGE);
		} catch (Exception e) {
			log.error("WEB Bootom: {}", MessageCode.CMM_SYSTEM_ERROR.getLog());
			result.addObject("message", "오류가 발생하였습니다. 관리자에게 문의주세요.");
			result.setViewName(this.ERROR_PAGE);
		}
		return result;
	}
	
	/**
	 * popup_page
	 * 사용자 메인페이지에 표출될 등록된 팝업 중 기간만료가 아니고 사용중인 팝업리스트를 조회 후 팝업페이지 파일경로와 사용할 데이터를 ModelAndView 객체로 ViewResolver로 전송
	 * @param dm_id 표출할 팝업 PK값을 문자열로 전달
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @return ModelAndView 사용자 메인/서브페이지 bottom 파일경로 + 화면에서 사용될 데이터 ModelAndView에 담아 return
	*/
	@RequestMapping("/popup.do")
	public ModelAndView popup_page(@RequestParam(value="dm_id", required=true) String dm_id,
			@RequestParam(value="layout_folder", required=true) String layout_folder,
			HttpServletRequest request) throws Exception {
		ModelAndView result = new ModelAndView();
		try {
			Dm_popup_vo popupVO = new Dm_popup_vo();
			popupVO.setDm_id(dm_id);
			popupVO = popupService.selectPopup(popupVO);
			
			request.setAttribute("layout_path", "/thema/" + layout_folder);
			request.setAttribute("popupVO", popupVO);
			
			result.setViewName("egovframework/diam/web/popup");
		} catch(DataAccessException dae) {
			log.error("WEB etc: {}", MessageCode.CMM_DATA_ERROR.getLog());
			result.addObject("message", "SQL오류가 발생하였습니다.");
			result.setViewName(this.ERROR_PAGE);
		} catch (Exception e) {
			log.error("WEB etc: {}", MessageCode.CMM_SYSTEM_ERROR.getLog());
			result.addObject("message", "오류가 발생하였습니다. 관리자에게 문의주세요.");
			result.setViewName(this.ERROR_PAGE);
		}
		return result;
	}
	
	@RequestMapping("/web/selectSearchPage.do")
	public ResponseEntity<?> selectSearchPage(Dm_pages_vo pageVO) {
		Map<String, Object> resultMap = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();	
		
		try {
			if (commonUtil.isNullOrEmpty(pageVO.getDm_domain())) {
				resultMap.put("notice", MessageCode.CMM_REQUEST_BADREQUEST.getMessage());
				return new ResponseEntity<>(resultMap,HttpStatus.BAD_REQUEST);
			} else {
				String uid = pageService.selectPageUtilUid(pageVO.getDm_domain(), pageVO.getDm_page_type());
				if (uid == null) {
					resultMap.put("result", "fail");
				} else {
					resultMap.put("result", "success");
					resultMap.put("rows", uid);
				}
			}
		} catch(DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			resultMap.put("notice", MessageCode.CMM_DATA_ERROR.getMessage());
			return new ResponseEntity<>(resultMap,HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			resultMap.put("notice", MessageCode.CMM_SYSTEM_ERROR.getMessage());
			return new ResponseEntity<>(resultMap,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(resultMap, HttpStatus.OK);
	}
	
	private void setThumbnail(List<Dm_write_vo> writeList) {
		CommonUtil commonUtil = new CommonUtil();		
		String[] extlist = {"jpg","gif","png","JPG","GIF","PNG","jpeg","JPEG"};
		for (int i = 0 ; i < writeList.size() ; i++) {
			if (writeList.get(i).getWr_file() != null && !"".equals(writeList.get(i).getWr_file())) {
				String tmp_wr_file = writeList.get(i).getWr_file();
				String[] tmp_wr_file_arr = tmp_wr_file.split("\\|");
				if (tmp_wr_file_arr.length > 0) {
					boolean beforeChk = false;
					for (int j = 0 ; j < tmp_wr_file_arr.length ; j++) {
						if (tmp_wr_file_arr[j] != null && !tmp_wr_file_arr[j].isEmpty()) {
							String[] tmp_arr = tmp_wr_file_arr[j].split("/");
							String ext = tmp_arr[4].substring(tmp_arr[4].indexOf(".") + 1);
							if(Arrays.asList(extlist).contains(ext)) {
								beforeChk = true;
								break;
							}
						}																				
					}
					
					if (beforeChk) {
						for (int j = 0 ; j < tmp_wr_file_arr.length ; j++) {
							if (tmp_wr_file_arr[j] != null && !tmp_wr_file_arr[j].isEmpty()) {
								String[] tmp_arr = tmp_wr_file_arr[j].split("/");
								String ext = tmp_arr[4].substring(tmp_arr[4].indexOf(".") + 1);
								boolean extchk = false;
								extchk = Arrays.asList(extlist).contains(ext);
								if(extchk) {
									String file_str = tmp_arr[0] +"/"+ tmp_arr[1] +"/"+ tmp_arr[2] +"/"+ tmp_arr[3] +"/gallery/t-"+ tmp_arr[4];
									writeList.get(i).setThumb(file_str);
									break;
								}
							}																			
						}
					} else {
						String wr_content_str = writeList.get(i).getWr_content();
						Pattern p = Pattern.compile("<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");
						Matcher m = p.matcher(wr_content_str);
						boolean b = m.find();
						if (b) {
							String matcher_result = m.group(1);
							String[] matcher_arr = matcher_result.split("/");
							String file_str = "";
							if (matcher_arr.length == 6) {
								file_str = matcher_arr[0] +"/"+matcher_arr[1]+"/"+matcher_arr[2]+"/"+matcher_arr[3]+"/"+matcher_arr[4]+"/thumb/t-"+matcher_arr[5];
							} else if (matcher_arr.length == 4) {
								file_str = matcher_arr[0] +"/"+matcher_arr[1]+"/"+matcher_arr[2]+"/thumb/t-"+matcher_arr[3];
							}		
							writeList.get(i).setThumb(file_str);
						}
					}											
				} else {
					String wr_content_str = writeList.get(i).getWr_content();
					Pattern p = Pattern.compile("<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");
					Matcher m = p.matcher(wr_content_str);
					boolean b = m.find();
					if (b) {
						String matcher_result = m.group(1);
						String[] matcher_arr = matcher_result.split("/");
						String file_str = "";
						if (matcher_arr.length == 6) {
							file_str = matcher_arr[0] +"/"+matcher_arr[1]+"/"+matcher_arr[2]+"/"+matcher_arr[3]+"/"+matcher_arr[4]+"/thumb/t-"+matcher_arr[5];
						} else if (matcher_arr.length == 4) {
							file_str = matcher_arr[0] +"/"+matcher_arr[1]+"/"+matcher_arr[2]+"/thumb/t-"+matcher_arr[3];
						}		
						writeList.get(i).setThumb(file_str);
					}
				}
			} else {
				String wr_content_str = writeList.get(i).getWr_content();
				if (!commonUtil.isNullOrEmpty(wr_content_str)) {
					Pattern p = Pattern.compile("<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");
					Matcher m = p.matcher(wr_content_str);
					boolean b = m.find();
					if (b) {
						String matcher_result = m.group(1);
						String[] matcher_arr = matcher_result.split("/");
						String file_str ="";
						if (matcher_arr.length == 6) {
							file_str = matcher_arr[0] +"/"+matcher_arr[1]+"/"+matcher_arr[2]+"/"+matcher_arr[3]+"/"+matcher_arr[4]+"/thumb/t-"+matcher_arr[5];
						} else if (matcher_arr.length == 4) {
							file_str = matcher_arr[0] +"/"+matcher_arr[1]+"/"+matcher_arr[2]+"/thumb/t-"+matcher_arr[3];
						}
						writeList.get(i).setThumb(file_str);
					}
				}
			}
		}
	}
	
	private Map<String, List<Dm_write_vo>> getMainSampleWrite(List<Dm_board_vo> boardList) throws Exception {
		Map<String, List<Dm_write_vo>> result = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		result = writeService.selectMainWriteList(boardList);
		for (Dm_board_vo vo : boardList) {
			if (result.get(vo.getDm_table()) != null) {
				if (vo.getDm_skin() != null && vo.getDm_skin().equals("gallery")) {
					setThumbnail(result.get(vo.getDm_table()));
				}
				result.get(vo.getDm_table()).forEach(item -> {
					item.setWr_content(commonUtil.removeHtml(item.getWr_content()));
				});
			}
		}
		return result;
	}
}
