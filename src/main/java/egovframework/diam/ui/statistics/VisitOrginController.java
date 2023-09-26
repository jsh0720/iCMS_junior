package egovframework.diam.ui.statistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

import egovframework.diam.biz.model.config.Dm_domain_list_vo;
import egovframework.diam.biz.model.statistics.Dm_visit_orgin_vo;
import egovframework.diam.biz.service.config.DomainService;
import egovframework.diam.biz.service.statistics.VisitOrginService;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
public class VisitOrginController {
	
	@Resource(name="domainService")
	private DomainService domainService;
	
	@Resource(name="visitOrginService")
	private VisitOrginService visitOrginService;
	
	@RequestMapping("/adm/get_visit_orgin.do")
	public ResponseEntity<?> get_visit_orgin (Dm_visit_orgin_vo visitOrginVO, @RequestParam(value="mode", required=true) String mode, 
			@RequestParam(value="search_domain", required=false) String search_domain) {
		
		String site_id = "";
		Map<String, Object> result = new HashMap<>();
		try {
			if (search_domain != null && !"".equals(search_domain)) {
				site_id = search_domain;
			} else {
				Dm_domain_list_vo domainVO = domainService.selectDomainMain();
				site_id = domainVO.getDm_id();
			}
			
			if("orgin_info".equals(mode)) {
				String[] engineArr = {"naver","nate","daum","google","bing"};
				Dm_visit_orgin_vo dm_visit_orgin_vo = new Dm_visit_orgin_vo();
				dm_visit_orgin_vo.setSearch_start_date(visitOrginVO.getSearch_start_date());
				dm_visit_orgin_vo.setSearch_end_date(visitOrginVO.getSearch_end_date());
				dm_visit_orgin_vo.setDm_domain(site_id);
				int total_count = visitOrginService.selectVisitOrginTotalCount(dm_visit_orgin_vo);
				dm_visit_orgin_vo.setDm_orgin_engine(engineArr);
				int search_count = visitOrginService.selectVisitOrginArrEngineCount(dm_visit_orgin_vo);
				
				String search_percent = "0";
				if(total_count != 0 && search_count != 0) {
					double search_percent_f = ((double)search_count/(double)total_count)*100;
					search_percent = String.format("%.2f", search_percent_f);
				}
				
				dm_visit_orgin_vo.setDm_orgin("naver");
				int naver_count = visitOrginService.selectVisitOrginEngineCount(dm_visit_orgin_vo);
				dm_visit_orgin_vo.setDm_orgin("daum");
				int daum_count = visitOrginService.selectVisitOrginEngineCount(dm_visit_orgin_vo);
				dm_visit_orgin_vo.setDm_orgin("google");
				int google_count = visitOrginService.selectVisitOrginEngineCount(dm_visit_orgin_vo);
				dm_visit_orgin_vo.setDm_orgin("nate");
				int nate_count = visitOrginService.selectVisitOrginEngineCount(dm_visit_orgin_vo);
				dm_visit_orgin_vo.setDm_orgin("bing");
				int bing_count = visitOrginService.selectVisitOrginEngineCount(dm_visit_orgin_vo);
				int etc_count = visitOrginService.selectVisitOrginArrNotEngineCount(dm_visit_orgin_vo);
				
				List<Integer> percent = new ArrayList<Integer>();
				List<String> top_eng = new ArrayList<String>();
				percent.add(naver_count);
				percent.add(daum_count);
				percent.add(google_count);
				percent.add(nate_count);
				percent.add(bing_count);
				percent.add(etc_count);
				percent.sort(Comparator.reverseOrder());
				
				for (int i = 0; i < percent.size(); i++) {
					int temp = percent.get(i);
					if(total_count != 0 && temp != 0) {
						double top_eng_f = (double)temp/(double)total_count*100;
						top_eng.add(String.format("%.2f", top_eng_f));
					} else {
						top_eng.add("0");
					}
				}
				
				result.put("search_count", search_count);
				result.put("total_count", total_count);
				result.put("search_percent", search_percent);
				result.put("top_eng", top_eng.get(0));
				
			} else if("weekend_orgin".equals(mode)) {
				Dm_visit_orgin_vo dm_visit_orgin_vo = new Dm_visit_orgin_vo();
				dm_visit_orgin_vo.setSearch_start_date(visitOrginVO.getSearch_start_date());
				dm_visit_orgin_vo.setSearch_end_date(visitOrginVO.getSearch_end_date());
				dm_visit_orgin_vo.setDm_domain(site_id);
				dm_visit_orgin_vo.setDm_orgin("naver");
				List<Dm_visit_orgin_vo> naver_list = visitOrginService.selectVisitOrginDateCountList(dm_visit_orgin_vo);
				dm_visit_orgin_vo.setDm_orgin("nate");
				List<Dm_visit_orgin_vo> nate_list = visitOrginService.selectVisitOrginDateCountList(dm_visit_orgin_vo);
				dm_visit_orgin_vo.setDm_orgin("google");
				List<Dm_visit_orgin_vo> google_list = visitOrginService.selectVisitOrginDateCountList(dm_visit_orgin_vo);
				dm_visit_orgin_vo.setDm_orgin("daum");
				List<Dm_visit_orgin_vo> daum_list = visitOrginService.selectVisitOrginDateCountList(dm_visit_orgin_vo);
				dm_visit_orgin_vo.setDm_orgin("bing");
				List<Dm_visit_orgin_vo> bing_list = visitOrginService.selectVisitOrginDateCountList(dm_visit_orgin_vo);
				String[] engineArr = {"naver","nate","daum","google","bing"};
				dm_visit_orgin_vo.setDm_orgin_engine(engineArr);
				List<Dm_visit_orgin_vo> etc_list = visitOrginService.selectVisitOrginEtcDatecountList(dm_visit_orgin_vo);
				
				List<Object> date_time = new ArrayList<>();
				List<Object> naver = new ArrayList<>();
				List<Object> nate = new ArrayList<>();
				List<Object> google = new ArrayList<>();
				List<Object> daum = new ArrayList<>();
				List<Object> bing = new ArrayList<>();
				List<Object> etc = new ArrayList<>();
				
				for (int i = 0; i < naver_list.size(); i++) {
					date_time.add(naver_list.get(i).getDm_datetime());
					naver.add(naver_list.get(i).getDm_count());
					nate.add(nate_list.get(i).getDm_count());
					google.add(google_list.get(i).getDm_count());
					daum.add(daum_list.get(i).getDm_count());
					bing.add(bing_list.get(i).getDm_count());
					etc.add(etc_list.get(i).getDm_count());
				}
				
				result.put("date", date_time);
				result.put("naver", naver);
				result.put("nate", nate);
				result.put("google", google);
				result.put("daum", daum);
				result.put("bing", bing);
				result.put("etc", etc);
				
			} else if("all_orgin".equals(mode)) {
				List<Object> jlist = new ArrayList<>();
				Dm_visit_orgin_vo dm_visit_orgin_vo = new Dm_visit_orgin_vo();
				dm_visit_orgin_vo.setSearch_start_date(visitOrginVO.getSearch_start_date());
				dm_visit_orgin_vo.setSearch_end_date(visitOrginVO.getSearch_end_date());
				dm_visit_orgin_vo.setDm_domain(site_id);
				
				dm_visit_orgin_vo.setDm_orgin("naver");
				int naver_count = visitOrginService.selectVisitOrginEngineCount(dm_visit_orgin_vo);
				dm_visit_orgin_vo.setDm_orgin("daum");
				int daum_count = visitOrginService.selectVisitOrginEngineCount(dm_visit_orgin_vo);
				dm_visit_orgin_vo.setDm_orgin("google");
				int google_count = visitOrginService.selectVisitOrginEngineCount(dm_visit_orgin_vo);
				dm_visit_orgin_vo.setDm_orgin("nate");
				int nate_count = visitOrginService.selectVisitOrginEngineCount(dm_visit_orgin_vo);
				dm_visit_orgin_vo.setDm_orgin("bing");
				int bing_count = visitOrginService.selectVisitOrginEngineCount(dm_visit_orgin_vo);
				
				String[] engineArr = {"naver","nate","daum","google","bing"};
				dm_visit_orgin_vo.setDm_orgin_engine(engineArr);
				int etc_count = visitOrginService.selectVisitOrginArrNotEngineCount(dm_visit_orgin_vo);
				int total_count = visitOrginService.selectVisitOrginTotalCount(dm_visit_orgin_vo);
				
				List<Map<String, Object>> list = new ArrayList<>();
				Map<String, Object> item_map1 = new HashMap<String, Object>();
				Map<String, Object> item_map2 = new HashMap<String, Object>();
				Map<String, Object> item_map3 = new HashMap<String, Object>();
				Map<String, Object> item_map4 = new HashMap<String, Object>();
				Map<String, Object> item_map5 = new HashMap<String, Object>();
				Map<String, Object> item_map6 = new HashMap<String, Object>();
				item_map1.put("name", "naver");
				item_map1.put("count", naver_count);
				list.add(item_map1);
				item_map2.put("name", "daum");
				item_map2.put("count", daum_count);
				list.add(item_map2);
				item_map3.put("name", "google");
				item_map3.put("count", google_count);
				list.add(item_map3);
				item_map4.put("name", "nate");
				item_map4.put("count", nate_count);
				list.add(item_map4);
				item_map5.put("name", "bing");
				item_map5.put("count", bing_count);
				list.add(item_map5);
				item_map6.put("name", "etc");
				item_map6.put("count", etc_count);
				list.add(item_map6);
				
				DescObj desc = new DescObj();
				Collections.sort(list,desc);

				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = new HashMap<>();
					map.put("number", i+1);
					map.put("site", list.get(i).get("name"));
					map.put("count", list.get(i).get("count"));
					double percent = 0.0;
					if(total_count != 0 && list.get(i).get("count") != null && !"".equals(list.get(i).get("count")) && !"0".equals(list.get(i).get("count"))) {
						int count = Integer.parseInt(list.get(i).get("count").toString());
						percent = (double)count/(double)total_count*100;
					}
					map.put("percent", String.format("%.2f", percent));
					jlist.add(map);
				}
				result.put("rows", jlist);
				result.put("total", jlist.size());
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

class DescObj implements Comparator<Map<String, Object>> {
	@Override
	public int compare(Map<String, Object> o1, Map<String, Object> o2) {
		Integer t1 = Integer.parseInt(o2.get("count").toString());
		return t1.compareTo((Integer)o1.get("count"));
	}
}
