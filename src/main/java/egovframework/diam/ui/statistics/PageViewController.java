package egovframework.diam.ui.statistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.diam.biz.model.config.Dm_domain_list_vo;
import egovframework.diam.biz.model.statistics.Dm_page_view_vo;
import egovframework.diam.biz.service.config.DomainService;
import egovframework.diam.biz.service.statistics.PageViewService;
import egovframework.diam.cmm.util.CommonUtil;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
public class PageViewController {
	
	@Resource(name="domainService")
	private DomainService domainService;
	
	@Resource(name="pageViewService")
	private PageViewService pageViewService;
	
	@RequestMapping("/adm/get_page_view.do")
	public ResponseEntity<?> get_page_view(Dm_page_view_vo pageViewVO, @RequestParam(value="mode", required=true) String mode, 
			@RequestParam(value="search_domain", required=false) String search_domain) {
		
		CommonUtil commonUtil = new CommonUtil();
		String site_id = "";
		
		Map<String, Object> result = new HashMap<>();
		
		try {
			if (!commonUtil.isNullOrEmpty(search_domain)) {
				site_id = search_domain;
			} else {
				Dm_domain_list_vo domainVO = domainService.selectDomainMain();
				site_id = domainVO.getDm_id();
			}
			
			if("page_view".equals(mode)) {
				pageViewVO.setDm_domain(site_id);
				List<Dm_page_view_vo> web_log_list = pageViewService.selectPageViewLogPvStatisticsList(pageViewVO);
				List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
				int total_count = 0;
				for (Dm_page_view_vo vo : web_log_list) {
					total_count += vo.getPage_count();
					Map<String, Object> item_map = new HashMap<String, Object>();
					item_map.put("count", vo.getPage_count());
					item_map.put("dm_fn_url", vo.getDm_fn_url());
					item_map.put("dm_type", vo.getDm_type());
					list.add(item_map);
				}
				DescObj desc = new DescObj();
				Collections.sort(list,desc);
				List<Map<String, Object>> rows = new ArrayList<>();
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = new HashMap<>();
					double percent = 0.0;
					if(total_count != 0 && !"0".equals(list.get(i).get("count"))) {
						int count = Integer.parseInt(list.get(i).get("count").toString());
						percent = (double)count/(double)total_count*100;
					}
					map.put("number", i+1);
					map.put("percent", String.format("%.2f", percent));
					map.put("dm_fn_url",list.get(i).get("dm_fn_url"));
					map.put("dm_type", list.get(i).get("dm_type"));
					map.put("page_count", list.get(i).get("count"));
					
					rows.add(map);
				}
				
				result.put("rows", rows);
				result.put("total", rows.size());
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
