package egovframework.diam.biz.service.statistics.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.diam.biz.db.statistics.StatisticsMemberMapper;
import egovframework.diam.biz.model.statistics.Dm_statistics_member_vo;
import egovframework.diam.biz.service.statistics.StatisticsMemberService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("statisticsMemberService")
public class StatisticsMemberServiceImpl extends EgovAbstractServiceImpl implements StatisticsMemberService {
	
	@Resource(name="statisticsMemberMapper")
	private StatisticsMemberMapper statisticsMemberMapper;

	@Override
	public List<Dm_statistics_member_vo> selectMemberStatisticsNewMember(Dm_statistics_member_vo vo) throws Exception {
		return statisticsMemberMapper.selectMemberStatisticsNewMember(vo);
	}

	@Override
	public Dm_statistics_member_vo selectMemberStatisticsMaxNewMember(Dm_statistics_member_vo vo) throws Exception {
		return statisticsMemberMapper.selectMemberStatisticsMaxNewMember(vo);
	}

	@Override
	public Dm_statistics_member_vo selectMemberStatisticsMinNewMember(Dm_statistics_member_vo vo) throws Exception {
		return statisticsMemberMapper.selectMemberStatisticsMinNewMember(vo);
	}

	@Override
	public Dm_statistics_member_vo selectMemberStatisticsTimeNewMember(Dm_statistics_member_vo vo) throws Exception {
		return statisticsMemberMapper.selectMemberStatisticsTimeNewMember(vo);
	}

	@Override
	public Dm_statistics_member_vo selectMemberStatisticsWeekNewMember(Dm_statistics_member_vo vo) throws Exception {
		return statisticsMemberMapper.selectMemberStatisticsWeekNewMember(vo);
	}

	@Override
	public Dm_statistics_member_vo selectMemberStatisticsMonthNewMember(Dm_statistics_member_vo vo) throws Exception {
		return statisticsMemberMapper.selectMemberStatisticsMonthNewMember(vo);
	}

	@Override
	public Dm_statistics_member_vo selectMemberStatisticsMaxMonthNewMember(Dm_statistics_member_vo vo)
			throws Exception {
		return statisticsMemberMapper.selectMemberStatisticsMaxMonthNewMember(vo);
	}

	@Override
	public Dm_statistics_member_vo selectMemberStatisticsMinMonthNewMember(Dm_statistics_member_vo vo)
			throws Exception {
		return statisticsMemberMapper.selectMemberStatisticsMinMonthNewMember(vo);
	}

	@Override
	public List<String> selectMemberStatisticsMonthList(Dm_statistics_member_vo vo) throws Exception {
		return statisticsMemberMapper.selectMemberStatisticsMonthList(vo);
	}

	@Override
	public Dm_statistics_member_vo selectMemberStatisticsAllDayMember(Dm_statistics_member_vo vo) throws Exception {
		return statisticsMemberMapper.selectMemberStatisticsAllDayMember(vo);
	}

	@Override
	public List<Dm_statistics_member_vo> selectMemberStatisticsAllDayMemberChart(Dm_statistics_member_vo vo)
			throws Exception {
		return statisticsMemberMapper.selectMemberStatisticsAllDayMemberChart(vo);
	}

	@Override
	public Dm_statistics_member_vo selectMemberStatisticsAllMonthMemberChart(Dm_statistics_member_vo vo)
			throws Exception {
		return statisticsMemberMapper.selectMemberStatisticsAllMonthMemberChart(vo);
	}
	
	@Override
	public int selectMemberCntExceptAdmin(Dm_statistics_member_vo vo) throws Exception {
		return statisticsMemberMapper.selectMemberCntExceptAdmin(vo);
	}

	@Override
	public int selectMemberStatisticsCount(Dm_statistics_member_vo vo) throws Exception {
		return statisticsMemberMapper.selectMemberStatisticsCount(vo);
	}

	@Override
	public List<Dm_statistics_member_vo> selectMemberStatisticsNewMemberMonthChart(Dm_statistics_member_vo vo)
			throws Exception {
		return statisticsMemberMapper.selectMemberStatisticsNewMemberMonthChart(vo);
	}

	@Override
	public Dm_statistics_member_vo selectMemberStatisticsAllMonthMember(Dm_statistics_member_vo vo)
			throws Exception {
		return statisticsMemberMapper.selectMemberStatisticsAllMonthMember(vo);
	}

	@Override
	public List<Dm_statistics_member_vo> selectMemberStatisticsAllMemberMonthChart(Dm_statistics_member_vo vo)
			throws Exception {
		return statisticsMemberMapper.selectMemberStatisticsAllMemberMonthChart(vo);
	}

	@Override
	public List<Dm_statistics_member_vo> selectMemberStatisticsWeeklyMember(Dm_statistics_member_vo vo)
			throws Exception {
		return statisticsMemberMapper.selectMemberStatisticsWeeklyMember(vo);
	}
}
