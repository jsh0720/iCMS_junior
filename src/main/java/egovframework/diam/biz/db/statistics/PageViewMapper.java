package egovframework.diam.biz.db.statistics;

import java.util.List;

import egovframework.diam.biz.model.statistics.Dm_page_view_vo;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("pageViewMapper")
public interface PageViewMapper {
	
	public List<Dm_page_view_vo> selectPageViewLogPvStatisticsList(Dm_page_view_vo vo);
}
