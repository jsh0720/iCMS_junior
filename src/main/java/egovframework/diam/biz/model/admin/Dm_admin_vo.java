/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.model.admin;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Class Name : Dm_admin_vo.java
 * @Description : 관리자계정 데이터 사용변수 정의 VO 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dm_admin_vo implements Serializable {
	
	public interface AdminAdminGroup {}
	
	public interface DuplicateAdminGroup {}
		
	/** VO 직렬화 시 version uid 값 */
	private static final long serialVersionUID = -3783370772281099313L;
	
	/** 관리자 PK */
	@Pattern(regexp="^[0-9]*$", message="올바르지 않은 요청값입니다.")
	private String dm_no;
	
	/** 관리자 아이디 */
	@Pattern(regexp="^[a-z]{1}[a-z0-9]{4,19}$", message="아이디는 영문 소문자로 시작해야하며, 영문 소문자 또는 숫자로 5 ~ 20자 이하의 값을 입력해주세요.", groups= {AdminAdminGroup.class, DuplicateAdminGroup.class})
	@Size(min=5, max=20, message="아이디는 5자이상 20자 이하로 입력해주세요.")
	private String dm_id;
	
	/** 관리자 비밀번호 */
	private String dm_password;
	
	/** 관리자 이름 */
	@Pattern(regexp="^[a-zA-Z가-힣ㄱ-ㅎ\\s]*$", message="이름 형식에 맞지않습니다. 이름은 한글,영문으로만 입력가능합니다.", groups= {AdminAdminGroup.class})
	@Size(min=1, max=20, message="이름은 1자 이상 20자 이하로 입력해주세요.", groups= {AdminAdminGroup.class})
	private String dm_name;
		
	/** 관리자 이메일 */
	@Pattern(regexp="^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*+@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$", message="이메일 형식에 맞지않습니다. 입력한 이메일을 확인해주세요.", groups= {AdminAdminGroup.class})
	@Size(min=1, max=50, message="이메일은 1자 이상 50자 이하로 입력해주세요.", groups= {AdminAdminGroup.class})
	private String dm_email;
	
	/** 관리자 권한레벨 */
	@Pattern(regexp="^[6-9]{1}$|^[1]{1}[0]{1}$", message="올바르지 않은 관리자권한값입니다.", groups= {AdminAdminGroup.class})
	private String dm_level;
	
	/** 관리자 전화번호 */
	@Pattern(regexp="^0\\d{1,3}-\\d{3,4}-\\d{4}|\\d{4}-\\d{4}$", message="올바르지 않은 전화번호 형식입니다.", groups= {AdminAdminGroup.class})
	@Size(min=9, max=14, message="전화번호는 9자이상 14자 이하로 입력해주세요.")
	private String dm_tel;
	
	/** 관리자 휴대폰번호 */
	@Pattern(regexp="^0\\d{2,3}-\\d{3,4}-\\d{4}$", message="올바르지 않은 휴대전화번호 형식입니다.", groups= {AdminAdminGroup.class})
	@Size(min=12, max=14, message="휴대폰번호는 12자이상 14자 이하로 입력해주세요.")
	private String dm_hp;
	
	/** 관리자 우편번호 */
	private String dm_zip;
	
	/** 관리자 기본주소 */
	private String dm_addr1;
	
	/** 관리자 상세주소 */
	@Size(max=50, message="상세주소는 최대 50자까지만 입력가능합니다.", groups= {AdminAdminGroup.class})
	private String dm_addr2;
	
	/** 관리자 기타주소 */
	@Size(max=50, message="기타주소는 최대 50자까지만 입력가능합니다.", groups= {AdminAdminGroup.class})
	private String dm_addr3;
	
	/** 관리자 지번주소 */
	private String dm_addr_jibeon;
	
	/** 관리자 등록일자 */
	private String dm_datetime;
	
	/** 관리자 등록아이피 */
	private String dm_ip;
	
	/** 관리자 탈퇴일 */
	private String dm_leave_date;
	
	/** 관리자 로그인 실패횟수 */
	private int dm_fail_cnt;
	
	/** 관리자 로그인 실패일자 */
	private String dm_fail_time;		
	
	/** 관리자 그룹아이디 */
	@Pattern(regexp="^GROUP_[0-9]{10}$", message="올바르지 않은 그룹값입니다.", groups= {AdminAdminGroup.class})
	private String group_id;
	
	private String dm_group_text;
	
	/** 관리자 종류 */
	private String user_se;
	
	/** 관리자 상태 */
	private String dm_status;
	
	private String dm_status_text;
	
	/** 관리자 고유아이디 */
	private String esntl_id;
		
	/** 관리자리스트 검색조건 */
	private String search_type;
	
	/** 관리자리스트 검색단어 */
	private String search_value;
	
	private String search_status;
	
	/** 관리자리스트 검색시작일 */
	private String search_start_date;
	
	/** 관리자리스트 검색종료일 */
	private String search_end_date;
	
	/** 관리자리스트 검색권한레벨 */
	private String search_level;
	
	/** 관리자리스트 페이지당 게시물 수 */
	private int rows;
	
	/** 관리자리스트 페이지번호 */
	private int page;
		
	/** 관리자 권한레벨명 */
	private String dm_level_text;
	
	private String dm_create_dt;
	
	private String dm_create_id;
	
	private String dm_modify_dt;
	
	private String dm_modify_id;
	
	private String dm_delete_dt;
	
	private String dm_delete_id;
	
	private String dm_delete_yn;

}
