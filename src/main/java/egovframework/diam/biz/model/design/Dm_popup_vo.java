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
 * @Class Name : Dm_popup_vo.java
 * @Description : 팝업 데이터 사용변수 정의 VO 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Dm_popup_vo implements Serializable {
	
	/** VO 직렬화 시 version uid 값 */
	private static final long serialVersionUID = -3837577561754604646L;

	/** 팝업 PK */
	@Pattern(regexp="^[0-9]*$", message="올바르지 않은 요청값입니다.")
	private String dm_id;
	
	/** 팝업 도메인PK */
	@Pattern(regexp="^[0-9]*$", message="올바르지 않은 도메인값입니다.")
	private String dm_domain;
	
	/** 팝업 제목 */
	@NotBlank(message="팝업 제목을 입력해주세요.")
	@Size(min=1, max=100, message="팝업 제목은 1자 이상 100자 이하로 입력해주세요.")
	private String dm_popup_nm;
	
	/** 팝업 만료시간 */
	@NotNull(message="만료시간을 입력해주세요.")
	@Pattern(regexp="^[1-9]{1}|[1-9]{1}[0-9]{1}$", message="만료시간은 1이상의 숫자만 입력해주세요.")
	@Size(min=1, max=2, message="만료시간은 2자리수 숫자까지만 입력가능합니다.")
	private String dm_popup_expired;
	
	/** 팝업 타입 */
	@NotNull(message="팝업 종류를 선택해주세요.")
	@Pattern(regexp="^2$|^1$", message="올바르지 않은 팝업 종류입니다.")
	@Size(min=1, max=1, message="올바른 팝업 종류를 선택해주세요.")
	private String dm_popup_type;
	
	/** 팝업 이미지 임시파일명 */
	private String dm_popup_img;	
	
	/** 팝업 이미지 원파일명 */
	private String dm_popup_img_ori;

	/** 팝업 시작일 */
	//@Pattern(regexp="^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$", message="올바르지 않은 시작일 형식입니다.")
	//@Size(min=10, max=10, message="올바르지 않은 시작일 형식입니다.")
	private String dm_start_dt;
	
	/** 팝업 종료일 */
	//@Pattern(regexp="^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$", message="올바르지 않은 종료일 형식입니다.")
	//@Size(min=10, max=10, message="올바르지 않은 종료일 형식입니다.")
	private String dm_end_dt;
	
	/** 팝업 링크 */
	@Pattern(regexp="^(https?://)(www.)?([a-zA-Z0-9]+)\\.[a-z]+([a-zA-Z0-9가-힣/+?=#&-_]+)?", message="연결할 링크는 http 프로토콜을 포함한 URL 형식이어야 합니다.")
	@Size(max=255, message="링크는 255자 까지 입력가능합니다.")
	private String dm_link;
	
	public void setDm_link(String dm_link) {
		this.dm_link = dm_link.isEmpty() ? null : dm_link;
	}
	
	/** 팝업 링크타겟 */
	@NotNull(message="링크타겟을 선택해주세요.")
	@Pattern(regexp="^_blank$|^_self$", message="올바르지 않은 링크타겟값입니다.")
	private String dm_link_type;
	
	/** 팝업 크기 */
	@Pattern(regexp="^[0-9]*$", message="팝업 크기는 1이상의 숫자만 입력해주세요.")
	@Size(min=1, max=3, message="팝업 크기는 3자리수 숫자까지만 입력가능합니다.")
	private String dm_popup_width;
	
	/** 팝업 높이 */
	@Pattern(regexp="^[0-9]*$", message="팝업 높이는 1이상의 숫자만 입력해주세요.")
	@Size(min=1, max=3, message="팝업 높이는 3자리수 숫자까지만 입력가능합니다.")
	private String dm_popup_height;
	
	/** 팝업 좌측위치 */
	@Pattern(regexp="^[0-9]*$", message="팝업 좌측위치는 1이상의 숫자만 입력해주세요.")
	@Size(min=1, max=3, message="팝업 좌측위치는 3자리수 숫자까지만 입력가능합니다.")
	private String dm_popup_left;
	
	/** 팝업 상단위치 */
	@Pattern(regexp="^[0-9]*$", message="팝업 상단위치는 1이상의 숫자만 입력해주세요.")
	@Size(min=1, max=3, message="팝업 상단위치는 3자리수 숫자까지만 입력가능합니다.")
	private String dm_popup_top;
	
	/** 팝업 사용여부 */
	@Pattern(regexp="^[0-1]{1}$", message="올바른 사용여부를 선택해주세요.")
	@Size(min=1, max=1, message="올바른 사용여부를 선택해주세요.")
	private String dm_status;
	
	/** 팝업 무제한 사용여부 */
	@Pattern(regexp="^$|^1$", message="올바르지 않은 무제한 사용여부값입니다.")
	private String dm_is_infinite;
	
	/** 팝업 등록일자 */
	private String dm_create_dt;
	
	/** 팝업 등록자 */
	private String dm_create_id;
	
	/** 팝업 수정일자 */
	private String dm_modify_dt;
	
	/** 팝업 수정자 */
	private String dm_modify_id;

	private String dm_delete_dt;
	
	private String dm_delete_id;
	
	private String dm_delete_yn;
		
	/** 팝업 삭제파일명 */
	private String dm_popup_del_img;
	
	/** 팝업 사용여부명 */
	private String dm_status_text;
	
	/** 팝업 도메인명 */
	private String dm_domain_text;
	
	private String dm_infinite_text;
		
	/** 팝업 업로드파일 */
	private MultipartFile multiFile;
	
	/** 팝업 리스트 검색조건 */
	private String search_type;
	
	/** 팝업 리스트 검색단어 */
	private String search_value;
	
	/** 팝업 리스트 검색사용여부 */
	private String search_status;
	
	/** 팝업 리스트 검색도메인 */
	private String search_domain;
	
	/** 팝업 리스트 검색시작일 */
	private String search_start_date;
	
	/** 팝업 리스트 검색종료일 */
	private String search_end_date;
	
	/** 팝업 리스트 페이지번호 */
	private int page;
	
	/** 팝업 리스트 페이지당 게시물 수 */
	private int rows;

}
