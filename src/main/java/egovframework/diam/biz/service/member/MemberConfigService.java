package egovframework.diam.biz.service.member;

import egovframework.diam.biz.model.member.Dm_member_config_vo;

public interface MemberConfigService {
	public Dm_member_config_vo selectMemberConfig() throws Exception;
	
	public int updateMemberConfig(Dm_member_config_vo vo) throws Exception;
	
	public int insertMemberConfig(Dm_member_config_vo vo) throws Exception;
}
