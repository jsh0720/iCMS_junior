/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.db.member;

import java.util.List;

import egovframework.diam.biz.model.member.Dm_login_log_vo;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("memberLoginLogMapper")
public interface MemberLoginLogMapper {
	
	/**
	 * selectLoginLogList
	 * 검색 값에 따른 회원 로그인로그 리스트데이터 조회
	 * @param vo 회원 로그인로그 데이터 검색조건,페이징 값을 vo객체에 담아 전달
	 * @return List<Dm_login_log_vo> 조회된 회원 로그인로그 데이터를 List 자료형으로 전달
	*/
	public List<Dm_login_log_vo> selectLoginLogList(Dm_login_log_vo vo);
	
	/**
	 * selectLoginLogListCnt
	 * 검색 값에 따른 회원 로그인로그 리스트데이터 개수 조회
	 * @param vo 회원 로그인로그 데이터 검색조건값을 vo객체에 담아 전달
	 * @return int 조회된 회원 로그인로그 데이터의 개수를 정수형으로 전달
	*/
	public int selectLoginLogListCnt(Dm_login_log_vo vo);
}
