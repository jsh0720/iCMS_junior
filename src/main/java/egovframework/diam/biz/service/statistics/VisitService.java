package egovframework.diam.biz.service.statistics;

import java.util.List;

import egovframework.diam.biz.model.statistics.Dm_visit_vo;

public interface VisitService {
	
	public int selectTodayTotalAccess (Dm_visit_vo vo) throws Exception;
	
	public int selectCountMaxTotalAccess (Dm_visit_vo vo) throws Exception;
	
	public int selectCountMinTotalAccess (Dm_visit_vo vo) throws Exception;
	
	public List<Dm_visit_vo> selectDatetimeTotalAccess (Dm_visit_vo vo) throws Exception;
	
	public String selectVisitDayName (Dm_visit_vo vo) throws Exception;
	
	public List<Dm_visit_vo> selectYearMonthTotalAccess (Dm_visit_vo vo) throws Exception;
	
	public List<Dm_visit_vo> selectYearMonthList (Dm_visit_vo vo) throws Exception;
	
	public Dm_visit_vo selectTimeVisitAccess (Dm_visit_vo vo) throws Exception;
	
	public int selectTotalAccess(Dm_visit_vo vo) throws Exception;
	
	public List<Dm_visit_vo> selectStatisticsCount (Dm_visit_vo vo) throws Exception;
	
	public int checktVisit (Dm_visit_vo vo) throws Exception;
	
	public void insertVisit (Dm_visit_vo vo) throws Exception;
	
	public void updateCountVisit (Dm_visit_vo vo) throws Exception;

}
