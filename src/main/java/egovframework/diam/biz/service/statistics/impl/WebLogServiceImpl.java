package egovframework.diam.biz.service.statistics.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.diam.biz.db.statistics.WebLogMapper;
import egovframework.diam.biz.model.statistics.Dm_web_log_vo;
import egovframework.diam.biz.service.statistics.WebLogService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("webLogService")
public class WebLogServiceImpl extends EgovAbstractServiceImpl implements WebLogService{
	
	@Resource(name="webLogMapper")
	private WebLogMapper webLogMapper;

	@Override
	public int selectWebLogTotalPvCount(Dm_web_log_vo vo) throws Exception {
		return webLogMapper.selectWebLogTotalPvCount(vo);
	}

	@Override
	public int selectWebLogMaxPvCount(Dm_web_log_vo vo) throws Exception {
		return webLogMapper.selectWebLogMaxPvCount(vo);
	}

	@Override
	public int selectWebLogMinPvCount(Dm_web_log_vo vo) throws Exception {
		return webLogMapper.selectWebLogMinPvCount(vo);
	}

	@Override
	public Dm_web_log_vo selectWebLogPvDatetime(Dm_web_log_vo vo) throws Exception {
		return webLogMapper.selectWebLogPvDatetime(vo);
	}

	@Override
	public int selectWebLogIpPv(Dm_web_log_vo vo) throws Exception {
		return webLogMapper.selectWebLogIpPv(vo);
	}

	@Override
	public int selectWebLogPv(Dm_web_log_vo vo) throws Exception {
		return webLogMapper.selectWebLogPv(vo);
	}

	@Override
	public int selectWebLogNewPv(Dm_web_log_vo vo) throws Exception {
		return webLogMapper.selectWebLogNewPv(vo);
	}

	@Override
	public int selectWebLogRePv(Dm_web_log_vo vo) throws Exception {
		return webLogMapper.selectWebLogRePv(vo);
	}

	@Override
	public String selectWebLogDayName(Dm_web_log_vo vo) throws Exception {
		return webLogMapper.selectWebLogDayName(vo);
	}

	@Override
	public int selectWebLogVisitorDayCount(Dm_web_log_vo vo) throws Exception {
		return webLogMapper.selectWebLogVisitorDayCount(vo);
	}

	@Override
	public int selectWebLogVisitDayCount(Dm_web_log_vo vo) throws Exception {
		return webLogMapper.selectWebLogVisitDayCount(vo);
	}

	@Override
	public int selectWebLogNewVisitDayCount(Dm_web_log_vo vo) throws Exception {
		return webLogMapper.selectWebLogNewVisitDayCount(vo);
	}

	@Override
	public int selectWebLogReVisitDayCount(Dm_web_log_vo vo) throws Exception {
		return webLogMapper.selectWebLogReVisitDayCount(vo);
	}

	@Override
	public List<Dm_web_log_vo> selectWebLogPvChart(Dm_web_log_vo vo) throws Exception {
		return webLogMapper.selectWebLogPvChart(vo);
	}

	@Override
	public Dm_web_log_vo selectWebLogPvYearMonth(Dm_web_log_vo vo) throws Exception {
		return webLogMapper.selectWebLogPvYearMonth(vo);
	}

	@Override
	public int selectWebLogIp(Dm_web_log_vo vo) throws Exception {
		return webLogMapper.selectWebLogIp(vo);
	}

	@Override
	public void insertWebLog(Dm_web_log_vo vo) throws Exception {
		webLogMapper.insertWebLog(vo);
		
	}
	
}
