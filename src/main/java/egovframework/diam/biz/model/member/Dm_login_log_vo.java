/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.model.member;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dm_login_log_vo implements Serializable {
	
	/** VO 직렬화 시 version uid 값 */
	private static final long serialVersionUID = 8101883721838653623L;

	/** 로그인로그 PK */
	private Integer dm_id;
	
	/** 로그인구분 */
	private String dm_type;
	
	/** 로그인일자 */
	private String dm_datetime;
	
	/** 로그인 아이디 */
	private String dm_login_id;
	
	/** 로그인 IP */
	private String dm_ip;
	
	/** 로그인 구분자 */
	private String dm_fn_code;
	
	/** 로그인 성공여부 */
	private String dm_fn_result;
	
	/** 로그인 접속 URL */
	private String dm_fn_url;
	
	/** 로그인 접속 기기정보 */
	private String dm_agent_info;
		
	/** 로그인로그 리스트 검색시작일 */
	private String search_start_date;
	
	/** 로그인로그 리스트 검색종료일 */
	private String search_end_date;
	
	/** 로그인로그 리스트 검색조건 */
	private String search_type;
	
	/** 로그인로그 리스트 검색단어 */
	private String search_value;
		
	/** 로그인로그 리스트 페이지당 게시물 수 */
	private int page;
	
	/** 로그인로그 리스트 페이지번호 */
	private int rows;
		
}

