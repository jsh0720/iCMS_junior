package egovframework.diam.ui.statistics;

import java.util.ArrayList;
import java.util.Collections;
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
import egovframework.diam.biz.model.statistics.Dm_visit_env_vo;
import egovframework.diam.biz.service.config.DomainService;
import egovframework.diam.biz.service.statistics.VisitEnvService;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
public class VisitEnvController {

	@Resource(name="visitEnvService")
	private VisitEnvService visitEnvService;
	
	@Resource(name="domainService")
	private DomainService domainService;
	
	@RequestMapping("/adm/get_visit_env.do")
	public ResponseEntity<?> get_visit_env(Dm_visit_env_vo visitEnvVO, @RequestParam(value="mode", required=true) String mode, 
			@RequestParam(value="search_domain", required=false) String search_domain) {
		
		String site_id = "";
		String search_start_date = visitEnvVO.getSearch_start_date();
		String search_end_date = visitEnvVO.getSearch_end_date();
		
		Map<String, Object> result = new HashMap<>();
		
		try {
			if (search_domain != null && !"".equals(search_domain)) {
				site_id = search_domain;
			} else {
				Dm_domain_list_vo domainVO = domainService.selectDomainMain();
				site_id = domainVO.getDm_id();
			}
			
			if("get_env".equals(mode)) {
				Dm_visit_env_vo dm_visit_env_vo = new Dm_visit_env_vo();
				dm_visit_env_vo.setDm_domain(site_id);
				dm_visit_env_vo.setSearch_start_date(search_start_date);
				dm_visit_env_vo.setSearch_end_date(search_end_date);
				/*dm_type = 1 :: 모바일*/
				dm_visit_env_vo.setDm_type("1");
				int mobile_count = visitEnvService.selectVisitEnvTypeCount(dm_visit_env_vo);
				/*dm_type = 0 :: pc*/
				dm_visit_env_vo.setDm_type("0");
				int pc_count = visitEnvService.selectVisitEnvTypeCount(dm_visit_env_vo);
				
				int total_count = mobile_count + pc_count;
				String pc_percent = "0.00";
				String mobile_percent = "0.00";
				if(total_count != 0 && pc_count != 0) {
					pc_percent = String.format("%.2f", ((double)pc_count/(double)total_count)*100);
				}
				if(total_count != 0 && mobile_count != 0) {
					mobile_percent = String.format("%.2f", ((double)mobile_count/(double)total_count)*100);
				}
				result.put("pc_percent", pc_percent);
				result.put("mobile_percent", mobile_percent);
				
				/*dm_type = 0 :: pc*/
				dm_visit_env_vo.setDm_type("0");
				Dm_visit_env_vo pc_vo = visitEnvService.selectVisitEnvMaxType(dm_visit_env_vo);
				/*dm_type = 1 :: 모바일*/
				dm_visit_env_vo.setDm_type("1");
				Dm_visit_env_vo mobile_vo = visitEnvService.selectVisitEnvMaxType(dm_visit_env_vo);
				if(pc_vo != null) {
					result.put("max_pc_count", pc_vo.getDm_count());
					result.put("max_pc_name", pc_vo.getDm_os());
				} else {
					result.put("max_pc_count", "0");
					result.put("max_pc_name", "");
				}
				if(mobile_vo != null) {
					result.put("max_mobile_count", mobile_vo.getDm_count());
					result.put("max_mobile_name", mobile_vo.getDm_os());
				} else {
					result.put("max_mobile_count", "0");
					result.put("max_mobile_name", "");
				}
			} else if("weekend_env".equals(mode)) {
				Dm_visit_env_vo dm_visit_env_vo = new Dm_visit_env_vo();
				dm_visit_env_vo.setDm_domain(site_id);
				dm_visit_env_vo.setSearch_start_date(search_start_date);
				dm_visit_env_vo.setSearch_end_date(search_end_date);
				
				List<Dm_visit_env_vo> os_list = visitEnvService.selectVisitEnvOsList(dm_visit_env_vo);
				List<Object> os_arr = new ArrayList<>();
				List<Object> date_arr = new ArrayList<>();
				for (int i = 0; i < os_list.size(); i++) {
					Map<String, Object> map = new HashMap<>();
					String os = os_list.get(i).getDm_os() != null ? os_list.get(i).getDm_os() : "";
					Dm_visit_env_vo vo = new Dm_visit_env_vo();
					vo.setDm_domain(site_id);
					vo.setSearch_start_date(search_start_date);
					vo.setSearch_end_date(search_end_date);
					vo.setDm_os(os);
					List<Object> count = new ArrayList<>();
					List<Dm_visit_env_vo> date_list = visitEnvService.selectVisitEnvDateCountList(vo);
					for (Dm_visit_env_vo date_vo : date_list) {
						count.add(date_vo.getDm_count());
						if(i==0) {
							date_arr.add(date_vo.getDm_datetime());
						}
					}
					map.put("count", count);
					map.put("os", os);
					os_arr.add(map);
				}
				result.put("os", os_arr);
				result.put("date", date_arr);
				
			} else if("weekend_env_table".equals(mode)) {
				List<Object> jlist = new ArrayList<>();
				Dm_visit_env_vo dm_visit_env_vo = new Dm_visit_env_vo();
				dm_visit_env_vo.setDm_domain(site_id);
				dm_visit_env_vo.setSearch_start_date(search_start_date);
				dm_visit_env_vo.setSearch_end_date(search_end_date);
				
				List<Dm_visit_env_vo> os_list = visitEnvService.selectVisitEnvOsList(dm_visit_env_vo);
				List<Map<String, Object>> list = new ArrayList<>();
				
				int total_count = 0;
				for (int i = 0; i < os_list.size(); i++) {
					String dm_os = os_list.get(i).getDm_os();
					dm_visit_env_vo.setDm_os(dm_os);
					int count = visitEnvService.selectVisitEnvCount(dm_visit_env_vo);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("count", count);
					map.put("dm_os", dm_os);
					list.add(map);
					total_count += count;
				}
				
				DescObj desc = new DescObj();
				Collections.sort(list,desc);
				
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = new HashMap<>();
					map.put("number", i+1);
					map.put("dm_os", list.get(i).get("dm_os"));
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
			} else if("weekend_env_browser".equals(mode)) {
				Dm_visit_env_vo dm_visit_env_vo = new Dm_visit_env_vo();
				dm_visit_env_vo.setDm_domain(site_id);
				dm_visit_env_vo.setSearch_start_date(search_start_date);
				dm_visit_env_vo.setSearch_end_date(search_end_date);
				
				List<Dm_visit_env_vo> browser_list = visitEnvService.selectVisitEnvBrowserList(dm_visit_env_vo);
				List<Object> brower_arr = new ArrayList<>();
				List<Object> date_arr = new ArrayList<>();
				for (int i = 0; i < browser_list.size(); i++) {
					Map<String, Object> map = new HashMap<>();
					String brower = browser_list.get(i).getDm_brower() != null ? browser_list.get(i).getDm_brower() : "";
					Dm_visit_env_vo vo = new Dm_visit_env_vo();
					vo.setDm_domain(site_id);
					vo.setSearch_start_date(search_start_date);
					vo.setSearch_end_date(search_end_date);
					vo.setDm_brower(brower);
					List<Object> count = new ArrayList<>();
					List<Dm_visit_env_vo> date_list = visitEnvService.selectVisitEnvBrowerDateCountList(vo);
					for (Dm_visit_env_vo date_vo : date_list) {
						count.add(date_vo.getDm_count());
						if(i==0) {
							date_arr.add(date_vo.getDm_datetime());
						}
					}
					map.put("count", count);
					map.put("brower", brower);
					brower_arr.add(map);
				}
				result.put("brower", brower_arr);
				result.put("date", date_arr);
			} else if("weekend_env_browser_table".equals(mode)) {
				List<Object> jlist = new ArrayList<>();
				Dm_visit_env_vo dm_visit_env_vo = new Dm_visit_env_vo();
				dm_visit_env_vo.setDm_domain(site_id);
				dm_visit_env_vo.setSearch_start_date(search_start_date);
				dm_visit_env_vo.setSearch_end_date(search_end_date);
				
				List<Dm_visit_env_vo> brower_list = visitEnvService.selectVisitEnvBrowserList(dm_visit_env_vo);
				List<Map<String, Object>> list = new ArrayList<>();
				
				int total_count = 0;
				for (int i = 0; i < brower_list.size(); i++) {
					String dm_brower = brower_list.get(i).getDm_brower();
					dm_visit_env_vo.setDm_brower(dm_brower);
					int count = visitEnvService.selectVisitEnvBrowerCount(dm_visit_env_vo);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("count", count);
					map.put("dm_brower", dm_brower);
					list.add(map);
					total_count += count;
				}
				
				DescObj desc = new DescObj();
				Collections.sort(list,desc);
				
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> item = new HashMap<>();
					item.put("number", i+1);
					item.put("brower", list.get(i).get("dm_brower"));
					item.put("count", list.get(i).get("count"));
					double percent = 0.0;
					if(total_count != 0 && list.get(i).get("count") != null && !"".equals(list.get(i).get("count")) && !"0".equals(list.get(i).get("count"))) {
						int count = Integer.parseInt(list.get(i).get("count").toString());
						percent = (double)count/(double)total_count*100;
					}
					item.put("percent", String.format("%.2f", percent));
					
					jlist.add(item);
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
