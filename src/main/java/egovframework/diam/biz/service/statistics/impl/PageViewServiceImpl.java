package egovframework.diam.biz.service.statistics.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.diam.biz.db.statistics.PageViewMapper;
import egovframework.diam.biz.model.statistics.Dm_page_view_vo;
import egovframework.diam.biz.service.statistics.PageViewService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("pageViewService")
public class PageViewServiceImpl extends EgovAbstractServiceImpl implements PageViewService{
	
	@Resource(name="pageViewMapper")
	private PageViewMapper pageViewMapper;

	@Override
	public List<Dm_page_view_vo> selectPageViewLogPvStatisticsList(Dm_page_view_vo vo) throws Exception {
		return pageViewMapper.selectPageViewLogPvStatisticsList(vo);
	}
	
}
