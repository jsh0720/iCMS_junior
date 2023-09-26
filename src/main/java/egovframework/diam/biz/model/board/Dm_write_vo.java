/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.model.board;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Class Name : Dm_write_vo.java
 * @Description : 게시물 데이터 사용변수 정의 VO 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Dm_write_vo implements Serializable{
	
	/** VO 직렬화 시 version uid 값 */
	private static final long serialVersionUID = -5393138658713990007L;

	/** 게시글 PK */
	@Pattern(regexp="^[0-9]*$", message="올바르지 않은 요청값입니다.")
	private String wr_id;
	
	/** 게시글 정렬번호 */
	private int wr_num;
		
	/** 게시판 FK */
	@Pattern(regexp="^[0-9]*$", message="올바르지 않은 요청값입니다.")
	private String wr_board;
	
	/** 게시글 답변여부 */
	private int wr_reply;
	
	/** 게시글 답변 졍렬순서 */
	private int wr_reply_reply;
	
	/** 게시글 상위게시글 PK */
	private String wr_parent;
		
	/** 게시글 댓글여부 */
	private int wr_is_comment;
	
	/** 게시글 댓글정렬순서 */
	private int wr_comment;
	
	/** 게시글 댓글의댓글 정렬순서 */
	private int wr_comment_reply;
	
	/** 게시글 카테고리명 */
	@Size(max=20, message="카테고리 값은 최대 20자까지만 가능합니다.")
	private String ca_name;
	
	/** 게시글 옵션 */
	@Pattern(regexp="^$|^secret$", message="올바르지 않은 비밀글 옵션값입니다.")
	@Size(max=20, message="비밀글 옵션값은 최대 20자까지만 가능합니다.")
	private String wr_option;
	
	/** 게시글 제목 */
	@NotBlank(message="제목을 입력해주세요.")
	@Size(min=1, max=255, message="제목은 1자 이상 255자까지 입력해주세요.")
	private String wr_subject;
	
	/** 게시글 내용 */
	@NotBlank(message="내용을 입력해주세요.")
	@Size(min=1, max=21844, message="내용은 1자 이상 21844자이하로 입력해주세요.")
	private String wr_content;
		
	/** 게시글 링크1 */
	@Pattern(regexp="^(https?://)(www.)?([a-zA-Z0-9]+)\\.[a-z]+([a-zA-Z0-9가-힣/+?=#&-_]+)?", message="연결할 링크는 http 프로토콜을 포함한 URL 형식이어야 합니다.")
	@Size(max=255, message="링크1 값은 최대 255자까지만 입력가능합니다.")
	private String wr_link1;
	
	public void setWr_link1 (String wr_link1) {
		this.wr_link1 = wr_link1.isEmpty() ? null : wr_link1;
	}
		
	/** 게시글 링크2 */
	@Pattern(regexp="^(https?://)(www.)?([a-zA-Z0-9]+)\\.[a-z]+([a-zA-Z0-9가-힣/+?=#&-_]+)?", message="연결할 링크는 http 프로토콜을 포함한 URL 형식이어야 합니다.")
	@Size(max=255, message="링크2 값은 최대 255자까지만 입력가능합니다.")
	private String wr_link2;
	
	public void setWr_link2 (String wr_link2) {
		this.wr_link2 = wr_link2.isEmpty() ? null : wr_link2;
	}
		
	/** 게시글 조회수 */
	private int wr_hit;
	
	/** 게시글 공지사항 여부 */
	@Pattern(regexp="^$|^1$", message="올바르지 않은 공지사항 옵션값입니다.")
	private String wr_is_notice;
		
	/** 게시글 등록자 */
	private String mb_id;
	
	/** 답변 게시글 등록자 */
	private String reply_mb_id;
	
	/** 게시글 비밀번호 */
	private String wr_password;
	
	/** 게시글 작성자 */
	@NotBlank(message="작성자를 입력해주세요.")
	@Pattern(regexp="^[a-zA-Z0-9가-힣ㄱ-ㅎ]*$", message="작성자는 영문,한글,숫자로만 입력가능합니다.")
	@Size(min=1, max=30, message="작성자는 1자이상 30이하로 입력해주세요.")
	private String wr_name;
	
	/** 게시글 등록일자 */
	private String wr_datetime;
	
	/** 게시글 첨부파일 임시파일명 */
	private String wr_file;
	
	/** 게시글 첨부파일 원파일명 */
	private String wr_ori_file_name;
	
	/** 게시글 등록 아이피 */
	private String wr_ip;
	
	private String dm_modify_id;
	
	private String dm_modify_dt;
	
	private String dm_delete_dt;
	
	private String dm_delete_yn;
	
	private String dm_delete_id;
	
	/** 게시글 게시판아이디 */
	private String dm_table;
	
	/** 게시글 게시판명 */
	private String dm_table_text;
		
	/** 게시글 이동 테이블아이디 */
	private String target_table;
	
	/** 게시글 댓글개수 */
	private String com_count;
	
	/** 게시글 답글개수 */
	private String re_count;
	
	/** 답변 원게시글 등록자 */
	private String ori_mb_id;
	
	/** 답변의 답변 등록 시 재배치할 position값  */
	private int new_position;
	
	/** 게시글리스트 페이지번호 */
	private int page;
	
	/** 게시글리스트 페이지당 게시물 수 */
	private int rows;
	
	/** 게시글리스트 검색 게시판PK*/
	private String search_board;
	
	/** 게시글리스트 검색조건 */
	private String search_type;
	
	/** 게시글리스트 검색단어 */
	private String search_value;
	
	/** 게시글리스트 검색시작일 */
	private String search_start_date;
	
	/** 게시글리스트 검색종료일 */
	private String search_end_date;
		
	/** 등록된 게시판리스트 */
	private List<String> boardList = new ArrayList<String>();
	
	/** 등록된 답글리스트 */
	private List<Dm_write_vo> replyList = new ArrayList<Dm_write_vo>();
	
	/** 게시글 업로드 첨부파일 리스트 */
	private List<MultipartFile> file = new ArrayList<MultipartFile>();
	
	/** 게시글리스트 검색카테고리 */
	private String search_cate;
	
	/** 게시글 썸네일 파일명 */
	private String thumb;
	
	private String prev_id;
	
	private String prev_subject;
	
	private String next_id;
	
	private String next_subject;
	
	private List<Dm_board_vo> searchBoardList = new ArrayList<>();
	
	private String type;
	
	private String uid;
	
	private List<Dm_write_vo> children = new ArrayList<>();
	
	private String state;
}