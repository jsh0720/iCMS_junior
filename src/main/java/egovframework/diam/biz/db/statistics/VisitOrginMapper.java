package egovframework.diam.biz.db.statistics;

import java.util.List;

import egovframework.diam.biz.model.statistics.Dm_visit_orgin_vo;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("visitOrginMapper")
public interface VisitOrginMapper {
	
	public void insertVisitOrgin (Dm_visit_orgin_vo vo);
	public int checkVisitOrgin (Dm_visit_orgin_vo vo);
	public void updateVisitOrgin (Dm_visit_orgin_vo vo);
	
	public int selectVisitOrginTotalCount (Dm_visit_orgin_vo vo);
	public int selectVisitOrginArrEngineCount (Dm_visit_orgin_vo vo);
	public int selectVisitOrginArrNotEngineCount (Dm_visit_orgin_vo vo);
	public int selectVisitOrginEngineCount (Dm_visit_orgin_vo vo);
	public List<Dm_visit_orgin_vo> selectVisitOrginDateCountList (Dm_visit_orgin_vo vo);
	public List<Dm_visit_orgin_vo> selectVisitOrginEtcDatecountList (Dm_visit_orgin_vo vo);

}
