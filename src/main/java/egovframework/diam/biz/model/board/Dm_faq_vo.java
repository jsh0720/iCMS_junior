/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.model.board;

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
 * @Class Name : Dm_faq_vo.java
 * @Description : FAQ 데이터 사용변수 정의 VO 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dm_faq_vo implements Serializable{
	
	public interface FaqAdminGroup {}
	
	/** VO 직렬화 시 version uid 값 */
	private static final long serialVersionUID = 9101795775891691942L;

	/** FAQ PK */
	@Pattern(regexp="^[0-9]*$", message="올바르지 않은 요청값입니다.")
	private String dm_id;
	
	/** FAQ 도메인 PK */
	@Pattern(regexp="^[0-9]*$", message="올바르지 않은 도메인값입니다.")
	@Size(min=1, max=10, message="올바르지 않은 도메인값입니다.")
	private String dm_domain;
		
	/** FAQ 질문 */
	@NotBlank(message="질문을 입력해주세요.")
	@Size(min=1, max=21844, message="질문은 1자리 이상 21884자이하로 입력해주세요.")
	private String dm_question;
	
	/** FAQ 답변 */
	@NotBlank(message="답변을 입력해주세요.")
	@Size(min=1, max=21844, message="답변은 1자리 이상 21884자이하로 입력해주세요.")
	private String dm_answer;
		
	/** FAQ 정렬순서 */
	@NotNull(message="정렬순서를 입력해주세요.")
	@Pattern(regexp="^[1-9]{1}|[1-9]{1}[0-9]{1}$", message="정렬순서는 1부터 99까지의 숫자만 입력가능합니다.")
	private String dm_order;
	
	/** FAQ 사용여부 */
	@NotNull(message="사용여부를 선택해주세요.")
	@Pattern(regexp="^[0-1]{1}$", message="올바른 사용여부를 선택해주세요.")
	@Size(min=1, max=1, message="올바른 사용여부를 선택해주세요.")
	private String dm_status;
		
	/** FAQ 등록일자 */
	private String dm_create_dt;
	
	/** FAQ 등록자 */
	private String dm_create_id;
	
	/** FAQ 수정일자 */
	private String dm_modify_dt;
	
	/** FAQ 수정자 */
	private String dm_modify_id;
	
	private String dm_delete_dt;
	
	private String dm_delete_id;
	
	private String dm_delete_yn;
	
	/** FAQ 도메인명 */
	private String dm_domain_nm;
	
	/** FAQ 사용여부명 */
	private String dm_state_txt;
	
	/** FAQ리스트 검색조건 */
	private String search_type;
	
	/** FAQ리스트 검색단어 */
	private String search_value;
	
	/** FAQ리스트 검색사용여부 */
	private String search_status;
	
	/** FAQ리스트 검색도메인 */
	private String search_domain;
	
	/** FAQ리스트 페이지번호 */
	private int page;
	
	/** FAQ리스트 페이지당 게시물 수 */
	private int rows;
}