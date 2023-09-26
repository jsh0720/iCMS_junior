package egovframework.diam.biz.service.statistics;

import java.util.List;

import egovframework.diam.biz.model.statistics.Dm_statistics_board_vo;

public interface StatisticsBoardService {
	
	public List<Dm_statistics_board_vo> selectStatisticsBoardCount(Dm_statistics_board_vo vo) throws Exception;

}
