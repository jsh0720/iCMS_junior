package egovframework.diam.biz.model.statistics;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dm_visit_env_vo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6756302613988263112L;
	
	private String dm_datetime;	//날짜
	private String dm_os;		//os
	private String dm_brower;	//브라우저
	private int dm_count;		//방문자
	private String dm_type;		//pc => 0/모바일 => 1
	private String dm_domain;	//접속한도메인
	
	//검색조건
	private String search_start_date;
	private String search_end_date;
	private String search_type;
	private String search_value;
	private String site_id;	

}
