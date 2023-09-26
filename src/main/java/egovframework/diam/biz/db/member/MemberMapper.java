/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.db.member;

import java.util.List;

import egovframework.diam.biz.model.member.Dm_member_vo;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

/**
 * @Class Name : MemberMapper.java
 * @Description : 일반회원 계정정보 CRUD 메소드를 수행하는 Mapper Interface
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

@Mapper("memberMapper")
public interface MemberMapper {
	
	/**
	 * selectMember
	 * 일반회원 PK값으로 1건의 일반회원 데이터 조회
	 * @param vo 일반회원 PK값을 vo객체에 담아 전달
	 * @return Dm_board_vo 조회된 일반회원 데이터를 일반회원 데이터 vo객체에 담아 전달
	*/
	public Dm_member_vo selectMember(Dm_member_vo vo);
	
	/**
	 * insertMember
	 * 사용자가 입력한 일반회원 데이터 DB에 insert
	 * @param vo 사용자가 입력한 일반회원 데이터를 vo객체에 담아 전달
	 * @return void 일반회원 데이터 insert 기능만 담당하는 메소드
	*/
	public void insertMember(Dm_member_vo vo);
	
	/**
	 * updateMember
	 * 일반회원 PK값으로 등록되어 있는 일반회원 데이터 DB에 update
	 * @param vo 사용자가 입력한 일반회원 데이터를 객체에 담아 전달
	 * @return void 일반회원 데이터 update 기능만 담당하는 메소드
	*/
	public void updateMember(Dm_member_vo vo);
	
	/**
	 * selectMemberCnt
	 * 검색 값에 따른 일반회원 리스트데이터 개수 조회
	 * @param vo 일반회원 데이터 검색조건값을 vo객체에 담아 전달
	 * @return int 조회된 일반회원 데이터의 개수를 정수형으로 전달
	*/
	public int selectMemberCnt(Dm_member_vo vo);
	
	/**
	 * selectMemberList
	 * 검색 값에 따른 일반회원 리스트데이터 조회
	 * @param vo 일반회원 데이터 검색조건,페이징 값을 vo객체에 담아 전달
	 * @return List<Dm_board_vo> 조회된 일반회원 데이터를 List 자료형으로 전달
	*/
	public List<Dm_member_vo> selectMemberList(Dm_member_vo vo);
	
	/**
	 * deleteMember
	 * 일반회원 PK값으로 등록되어 있는 일반회원 데이터 삭제
	 * @param vo 사용자가 삭제하고자 하는 일반회원 데이터의 PK값을 vo객체에 담아 전달
	 * @return void 일반회원 데이터 delete 기능만 담당하는 메소드
	*/
	public int deleteMember(Dm_member_vo vo);
	
	/**
	 * kickMember
	 * 일반회원 PK값으로 등록되어 있는 일반회원 데이터 탈퇴처리
	 * @param vo 사용자가 탈퇴처리 하고자 하는 일반회원 데이터의 PK값을 vo객체에 담아 전달
	 * @return void 일반회원 데이터 탈퇴처리 기능만 담당하는 메소드
	*/
	public int kickMember(Dm_member_vo vo);

	/**
	 * selectMemberIdDupChk
	 * 일반회원 정보등록 시 회원 아이디 값 중복 조회
	 * @param vo 사용자가 입력한 회원 아이디 값을 vo객체에 담아 전달
	 * @return Dm_member_vo 조회된 일반회원 데이터를 일반회원 데이터 vo객체에 담아 전달
	*/
	public Dm_member_vo selectMemberIdDupChk(Dm_member_vo vo);
	
	public List<Dm_member_vo> selectMemberInfo(Dm_member_vo vo);
	
	public void updateMemberPassword(Dm_member_vo vo);
	
	public int chageMemberStatus(Dm_member_vo vo);
}
