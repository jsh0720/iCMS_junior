package egovframework.diam.ui.statistics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.diam.biz.model.statistics.Dm_visit_vo;
import egovframework.diam.biz.model.statistics.Dm_web_log_vo;
import egovframework.diam.biz.service.config.DomainService;
import egovframework.diam.biz.service.statistics.VisitService;
import egovframework.diam.biz.service.statistics.WebLogService;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
public class VisitController {
	
	@Resource(name="domainService")
	private DomainService domainService;
	
	@Resource(name="visitService")
	private VisitService visitService;
	
	@Resource(name="webLogService")
	private WebLogService webLogService;
	
	@RequestMapping("/adm/get_today_visit.do")
	public @ResponseBody ResponseEntity<?> get_today_visit(Dm_visit_vo visitVO) throws Exception {
		
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		String today = (new SimpleDateFormat("yyyy-MM-dd").format(date));
		String domainId = visitVO.getSearch_domain();
		String mode = visitVO.getMode();
		Map<String, Object> result = new HashMap<>();
		try {
			if("total_accessor".equals(mode)) {
				visitVO.setSearch_type("ymd");
				int today_count = visitService.selectTodayTotalAccess(visitVO);
				
				int max_count = visitService.selectCountMaxTotalAccess(visitVO);
				
				int min_count = visitService.selectCountMinTotalAccess(visitVO);
				
				visitVO.setDm_count(max_count);
				System.out.println("===================selectDatetimeTotalAccess");
				List<Dm_visit_vo> max_visit_List = visitService.selectDatetimeTotalAccess(visitVO);
				
				visitVO.setDm_count(min_count);
				System.out.println("====================selectDatetimeTotalAccess");
				List<Dm_visit_vo> min_visit_List = visitService.selectDatetimeTotalAccess(visitVO);
				
				Dm_web_log_vo webLogVO = new Dm_web_log_vo();
				webLogVO.setDm_domain(domainId);
				webLogVO.setSearch_start_date(visitVO.getSearch_start_date());
				webLogVO.setSearch_end_date(visitVO.getSearch_end_date());
				webLogVO.setSearch_type("ymd");
				int max_pv_count = webLogService.selectWebLogMaxPvCount(webLogVO);
				int min_pv_count = webLogService.selectWebLogMinPvCount(webLogVO);
				int total_pv_count = webLogService.selectWebLogTotalPvCount(webLogVO);
				
				webLogVO.setPv_count(max_pv_count);
				Dm_web_log_vo min_pv_log_vo = webLogService.selectWebLogPvDatetime(webLogVO);

				webLogVO.setPv_count(min_pv_count);
				Dm_web_log_vo max_pv_log_vo = webLogService.selectWebLogPvDatetime(webLogVO);
				
				result.put("today", today);
				result.put("today_count", today_count);
				result.put("max_count", max_count);
				result.put("max_date", max_visit_List.get(0).getDm_datetime());
				result.put("min_count", min_count);
				result.put("min_date", min_visit_List.get(0).getDm_datetime());

				result.put("total_pv_count", total_pv_count);
				result.put("max_pv_count", max_pv_count);
				result.put("max_pv_date", max_pv_log_vo.getDm_datetime());
				result.put("min_pv_count", min_pv_count);
				result.put("min_pv_date", min_pv_log_vo.getDm_datetime());
			} else if("day".equals(mode)) {
				Dm_web_log_vo webLogVO = new Dm_web_log_vo();
				webLogVO.setDm_domain(domainId);
				webLogVO.setSearch_start_date(visitVO.getSearch_start_date());
				webLogVO.setSearch_end_date(visitVO.getSearch_end_date());
				webLogVO.setSearch_type("ymd");
				int visit_count = webLogService.selectWebLogIpPv(webLogVO);
				int pv_count = webLogService.selectWebLogPv(webLogVO);
				int new_pv_count = webLogService.selectWebLogNewPv(webLogVO);
				int re_pv_count = webLogService.selectWebLogRePv(webLogVO);
				double pv = 0;
				if(visit_count != 0 && pv_count != 0) {
					 pv = Math.ceil(pv_count/visit_count);
				}
				result.put("visitor", visit_count);
				result.put("visit_count", pv_count);
				result.put("new_count", new_pv_count);
				result.put("re_count", re_pv_count);
				result.put("pv", pv);
			}else if("day_of_week_accessor".equals(mode)) {
				visitVO.setSearch_type("ymd");
				int max_count = visitService.selectCountMaxTotalAccess(visitVO);
				visitVO.setDm_count(max_count);
				String max_date = visitService.selectVisitDayName(visitVO);

				int min_count = visitService.selectCountMinTotalAccess(visitVO);
				visitVO.setDm_count(min_count);
				String min_date = visitService.selectVisitDayName(visitVO);

				result.put("max_count", max_count);
				result.put("max_date", max_date);
				result.put("min_count", min_count);
				result.put("min_date", min_date);

				Dm_web_log_vo webLogVO = new Dm_web_log_vo();
				webLogVO.setDm_domain(domainId);
				webLogVO.setSearch_start_date(visitVO.getSearch_start_date());
				webLogVO.setSearch_end_date(visitVO.getSearch_end_date());
				webLogVO.setSearch_type("ymd");
				int max_pv_count = webLogService.selectWebLogMaxPvCount(webLogVO);
				webLogVO.setPv_count(max_pv_count);
				String max_pv_date = webLogService.selectWebLogDayName(webLogVO);

				int min_pv_count = webLogService.selectWebLogMinPvCount(webLogVO);
				webLogVO.setPv_count(min_pv_count);
				String min_pv_date = webLogService.selectWebLogDayName(webLogVO);

				result.put("max_pv_count", max_pv_count);
				result.put("max_pv_date", max_pv_date);
				result.put("min_pv_count", min_pv_count);
				result.put("min_pv_date", min_pv_date);
			} else if("day_of_week_accessor_table".equals(mode) || "day_of_week_accessor_chart".equals(mode)) {
				String[] day_arr = {"일요일","월요일","화요일","수요일","목요일","금요일","토요일"};
				List<Object> list = new ArrayList<>();
				List<Object> visitor_array = new ArrayList<>();
				List<Object> visit_count_array = new ArrayList<>();
				List<Object> new_count_array = new ArrayList<>();
				List<Object> re_count_array = new ArrayList<>();
				List<Object> pv_array = new ArrayList<>();

				Dm_web_log_vo webLogVO = new Dm_web_log_vo();
				webLogVO.setDm_domain(domainId);
				webLogVO.setSearch_start_date(visitVO.getSearch_start_date());
				webLogVO.setSearch_end_date(visitVO.getSearch_end_date());

				for (int i = 0; i < 7; i++) {
					Map<String, Object> map = new HashMap<>();
					webLogVO.setDay_num(i+1);
					int visitor_count = webLogService.selectWebLogVisitorDayCount(webLogVO);
					int visit_count = webLogService.selectWebLogVisitDayCount(webLogVO);
					int new_visitor_count = webLogService.selectWebLogNewVisitDayCount(webLogVO);
					int re_visitor_count = webLogService.selectWebLogReVisitDayCount(webLogVO);

					double pv = 0;
					if(visit_count != 0 && visitor_count != 0) {
						pv = Math.ceil(visit_count/visitor_count);
					}
					map.put("dm_date", day_arr[i]);
					map.put("dm_visitor", visitor_count);
					map.put("visit_count", visit_count);
					map.put("new_count", new_visitor_count);
					map.put("re_count", re_visitor_count);
					map.put("pv", pv);
					list.add(map);

					visitor_array.add(visitor_count);
					visit_count_array.add(visit_count);
					new_count_array.add(new_visitor_count);
					re_count_array.add(re_visitor_count);
					pv_array.add(pv);
				}

				if("day_of_week_accessor_chart".equals(mode)) {
					result.put("visitor", visitor_array);
					result.put("visit_count", visit_count_array);
					result.put("new_count", new_count_array);
					result.put("re_count", re_count_array);
					result.put("pv", pv_array);
				} else if("day_of_week_accessor_table".equals(mode)) {
					result.put("rows", list);
					result.put("total", list.size());
				}

			} else if("week".equals(mode)) {
				List<Object> date_arr = new ArrayList<>();
				List<Object> visitor_arr = new ArrayList<>();
				List<Object> visit_count_arr = new ArrayList<>();
				List<Object> new_count_arr = new ArrayList<>();
				List<Object> re_count_arr = new ArrayList<>();
				List<Object> pv_arr = new ArrayList<>();
				List<Object> list = new ArrayList<>();

				Dm_web_log_vo webLogVO = new Dm_web_log_vo();
				webLogVO.setDm_domain(domainId);
				webLogVO.setSearch_start_date(visitVO.getSearch_start_date());
				webLogVO.setSearch_end_date(visitVO.getSearch_end_date());
				List<Dm_web_log_vo> chart_list = webLogService.selectWebLogPvChart(webLogVO);

				for (Dm_web_log_vo chart : chart_list) {
					int visit_count = chart.getLog_ip_pv_count();
					int pv_count = chart.getLog_pv_count();
					int new_pv_count = chart.getLog_new_pv_count();
					int re_pv_count = chart.getLog_re_pv_count();
					String day = chart.getDm_datetime();

					double pv = 0;
					if(visit_count != 0 && pv_count != 0) {
						 pv = Math.ceil(pv_count/visit_count);
					}
					date_arr.add(day);
					visitor_arr.add(visit_count);
					visit_count_arr.add(pv_count);
					new_count_arr.add(new_pv_count);
					re_count_arr.add(re_pv_count);
					pv_arr.add(pv);

					Map<String, Object> map = new HashMap<>();
					map.put("dm_date", day);
					map.put("dm_visitor", visit_count);
					map.put("visit_count", pv_count);
					map.put("new_count", new_pv_count);
					map.put("re_count", re_pv_count);
					map.put("pv", pv);

					list.add(map);
				}

				result.put("date", date_arr);
				result.put("visitor", visitor_arr);
				result.put("visit_count", visit_count_arr);
				result.put("new_count", new_count_arr);
				result.put("re_count", re_count_arr);
				result.put("pv", pv_arr);

				result.put("rows", list);
				result.put("total", list.size());
			} else if("month_accessor".equals(mode)) {
				visitVO.setDm_domain(domainId);
				visitVO.setSearch_type("yearMonth");
				int max_count = visitService.selectCountMaxTotalAccess(visitVO);
				visitVO.setDm_count(max_count);
				List<Dm_visit_vo> max_list = visitService.selectYearMonthTotalAccess(visitVO);

				int min_count = visitService.selectCountMinTotalAccess(visitVO);
				visitVO.setDm_count(min_count);
				List<Dm_visit_vo> min_list = visitService.selectYearMonthTotalAccess(visitVO);

				Dm_web_log_vo webLogVO = new Dm_web_log_vo();
				webLogVO.setSearch_start_date(visitVO.getSearch_start_date());
				webLogVO.setSearch_end_date(visitVO.getSearch_end_date());
				webLogVO.setDm_domain(domainId);
				webLogVO.setSearch_type("yearMonth");

				int max_pv_count = webLogService.selectWebLogMaxPvCount(webLogVO);
				webLogVO.setPv_count(max_pv_count);
				Dm_web_log_vo max_log_vo = webLogService.selectWebLogPvYearMonth(webLogVO);

				int min_pv_count = webLogService.selectWebLogMinPvCount(webLogVO);
				webLogVO.setPv_count(min_pv_count);
				Dm_web_log_vo min_log_vo = webLogService.selectWebLogPvYearMonth(webLogVO);

				double minus_count = 0;
				if(max_count != 0 && min_count != 0) {
					minus_count = max_count - min_count;
				}
				double minus_pv = 0;
				if(max_pv_count != 0 && min_pv_count != 0) {
					minus_pv = max_pv_count - min_pv_count;
				}
				result.put("max_count", max_count);
				result.put("max_date", max_list.get(0).getDm_datetime());
				result.put("min_count", min_count);
				result.put("min_date", min_list.get(0).getDm_datetime());
				result.put("min_pv_count", min_pv_count);
				result.put("min_pv_date", min_log_vo != null ? min_log_vo.getDm_datetime() : "");
				result.put("max_pv_count", max_pv_count);
				result.put("max_pv_date", max_log_vo != null ? max_log_vo.getDm_datetime() : "");
				result.put("minus_count", minus_count);
				result.put("minus_pv", minus_pv);

			} else if("month_accessor_chart".equals(mode) || "month_accessor_table".equals(mode) ) {
				visitVO.setDm_domain(domainId);
				
				List<Dm_visit_vo> month_list = visitService.selectYearMonthList(visitVO);

				if(month_list != null && month_list.size() > 0) {
					
					List<Object> visitor_array = new ArrayList<>();
					List<Object> visit_count_array = new ArrayList<>();
					List<Object> new_count_array = new ArrayList<>();
					List<Object> re_count_array = new ArrayList<>();
					List<Object> pv_array = new ArrayList<>();
					List<Object> yearMonth = new ArrayList<>();
					List<Object> list = new ArrayList<>();
					
					for (Dm_visit_vo list_vo: month_list) {
						Dm_web_log_vo webLogVO = new Dm_web_log_vo();
						webLogVO.setDm_domain(domainId);
						webLogVO.setSearch_type("yearMonth");
						webLogVO.setSearch_start_date(list_vo.getDm_datetime());
						webLogVO.setSearch_end_date(list_vo.getDm_datetime());

						int visitor_count = webLogService.selectWebLogIpPv(webLogVO);
						int visit_count = webLogService.selectWebLogPv(webLogVO);
						int new_count = webLogService.selectWebLogNewPv(webLogVO);
						int re_count = webLogService.selectWebLogRePv(webLogVO);

						double pv = 0;
						if(visit_count != 0 && visitor_count != 0) {
							pv = Math.ceil(visit_count/visitor_count);
						}

						yearMonth.add(list_vo.getDm_datetime());
						visitor_array.add(visitor_count);
						visit_count_array.add(visit_count);
						new_count_array.add(new_count);
						re_count_array.add(re_count);
						pv_array.add(pv);

						Map<String, Object> map = new HashMap<>();
						map.put("dm_date", list_vo.getDm_datetime());
						map.put("dm_visitor", visitor_count);
						map.put("visit_count", visit_count);
						map.put("new_count", new_count);
						map.put("re_count", re_count);
						map.put("pv", pv);
						list.add(map);
					}

					if("month_accessor_chart".equals(mode)) {
						result.put("date", yearMonth);
						result.put("visitor", visitor_array);
						result.put("visit_count", visit_count_array);
						result.put("new_count", new_count_array);
						result.put("re_count", re_count_array);
						result.put("pv", pv_array);
					} else if("month_accessor_table".equals(mode)) {
						result.put("rows", list);
						result.put("total", list.size());
					}
				}
			} else if("select_accessor".equals(mode)) {
				List<Object> list = new ArrayList<>();
				
				for (int i = 0; i < 24; i++) {
					Map<String, Object> map = new HashMap<>();
					String date_time = "";
					if(i < 10) {
						date_time = "0" + i;
					} else {
						date_time = Integer.toString(i);
					}
					visitVO.setDm_datetime(date_time);
					visitVO.setDm_domain(domainId);
					Dm_visit_vo result_vo = visitService.selectTimeVisitAccess(visitVO);
					if(result_vo != null) {
						map.put("dm_date", result_vo.getDm_date() != null ? result_vo.getDm_date() : date_time);
						map.put("dm_visit_count", result_vo.getDm_visit_count());
						map.put("dm_re_visit_count", result_vo.getDm_re_visit_count());
						map.put("dm_new_visit_count", result_vo.getDm_new_visit_count());
						map.put("dm_page_view_count", result_vo.getDm_page_view_count());
					} else {
						map.put("dm_date", date_time+":00");
						map.put("dm_visit_count", 0);
						map.put("dm_re_visit_count", 0);
						map.put("dm_new_visit_count", 0);
						map.put("dm_page_view_count", 0);
					}
					list.add(map);
				}
				result.put("rows", list);
				result.put("total", list.size());
			} else if("dash_board".equals(mode)) {
				String search_type = visitVO.getSearch_type() != null ? visitVO.getSearch_type().trim() : "";
				
				if("total".equals(search_type)) {
					int today_count = visitService.selectTodayTotalAccess(visitVO);
					
					calendar.add(Calendar.DATE, -1);
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					String yesterday = format.format(calendar.getTime());
					
					visitVO.setSearch_start_date(yesterday);
					visitVO.setSearch_end_date(yesterday);
					int yesterday_count = visitService.selectTodayTotalAccess(visitVO);
					result.put("today_visitor", today_count);
					
					if(today_count > 0 && yesterday_count > 0) {
						result.put("today_compare", Math.ceil((double)today_count / yesterday_count * 100));
					} else if (today_count > 0 && yesterday_count < 1) {
						result.put("today_compare", 100);						
					} else {
						result.put("today_compare", 0);
					}
				} else if("total_visit".equals(search_type)) {
					
					result.put("total_visit", visitService.selectTotalAccess(visitVO));
					
				} else if("today".equals(search_type)) {
					visitVO.setSearch_start_date(today);
					visitVO.setSearch_end_date(today);
					int dm_visit_count = 0;
					int dm_re_visit_count = 0;
					int dm_new_visit_count = 0;
					List<Dm_visit_vo> list = visitService.selectStatisticsCount(visitVO);
					for (Dm_visit_vo count_vo : list) {
						dm_visit_count += count_vo.getDm_visit_count();
						dm_re_visit_count += count_vo.getDm_re_visit_count();
						dm_new_visit_count += count_vo.getDm_new_visit_count();
					}
					double result_re_visit_count = 0.00;
					double result_new_visit_count = 0.00;
					if(dm_visit_count > 0) {
						result_re_visit_count = Math.ceil((double)dm_re_visit_count / dm_visit_count * 100);
						result_new_visit_count = Math.ceil((double)dm_new_visit_count / dm_visit_count * 100);
					}

					result.put("search_date", today);
					result.put("dm_visit_count", dm_visit_count);
					result.put("dm_re_visit_count", result_re_visit_count);
					result.put("dm_new_visit_count", result_new_visit_count);
				} else if("weekend".equals(search_type)) {
					calendar.add(Calendar.DATE, -6);
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					String start_date = format.format(calendar.getTime());
					visitVO.setSearch_start_date(start_date);
					visitVO.setSearch_end_date(today);
					int dm_visit_count = 0;
					int dm_re_visit_count = 0;
					int dm_new_visit_count = 0;

					List<Dm_visit_vo> list = visitService.selectStatisticsCount(visitVO);
					for (Dm_visit_vo count_vo : list) {
						dm_visit_count += count_vo.getDm_visit_count();
						dm_re_visit_count += count_vo.getDm_re_visit_count();
						dm_new_visit_count += count_vo.getDm_new_visit_count();
					}

					double result_re_visit_count = 0.00;
					double result_new_visit_count = 0.00;
					if(dm_visit_count > 0) {
						result_re_visit_count = Math.ceil((double)dm_re_visit_count / dm_visit_count * 100);
						result_new_visit_count = Math.ceil((double)dm_new_visit_count / dm_visit_count * 100);
					}

					result.put("search_date", start_date + "~" + today);
					result.put("dm_visit_count", dm_visit_count);
					result.put("dm_re_visit_count", result_re_visit_count);
					result.put("dm_new_visit_count", result_new_visit_count);
				} else if("last_week".equals(search_type)) {
					Dm_visit_vo dm_visit_vo = new Dm_visit_vo();

					calendar.add(Calendar.DATE, -7);
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DATE, -13);
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					String start_date = format.format(cal.getTime());
					String end_date = format.format(calendar.getTime());

					dm_visit_vo.setSearch_start_date(start_date);
					dm_visit_vo.setSearch_end_date(end_date);
					dm_visit_vo.setDm_domain(domainId);
					
					int dm_visit_count = 0;
					int dm_re_visit_count = 0;
					int dm_new_visit_count = 0;
					List<Dm_visit_vo> list = visitService.selectStatisticsCount(dm_visit_vo);
					for (Dm_visit_vo count_vo : list) {
						dm_visit_count += count_vo.getDm_visit_count();
						dm_re_visit_count += count_vo.getDm_re_visit_count();
						dm_new_visit_count += count_vo.getDm_new_visit_count();
					}

					double result_re_visit_count = 0.00;
					double result_new_visit_count = 0.00;
					if(dm_visit_count > 0) {
						result_re_visit_count = Math.ceil((double)dm_re_visit_count / dm_visit_count * 100);
						result_new_visit_count = Math.ceil((double)dm_new_visit_count / dm_visit_count * 100);
					}

					result.put("search_date", start_date + "~" + end_date);
					result.put("dm_visit_count", dm_visit_count);
					result.put("dm_re_visit_count", result_re_visit_count);
					result.put("dm_new_visit_count", result_new_visit_count);
				}
			} else {
				result.put("result", "fail");
				result.put("notice", "통계 정보가 없습니다.");
			}
			
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
