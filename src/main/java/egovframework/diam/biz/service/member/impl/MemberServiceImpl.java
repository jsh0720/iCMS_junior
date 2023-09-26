/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.service.member.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.diam.biz.db.member.MemberMapper;
import egovframework.diam.biz.model.member.Dm_member_vo;
import egovframework.diam.biz.service.member.MemberService;
import egovframework.diam.cmm.util.MessageCode;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;

/**
 * @Class Name : MemberServiceImpl.java
 * @Description : 일반회원 계정정보 CRUD 메소드를 수행하는 Service Interface 구현 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

@Service("memberService")
public class MemberServiceImpl extends EgovAbstractServiceImpl implements MemberService {
	
	@Resource(name="memberMapper")
	private MemberMapper memberMapper;
	
	@Resource(name="egovDiamEsntlIdGnrService")
	private EgovIdGnrService egovDiamEsntlIdGnrService;
	
	/**
	 * selectMember
	 * 일반회원 PK값으로 1건의 일반회원 데이터 조회
	 * @param vo 일반회원 PK값을 vo객체에 담아 전달
	 * @return Dm_board_vo 조회된 일반회원 데이터를 일반회원 데이터 vo객체에 담아 전달
	*/
	@Override
	public Dm_member_vo selectMember(Dm_member_vo vo) throws Exception {
		Dm_member_vo result = memberMapper.selectMember(vo);
		return result;
	}
	
	/**
	 * insertMember
	 * 사용자가 입력한 일반회원 데이터 DB에 insert
	 * @param vo 사용자가 입력한 일반회원 데이터를 vo객체에 담아 전달
	 * @return void 일반회원 데이터 insert 기능만 담당하는 메소드
	*/
	@Override
	public void insertMember(Dm_member_vo vo) throws Exception {
		vo.setEsntl_id(egovDiamEsntlIdGnrService.getNextStringId());
		memberMapper.insertMember(vo);
	}
	
	/**
	 * updateMember
	 * 일반회원 PK값으로 등록되어 있는 일반회원 데이터 DB에 update
	 * @param vo 사용자가 입력한 일반회원 데이터를 객체에 담아 전달
	 * @return void 일반회원 데이터 update 기능만 담당하는 메소드
	*/
	@Override
	public void updateMember(Dm_member_vo vo) throws Exception {
		memberMapper.updateMember(vo);
	}
	
	/**
	 * selectMemberCnt
	 * 검색 값에 따른 일반회원 리스트데이터 개수 조회
	 * @param vo 일반회원 데이터 검색조건값을 vo객체에 담아 전달
	 * @return int 조회된 일반회원 데이터의 개수를 정수형으로 전달
	*/
	@Override
	public int selectMemberCnt(Dm_member_vo vo) throws Exception {
		int result = memberMapper.selectMemberCnt(vo);
		return result;
	}
	
	/**
	 * selectMemberList
	 * 검색 값에 따른 일반회원 리스트데이터 조회
	 * @param vo 일반회원 데이터 검색조건,페이징 값을 vo객체에 담아 전달
	 * @return List<Dm_board_vo> 조회된 일반회원 데이터를 List 자료형으로 전달
	*/
	@Override
	public List<Dm_member_vo> selectMemberList(Dm_member_vo vo) throws Exception {
		List<Dm_member_vo> result = memberMapper.selectMemberList(vo);
		return result;
	}
	
	/**
	 * deleteMember
	 * 일반회원 PK값으로 등록되어 있는 일반회원 데이터 삭제
	 * @param vo 사용자가 삭제하고자 하는 일반회원 데이터의 PK값을 vo객체에 담아 전달
	 * @return void 일반회원 데이터 delete 기능만 담당하는 메소드
	*/
	@Override
	@Transactional
	public void deleteMember(List<Dm_member_vo> list) throws Exception {
		if (list.size() > 0) {
			list.forEach(item -> {
				if (memberMapper.deleteMember(item) < 1) throw new RuntimeException(MessageCode.CMS_DELETE_FAIL.getLog());
			});
		}
		
	}
	
	/**
	 * kickMember
	 * 일반회원 PK값으로 등록되어 있는 일반회원 데이터 탈퇴처리
	 * @param vo 사용자가 탈퇴처리 하고자 하는 일반회원 데이터의 PK값을 vo객체에 담아 전달
	 * @return void 일반회원 데이터 탈퇴처리 기능만 담당하는 메소드
	*/
	@Override
	@Transactional
	public void kickMember(List<Dm_member_vo> list) throws Exception {
		if (list.size() > 0) {
			list.forEach(item -> {
				if (memberMapper.kickMember(item) < 1) throw new RuntimeException(MessageCode.CMS_UPDATE_FAIL.getLog());
			});			
		}
	}

	/**
	 * selectMemberIdDupChk
	 * 일반회원 정보등록 시 회원 아이디 값 중복 조회
	 * @param vo 사용자가 입력한 회원 아이디 값을 vo객체에 담아 전달
	 * @return Dm_member_vo 조회된 일반회원 데이터를 일반회원 데이터 vo객체에 담아 전달
	*/
	@Override
	public Dm_member_vo selectMemberIdDupChk(Dm_member_vo vo) throws Exception {
		Dm_member_vo result = memberMapper.selectMemberIdDupChk(vo);
		return result;
	}

	@Override
	public List<Dm_member_vo> selectMemberInfo(Dm_member_vo vo) throws Exception {
		return memberMapper.selectMemberInfo(vo);
	}

	@Override
	public void updateMemberPassword(Dm_member_vo vo) throws Exception {
		memberMapper.updateMemberPassword(vo);
	}

	@Override
	public int chageMemberStatus(Dm_member_vo vo) throws Exception {
		return memberMapper.chageMemberStatus(vo);
	}
}
