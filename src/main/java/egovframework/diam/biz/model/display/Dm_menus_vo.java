/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.model.display;

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
 * @Class Name : Dm_menus_vo.java
 * @Description : 메뉴 데이터 사용변수 정의 VO 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Dm_menus_vo implements Serializable {
	
	/** VO 직렬화 시 version uid 값 */
	private static final long serialVersionUID = -2141232312529661716L;

	/** 메뉴 PK */
	@Pattern(regexp="^[0-9]*$", message="올바르지 않은 요청값입니다.")
	private String dm_id;
	
	/** 메뉴 도메인PK */
	@Pattern(regexp="^[0-9]*$", message="올바르지 않은 도메인값입니다.")
	private String dm_domain;
	
	/** 메뉴 상위메뉴 PK */
/*	@NotNull(message="부모아이디 값이 누락되었습니다.")*/
	@Pattern(regexp="^[0-9]*$", message="올바르지 않은 상위메뉴값입니다.")
	private String dm_parent_id;
	
	/** 메뉴명 */
	@NotBlank(message="메뉴명을 입력해주세요.")
	@Size(min=1, max=30, message="메뉴명은 1자 이상 30자 이하로 입력해주세요.")
	private String dm_menu_text;
		
	/** 관리자메뉴 연결타입 */
	/*@Pattern(regexp="^[1-2]{1}$", message="올바른 연결타입을 선택해주세요.")*/
	@Pattern(regexp="^[0-9]*$", message="올바르지 않은 연결타입값입니다.")
	private String dm_link_type;
	
	/** 메뉴 URL */
	@Size(min=1, max=255, message="URL은 1자 이상 255자 이하로 입력해주세요.")	
	private String dm_url;
			
	/** 메뉴 연결대상 */
	private String dm_link_data;
	
	/** 메뉴 링크타겟 */
	@Pattern(regexp="^_blank$|^_self$", message="올바르지 않은 링크타겟값입니다.")
	private String dm_link_target;
		
	/** 메뉴 설명 */
	@Size(max=200, message="메뉴 설명은 최대 200자 까지 입력가능합니다.")	
	private String dm_menu_desc;
	
	/** 메뉴 출력여부 */
	@Pattern(regexp="^[0-9]*$", message="올바르지 않은 메뉴사용여부값입니다.")
	private String dm_menu_view;
	
	/** 메뉴 숨김여부 */
	@Pattern(regexp="^[0-9]*$", message="올바르지 않은 메뉴숨김여부값입니다.")
	private String dm_menu_hidden;
	
	/** 메뉴 순서 */
	@Pattern(regexp="^[1-9]{1}|[1-9]{1}[0-9]{1}$", message="메뉴순서는 1부터 99까지의 숫자만 입력가능합니다.")
	private String dm_menu_order;
	
	/** 메뉴 depth */
	private String dm_depth;
	
	/** 메뉴 등록일자 */
	private String dm_create_dt;
	
	/** 메뉴 등록자 */
	private String dm_create_id;
	
	/** 메뉴 수정일자 */	
	private String dm_modify_dt;
	
	/** 메뉴 수정자 */
	private String dm_modify_id;
	
	/** 메뉴 상위메뉴명 */
	private String dm_parent_text;
	
	private String dm_delete_id;
	
	private String dm_delete_dt;
	
	private String dm_delete_yn;

}
