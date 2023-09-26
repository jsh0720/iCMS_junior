package egovframework.diam.biz.db.statistics;

import java.util.List;

import egovframework.diam.biz.model.statistics.Dm_visit_vo;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("visitMapper")
public interface VisitMapper {
	public int selectTodayTotalAccess (Dm_visit_vo vo);
	
	public int selectCountMaxTotalAccess (Dm_visit_vo vo);
	
	public int selectCountMinTotalAccess (Dm_visit_vo vo);
	
	public List<Dm_visit_vo> selectDatetimeTotalAccess (Dm_visit_vo vo);
	
	public String selectVisitDayName (Dm_visit_vo vo);
	
	public List<Dm_visit_vo> selectYearMonthTotalAccess (Dm_visit_vo vo);
	
	public List<Dm_visit_vo> selectYearMonthList (Dm_visit_vo vo);
	
	public Dm_visit_vo selectTimeVisitAccess (Dm_visit_vo vo);
	
	public int selectTotalAccess(Dm_visit_vo vo);
	
	public List<Dm_visit_vo> selectStatisticsCount (Dm_visit_vo vo);
	
	public int checktVisit (Dm_visit_vo vo);
	
	public void insertVisit (Dm_visit_vo vo);
	
	public void updateCountVisit (Dm_visit_vo vo);
}
