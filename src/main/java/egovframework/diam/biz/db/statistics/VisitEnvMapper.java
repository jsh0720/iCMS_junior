package egovframework.diam.biz.db.statistics;

import java.util.List;

import egovframework.diam.biz.model.statistics.Dm_visit_env_vo;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("visitEnvMapper")
public interface VisitEnvMapper {
	public void insertVisitEnv (Dm_visit_env_vo vo);
	public int checkVisitEnv (Dm_visit_env_vo vo);
	public void updateVisitEnv (Dm_visit_env_vo vo);
	
	public int selectVisitEnvTypeCount (Dm_visit_env_vo vo);
	public Dm_visit_env_vo selectVisitEnvMaxType (Dm_visit_env_vo vo);
	public List<Dm_visit_env_vo> selectVisitEnvOsList (Dm_visit_env_vo vo);
	public List<Dm_visit_env_vo> selectVisitEnvDateCountList (Dm_visit_env_vo vo);
	public int selectVisitEnvCount (Dm_visit_env_vo vo);
	public List<Dm_visit_env_vo> selectVisitEnvBrowserList (Dm_visit_env_vo vo);
	public int selectVisitEnvBrowerCount (Dm_visit_env_vo vo);
	public List<Dm_visit_env_vo> selectVisitEnvBrowerDateCountList (Dm_visit_env_vo vo);
}
