/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.model.design;

import java.io.Serializable;

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
 * @Class Name : Dm_main_visual_vo.java
 * @Description : 메인비주얼 데이터 사용변수 정의 VO 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dm_main_visual_vo implements Serializable {
	
	/** VO 직렬화 시 version uid 값 */
	private static final long serialVersionUID = 6178782733606678771L;

	/** 메인비주얼 PK */
	@Pattern(regexp="^[0-9]*$", message="올바르지 않은 요청값입니다.")
	private String dm_id;
	
	/** 메인비주얼 도메인PK */
	@Pattern(regexp="^[0-9]*$", message="올바르지 않은 도메인값입니다.")
	@Size(min=1, max=10, message="올바르지 않은 도메인값입니다.")
	private String dm_domain;
		
	/** 메인비주얼 이미지 설명 */
	@NotBlank(message="메인비주얼 제목을 입력해주세요.")
	@Size(min=1, max=100, message="메인비주얼 제목은 1자 이상 100자 이하로 입력해주세요.")
	private String dm_visual_alt;
		
	/** 메인비주얼 이미지 임시파일명 */
	private String dm_visual_name;
	
	/** 메인비주얼 이미지 원파일명 */
	private String dm_visual_name_ori;
		
	/** 메인비주얼 링크 */
	@Pattern(regexp="^(https?://)(www.)?([a-zA-Z0-9]+)\\.[a-z]+([a-zA-Z0-9가-힣/+?=#&-_]+)?", message="연결할 링크는 http 프로토콜을 포함한 URL 형식이어야 합니다.")
	@Size(max=255, message="링크는 255자 까지 입력가능합니다.")	
	private String dm_visual_link;
	
	public void setDm_visual_link(String dm_visual_link) {
		this.dm_visual_link = dm_visual_link.isEmpty() ? null : dm_visual_link;
	}
	
	/** 메인비주얼 링크타겟 */
	@NotNull(message="링크타겟을 선택해주세요.")
	@Pattern(regexp="^_blank$|^_self$", message="올바르지 않은 링크타겟값입니다.")
	private String dm_visual_link_type;
		
	/** 메인비주얼 시작일 */
	//@Pattern(regexp="^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$", message="올바르지 않은 시작일 형식입니다.")
	//@Size(min=10, max=10, message="올바르지 않은 시작일 형식입니다.")
	private String dm_start_dt;
		
	/** 메인비주얼 종료일 */
	//@Pattern(regexp="^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$", message="올바르지 않은 종료일 형식입니다.")
	//@Size(min=10, max=10, message="올바르지 않은 종료일 형식입니다.")
	private String dm_end_dt;
	
	/** 메인비주얼 정렬순서 */
	@NotNull(message="정렬순서를 입력해주세요.")
	@Pattern(regexp="^[1-9]{1}|[1-9]{1}[0-9]{1}$", message="정렬순서는 1부터 99까지의 숫자만 입력가능합니다.")
	private String dm_order;
	
	/** 메인비주얼 사용여부 */
	@Pattern(regexp="^[0-1]{1}$", message="올바른 사용여부를 선택해주세요.")
	@Size(min=1, max=1, message="올바른 사용여부를 선택해주세요.")
	private String dm_status;
	
	/** 메인비주얼 무제한 사용여부 */
	@Pattern(regexp="^$|^1$", message="올바르지 않은 무제한 사용여부값입니다.")
	private String dm_is_infinite;
	
	/** 메인비주얼 등록일자 */
	private String dm_create_dt;
	
	/** 메인비주얼 등록자 */
	private String dm_create_id;
	
	/** 메인비주얼 수정일자 */
	private String dm_modify_dt;
	
	/** 메인비주얼 수정자 */
	private String dm_modify_id;
	
	private String dm_delete_dt;
	private String dm_delete_id;
	private String dm_delete_yn;
	
	/** 메인비주얼  업로드 파일 */
	private MultipartFile multifile1;
	
	/** 메인비주얼 삭제파일명 */
	private String dm_del_image;
	
	/** 메인비주얼 도메인명 */
	private String dm_domain_text;
	
	/** 메인비주얼 사용여부명 */
	private String dm_status_text;
	
	private String dm_infinite_text;
	
	/** 메인비주얼 리스트 검색조건 */
	private String search_type;
	
	/** 메인비주얼 리스트 검색단어 */
	private String search_value;
	
	/** 메인비주얼 리스트 검색사용여부 */
	private String search_status;
	
	/** 메인비주얼 리스트 검색도메인 */
	private String search_domain;
	
	/** 메인비주얼 리스트 검색시작일 */
	private String search_start_date;
	
	/** 메인비주얼 리스트 검색종료일 */
	private String search_end_date;
	
	/** 메인비주얼 리스트 페이지번호 */
	private int page;
	
	/** 메인비주얼 리스트 페이지당 게시물 수 */
	private int rows;

}
