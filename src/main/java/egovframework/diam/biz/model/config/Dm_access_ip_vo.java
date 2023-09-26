/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.model.config;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * @Class Name : Dm_access_ip_vo.java
 * @Description : 접근아이피 데이터 사용변수 정의 VO 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

@Data
public class Dm_access_ip_vo implements Serializable {
		
	/** VO 직렬화 시 version uid 값 */
	private static final long serialVersionUID = 4593660293842713283L;
	
	/** 접근아이피 끝자리 아이피 대역 검증 정규식 */
	private static final String pattern1 = "(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.\\*";
	
	/** 접근아이피 검증 정규식 */
	private static final String pattern2 = "(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])";
	
	/** 접근아이피 PK */
	@Pattern(regexp="^[0-9]*$", message="올바르지 않은 요청값입니다.")
	private String dm_id;
	
	/** 접근아이피 주소 */
	@Pattern(regexp=pattern1 + "|" + pattern2, message="아이피주소 형식이 맞지 않습니다.")
	@Size(min=1, max=15, message="아이피주소를 올바르게 입력해주세요.")
	private String dm_ip;
		
	/** 접근아이피명 */
	@NotBlank(message="아이피명을 입력해주세요.")
	@Pattern(regexp="^[0-9ㄱ-ㅎ가-힣a-zA-Z/\\s]*$", message="아이피명은 한글, 영문, 숫자로 입력해주세요.")
	@Size(min=1, max=30, message="아이피명은 1자 이상 30자 이하로 입력해주세요.")
	private String dm_name;
	
	/** 접근아이피 사용여부 */
	@NotNull(message="사용여부를 선택해주세요.")
	@Pattern(regexp="^[0-1]{1}$", message="올바른 사용여부를 선택해주세요.")
	@Size(min=1, max=1, message="올바른 사용여부를 선택해주세요.")
	private String dm_status;
	
	private String status_text;
	
	/** 접근아이피 등록일자 */
	private String dm_create_dt;
	
	/** 접근아이피 등록자 */
	private String dm_create_id;
	
	/** 접근아이피 수정일자 */
	private String dm_modify_dt;
	
	/** 접근아이피 수정자 */
	private String dm_modify_id;
	
	/** 접근아이피 리스트 검색조건 */
	private String search_type;
	
	/** 접근아이피 리스트 검색단어 */
	private String search_value;	
	
	/** 접근아이피 리스트 검색사용여부 */
	private String search_status;
	
	/** 접근아이피 리스트 페이지번호 */
	private int page;	
	
	/** 접근아이피 리스트 페이지당 게시물 수 */
	private int rows;

	private String dm_delete_dt;
	
	private String dm_delete_yn;
	
	private String dm_delete_id;

}
