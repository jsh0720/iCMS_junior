package egovframework.diam.biz.service.statistics;

import java.util.List;

import egovframework.diam.biz.model.statistics.Dm_page_view_vo;

public interface PageViewService {
	
	public List<Dm_page_view_vo> selectPageViewLogPvStatisticsList(Dm_page_view_vo vo) throws Exception;
}
