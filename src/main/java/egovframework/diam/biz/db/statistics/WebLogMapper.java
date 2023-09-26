package egovframework.diam.biz.db.statistics;

import java.util.List;

import egovframework.diam.biz.model.statistics.Dm_web_log_vo;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("webLogMapper")
public interface WebLogMapper {
	public int selectWebLogTotalPvCount (Dm_web_log_vo vo);
	public int selectWebLogMaxPvCount (Dm_web_log_vo vo);
	public int selectWebLogMinPvCount (Dm_web_log_vo vo);
	
	public Dm_web_log_vo selectWebLogPvDatetime (Dm_web_log_vo vo);
	
	public int selectWebLogIpPv (Dm_web_log_vo vo);
	public int selectWebLogPv (Dm_web_log_vo vo);
	public int selectWebLogNewPv (Dm_web_log_vo vo);
	public int selectWebLogRePv (Dm_web_log_vo vo);
	
	public String selectWebLogDayName (Dm_web_log_vo vo);
	
	public int selectWebLogVisitorDayCount (Dm_web_log_vo vo);
	public int selectWebLogVisitDayCount (Dm_web_log_vo vo);
	public int selectWebLogNewVisitDayCount (Dm_web_log_vo vo);
	public int selectWebLogReVisitDayCount (Dm_web_log_vo vo);
	
	public List<Dm_web_log_vo> selectWebLogPvChart (Dm_web_log_vo vo);
	
	public Dm_web_log_vo selectWebLogPvYearMonth (Dm_web_log_vo vo);
	
	public int selectWebLogIp (Dm_web_log_vo vo);
	
	public void insertWebLog (Dm_web_log_vo vo);
}
