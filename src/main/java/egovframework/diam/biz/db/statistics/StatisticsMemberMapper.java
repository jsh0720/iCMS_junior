package egovframework.diam.biz.db.statistics;

import java.util.List;

import egovframework.diam.biz.model.statistics.Dm_statistics_member_vo;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("statisticsMemberMapper")
public interface StatisticsMemberMapper {

	public List<Dm_statistics_member_vo> selectMemberStatisticsNewMember(Dm_statistics_member_vo vo);
	public Dm_statistics_member_vo selectMemberStatisticsMaxNewMember(Dm_statistics_member_vo vo);
	public Dm_statistics_member_vo selectMemberStatisticsMinNewMember(Dm_statistics_member_vo vo);
	public Dm_statistics_member_vo selectMemberStatisticsTimeNewMember(Dm_statistics_member_vo vo);
	public Dm_statistics_member_vo selectMemberStatisticsWeekNewMember(Dm_statistics_member_vo vo);
	public Dm_statistics_member_vo selectMemberStatisticsMonthNewMember(Dm_statistics_member_vo vo);
	public Dm_statistics_member_vo selectMemberStatisticsMaxMonthNewMember(Dm_statistics_member_vo vo);
	public Dm_statistics_member_vo selectMemberStatisticsMinMonthNewMember(Dm_statistics_member_vo vo);
	public List<String> selectMemberStatisticsMonthList(Dm_statistics_member_vo vo);
	public Dm_statistics_member_vo selectMemberStatisticsAllDayMember(Dm_statistics_member_vo vo);
	public List<Dm_statistics_member_vo> selectMemberStatisticsAllDayMemberChart(Dm_statistics_member_vo vo);
	public Dm_statistics_member_vo selectMemberStatisticsAllMonthMemberChart(Dm_statistics_member_vo vo);
	public int selectMemberCntExceptAdmin(Dm_statistics_member_vo vo);
	public int selectMemberStatisticsCount(Dm_statistics_member_vo vo);
	public List<Dm_statistics_member_vo> selectMemberStatisticsNewMemberMonthChart(Dm_statistics_member_vo vo);
	public Dm_statistics_member_vo selectMemberStatisticsAllMonthMember(Dm_statistics_member_vo vo);
	public List<Dm_statistics_member_vo> selectMemberStatisticsAllMemberMonthChart(Dm_statistics_member_vo vo);
	public List<Dm_statistics_member_vo> selectMemberStatisticsWeeklyMember(Dm_statistics_member_vo vo);
}
