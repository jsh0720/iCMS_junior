/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.model.config;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Class Name : Dm_domain_list_vo.java
 * @Description : 도메인 데이터 사용변수 정의 VO 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dm_domain_list_vo implements Serializable{
	
	/** VO 직렬화 시 version uid 값 */
	private static final long serialVersionUID = 999929672538019779L;

	/** 도메인 PK */
	@Pattern(regexp="^[0-9]*$", message="올바르지 않은 요청값입니다.")
	private String dm_id;
	
	/** 도메인명 */
	@NotBlank(message="도메인명을 입력해주세요.")
	@Size(min=1, max=50, message="도메인명은 1자 이상 50자 이하로 입력해주세요.")
	@Pattern(regexp="^[a-zA-Z0-9가-힣\\s]+$", message="도메인명은 특수문자를 제외한 영문, 숫자, 한글만 입력가능합니다.")
	private String dm_domain_nm;
		
	/** 도메인 root디렉토리 */
	@NotNull(message="디렉토리를 입력해주세요.")
	@Size(min=1, max=30, message="디렉토리는 1자 이상 30자 이하로 입력해주세요.")
	@Pattern(regexp="^[a-zA-Z0-9]*$", message="디렉토리는 영문과 숫자만 입력가능합니다.")
	private String dm_domain_root;
	
	/** 도메인 사용여부 */
	@NotNull(message="사용여부를 선택해주세요.")
	@Size(min=1, max=1, message="올바른 사용여부를 선택해주세요.")
	@Pattern(regexp="^[0-1]{1}$", message="올바른 사용여부를 선택해주세요.")
	private String dm_domain_status;
	
	/** 도메인 설명 */
	@Size(max=200, message="설명 값은 최대 200자까지만 입력가능합니다.")
	private String dm_domain_description;
		
	/** 도메인 메인도메인 여부 */
	@Pattern(regexp="^[0-1]{1}$", message="올바르지 않은 요청값입니다.")
	private String dm_domain_main;
	
	/** 도메인 사용여부명*/
	private String dm_domain_status_nm;
	
	private String dm_create_dt;
	private String dm_create_id;
	private String dm_modify_dt;
	private String dm_modify_id;
	private String dm_delete_dt;
	private String dm_delete_id;
	private String dm_delete_yn;
	
	/** 도메인리스트 검색조건 */
	private String search_type;
	
	/** 도메인리스트 검색단어 */
	private String search_value;	
	
	/** 도메인리스트 검색사용여부 */
	private String search_status;
		
	/** 도메인리스트 페이지번호 */
	private int page;
	
	/** 도메인리스트 페이지당 게시물 수 */
	private int rows;
	
	/* config에 등록된 url 정보 변수 */
	private String domain_url;
		
	/* domain에 적용된 layout의 루트 경로 이름 */
	private String theme_text;
	
	/* 콤보박스 사용시 default 선택용 변수 */
	private boolean selected;
}
