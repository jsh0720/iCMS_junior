/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.model.admin;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Class Name : Dm_access_admin_menu_vo.java
 * @Description : 관리자페이지 메뉴 데이터 사용변수 정의 VO 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Dm_access_admin_menu_vo implements Serializable {
		
	/** VO 직렬화 시 version uid 값 */
	private static final long serialVersionUID = -1709705478527511477L;

	/** 관리자메뉴 PK */
	@Pattern(regexp="^[0-9]*$", message="올바르지 않은 요청값입니다.")
	private String dm_id;
		
	/** 관리자메뉴명 */
	@NotBlank(message="메뉴명을 입력해주세요.")
	@Size(min=1, max=30, message="메뉴명은 1자 이상 30자 이하로 입력해주세요.")
	private String dm_title;
	
	/** 관리자메뉴 상위메뉴 PK */
	@NotNull(message="부모아이디 값이 누락되었습니다.")
	@Pattern(regexp="^[0-9]*$", message="올바르지 않은 부모아이디입니다.")
	private String dm_parent_id;
	
	/** 관리자메뉴 연결경로 */
	@NotNull(message="URL을 입력해주세요.")
	@Size(min=1, max=50, message="URL은 1자 이상 50자 이하로 입력해주세요.")	
	private String dm_link_url;

	/** 관리자메뉴 사용권한 레벨 */
	@NotNull(message="레벨값을 선택해주세요.")
	@Pattern(regexp="^[6-9]{1}$|^[1]{1}[0]{1}$", message="올바르지 않은 레벨값입니다.")
	private String dm_access_level;
	
	/** 관리자메뉴 사용여부 */
	@Pattern(regexp="^[0-1]{1}$", message="올바른 메뉴사용여부를 선택해주세요.")
	private String dm_status;
	
	/** 관리자메뉴 정렬순서 */
	@Pattern(regexp="^[1-9]{1}|[1-9]{1}[0-9]{1}$", message="정렬순서는 1부터 99까지의 숫자만 입력가능합니다.")
	private String dm_view_order;
	
	/** 관리자메뉴 depth */
	private String dm_depth;
	
	/** 관리자메뉴 등록일자 */
	private String dm_create_dt;
	
	/** 관리자메뉴 등록자 */
	private String dm_create_id;
	
	/** 관리자메뉴 수정일자 */
	private String dm_modify_dt;
	
	/** 관리자메뉴 수정자 */
	private String dm_modify_id;
	
	private String dm_delete_dt;
	
	private String dm_delete_id;
	
	private String dm_delete_yn;
	
	/** 관리자메뉴 상위메뉴명 */
	private String dm_parent_text;
	
}
