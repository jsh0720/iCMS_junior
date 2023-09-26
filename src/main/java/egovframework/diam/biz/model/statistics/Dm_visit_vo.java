package egovframework.diam.biz.model.statistics;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dm_visit_vo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2503854753084922457L;
	
	private String dm_datetime;		//날짜
	private int dm_count;			//방문자
	private String dm_type;			//접속페이지 타입
	private String dm_page_name;	//접속페이지명
	private String dm_domain;		//접속도메인
	
	//검색조건
	private String search_start_date;
	private String search_end_date;
	private String search_type;
	private String search_value;
	private String search_domain;
	private String site_id;
	private String mode;
	
	
	private String dm_date;
	private int dm_visit_count;
	private int dm_re_visit_count;
	private int dm_new_visit_count;
	private int dm_page_view_count;	
	
}
