package egovframework.diam.biz.service.statistics.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.diam.biz.db.statistics.VisitOrginMapper;
import egovframework.diam.biz.model.statistics.Dm_visit_orgin_vo;
import egovframework.diam.biz.service.statistics.VisitOrginService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("visitOrginService")
public class VisitOrginServiceImpl extends EgovAbstractServiceImpl implements VisitOrginService {

	@Resource(name="visitOrginMapper")
	private VisitOrginMapper visitOrginMapper;

	@Override
	public void insertVisitOrgin(Dm_visit_orgin_vo vo) throws Exception {
		visitOrginMapper.insertVisitOrgin(vo);
		
	}

	@Override
	public int checkVisitOrgin(Dm_visit_orgin_vo vo) throws Exception {
		return visitOrginMapper.checkVisitOrgin(vo);
	}

	@Override
	public void updateVisitOrgin(Dm_visit_orgin_vo vo) throws Exception {
		visitOrginMapper.updateVisitOrgin(vo);
	}

	@Override
	public int selectVisitOrginTotalCount(Dm_visit_orgin_vo vo) throws Exception {
		return visitOrginMapper.selectVisitOrginTotalCount(vo);
	}

	@Override
	public int selectVisitOrginArrEngineCount(Dm_visit_orgin_vo vo) throws Exception {
		return visitOrginMapper.selectVisitOrginArrEngineCount(vo);
	}

	@Override
	public int selectVisitOrginArrNotEngineCount(Dm_visit_orgin_vo vo) throws Exception {
		return visitOrginMapper.selectVisitOrginArrNotEngineCount(vo);
	}

	@Override
	public int selectVisitOrginEngineCount(Dm_visit_orgin_vo vo) throws Exception {
		return visitOrginMapper.selectVisitOrginEngineCount(vo);
	}

	@Override
	public List<Dm_visit_orgin_vo> selectVisitOrginDateCountList(Dm_visit_orgin_vo vo) throws Exception {
		return visitOrginMapper.selectVisitOrginDateCountList(vo);
	}

	@Override
	public List<Dm_visit_orgin_vo> selectVisitOrginEtcDatecountList(Dm_visit_orgin_vo vo) throws Exception {
		return visitOrginMapper.selectVisitOrginEtcDatecountList(vo);
	}
	
}
