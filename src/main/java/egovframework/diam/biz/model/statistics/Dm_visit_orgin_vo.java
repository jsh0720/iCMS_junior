package egovframework.diam.biz.model.statistics;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dm_visit_orgin_vo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4202220120555304169L;
	
	private String dm_datetime;	//날짜
	private int dm_count;		//방문자수
	private String dm_orgin;	//방문경로
	private String dm_domain;	//접속한도메인
	
	//검색조건
	private String search_start_date;
	private String search_end_date;
	private String search_type;
	private String search_value;
	private String site_id;
	
	private String[] dm_orgin_engine;

}
