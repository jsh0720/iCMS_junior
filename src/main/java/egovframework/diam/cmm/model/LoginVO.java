/**
 * Description Date(Format: 2009/03/03)
 */
package egovframework.diam.cmm.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * @Class Name : LoginVO.java
 * @Description : Login VO class
 * @Modification Information
 * @author 공통서비스 개발팀 박지욱
 * @since 2009.03.03
 * @version 1.0
 */

@Data
public class LoginVO implements Serializable{
		
	/** VO 직렬화 시 version uid 값 */
	private static final long serialVersionUID = -8274004534207618049L;
	
	/** 아이디 */
	@NotNull(message="아이디를 입력해주세요.")
	@Pattern(regexp="^[a-z]{1}[a-z0-9]{4,19}$", message="아이디 형식에 맞지않습니다. 아이디는 영문 소문자로 시작해야하며, 영문 소문자 또는 숫자로 5 ~ 20자 이하의 값을 입력해주세요.")
	@Size(min=5, max=20, message="아이디는 5자이상 20자 이하로 입력해주세요.")
	private String id;
	/** 이름 */
	private String name;
	/** 주민등록번호 */
	private String ihidNum;
	/** 이메일주소 */
	private String email;
	/** 비밀번호 */
	private String password;
	/** 비밀번호 힌트 */
	private String passwordHint;
	/** 비밀번호 정답 */
	private String passwordCnsr;
	/** 사용자구분 */
	private String userSe;
	/** 조직(부서)ID */
	private String orgnztId;
	/** 조직(부서)명 */
	private String orgnztNm;
	/** 고유아이디 */
	private String uniqId;
	/** 로그인 후 이동할 페이지 */
	private String url;
	/** 사용자 IP정보 */
	private String ip;
	/** GPKI인증 DN */
	private String dn;
	/** 가입일 */
	private String dm_datetime;
	/** 로그인 구분 */
	private String dm_fn_code;
	/** 로그인 성공여부 */
	private String dm_fn_result;
	/** 로그인 접속URL */
	private String dm_fn_url;
	/** 로그인 접속 기기정보 */
	private String dm_agent_info;
	/** 로그인구분 */	
	private String dm_type;
	/** 탈퇴일 */
	private String dm_leave_date;
	/** 로그인 실패횟수 */
	private int dm_fail_cnt;
	/** 로그인 실패일자 */
	private String dm_fail_time;
	/** 로그인 일자 */
	private String login_date;
	/** 관리자 여부 */
	private boolean is_admin = false;
	/** 권한레벨 */
	private String dm_level;
	/** 회원상태 */
	private String dm_status;
	/** 회원 그룹아이디 */
	private String group_id;
	/** 회원 고유아이디 */
	private String esntl_id;
	/** 회원 PK */
	private String dm_no;
	// 세션 만료시간
	private int expired;
	
	public boolean getIs_admin() {
		return is_admin;
	}
	public void setIs_admin(boolean is_admin) {
		this.is_admin = is_admin;
	}
	
	
	
}
