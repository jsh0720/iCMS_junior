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
 * @Class Name : Dm_pages_vo.java
 * @Description : 페이지 데이터 사용변수 정의 VO 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Dm_pages_vo implements Serializable {
	
	/** VO 직렬화 시 version uid 값 */
	private static final long serialVersionUID = -6564337250055867416L;

	/** 페이지 PK */
	@Pattern(regexp="^[0-9]*$", message="올바르지 않은 요청값입니다.")
	private String dm_id;
		
	/** 페이지 도메인PK */
	@Pattern(regexp="^[0-9]*$", message="올바르지 않은 도메인입니다.")
	@Size(min=1, max=10, message="올바르지 않은 도메인입니다.")
	private String dm_domain;
	
	/** 페이지 UID */
	private String dm_uid;
	
	/** 페이지 파일명 */
	private String dm_file_name;
	
	/** 페이지 파일경로 */
	private String dm_file_src;
	
	/** 페이지 메인페이지 여부 */
	@Pattern(regexp="^$|[1]{1}$", message="올바르지 않는 메인페이지 여부값입니다.")
	private String dm_main_content;
	
	/** 페이지 이름 */
	@NotBlank(message="페이지이름을 입력해주세요.")
	@Size(min=1, max=50, message="페이지이름은 1자 이상 50자 이하로 입력해주세요.")
	private String dm_page_name;
		
	/** 페이지 구분 */
	@Pattern(regexp="^PAGE$|^BOARD$|^LOGIN$|^MEMBER$|^FAQ$|^SEARCH$|^CLAUSE$", message="올바르지 않은 페이지타입 입니다.")
	private String dm_page_type;
	
	/** 페이지 게시판PK */
	@Pattern(regexp="^[0-9]*$", message="올바르지 않은 게시판아이디 입니다.")
	private String dm_board_id;
	
	/** 페이지 버전 */
	private String dm_version;
	
	/** 페이지 사용여부 */
	private String dm_status;
	
	/** 페이지 등록일자 */
	private String dm_create_dt;
	
	/** 페이지 등록자 */
	private String dm_create_id;
	
	/** 페이지 수정일자 */
	private String dm_modify_dt;
	
	/** 페이지 수정자 */
	private String dm_modify_id;
	
	private String dm_delete_id;
	
	private String dm_delete_dt;
	
	private String dm_delete_yn;
		
	/** 페이지 사용여부명 */
	private String dm_status_text;
	
	/** 페이지 도메인명 */
	private String dm_domain_text;
	
	/** 페이지 도메인 URL */
	private String dm_domain_url;
	
	/** 페이지리스트 검색조건 */
	private String search_type;
	
	/** 페이지리스트 검색단어 */
	private String search_value;
	
	/** 페이지리스트 검색도메인 */
	private String search_domain;
	
	/** 페이지리스트 검색타이틀 */
	private String search_nm;
	
	/** 페이지리스트 검색사용여부 */
	private String search_status;
	
	/** 페이지리스트 페이지번호 */
	private int page;
	
	/** 페이지리스트 페이지당 게시물 수 */
	private int rows;
}
