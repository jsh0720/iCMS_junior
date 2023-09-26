package egovframework.diam.biz.model.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dm_statistics_member_vo {
	
	private String dm_datetime;
	
	private int total_count;
	private String yoil;
	private int new_count;
	private int leave_count;
	private int man_count;
	private int woman_count;
	private int undefined_count;
	private int age_count;
	private String age;
	private int age_10;
	private int age_20;
	private int age_30;
	private int age_40;
	private int age_50;
	private int age_60;
	private int age_70;
	private int age_undefined;
	
	private String search_type;
	private String search_value;
	private String search_start_date;
	private String search_end_date;
	private String search_level;
	private String search_gender;
	private String search_leave;
	private int rows;
	private int page;
	
}
