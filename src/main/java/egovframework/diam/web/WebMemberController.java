/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.web;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.util.ArrayList;
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
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import egovframework.diam.biz.model.config.Dm_config_vo;
import egovframework.diam.biz.model.member.Dm_member_vo;
import egovframework.diam.biz.model.member.Dm_member_vo.DupGroup;
import egovframework.diam.biz.model.member.Dm_member_vo.MemberUserGroup;
import egovframework.diam.biz.service.member.MemberService;
import egovframework.diam.cmm.model.LoginVO;
import egovframework.diam.cmm.util.CommonUtil;
import egovframework.diam.cmm.util.EgovUserDetailsHelper;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;

/**
 * @Class Name : WebMemberController.java
 * @Description : 사용자페이지 회원가입/정보수정/회원탈퇴 기능구현 Controller클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
 */

@Controller
@Log4j2
public class WebMemberController {
	
	@Resource(name="memberService")
	private MemberService memberService;
	
	/**
	 * set_member
	 * 회원가입/정보수정 시 사용자가 입력한 회원정보로 회원가입/정보수정 동작 수행
	 * @param type 입력 폼에서 전달받는 insert/update 수행명령값
	 * @param memberVO 사용자가 입력한 회원정보 데이터를 전달받는 vo객체
	 * @param br vo객체의 hibernate validator 검증결과를 return 하기위한 객체
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 BindingResult객체
	 * @param session 회원가입/정보수정에서 사용하는 비밀번호 RSA암호화 세션 조회/삭제 시 사용하는 HttpSession 객체
	 * @return String 회원가입/정보수정 후 화면에 전달하는 메시지에 따라 완료메시지를 출력해주는 결과페이지 경로를 문자열로 전달
	*/
	@RequestMapping(value= {"/web/set_member.do", "/{domain}/web/set_member.do"}, method=RequestMethod.POST)
	public String set_member(@RequestParam(value="type", required=true) String type,
			@Validated(MemberUserGroup.class) Dm_member_vo memberVO, BindingResult br,
			HttpServletRequest request, HttpSession session, ModelMap model) throws Exception {
		CommonUtil commonUtil = new CommonUtil();
		model.addAttribute("root", commonUtil.getDomain(request));
		
		if(br.hasErrors()) {
			List<FieldError> errors = br.getFieldErrors();
			String msg = errors.get(0).getDefaultMessage();
						
			model.addAttribute("message","member.fail.validate");
			model.addAttribute("notice", msg);
			return "egovframework/diam/web/base/member/result";
		}
		
		String dm_password = memberVO.getDm_password();
		try {
			Dm_member_vo checkVO = new Dm_member_vo();
			
			if (dm_password != null && !dm_password.isEmpty()) {
				PrivateKey privateKey = (PrivateKey) session.getAttribute("DIAM_WEB_MEMBER_RSA_KEY");
				dm_password = commonUtil.decryptRsa(privateKey, dm_password);
				
				String password_pattern = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,}$";
				Pattern pattern_chk = Pattern.compile(password_pattern);
				Matcher matcher = pattern_chk.matcher(dm_password);
				boolean pattern_result = matcher.find();
				
				if (!pattern_result) {
					model.addAttribute("message", "member.invalid.password");
					return "egovframework/diam/web/base/member/result";
				} else {
					memberVO.setDm_password(commonUtil.encryptPassword(dm_password, memberVO.getDm_id()));
				}
			} else {
				if ("insert".equals(type)) {
					model.addAttribute("message", "member.notenter.password");
					return "egovframework/diam/web/base/member/result";
				}
			}
			memberVO.setDm_status("J");
			memberVO.setUser_se("USR");
			memberVO.setDm_ip(commonUtil.getUserIp(request));
						
			if ("insert".equals(type)) {
				if (EgovUserDetailsHelper.isAuthenticated()) {			
					model.addAttribute("message", "member.already.login");					
				} else {
					checkVO.setDm_id(memberVO.getDm_id());
					boolean dupId = memberService.selectMemberIdDupChk(checkVO) == null ? true : false;
					checkVO.setCommand("email");
					checkVO.setDm_email(memberVO.getDm_email());
					boolean dupEmail = memberService.selectMemberIdDupChk(checkVO) == null ? true : false;
					boolean dupNick = true;
					if (!commonUtil.isNullOrEmpty(memberVO.getDm_nick())) {
						checkVO.setCommand("nick");
						checkVO.setDm_nick(memberVO.getDm_nick());
						dupNick = memberService.selectMemberIdDupChk(checkVO) == null ? true : false;
					}
					if (!dupId) {
						model.addAttribute("message", "member.id.duplicate");
					}
					if (!dupEmail) {
						model.addAttribute("message", "member.email.duplicate");
					}
					if (!dupNick) {
						model.addAttribute("message", "member.nick.duplicate");
					}
					memberVO.setDm_level("1");
					memberVO.setDm_create_id("신규");
					memberService.insertMember(memberVO);
					session.removeAttribute("DIAM_WEB_MEMBER_RSA_KEY");						
					model.addAttribute("message", "member.success.join");
				}
			} else if ("update".equals(type)) {
				if (!EgovUserDetailsHelper.isAuthenticated()) {			
					model.addAttribute("message", "member.not.login");
				} else {
					checkVO.setDm_no(memberVO.getDm_no());
					checkVO = memberService.selectMember(checkVO);
					if (checkVO != null) {
						if (Integer.parseInt(checkVO.getDm_level()) >= 6) {						
							model.addAttribute("message", "member.info.admin");
						} else {
							if (checkVO.getDm_password().equals(memberVO.getDm_password())) {
								model.addAttribute("message", "member.same.password");							
							} else {
								LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
											
								if (checkVO.getDm_id().equals(loginVO.getId())) {
									memberVO.setDm_level(loginVO.getDm_level());
									memberVO.setDm_modify_id(loginVO.getId());
									memberService.updateMember(memberVO);			
									session.removeAttribute("DIAM_WEB_MEMBER_RSA_KEY");
									model.addAttribute("message", "member.success.modify");
									
									if (memberVO.getDm_password() != null && !memberVO.getDm_password().isEmpty()) {
										model.addAttribute("notice", "회원정보를 수정하였습니다. 새로운 정보로 다시 로그인을 진행해주세요.");
										execute_logout(session);
									} else {
										model.addAttribute("notice", "회원정보를 수정하였습니다.");
									}
								} else {
									model.addAttribute("message", "member.fault.access");
								}														
							}
						}
					} else {
						model.addAttribute("message", "member.info.notfound");		
					}
				}				
			} else {
				model.addAttribute("message", "member.invalid.command");				
			}		
		} catch(InvalidKeyException ike) {
			log.error(MessageCode.CMM_ENCRYPT_EXPIRED.getLog());
			model.addAttribute("message", "member.encrypt.expired");		
		} catch(BadPaddingException bpe) {
			log.error(MessageCode.CMM_ENCRYPT_EXPIRED.getLog());
			model.addAttribute("message", "member.encrypt.expired");		
		} catch (DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			model.addAttribute("message", "member.sql.error");			
		} catch (Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			model.addAttribute("message", "member.other.error");				
		}
		return "egovframework/diam/web/base/member/result";			
	}		
	
	/**
	 * leave_member
	 * 전달받은 회원정보PK로 회원정보 조회 후 회원탈퇴 처리를 하는 동작 수행
	 * @param dm_no 회원탈퇴를 하고자 하는 회원정보PK를 문자열로 전달
	 * @param model 회원탈퇴 후 결과페이지에 메시지를 전달하기 위해 사용하는 ModelMap 객체
	 * @param session 회원탈퇴 후 로그인 세션을 초기화하기 위해 사용하는 HttpSession 객체
	 * @return String 회원탈퇴 후 화면에 전달하는 메시지에 따라 완료메시지를 출력해주는 결과페이지 경로를 문자열로 전달
	*/
	@RequestMapping(value= {"/web/leave_member.do", "/{domain}/web/leave_member.do"})
	public String leave_member(@RequestParam(value="dm_no", required=true) String dm_no, ModelMap model, HttpSession session, HttpServletRequest request) throws Exception {
		CommonUtil commonUtil = new CommonUtil();
		model.addAttribute("root", commonUtil.getDomain(request));
		
		try {
			if (!EgovUserDetailsHelper.isAuthenticated()) {
				model.addAttribute("message", "member.not.login");
			} else {
				Dm_member_vo checkVO = new Dm_member_vo();
				checkVO.setDm_no(dm_no);
				checkVO = memberService.selectMember(checkVO);
				
				if (checkVO != null) {
					LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
					if (checkVO.getDm_id().equals(loginVO.getId())) {
						if (Integer.parseInt(checkVO.getDm_level()) >= 6) {
							model.addAttribute("message", "member.admin.notleave");
						} else {
							checkVO.setDm_modify_id(loginVO.getId());
							checkVO.setDm_status("J");
							memberService.chageMemberStatus(checkVO);
							model.addAttribute("message", "member.success.leave");
							execute_logout(session);
						}				
					} else {
						model.addAttribute("message", "member.fault.access");
					}											
				} else {
					model.addAttribute("message", "member.info.notfound");
				}
			}
		} catch (DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			model.addAttribute("message", "member.sql.error");
		} catch (Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			model.addAttribute("message", "member.other.error");			
		}				
		return "egovframework/diam/web/base/member/result";
	}
	
	/**
	 * execute_logout
	 * 회원탈퇴/정보수정 시 로그아웃 처리
	 * @param session 로그아웃 처리를 위해 session정보를 HttpSession 객체로 전달
	 * @return void 전달받은 HttpSession 객체를 이용하여 로그아웃 처리수행
	*/
	private void execute_logout(HttpSession session) throws Exception {
		session.setAttribute("LoginVO", null);
		RequestContextHolder.getRequestAttributes().removeAttribute("LoginVO", RequestAttributes.SCOPE_SESSION);
	}
	
	@RequestMapping("/web/dupChk.do")
	private @ResponseBody void dupChk(@Validated(DupGroup.class) Dm_member_vo memberVO, BindingResult br, HttpServletResponse response) throws Exception {
		JSONObject result = new JSONObject();
		
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
			memberVO = memberService.selectMemberIdDupChk(memberVO);
			if(memberVO == null) {
				result.put("result", "success");
				result.put("notice", "사용할 수 있습니다.");
			} else {
				result.put("result", "dup");
				result.put("notice", "사용할 수 없습니다.");
			}
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
	
	@RequestMapping("/web/selectMemberInfo.do")
	private @ResponseBody void selectMemberInfo(Dm_member_vo memberVO, HttpServletRequest request,HttpServletResponse response, HttpSession session) throws Exception {
		
		Dm_config_vo configVO = (Dm_config_vo) request.getAttribute("CONFIG_INFO");	
		JSONObject result = new JSONObject();
		
		String command = memberVO.getCommand();
		try {
			List<Dm_member_vo> findList = memberService.selectMemberInfo(memberVO);
			
			if (command.equals("id")) {
				
				List<String> arr = new ArrayList<String>();
				if (findList.size() > 0) {
					for (Dm_member_vo item : findList) {
						int maskingLength = item.getDm_id().length() / 3;
						StringBuffer sb = new StringBuffer();
						sb.append(item.getDm_id());
						String masking = "";
						for (int i = maskingLength *2 ; i < item.getDm_id().length(); i++) {
							masking += "*";
						}
						sb.replace(maskingLength, item.getDm_id().length() - maskingLength, masking);
						String srtReturn = sb.toString();
						if (sb.length() > srtReturn.length()) {
							srtReturn = srtReturn.replace("*", "");
						}
						arr.add(srtReturn);
					}
				}
				if (arr.size() > 0) {
					result.put("result", "success");
					result.put("row", arr);
				} else {
					result.put("result",  "fail");
					result.put("notice", "등록된 정보가 없습니다.");
				}
			} else {
				//비번 찾기
				if (findList.size() > 0) {
					memberVO = findList.get(0);
					if (memberVO != null) {
						CommonUtil commonUtil = new CommonUtil();
						String temp_password = commonUtil.randomPassword();
						memberVO.setDm_password(commonUtil.encryptPassword(temp_password, memberVO.getDm_id()));
						memberService.updateMemberPassword(memberVO);
						
						result.put("result", "success");
						result.put("row", temp_password);
					}
				} else {
					result.put("result",  "fail");
					result.put("notice", "등록된 정보가 없습니다.");
				}
				
			}
			
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

}
