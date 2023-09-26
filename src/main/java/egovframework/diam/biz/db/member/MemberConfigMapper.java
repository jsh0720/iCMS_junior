package egovframework.diam.biz.db.member;

import egovframework.diam.biz.model.member.Dm_member_config_vo;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("memberConfigMapper")
public interface MemberConfigMapper {

	public Dm_member_config_vo selectMemberConfig();
	
	public int updateMemberConfig(Dm_member_config_vo vo);
	
	public int insertMemberConfig(Dm_member_config_vo vo);

}