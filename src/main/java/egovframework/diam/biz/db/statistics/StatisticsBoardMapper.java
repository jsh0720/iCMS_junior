package egovframework.diam.biz.db.statistics;

import java.util.List;

import egovframework.diam.biz.model.statistics.Dm_statistics_board_vo;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("statisticsBoardMapper")
public interface StatisticsBoardMapper {

	public List<Dm_statistics_board_vo> selectStatisticsBoardCount(Dm_statistics_board_vo vo);
}
