package egovframework.diam.biz.service.statistics.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.diam.biz.db.statistics.StatisticsBoardMapper;
import egovframework.diam.biz.model.statistics.Dm_statistics_board_vo;
import egovframework.diam.biz.service.statistics.StatisticsBoardService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("statisticsBoardService")
public class StatisticsBoardServiceImpl extends EgovAbstractServiceImpl implements StatisticsBoardService{
	
	@Resource(name="statisticsBoardMapper") private StatisticsBoardMapper statisticsBoardMapper;

	@Override
	public List<Dm_statistics_board_vo> selectStatisticsBoardCount(Dm_statistics_board_vo vo) throws Exception {
		return statisticsBoardMapper.selectStatisticsBoardCount(vo);
	}

}
