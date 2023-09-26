/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.model.config;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Class Name : Dm_config_vo.java
 * @Description : 도메인 기본설정 데이터 사용변수 정의 VO 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dm_config_vo implements Serializable {
	
	public interface validGroupConfig {};
	public interface validGroupPage {};
	public interface validDup {};
	
	/** VO 직렬화 시 version uid 값 */
	private static final long serialVersionUID = -9082307196361692995L;

	/** 문자 URL 검증 정규식 */
	private static final String pattern1 = "^(www.)?([a-zA-Z0-9]+)\\.[a-z]+([a-zA-Z0-9./]+)?(\\w)$";
	
	/** IP URL 검증 정규식 */
	private static final String pattern2 = "(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])+([a-zA-Z0-9/]+)?";
	
	/** 도메인 기본설정PK */
	@Pattern(regexp="^[0-9]*$", message="올바르지 않은 요청값입니다.", groups=validGroupConfig.class)
	private String dm_id;
	
	/** 도메인 기본설정 도메인PK */
	@NotNull(message="도메인값이 누락되었습니다.", groups= {validGroupPage.class, validGroupConfig.class})
	@Pattern(regexp="^[0-9]*$", message="올바르지 않은 도메인값입니다.", groups= {validGroupPage.class, validGroupConfig.class})
	private String dm_domain_id;
	
	/** 도메인 기본설정 도메인명 */
	private String dm_site_name;
	
	/** 도메인 기본설정 도메인주소 */
	@Pattern(regexp=pattern1 + "|" + pattern2, message="도메인주소 형식에 맞지 않습니다. 입력값을 확인해주세요.", groups= {validGroupConfig.class, validDup.class})
	@Size(min=1, max=50, message="도메인주소는 1자 이상 50자 이하로 입력해주세요.", groups= {validGroupConfig.class, validDup.class})
	private String dm_url;
	
	/** 도메인 기본설정 상단타이틀 */
	@NotBlank(message="상단 타이틀을 입력해주세요.", groups=validGroupConfig.class)
	@Size(min=1, max=50, message="상단타이틀은 1자 이상 50자 이하로 입력해주세요.", groups=validGroupConfig.class)
	private String dm_title;
	
	/** 도메인 기본설정 상호명 */
	@Size(max=50, message="상호명은 최대 50자 까지 입력가능합니다.", groups=validGroupConfig.class)
	private String dm_company_name;
	
	/** 도메인 기본설정 사업자등록번호 */
	private String dm_company_number;
	
	/** 도메인 기본설정 통신판매신고번호 */
	@Size(max=50, message="통신판매신고번호는 최대 50자 까지 입력가능합니다.")
	private String dm_tel_company_number;
	
	/** 도메인 기본설정 대표자명 */
	@Size(max=10, message="대표자명은 최대 10자 까지 입력가능합니다.")
	private String dm_ceo;
		
	/** 도메인 기본설정 대표이메일 */
	private String dm_ceo_email;
	
	/** 도메인 기본설정 우편번호 */
	@Pattern(regexp="^[0-9]*$", message="우편번호는 숫자만 입력해주세요.")
	@Size(max=5, message="우편번호는 최대 5자 까지 입력가능합니다.")
	private String dm_zip;
	
	/** 도메인 기본설정 주소 */
	@Size(max=50, message="주소는 최대 50자 까지 입력가능합니다.")
	private String dm_addr1;
	
	/** 도메인 기본설정 상세주소 */
	@Size(max=50, message="상세주소는 최대 50자 까지 입력가능합니다.")
	private String dm_addr2;
	
	/** 도메인 기본설정 기타주소 */
	@Size(max=50, message="기타주소는 최대 50자까지만 입력가능합니다.")
	private String dm_addr3;
		
	/** 도메인 기본설정 대표전화번호 */
	@Pattern(regexp="^$|\\d{2,4}-\\d{3,4}-\\d{4}|\\d{4}-\\d{4}$", message="대표전화번호 형식은 xxx-xxxx-xxxx 혹은 xxxx-xxxx로 입력해주세요.")
	@Size(max=14, message="대표전화번호는 최대 14자까지만 입력가능합니다.")
	private String dm_tel;
	
	/** 도메인 기본설정 팩스번호 */
	@Pattern(regexp="^$|\\d{2,4}-\\d{3,4}-\\d{4}|\\d{4}-\\d{4}$", message="팩스번호 형식은 xxx-xxxx-xxxx 혹은 xxxx-xxxx로 입력해주세요.")
	@Size(max=14, message="팩스번호는 최대 14자까지만 입력가능합니다.")
	private String dm_fax;
	
	/** 도메인 기본설정 상단로고 임시파일명 */
	private String dm_top_logo;
	
	/** 도메인 기본설정 상단로고 원파일명 */
	private String dm_top_logo_name;
	
	/** 도메인 기본설정 하단로고 임시파일명 */
	private String dm_bottom_logo;
	
	/** 도메인 기본설정 하단로고 원파일명 */
	private String dm_bottom_logo_name;
	
	/** 도메인 기본설정 이메일 앞자리 */
	@Pattern(regexp="^$|[A-Za-z0-9_-]*$", message="올바르지 않은 이메일 앞자리입니다.", groups=validGroupConfig.class)
	@Size(max=25, message="이메일 앞자리는 최대 25자까지만 입력가능합니다.", groups=validGroupConfig.class)
	private String dm_ceo_email1;
	
	/** 도메인 기본설정 이메일 뒷자리 */
	@Pattern(regexp="^$|[A-Za-z0-9._-]*$", message="올바르지 않은 이메일 뒷자리입니다.", groups=validGroupConfig.class)
	@Size(max=25, message="이메일 뒷자리는 최대 25자까지만 입력가능합니다.", groups=validGroupConfig.class)
	private String dm_ceo_email2;
	
	/** 도메인 기본설정 사업자번호 앞자리 */
	@Pattern(regexp="^$|[0-9]*$", message="사업자 번호는 숫자만 입력가능합니다.", groups=validGroupConfig.class)
	@Size(max=3, message="사업자번호 앞자리는 최대 3자까지만 입력가능합니다.", groups=validGroupConfig.class)
	private String dm_company_number1;
	
	/** 도메인 기본설정 사업자번호 중간자리 */
	@Pattern(regexp="^$|[0-9]*$", message="사업자 번호는 숫자만 입력가능합니다.", groups=validGroupConfig.class)
	@Size(max=2, message="사업자번호 중간자리는 최대 2자까지만 입력가능합니다.", groups=validGroupConfig.class)
	private String dm_company_number2;
	
	/** 도메인 기본설정 사업자번호 뒷자리 */
	@Pattern(regexp="^$|[0-9]*$", message="사업자 번호는 숫자만 입력가능합니다.", groups=validGroupConfig.class)
	@Size(max=5, message="사업자번호 뒷자리는 최대 5자까지만 입력가능합니다.", groups=validGroupConfig.class)
	private String dm_company_number3;
		
	/** 도메인 기본설정 상단로고 업로드파일 */
	private MultipartFile dm_top_logo_file;
	
	/** 도메인 기본설정 하단로고 업로드파일 */
	private MultipartFile dm_bottom_logo_file;
	
	private String dm_meta_desc;
	
	private String dm_meta_keyword;
	
	private String dm_naver_site_verification;
	
	private String dm_personal_image;
	
	private String dm_personal_image_original_name;
	
	private MultipartFile dm_personal_image_file;
	
	
	/** 이용약관 사용여부 **/
	@NotNull(message="홈페이지 하단 노출 여부를 선택해주세요.", groups=validGroupPage.class)
	private String dm_policy_status;
	
	/** 이용약관 내용 **/
	@Size(max=21844, message="이용약관 내용은 21844자이하로 입력해주세요.", groups=validGroupPage.class)
	private String dm_policy_text;
	
	/** 개인정보취급방침 사용여부 **/
	@NotNull(message="홈페이지 하단 노출 여부를 선택해주세요.", groups=validGroupPage.class)
	private String dm_private_status;
	
	/** 개인정보취급방침 내용 **/
	@Size(max=21844, message="개인정보취급방침 내용은 21844자이하로 입력해주세요.", groups=validGroupPage.class)
	private String dm_private_text;
	
	/** 이메일무단수집거부 사용여부 **/
	@NotNull(message="홈페이지 하단 노출 여부를 선택해주세요.", groups=validGroupPage.class)
	private String dm_reject_status;
	
	/** 이메일무단수집거부 내용 **/
	@Size(max=21844, message="이메일무단수집거부 내용은 21844자이하로 입력해주세요.", groups=validGroupPage.class)
	private String dm_reject_text;
	
	//도메인 기본 레이아웃 설정
	private String dm_theme;
	
}
