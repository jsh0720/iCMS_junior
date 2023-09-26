package egovframework.diam.biz.service.statistics;

import java.util.List;

import egovframework.diam.biz.model.statistics.Dm_web_log_vo;

public interface WebLogService {
	public int selectWebLogTotalPvCount (Dm_web_log_vo vo) throws Exception;
	public int selectWebLogMaxPvCount (Dm_web_log_vo vo) throws Exception;
	public int selectWebLogMinPvCount (Dm_web_log_vo vo) throws Exception;
	
	public Dm_web_log_vo selectWebLogPvDatetime (Dm_web_log_vo vo) throws Exception;
	
	public int selectWebLogIpPv (Dm_web_log_vo vo) throws Exception;
	public int selectWebLogPv (Dm_web_log_vo vo) throws Exception;
	public int selectWebLogNewPv (Dm_web_log_vo vo) throws Exception;
	public int selectWebLogRePv (Dm_web_log_vo vo) throws Exception;
	
	public String selectWebLogDayName (Dm_web_log_vo vo) throws Exception;
	
	public int selectWebLogVisitorDayCount (Dm_web_log_vo vo) throws Exception;
	public int selectWebLogVisitDayCount (Dm_web_log_vo vo) throws Exception;
	public int selectWebLogNewVisitDayCount (Dm_web_log_vo vo) throws Exception;
	public int selectWebLogReVisitDayCount (Dm_web_log_vo vo) throws Exception;
	
	public List<Dm_web_log_vo> selectWebLogPvChart (Dm_web_log_vo vo) throws Exception;
	
	public Dm_web_log_vo selectWebLogPvYearMonth (Dm_web_log_vo vo) throws Exception;
	
	public int selectWebLogIp (Dm_web_log_vo vo) throws Exception;
	
	public void insertWebLog (Dm_web_log_vo vo) throws Exception;
}
