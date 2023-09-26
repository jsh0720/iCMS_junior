/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.model.board;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Min;
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
 * @Class Name : Dm_board_vo.java
 * @Description : 게시판 데이터 사용변수 정의 VO 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dm_board_vo implements Serializable {
	
	/** VO 직렬화 시 version uid 값 */
	private static final long serialVersionUID = -3360595777031864037L;

	/** 게시판 선택여부 */
	private boolean selected;
	
	/** 게시판 PK */
	@Pattern(regexp="^[0-9]*$", message="올바르지 않은 요청값입니다.")
	private String dm_id;
	
	/** 게시판 도메인PK */
	@NotNull(message="도메인값이 누락되었습니다.")
	@Pattern(regexp="^[0-9]*$", message="올바르지 않은 도메인값입니다.")
	@Size(min=1, max=10, message="올바르지 않은 도메인값입니다.")
	private String dm_domain;
		
	/** 게시판 도메인명 */
	private String dm_domain_text;
	
	/** 게시판 아이디 */
	@NotNull(message="게시판 아이디를 입력해주세요.")
	@Pattern(regexp="^[a-zA-Z0-9]*$", message="게시판 아이디는 영문과 숫자만 입력가능합니다.")
	@Size(min=1, max=20, message="게시판 아이디는 1자 이상 20자 이하로 입력해주세요.")
	private String dm_table;
	
	/** 게시판명 */
	@NotBlank(message="게시판명을 입력해주세요.")
	@Size(min=1, max=100, message="게시판명은 1자 이상 100자 이하로 입력해주세요.")
	private String dm_subject;
		
	/** 게시판 권한유형 */
	@NotNull(message="권한유형을 입력해주세요.")
	@Pattern(regexp="^[1-3]{1}$", message="올바르지 않은 권한유형입니다.")
	@Size(min=1, max=1, message="올바른 권한유형을 선택해주세요.")
	private String dm_auth_type;
		
	/** 게시판 리스트 권한레벨 */
	@NotNull(message="리스트권한 레벨을 선택해주세요.")
	@Pattern(regexp="^0$|^6$|^1$", message="올바르지 않은 리스트권한 레벨값입니다.")
	private String dm_list_level;
	
	/** 게시판 리스트 권한그룹 */
	private String dm_list_group;
	
	/** 게시판 읽기 권한레벨 */
	@Pattern(regexp="^0$|^6$|^1$", message="올바르지 않은 읽기권한 레벨값입니다.")
	private String dm_read_level;
	
	/** 게시판 읽기 권한그룹 */
	private String dm_read_group;
	
	/** 게시판 쓰기 권한레벨 */
	@Pattern(regexp="^0$|^6$|^1$", message="올바르지 않은 쓰기권한 레벨값입니다.")
	private String dm_write_level;
	
	/** 게시판 쓰기 권한그룹 */
	private String dm_write_group;
	
	/** 게시판 답변 권한레벨 */
	@Pattern(regexp="^0$|^6$|^1$", message="올바르지 않은 답변권한 레벨값입니다.")
	private String dm_reply_level;
	
	/** 게시판 답변 권한그룹 */
	private String dm_reply_group;
	
	/** 게시판 답변기능 사용여부 */
	@Pattern(regexp="^$|^1$", message="올바르지 않은 답변기능 사용여부값입니다.")
	private String dm_is_reply;
	
	/** 게시판 댓글 권한레벨 */
	@Pattern(regexp="^0$|^6$|^1$", message="올바르지 않은 댓글권한 레벨값입니다.")
	private String dm_comment_level;
	
	/** 게시판 댓글 권한그룹 */
	private String dm_comment_group;
	
	/** 게시판 댓글기능 사용여부 */
	@Pattern(regexp="^$|^1$", message="올바르지 않은 댓글기능 사용여부값입니다.")
	private String dm_is_comment;
	
	/** 게시판 파일업로드 권한레벨 */
	@Pattern(regexp="^0$|^6$|^1$", message="올바르지 않은 파일업로드권한 레벨값입니다.")
	private String dm_upload_level;
	
	/** 게시판 파일업로드 권한그룹 */
	private String dm_upload_group;
	
	/** 게시판 링크 권한레벨 */
	@Pattern(regexp="^0$|^6$|^1$", message="올바르지 않은 링크권한 레벨값입니다.")
	private String dm_link_level;
	
	/** 게시판 링크 권한그룹 */
	private String dm_link_group;
	
	/** 게시판 분류기능 사용여부 */
	@Pattern(regexp="^$|^1$", message="올바르지 않은 분류기능 사용여부값입니다.")
	private String dm_use_category;
	
	/** 게시판 분류탭기능 사용여부 */
	@Pattern(regexp="^$|^1$", message="올바르지 않은 분류탭기능 사용여부값입니다.")
	private String dm_use_list_category;
	
	/** 게시판 카테고리 분류 */
	@Pattern(regexp="^[^?!%/:;.#?@<>%&=$+~\\\\]*$", message="카테고리 분류에는 ?,!,%,/,:,;,.,#,?,@,<,>,%,&,=,$,+,~,\\ 에 해당하는 특수문자를 입력할 수 없습니다.")
	@Size(max=255, message="카테고리 분류는 255자까지만 입력가능합니다.")
	private String dm_category_list;
		
	/** 게시판 비밀글 사용여부 */
	@NotNull(message="비밀글 설정값을 선택해주세요.")
	@Pattern(regexp="^[0-1]{1}$", message="올바르지 않은 비밀글 설정값입니다.")
	private String dm_use_secret;
	
	/** 게시판 에디터 사용여부 */
	@Pattern(regexp="^$|^1$", message="올바르지 않은 에디터 사용여부값입니다.")
	private String dm_use_dhtml_editor;
			
	/** 게시판 페이지당 게시물 수 */
	@NotNull(message="페이지당 게시물 수를 입력해주세요.")
	@Pattern(regexp="^[1-9]{1}|[1-9]{1}[0-9]{1}$", message="페이지당 게시물 수는 1부터 99까지 입력가능합니다.")
	@Size(min=1, max=2, message="페이지당 게시물 수는 2자리수까지만 입력가능합니다.")
	private String dm_page_rows;
	
	/** 게시판 스킨 */
	@NotNull(message="스킨을 선택해주세요.")
	@Pattern(regexp="^basic$|^gallery$|^video$", message="등록되지 않은 스킨값입니다.")
	@Size(min=1, max=30, message="스킨값은 30자리까지 입력가능합니다.")
	private String dm_skin;
	
	/** 게시판 썸네일 이미지폭 */
	@NotNull(message="썸네일 이미지폭을 입력해주세요.")
	@Pattern(regexp="^[0-9]*$", message="썸네일 이미지폭은 숫자만 입력해주세요.")
	@Size(min=1, max=3, message="썸네일 이미지폭은 1자리 이상 3자리 이하의 숫자를 입력해주세요.")
	private String dm_gallery_width;
	
	/** 게시판 썸네일 이미지높이 */
	@NotNull(message="썸네일 이미지높이를 입력해주세요.")
	@Pattern(regexp="^[0-9]*$", message="썸네일 이미지높이는 숫자만 입력해주세요.")
	@Size(min=1, max=3, message="썸네일 이미지높이는 1자리 이상 3자리 이하의 숫자를 입력해주세요.")
	private String dm_gallery_height;
		
	/** 게시판 첨부파일 개수 */
	@NotNull(message="첨부파일 갯수를 입력해주세요.")
	@Pattern(regexp="^[1-9]{1}$", message="첨부파일 갯수는 1부터 9까지의 숫자 1자리만 입력가능합니다.")
	@Size(min=1, max=1, message="첨부파일 갯수는 1부터 9까지의 숫자 1자리만 입력가능합니다.")
	private String dm_upload_count;
			
	/** 게시판 본문 */
	@Size(max=21844, message="본문설정은 최대 21884자까지 입력가능합니다.")
	private String dm_basic_content;
	
	/** 게시판 상단디자인 */
	@Size(max=21844, message="상단디자인은 최대 21884자까지 입력가능합니다.")
	private String dm_header_content;
	
	/** 게시판 하단디자인 */
	@Size(max=21844, message="하단디자인은 최대 21884자까지 입력가능합니다.")
	private String dm_footer_content;
	
	/** 게시판 비밀댓글 사용여부 */
	@NotNull(message="비밀댓글 설정값을 선택해주세요.")
	@Pattern(regexp="^[0-1]{1}$", message="유효하지 않은 비밀댓글 설정값 입니다.")
	@Size(min=1, max=1, message="올바르지 않은 비밀댓글 설정값입니다.")
	private String dm_use_comment_secret;
	
	/** 게시판 작성자 표시방법 */
	@NotNull(message="작성자 표시방법을 선택해주세요.")
	@Pattern(regexp="^name$|^id$", message="유효하지 않은 작성자 표시방법 입니다.")
	@Size(min=1, max=10, message="올바르지 않은 작성자 표시방법입니다.")
	private String dm_writer_type;
	
	/** 게시판 작성자 노출제한 */
	@NotNull(message="작성자 노출제한을 선택해주세요.")
	@Pattern(regexp="^[1-4]{1}$", message="유효하지 않은 작성자 노출제한 입니다.")
	@Size(min=1, max=1, message="올바르지 않은 작성자 노출제한입니다.")
	private String dm_writer_secret;
	
	/** 게시판  조회당 hit수 */
	@NotNull(message="조회당 Hit증가수를 입력해주세요.")
	@Pattern(regexp="^[1-5]{1}$", message="조회당 Hit증가수는 1부터 5까지의 숫자 1자리만 입력가능합니다.")
	@Size(min=1, max=1, message="조회당 Hit증가수는 1부터 5까지의 숫자 1자리만 입력가능합니다.")
	private String dm_hit_count;
	
	/** 게시판  hit아이콘 사용여부 */
	@Pattern(regexp="^$|^1$", message="올바르지 않은 HIT 아이콘 사용여부값입니다.")
	private String dm_is_hit;
	
	/** 게시판  hit아이콘 조회수 */
	@NotNull(message="HIT 아이콘 조회수를 입력해주세요.")
	@Pattern(regexp="^[0-9]*$", message="HIT 아이콘 조회수는 숫자만 입력가능합니다.")
	@Size(min=1, max=4, message="HIT 아이콘 조회수는 1자리 이상 4자리 이하 숫자만 입력가능합니다.")
	private String dm_hit_max;
		
	/** 게시판  hit아이콘 파일명 */
	private String dm_hit_icon;
	
	/** 게시판 new아이콘 사용여부 */
	@Pattern(regexp="^$|^1$", message="올바르지 않은 NEW 아이콘 사용여부값입니다.")
	private String dm_is_new;
	
	/** 게시판 new아이콘 표출시간 */
	@NotNull(message="게시글 등록 후 NEW 아이콘 표출 시간을 입력해주세요.")
	@Pattern(regexp="^[0-9]*$", message="NEW 아이콘 표출 시간은 숫자만 입력가능합니다.")
	@Size(min=1, max=2, message="NEW 아이콘 표출 시간은 1자리 이상 2자리 이하 숫자만 입력가능합니다.")
	private String dm_new_time;
		
	/** 게시판 new아이콘 파일명 */
	private String dm_new_icon;
	
	/** 게시판 첨부파일아이콘 사용여부 */
	@Pattern(regexp="^$|^1$", message="올바르지 않은 첨부파일 아이콘 사용여부값입니다.")
	private String dm_use_file_icon;
	
	/** 게시판 게시글 삭제설정 */
	@NotNull(message="게시글 삭제 설정값을 선택해주세요.")
	@Pattern(regexp="^alone$|^both$", message="유효하지 않은 게시글 삭제 설정값 입니다.")
	@Size(min=1, max=10, message="올바르지 않은 게시글 삭제 설정값입니다.")
	private String dm_reply_delete_type;
	
	/** 게시판 링크 사용여부 */
	@Pattern(regexp="^$|^1$", message="올바르지 않은 링크 사용여부값입니다.")
	private String dm_use_link;
	
	/** 게시판 첨부파일 사용여부 */
	@Pattern(regexp="^$|^1$", message="올바르지 않은 첨부파일 사용여부값입니다.")
	private String dm_use_file;
	
	/** 게시판리스트 검색조건 */
	private String search_type;
	
	/** 게시판리스트 검색단어 */
	private String search_value;
	
	/** 게시판리스트 검색도메인 */
	private String search_domain;
		
	/** 게시판리스트 페이지당 게시물 수 */
	private int rows;			
	
	/** 게시판리스트 페이지번호 */
	private int page;
	
	/** 게시판리스트 권한 그룹리스트 */
	@Size(max=5, message="리스트권한 그룹은 5개까지 선택하능합니다.")
	private List<@Pattern(regexp="^GROUP_[0-9]{10}$", message="올바르지 않은 리스트권한 그룹값입니다.") String> dm_list_group_arr;
	
	/** 게시판읽기 권한 그룹리스트 */
	@Size(max=5, message="읽기권한 그룹은 5개까지 선택하능합니다.")
	private List<@Pattern(regexp="^GROUP_[0-9]{10}$", message="올바르지 않은 읽기권한 그룹값입니다.") String> dm_read_group_arr;
	
	/** 게시판쓰기 권한 그룹리스트 */
	@Size(max=5, message="쓰기권한 그룹은 5개까지 선택하능합니다.")
	private List<@Pattern(regexp="^GROUP_[0-9]{10}$", message="올바르지 않은 쓰기권한 그룹값입니다.") String> dm_write_group_arr;
	
	/** 게시판답변 권한 그룹리스트 */
	@Size(max=5, message="답변권한 그룹은 5개까지 선택하능합니다.")
	private List<@Pattern(regexp="^GROUP_[0-9]{10}$", message="올바르지 않은 답변권한 그룹값입니다.") String> dm_reply_group_arr;
	
	/** 게시판댓글 권한 그룹리스트 */
	@Size(max=5, message="댓글권한 그룹은 5개까지 선택하능합니다.")
	private List<@Pattern(regexp="^GROUP_[0-9]{10}$", message="올바르지 않은 댓글권한 그룹값입니다.") String> dm_comment_group_arr;
	
	/** 게시판링크 권한 그룹리스트 */
	@Size(max=5, message="링크권한 그룹은 5개까지 선택하능합니다.")
	private List<@Pattern(regexp="^GROUP_[0-9]{10}$", message="올바르지 않은 링크권한 그룹값입니다.") String> dm_link_group_arr;
	
	/** 게시판업로드 권한 그룹리스트 */
	@Size(max=5, message="업로드권한 그룹은 5개까지 선택하능합니다.")
	private List<@Pattern(regexp="^GROUP_[0-9]{10}$", message="올바르지 않은 업로드권한 그룹값입니다.") String> dm_upload_group_arr;
		
	/** hit아이콘 업로드파일  */
	private MultipartFile dm_hit_icon_file;
	
	/** new아이콘 업로드파일  */
	private MultipartFile dm_new_icon_file;
	
	/** hit아이콘 삭제여부  */
	private String dm_del_hit;
	
	/** new아이콘 삭제여부  */
	private String dm_del_new;
	
	private String dm_uid;
	
	/** 메인페이지 노출 여부  */
	@Pattern(regexp="^[0-1]{1}$", message="유효하지 않은 메인페이지 노출 설정값 입니다.")
	private String dm_main_use;
	
	/** 메인페이지 노출 순서  */
	@Pattern(regexp="^[0-9]*$", message="게시판 노출 순서는 숫자만 입력가능합니다.")
	@Size(min=1, max=2, message="게시판 노출 순서는 1자리 이상 2자리 이하 숫자만 입력가능합니다.")
	@Min(value=1, message="게시판 노출 순서는 0보다 커야합니다.")
	private String dm_main_order;
	
	/** 메인페이지 노출 건수  */
	@Pattern(regexp="^[0-9]*$", message="메인페이지 노출 건수는 숫자만 입력가능합니다.")
	@Size(min=1, max=1, message="메인페이지 노출 건수는 1자리 숫자만 입력가능합니다.")
	private String dm_main_count;
	
	private String dm_create_dt;
	
	private String dm_create_id;
	
	private String dm_modify_dt;
	
	private String dm_modify_id;
	
	private String dm_delete_yn;
	
	private String dm_delete_id;
	
	private String dm_delete_dt;

}
