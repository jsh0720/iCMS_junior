/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.cmm.model;

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
 * @Class Name : Dm_common_code_vo.java
 * @Description : CMS 공통코드 데이터 사용변수 정의 VO 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Dm_common_code_vo implements Serializable  {
	
	/** VO 직렬화 시 version uid 값 */
	private static final long serialVersionUID = 5310201963726051502L;

	/** 공통코드 PK */
	@Pattern(regexp="^[0-9]*$", message="올바르지 않은 요청값입니다.")
	private String dm_code_id;
	
	/** 공통코드 그룹 */
	@Pattern(regexp="^[0-9]*$", message="코드그룹은 숫자만 입력가능합니다.")
	@Size(min=1, max=4, message="코드그룹은 1자 이상 4자 이하로 입력해주세요.")
	private String dm_code_group;
	
	/** 공통코드 값 */
	@Pattern(regexp="^[a-zA-Z0-9_]*$", message="코드값은 영문, 숫자, 특수문자(_)만 입력가능합니다.")
	@Size(min=1, max=20, message="코드값은 1자 이상 20자 이하로 입력해주세요.")
	private String dm_code_value;
	
	/** 공통코드 이름 */
	@NotBlank(message="코드 이름을 입력해주세요.")
	@Size(min=1, max=20, message="코드이름은 1자 이상 20자 이하로 입력해주세요.")
	private String dm_code_name;
	
	/** 공통코드 정렬값 */
	@NotNull(message="코드정렬값을 입력해주세요.")
	@Pattern(regexp="^[1-9]{1}|[1-9]{1}[0-9]{1}$", message="코드정렬값은 1부터 99까지의 숫자만 입력가능합니다.")
	private String dm_code_asc;
		
	/** 공통코드 확장데이터 */
	@Size(max=20, message="확장데이터는 최대 20자 까지 입력가능합니다.")
	private String dm_code_var_name;
	
	/** 공통코드 설명 */
	@Size(max=200, message="코드설명은 최대 200자 까지 입력가능합니다.")
	private String dm_code_desc;
	
	private String dm_create_dt;
	
	private String dm_create_id;
	
	private String dm_modify_dt;
	
	private String dm_modify_id;
	
	private String dm_delete_dt;
	
	private String dm_delete_id;
	
	private String dm_delete_yn;
		
	/** 공통코드 리스트 검색조건 */
	private String search_type;
	
	/** 공통코드 리스트 검색단어 */
	private String search_value;
	
	/** 공통코드 리스트 페이지번호 */
	private int page;
	
	/** 공통코드 리스트 페이지당 게시물 수 */
	private int rows;

	/* 콤보박스 사용시 default 선택용 변수 */
	private boolean selected;
	
}