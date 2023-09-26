package egovframework.diam.biz.service.statistics;

import java.util.List;

import egovframework.diam.biz.model.statistics.Dm_visit_orgin_vo;

public interface VisitOrginService {
	
	public void insertVisitOrgin (Dm_visit_orgin_vo vo) throws Exception;
	public int checkVisitOrgin (Dm_visit_orgin_vo vo) throws Exception;
	public void updateVisitOrgin (Dm_visit_orgin_vo vo) throws Exception;
	
	public int selectVisitOrginTotalCount (Dm_visit_orgin_vo vo) throws Exception;
	public int selectVisitOrginArrEngineCount (Dm_visit_orgin_vo vo) throws Exception;
	public int selectVisitOrginArrNotEngineCount (Dm_visit_orgin_vo vo) throws Exception;
	public int selectVisitOrginEngineCount (Dm_visit_orgin_vo vo) throws Exception;
	public List<Dm_visit_orgin_vo> selectVisitOrginDateCountList (Dm_visit_orgin_vo vo) throws Exception;
	public List<Dm_visit_orgin_vo> selectVisitOrginEtcDatecountList (Dm_visit_orgin_vo vo) throws Exception;

}
