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
 * @Class Name : Dm_group_vo.java
 * @Description : 관리자그룹 데이터 사용변수 정의 VO 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Dm_group_vo implements Serializable {
		
	/** VO 직렬화 시 version uid 값 */
	private static final long serialVersionUID = 2061181777668650192L;

	/** 그룹 PK */
	@Pattern(regexp="^[0-9]*$", message="올바르지 않은 요청값입니다.")
	private String dm_id;
	
	/** 그룹 아이디 */
	private String dm_group_id;
	
	/** 그룹 이름 */
	@NotBlank(message="그룹이름을 입력해주세요.")
	@Pattern(regexp="^[a-zA-Z0-9가-힣ㄱ-ㅎ\\s]*$", message="그룹이름은 한글, 영문, 숫자로 입력해주세요.")
	@Size(min=1, max=20, message="그룹이름은 1자 이상 20자 이하로 입력해주세요.")
	private String dm_group_name;
	
	/** 그룹 설명 */
	@NotBlank(message="그룹설명을 입력해주세요.")
	@Size(min=1, max=200, message="그룹설명은 1자 이상 200자 이하로 입력해주세요.")
	private String dm_group_desc;
	
	/** 그룹 등록일자 */
	private String dm_create_dt;
	
	/** 그룹 등록자 */
	private String dm_create_id;
	
	/** 그룹 수정일자 */
	private String dm_modify_dt;
	
	/** 그룹 수정자 */
	private String dm_modify_id;
	
	private String dm_delete_dt;
	
	private String dm_delete_id;
	
	private String dm_delete_yn;
	
	/** 그룹리스트 검색조건 */
	private String search_type;
	
	/** 그룹리스트 검색단어 */
	private String search_value;
	
	/** 그룹리스트 페이지번호 */
	private int page;
	
	/** 그룹리스트 페이지당 게시물 수 */
	private int rows;
	
	/* 콤보박스 사용시 default 선택용 변수 */
	private boolean selected;

}
