package egovframework.diam.biz.service.statistics.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.diam.biz.db.statistics.VisitMapper;
import egovframework.diam.biz.model.statistics.Dm_visit_vo;
import egovframework.diam.biz.service.statistics.VisitService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("visitService")
public class VisitServiceImpl extends EgovAbstractServiceImpl implements VisitService{
	
	@Resource(name="visitMapper")
	private VisitMapper visitMapper;
	
	@Override
	public int selectTodayTotalAccess(Dm_visit_vo vo) throws Exception {
		return visitMapper.selectTodayTotalAccess(vo);
	}

	@Override
	public int selectCountMaxTotalAccess(Dm_visit_vo vo) throws Exception {
		return visitMapper.selectCountMaxTotalAccess(vo);
	}

	@Override
	public int selectCountMinTotalAccess(Dm_visit_vo vo) throws Exception {
		return visitMapper.selectCountMinTotalAccess(vo);
	}

	@Override
	public List<Dm_visit_vo> selectDatetimeTotalAccess(Dm_visit_vo vo) throws Exception {
		return visitMapper.selectDatetimeTotalAccess(vo);
	}

	@Override
	public String selectVisitDayName(Dm_visit_vo vo) throws Exception {
		return visitMapper.selectVisitDayName(vo);
	}

	@Override
	public List<Dm_visit_vo> selectYearMonthTotalAccess(Dm_visit_vo vo) throws Exception {
		return visitMapper.selectYearMonthTotalAccess(vo);
	}

	@Override
	public List<Dm_visit_vo> selectYearMonthList(Dm_visit_vo vo) throws Exception {
		return visitMapper.selectYearMonthList(vo);
	}

	@Override
	public Dm_visit_vo selectTimeVisitAccess(Dm_visit_vo vo) throws Exception {
		return visitMapper.selectTimeVisitAccess(vo);
	}

	@Override
	public int selectTotalAccess(Dm_visit_vo vo) throws Exception {
		return visitMapper.selectTotalAccess(vo);
	}

	@Override
	public List<Dm_visit_vo> selectStatisticsCount(Dm_visit_vo vo) throws Exception {
		return visitMapper.selectStatisticsCount(vo);
	}

	@Override
	public int checktVisit(Dm_visit_vo vo) throws Exception {
		return visitMapper.checktVisit(vo);
	}

	@Override
	public void insertVisit(Dm_visit_vo vo) throws Exception {
		visitMapper.insertVisit(vo);
	}

	@Override
	public void updateCountVisit(Dm_visit_vo vo) throws Exception {
		visitMapper.updateCountVisit(vo);
	}
	
}
