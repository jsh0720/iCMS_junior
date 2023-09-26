package egovframework.diam.biz.service.statistics;

import java.util.List;

import egovframework.diam.biz.model.statistics.Dm_visit_env_vo;

public interface VisitEnvService {
	
	public void insertVisitEnv (Dm_visit_env_vo vo) throws Exception;
	public int checkVisitEnv (Dm_visit_env_vo vo) throws Exception;
	public void updateVisitEnv (Dm_visit_env_vo vo) throws Exception;
	
	public int selectVisitEnvTypeCount (Dm_visit_env_vo vo) throws Exception;
	public Dm_visit_env_vo selectVisitEnvMaxType (Dm_visit_env_vo vo) throws Exception;
	public List<Dm_visit_env_vo> selectVisitEnvOsList (Dm_visit_env_vo vo) throws Exception;
	public List<Dm_visit_env_vo> selectVisitEnvDateCountList (Dm_visit_env_vo vo) throws Exception;
	public int selectVisitEnvCount (Dm_visit_env_vo vo) throws Exception;
	public List<Dm_visit_env_vo> selectVisitEnvBrowserList (Dm_visit_env_vo vo) throws Exception;
	public int selectVisitEnvBrowerCount (Dm_visit_env_vo vo) throws Exception;
	public List<Dm_visit_env_vo> selectVisitEnvBrowerDateCountList (Dm_visit_env_vo vo) throws Exception;

}
