package egovframework.diam.biz.model.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dm_web_log_vo {
	private Integer dm_id;
	private String dm_domain;
	private String dm_type;
	private String dm_title;
	private String dm_datetime;
	private String dm_login_id;
	private String dm_ip;
	private String dm_fn_code;
	private String dm_fn_url;
	private String dm_agent_info;
	private String dm_re_visit;
	private String dm_status;
	
	//검색조건
	private String search_start_date;
	private String search_end_date;
	private String search_type;
	private String search_value;
	private int page;
	private int rows;
	private String site_id;
	
	private int pv_count;
	
	private int day_num;
	private int page_count;
	
	private int log_ip_pv_count;
	private int log_pv_count;
	private int log_new_pv_count;
	private int log_re_pv_count;	
}
