package egovframework.diam.biz.model.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dm_statistics_board_vo {
	
	private String board_name;
	
	private String domain_name;

	private int total_count;
	
	private int write_count;
	
	private int reply_count;
	
	private int comment_count;
	
	private String board_id;
	
	private String search_start_date;
	
	private String search_end_date;
}
