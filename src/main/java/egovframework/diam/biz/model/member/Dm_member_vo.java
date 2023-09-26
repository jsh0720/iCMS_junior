/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.model.member;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import egovframework.diam.biz.model.admin.Dm_admin_vo.AdminAdminGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Class Name : Dm_member_vo.java
 * @Description : 일반회원 데이터 사용변수 정의 VO 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Dm_member_vo implements Serializable {
	
	public interface MemberAdminGroup {}
	public interface MemberUserGroup {}
	public interface DupGroup{}
	
	/** VO 직렬화 시 version uid 값 */
	private static final long serialVersionUID = -6196749080335942639L;

	/** 회원 PK */
	@Pattern(groups= {MemberAdminGroup.class}, regexp="^[0-9]*$", message="올바르지 않은 요청값입니다.")
	private String dm_no;
	
	/** 회원 아이디 */
	@Pattern(groups= {MemberAdminGroup.class, MemberUserGroup.class, DupGroup.class}, regexp="^[a-z]{1}[a-z0-9]{4,19}$", message="아이디는 영문 소문자로 시작해야하며, 영문 소문자 또는 숫자로 5 ~ 20자 이하의 값을 입력해주세요.")
	@Size(groups= {MemberAdminGroup.class, MemberUserGroup.class}, min=5, max=20, message="아이디는 5자이상 20자 이하로 입력해주세요.")
	private String dm_id;
	
	/** 회원 비밀번호 */
	@NotNull(groups= {MemberAdminGroup.class, MemberUserGroup.class})
	private String dm_password;
	
	/** 회원 이름 */
	@Pattern(groups= {MemberAdminGroup.class, MemberUserGroup.class}, regexp="^[a-zA-Z가-힣ㄱ-ㅎ ]*$", message="이름은 공백을 포함한 한글, 영문으로 1자이상 20자 이하로 입력가능합니다.")
	@Size(groups= {MemberAdminGroup.class, MemberUserGroup.class}, min=1, max=20, message="이름은 1자 이상 20자 이하로 입력해주세요.")
	private String dm_name;
	
	@Pattern(groups= {MemberAdminGroup.class,MemberUserGroup.class}, regexp="^[0-9a-zA-Z가-힣ㄱ-ㅎ]*$", message="닉네임은 한글, 영문, 숫자로 입력가능합니다.")
	@Size(max=20, groups= {MemberAdminGroup.class,MemberUserGroup.class}, message="닉네임은 최대 20자까지 입력가능합니다.")
	private String dm_nick;
	
	public void setDm_nick(String dm_nick) {
		this.dm_nick = dm_nick.isEmpty() ? null : dm_nick;
	}
	
	/** 회원 이메일 */
	@Pattern(groups= {MemberAdminGroup.class, MemberUserGroup.class}, regexp="^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message="이메일 형식에 맞게 입력해주세요.")
	@Size(groups= {MemberAdminGroup.class, MemberUserGroup.class}, min=10, max=50, message="이메일은 최대 50자까지 작성가능합니다.")
	private String dm_email;
	
	/** 회원 권한레벨 */
	@NotNull(groups= {MemberAdminGroup.class}, message="레벨값을 선택해주세요.")
	@Pattern(groups= {MemberAdminGroup.class}, regexp="^[0-5]{1}$", message="올바르지 않은 레벨값입니다.")
	private String dm_level;
	
	/** 회원 전화번호 */
	@Pattern(regexp="^0\\d{1,3}-\\d{3,4}-\\d{4}|\\d{4}-\\d{4}$", message="올바르지 않은 전화번호 형식입니다.", groups= {MemberAdminGroup.class, MemberUserGroup.class})
	@Size(groups= {MemberAdminGroup.class, MemberUserGroup.class}, min=9, max=14, message="전화번호는 9자이상 14자 이하로 입력해주세요.")
	private String dm_tel;
	
	public void setDm_tel(String dm_tel) {
		this.dm_tel = dm_tel.replace("-", "").isEmpty() ? null : dm_tel;
	}
	
	/** 회원 휴대폰번호 */
	@Pattern(regexp="^0\\d{2,3}-\\d{3,4}-\\d{4}$", message="올바르지 않은 휴대전화번호 형식입니다.", groups= {MemberAdminGroup.class, MemberUserGroup.class})
	@Size(groups= {MemberAdminGroup.class, MemberUserGroup.class}, min=12, max=14, message="휴대폰번호는 12자이상 14자 이하로 입력해주세요.")
	private String dm_hp;
	
	public void setDm_hp(String dm_hp) {
		this.dm_hp = dm_hp.isEmpty() ? null : dm_hp;
 	}
	
	/** 회원 우편번호 */
	@Pattern(groups= {MemberAdminGroup.class, MemberUserGroup.class}, regexp="^[0-9]{5}$", message="우편번호는 숫자 5자만 입력가능합니다.")
	private String dm_zip;
	
	public void setDm_zip(String dm_zip) {
		this.dm_zip = dm_zip.isEmpty() ? null : dm_zip;
	}
	
	/** 회원 기본주소 */
	@Size(groups= {MemberAdminGroup.class, MemberUserGroup.class}, max=255, message="주소는 255자 이하로 입력해주세요.")
	private String dm_addr1;
	
	/** 회원 상세주소 */
	@Size(groups= {MemberAdminGroup.class, MemberUserGroup.class}, max=255, message="상세주소는 255자 이하로 입력해주세요.")
	private String dm_addr2;
	
	/** 회원 기타주소 */
	@Size(groups= {MemberAdminGroup.class, MemberUserGroup.class}, max=255, message="기타주소는 255자 이하로 입력해주세요.")
	private String dm_addr3;
	
	@Pattern(groups={MemberAdminGroup.class, MemberUserGroup.class}, regexp="^[MF]$", message="성별이 유효하지 않습니다.")
	private String dm_sex;
	
	public void setDm_sex(String dm_sex) {
		this.dm_sex = dm_sex.isEmpty() ? null : dm_sex;
	}
	
	@Pattern(groups={MemberAdminGroup.class, MemberUserGroup.class}, regexp="^\\d{4}-\\d{2}-\\d{2}$", message="생년월일은 YYYY-MM-DD 형식으로 입력해주세요.")
	private String dm_birth_date;
	
	public void setDm_birth_date(String dm_birth_date) {
		this.dm_birth_date = dm_birth_date.isEmpty() ? null : dm_birth_date;
	}
	
	private String dm_mailling;
	
	private String dm_sms;
	
	@Size(groups= {MemberAdminGroup.class, MemberUserGroup.class}, max=21844, message="자기소개 내용은 최대 21844자까지만 입력가능합니다.")
	private String dm_about_me;
	
	/** 회원 지번주소 */
	private String dm_addr_jibeon;
	
	/** 회원 등록일자 */
	private String dm_datetime;
	
	/** 회원 등록아이피 */
	private String dm_ip;
	
	/** 회원 탈퇴일 */
	private String dm_leave_date;
	
	/** 회원 로그인 실패횟수 */
	private int dm_fail_cnt;
	
	/** 회원 로그인 실패일자 */
	private String dm_fail_time;
	
	/** 회원 최근 로그인일자 */
	private String dm_login_date;
	
	/** 회원 그룹아이디 */
	private String group_id;
	
	/** 회원 종류 */
	private String user_se;
	
	/** 회원 상태 */
	private String dm_status;
	
	@Size(groups= {MemberAdminGroup.class, MemberUserGroup.class}, max=20, message="추천인은 최대 20자까지만 입력가능합니다.")
	private String dm_recommend;
	
	@Size(groups= {MemberAdminGroup.class, MemberUserGroup.class}, max=255, message="홈페이지는 최대 255자까지만 입력가능합니다.")
	private String dm_homepage;
	
	/** 회원 고유아이디 */
	private String esntl_id;
		
	/** 회원리스트 검색조건 */
	private String search_type;
	
	/** 회원리스트 검색단어 */
	private String search_value;
	
	/** 회원리스트 검색시작일 */
	private String search_start_date;
	
	/** 회원리스트 검색종료일 */
	private String search_end_date;
	
	/** 회원리스트 검색권한레벨 */
	private String search_level;
	
	/** 회원리스트 검색회원구분 */
	private String search_gubun;
	
	/** 회원리스트 검색상태 */
	private String search_status;
	
	/** 회원리스트 이메일수신여부 */
	private String search_mailling_agree;
	
	private String dm_create_dt;
	
	private String dm_create_id;
	
	private String dm_modify_dt;
	
	private String dm_modify_id;
	
	private String dm_delete_dt;
	
	private String dm_delete_id;
	
	private String dm_delete_yn;
	
	/** 회원리스트 페이지당 게시물 수 */
	private int rows;
	
	/** 회원리스트 페이지번호 */
	private int page;
		
	/** 회원 권한레벨명 */
	private String dm_level_text;
	
	/** 회원 상태레벨명 */
	private String dm_status_text;
	
	/** 회원 이메일수신여부명*/
	private String dm_mailling_text;
	
	/** 조회 조건*/
	private String command;
}
