package egovframework.diam.biz.service.member.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.diam.biz.db.member.MemberConfigMapper;
import egovframework.diam.biz.model.member.Dm_member_config_vo;
import egovframework.diam.biz.service.member.MemberConfigService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("memberConfigService")
public class MemberConfigServiceImpl extends EgovAbstractServiceImpl implements MemberConfigService {
	
	@Resource(name="memberConfigMapper")
	private MemberConfigMapper memberConfigMapper;
	
	@Override
	public Dm_member_config_vo selectMemberConfig() throws Exception {
		return memberConfigMapper.selectMemberConfig();
	}

	@Override
	public int updateMemberConfig(Dm_member_config_vo vo) throws Exception {
		return memberConfigMapper.updateMemberConfig(vo);
	}

	@Override
	public int insertMemberConfig(Dm_member_config_vo vo) throws Exception {
		return memberConfigMapper.insertMemberConfig(vo);
	}

}
