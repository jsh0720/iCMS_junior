/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.model.display;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dm_layout_vo implements Serializable {
	
	/** VO 직렬화 시 version uid 값 */
	private static final long serialVersionUID = -149888674920167197L;

	/** 레이아웃 PK */
	@Pattern(regexp="^[0-9]*$", message="올바르지 않은 요청값입니다.")
	private String dm_id;
	
	/** 레이아웃명 */
	@NotBlank(message="레이아웃명을 입력해주세요.")
	@Pattern(regexp="^[0-9ㄱ-ㅎ가-힣a-zA-Z/\\s]*$", message="레이아웃명은 한글, 영문, 숫자로 입력해주세요.")
	@Size(min=1, max=30, message="레이아웃명은 1자 이상 30자 이하로 입력해주세요.")
	private String dm_layout_nm;
			
	/** 레이아웃 폴더명 */
	@Pattern(regexp="^[a-zA-Z0-9]*$", message="레이아웃 폴더명은 영문, 숫자로 입력해주세요.")
	@Size(min=1, max=20, message="레이아웃 폴더명은 1자 이상 20자 이하로 입력해주세요.")
	private String dm_layout_folder;
		
	/** 레이아웃 등록일자 */
	private String dm_create_dt;
	
	/** 레이아웃 등록자 */
	private String dm_create_id;
	
	/** 레이아웃 수정일자 */
	private String dm_modify_dt;
	
	/** 레이아웃 수정자 */
	private String dm_modify_id;
	
	/** 레이아웃 삭제일자 */
	private String dm_delete_dt;
	
	/** 레이아웃 삭제자 */
	private String dm_delete_id;
	
	/** 레이아웃 삭제유무 */
	private String dm_delete_yn;
	
	/** 레이아웃 리스트 검색조건 */
	private String search_type;
	
	/** 레이아웃 리스트 검색단어 */
	private String search_value;	
	
	/** 레이아웃 리스트 페이지번호 */
	private int page;
	
	/** 레이아웃 리스트 페이지당 게시물 수 */
	private int rows;
	
	/** 콤보박스 기본 선택용 변수 */
	private boolean selected;

}
