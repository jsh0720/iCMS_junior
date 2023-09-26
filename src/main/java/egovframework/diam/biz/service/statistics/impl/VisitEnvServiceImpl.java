package egovframework.diam.biz.service.statistics.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.diam.biz.db.statistics.VisitEnvMapper;
import egovframework.diam.biz.model.statistics.Dm_visit_env_vo;
import egovframework.diam.biz.service.statistics.VisitEnvService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("visitEnvService")
public class VisitEnvServiceImpl extends EgovAbstractServiceImpl implements VisitEnvService {
	
	@Resource(name="visitEnvMapper")
	private VisitEnvMapper visitEnvMapper;

	@Override
	public void insertVisitEnv(Dm_visit_env_vo vo) throws Exception {
		visitEnvMapper.insertVisitEnv(vo);
	}

	@Override
	public int checkVisitEnv(Dm_visit_env_vo vo) throws Exception {
		return visitEnvMapper.checkVisitEnv(vo);
	}

	@Override
	public void updateVisitEnv(Dm_visit_env_vo vo) throws Exception {
		visitEnvMapper.updateVisitEnv(vo);		
	}

	@Override
	public int selectVisitEnvTypeCount(Dm_visit_env_vo vo) throws Exception {
		return visitEnvMapper.selectVisitEnvTypeCount(vo);
	}

	@Override
	public Dm_visit_env_vo selectVisitEnvMaxType(Dm_visit_env_vo vo) throws Exception {
		return visitEnvMapper.selectVisitEnvMaxType(vo);
	}

	@Override
	public List<Dm_visit_env_vo> selectVisitEnvOsList(Dm_visit_env_vo vo) throws Exception {
		return visitEnvMapper.selectVisitEnvOsList(vo);
	}

	@Override
	public List<Dm_visit_env_vo> selectVisitEnvDateCountList(Dm_visit_env_vo vo) throws Exception {
		return visitEnvMapper.selectVisitEnvDateCountList(vo);
	}

	@Override
	public int selectVisitEnvCount(Dm_visit_env_vo vo) throws Exception {
		return visitEnvMapper.selectVisitEnvCount(vo);
	}

	@Override
	public List<Dm_visit_env_vo> selectVisitEnvBrowserList(Dm_visit_env_vo vo) throws Exception {
		return visitEnvMapper.selectVisitEnvBrowserList(vo);
	}

	@Override
	public int selectVisitEnvBrowerCount(Dm_visit_env_vo vo) throws Exception {
		return visitEnvMapper.selectVisitEnvBrowerCount(vo);
	}

	@Override
	public List<Dm_visit_env_vo> selectVisitEnvBrowerDateCountList(Dm_visit_env_vo vo) throws Exception {
		return visitEnvMapper.selectVisitEnvBrowerDateCountList(vo);
	}

}
