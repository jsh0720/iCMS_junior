package egovframework.diam.ui.statistics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.diam.biz.model.statistics.Dm_statistics_member_vo;
import egovframework.diam.biz.service.statistics.StatisticsMemberService;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
public class StatisticsMemberController {
	
	@Resource(name="statisticsMemberService")
	private StatisticsMemberService statisticsMemberService;
	
	@RequestMapping("/adm/get_statistics_member.do")
	public ResponseEntity<?> get_statistics_member(Dm_statistics_member_vo statisticsVO, @RequestParam(value="mode", required=true) String mode, 
			@RequestParam(value="search_domain", required=false) String search_domain) {
		
		Map<String, Object> result = new HashMap<>();

		try {
			if("day_member_chart".equals(mode) || "day_member_table".equals(mode)) {
				List<Dm_statistics_member_vo> list = statisticsMemberService.selectMemberStatisticsNewMember(statisticsVO);

				int total_count = 0;
				for (Dm_statistics_member_vo vo : list) {
					total_count += vo.getTotal_count();
				}
				if("day_member_chart".equals(mode)) {
					List<Object> percent_arr = new ArrayList<>();
					List<Object> total_count_arr = new ArrayList<>();
					List<Object> date_arr = new ArrayList<>();

					for (Dm_statistics_member_vo vo : list) {
						double percent = 0.0;
						if(total_count != 0 && vo.getTotal_count() != 0) {
							int count = vo.getTotal_count();
							percent = (double)count/(double)total_count*100;
						}
						percent_arr.add(String.format("%.2f", percent));
						total_count_arr.add(vo.getTotal_count());
						date_arr.add(vo.getDm_datetime());
					}

					result.put("total_count", total_count_arr);
					result.put("percent", percent_arr);
					result.put("date", date_arr);
				} else if("day_member_table".equals(mode)) {
					List<Object> jlist = new ArrayList<>();
					for (Dm_statistics_member_vo vo : list) {
						Map<String, Object> map = new HashMap<>();
						map.put("total_count", vo.getTotal_count());
						map.put("dm_date", vo.getDm_datetime());
						double percent = 0.0;
						if(total_count != 0 && vo.getTotal_count() != 0) {
							int count = vo.getTotal_count();
							percent = (double)count/(double)total_count*100;
						}
						map.put("percent", String.format("%.2f", percent));
						jlist.add(map);
					}
					result.put("rows", jlist);
					result.put("total", jlist.size());
				}
			} else if("weekly_member_chart".equals(mode)) {
				List<Dm_statistics_member_vo> list = statisticsMemberService.selectMemberStatisticsWeeklyMember(statisticsVO);
				List<Object> total_count_arr = new ArrayList<>();
				List<Object> leave_count_arr = new ArrayList<>();
				List<Object> date_arr = new ArrayList<>();

				for (Dm_statistics_member_vo vo : list) {
					leave_count_arr.add(vo.getLeave_count());
					total_count_arr.add(vo.getTotal_count());
					date_arr.add(vo.getDm_datetime());
				}
				result.put("leave_count", leave_count_arr);
				result.put("total_count", total_count_arr);
				result.put("date", date_arr);
			} else if("day_member".equals(mode)) {
				List<Dm_statistics_member_vo> list = statisticsMemberService.selectMemberStatisticsNewMember(statisticsVO);
				//Dm_statistics_member_vo today_vo = list.get(0);
				int today_total = 0;
				for(int i=0; i<list.size(); i++) {
					today_total += list.get(i).getTotal_count();
				}

				result.put("total_count", today_total);
				 
				Dm_statistics_member_vo max_vo = statisticsMemberService.selectMemberStatisticsMaxNewMember(statisticsVO);
				Dm_statistics_member_vo min_vo = statisticsMemberService.selectMemberStatisticsMinNewMember(statisticsVO);
				
				int max_count = 0;
				String max_date = "";
				String max_yoil = "";

				int min_count = 0;
				String min_date = "";
				String min_yoil = "";

				if(max_vo != null) {
					max_count = max_vo.getTotal_count();
					max_date = max_vo.getDm_datetime();
					max_yoil = max_vo.getYoil();
				}
				if(min_vo != null) {
					min_count = min_vo.getTotal_count();
					min_date = min_vo.getDm_datetime();
					min_yoil = min_vo.getYoil();
				}
				result.put("max_count", max_count);
				result.put("max_date", max_date);
				result.put("max_yoil", max_yoil);

				result.put("min_count", min_count);
				result.put("min_date", min_date);
				result.put("min_yoil", min_yoil);
			} else if("time_member_chart".equals(mode) || "time_member_table".equals(mode)) {
				List<Map<String, Object>> jlist = new ArrayList<>();
				List<Object> total_arr = new ArrayList<>();
				List<Object> dm_time_arr = new ArrayList<>();
				int total_count = 0;
				for (int i = 0; i < 24; i++) {
					Map<String, Object> map = new HashMap<>();
					String date_time = "";
					if(i < 10) {
						date_time = "0" + i;
					} else {
						date_time = Integer.toString(i);
					}
					statisticsVO.setDm_datetime(date_time);
					Dm_statistics_member_vo time_vo = statisticsMemberService.selectMemberStatisticsTimeNewMember(statisticsVO);
					if(time_vo != null) {
						map.put("total_count", time_vo.getTotal_count());
						map.put("dm_time", date_time+":00");

						total_arr.add(time_vo.getTotal_count());
						dm_time_arr.add(date_time+":00");

						total_count += time_vo.getTotal_count();
					} else {
						map.put("total_count", 0);
						map.put("pc_count", 0);
						map.put("mobile_count", 0);
						map.put("dm_time", date_time+":00");

						total_arr.add(0);
						dm_time_arr.add(date_time+":00");

						total_count += 0;
					}
					jlist.add(map);
				}

				if("time_member_chart".equals(mode)) {
					result.put("total_count", total_arr);
				} else if("time_member_table".equals(mode)) {
					List<Object> tableList = new ArrayList<>();
					for (int i = 0; i < jlist.size(); i++) {
						Map<String, Object> item = jlist.get(i);
						double percent = 0.0;
						if(total_count != 0 && !"0".equals(item.get("total_count"))) {
							int count = Integer.parseInt(item.get("total_count").toString());
							percent = (double)count/(double)total_count*100;
						}
						item.put("percent", String.format("%.2f", percent));
						tableList.add(item);
					}

					result.put("rows", tableList);
					result.put("total", tableList.size());
				}

			} else if("week_member_chart".equals(mode) || "week_member_table".equals(mode)) {

				List<Map<String, Object>> jlist = new ArrayList<>();
				List<Object> total_arr = new ArrayList<>();
				String[] week = {"일요일","월요일","화요일","수요일","목요일","금요일","토요일"};
				int total_count = 0;
				for (int i = 1; i <= 7; i++) {
					Map<String, Object> map = new HashMap<>();
					statisticsVO.setDm_datetime(Integer.toString(i));
					Dm_statistics_member_vo time_vo = statisticsMemberService.selectMemberStatisticsWeekNewMember(statisticsVO);
					if(time_vo != null) {
						map.put("total_count", time_vo.getTotal_count());
						map.put("dm_date", week[i-1]);
						total_arr.add(time_vo.getTotal_count());
						total_count += time_vo.getTotal_count();
					} else {
						map.put("total_count", 0);
						map.put("dm_date", week[i-1]);

						total_arr.add(0);
						total_count += 0;
					}
					jlist.add(map);
				}
				if("week_member_chart".equals(mode)) {
					result.put("total", total_arr);
				} else if("week_member_table".equals(mode)) {
					List<Object> tableList = new ArrayList<>();
					for (int i = 0; i < jlist.size(); i++) {
						Map<String, Object> item = jlist.get(i);
						double percent = 0.0;
						if(total_count != 0 && !"0".equals(item.get("total_count"))) {
							int count = Integer.parseInt(item.get("total_count").toString());
							percent = (double)count/(double)total_count*100;
						}
						item.put("percent", String.format("%.2f", percent));
						tableList.add(item);
					}

					result.put("rows", tableList);
					result.put("total", tableList.size());
				}


			} else if("month_member".equals(mode)) {
				Dm_statistics_member_vo month_vo = statisticsMemberService.selectMemberStatisticsMonthNewMember(statisticsVO);
				result.put("total_count", month_vo.getTotal_count());
				Dm_statistics_member_vo max_vo = statisticsMemberService.selectMemberStatisticsMaxMonthNewMember(statisticsVO);
				result.put("max_count", max_vo.getTotal_count());
				result.put("max_date", max_vo.getDm_datetime());
				Dm_statistics_member_vo min_vo = statisticsMemberService.selectMemberStatisticsMinMonthNewMember(statisticsVO);
				result.put("min_count", min_vo.getTotal_count());
				result.put("min_date", min_vo.getDm_datetime());
				
			} else if("month_member_chart".equals(mode) || "month_member_table".equals(mode)) {
				
				List<Dm_statistics_member_vo> list = statisticsMemberService.selectMemberStatisticsNewMemberMonthChart(statisticsVO);
				
				int total_count = 0;
				for (Dm_statistics_member_vo vo : list) {
					total_count += vo.getTotal_count();
				}
				if("month_member_chart".equals(mode)) {
					List<Object> percent_arr = new ArrayList<>();
					List<Object> total_count_arr = new ArrayList<>();
					List<Object> date_arr = new ArrayList<>();
					
					for (Dm_statistics_member_vo vo : list) {
						double percent = 0.0;
						if(total_count != 0 && vo.getTotal_count() != 0) {
							int count = vo.getTotal_count();
							percent = (double)count/(double)total_count*100;
						}
						percent_arr.add(String.format("%.2f", percent));
						total_count_arr.add(vo.getTotal_count());
						date_arr.add(vo.getDm_datetime());
					}
					
					result.put("total_count", total_count_arr);
					result.put("percent", percent_arr);
					result.put("date", date_arr);
				
				} else if("month_member_table".equals(mode)) {
					List<Object> jlist = new ArrayList<>();
					for (Dm_statistics_member_vo vo : list) {
						Map<String, Object> map = new HashMap<>();
						map.put("total_count", vo.getTotal_count());
						map.put("dm_date", vo.getDm_datetime());
						double percent = 0.0;
						if(total_count != 0 && vo.getTotal_count() != 0) {
							int count = vo.getTotal_count();
							percent = (double)count/(double)total_count*100;
						}
						map.put("percent", String.format("%.2f", percent));
						jlist.add(map);
					}
					result.put("rows", jlist);
					result.put("total", jlist.size());
				}
			} else if("all_day_member".equals(mode)) {
				Dm_statistics_member_vo all_day_member = statisticsMemberService.selectMemberStatisticsAllDayMember(statisticsVO);
				int total_count = 0;
				int new_count = 0;
				int leave_count = 0;
				if(all_day_member != null) {
					total_count = all_day_member.getTotal_count();
					new_count = all_day_member.getNew_count();
					leave_count = all_day_member.getLeave_count();
				}
				result.put("total_count", total_count);
				result.put("new_count", new_count);
				result.put("leave_count", leave_count);
			} else if("all_day_member_chart".equals(mode) || "all_day_member_table".equals(mode)) {

				List<Object> jlist = new ArrayList<>();
				List<Object> total_arr = new ArrayList<>();
				List<Object> intercept_arr = new ArrayList<>();
				List<Object> leave_arr = new ArrayList<>();
				List<Object> date_arr = new ArrayList<>();
				List<Dm_statistics_member_vo> chart_list = statisticsMemberService.selectMemberStatisticsAllDayMemberChart(statisticsVO);

				for (Dm_statistics_member_vo vo : chart_list) {
					Map<String,Object> map = new HashMap<>();
					total_arr.add(vo.getTotal_count());
					leave_arr.add(vo.getLeave_count());
					date_arr.add(vo.getDm_datetime());

					map.put("dm_datetime", vo.getDm_datetime());
					map.put("total", vo.getTotal_count());
					map.put("leave", vo.getLeave_count());

					jlist.add(map);
				}
				if("all_day_member_chart".equals(mode)) {
					result.put("total", total_arr);
					result.put("intercept", intercept_arr);
					result.put("leave", leave_arr);
					result.put("date", date_arr);
				} else {
					result.put("rows", jlist);
					result.put("total", jlist.size());
				}
			} else if("all_month_member".equals(mode)) {
				
				Dm_statistics_member_vo all_day_member = statisticsMemberService.selectMemberStatisticsAllMonthMember(statisticsVO);
				int total_count = 0;
				int new_count = 0;
				int leave_count = 0;
				if(all_day_member != null) {
					total_count = all_day_member.getTotal_count();
					new_count = all_day_member.getNew_count();
					leave_count = all_day_member.getLeave_count();
				}
				result.put("total_count", total_count);
				result.put("new_count", new_count);
				result.put("leave_count", leave_count);
			
			} else if("all_month_member_chart".equals(mode) || "all_month_member_table".equals(mode)) {
				
				List<Dm_statistics_member_vo> list = statisticsMemberService.selectMemberStatisticsAllMemberMonthChart(statisticsVO);
				
				List<Object> date_arr = new ArrayList<>();
				List<Object> new_count_arr = new ArrayList<>();
				List<Object> leave_count_arr = new ArrayList<>();
				
				for (Dm_statistics_member_vo vo : list) {
					new_count_arr.add(vo.getNew_count());
					leave_count_arr.add(vo.getLeave_count());
					date_arr.add(vo.getDm_datetime());
				}
				
				result.put("new_count", new_count_arr);
				result.put("leave_count", leave_count_arr);
				result.put("date", date_arr);
				result.put("rows", list);
				
			} else if("dash_board".equals(mode)) {
				Dm_statistics_member_vo dm_statistics_member_vo = new Dm_statistics_member_vo();
				int total_member = statisticsMemberService.selectMemberStatisticsCount(dm_statistics_member_vo);

				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.MONTH, -1);
				dm_statistics_member_vo.setSearch_start_date(format.format(calendar.getTime()));
				int ago_month_member = statisticsMemberService.selectMemberStatisticsCount(dm_statistics_member_vo);


				if(total_member > 0) {
					result.put("member_compare", Math.ceil((double)ago_month_member / total_member * 100));
				} else {
					result.put("member_compare", 0);
				}
				result.put("member_count", total_member);
			}

			result.put("result", "success");
			result.put("_return", "");
		} catch(DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			result.put("notice", MessageCode.CMM_DATA_ERROR.getMessage());
			return new ResponseEntity<>(result , HttpStatus.INTERNAL_SERVER_ERROR);
		} catch(Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			result.put("notice", MessageCode.CMM_SYSTEM_ERROR.getMessage());
			return new ResponseEntity<>(result , HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
