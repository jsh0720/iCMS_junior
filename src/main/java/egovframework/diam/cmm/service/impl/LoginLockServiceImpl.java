/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.cmm.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.diam.biz.model.member.Dm_member_vo;
import egovframework.diam.cmm.db.LoginLockMapper;
import egovframework.diam.cmm.service.LoginLockService;
import egovframework.diam.cmm.util.MessageCode;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : LoginLockServiceImpl.java
 * @Description : 관리자 페이지 로그인 실패 시 잠금 프로세스 메소드를 수행하는 Service Interface 구현 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

@Service("loginLockService")
public class LoginLockServiceImpl extends EgovAbstractServiceImpl implements LoginLockService {
	
	@Resource(name="loginLockMapper")
	private LoginLockMapper loginLockMapper;
	
	/**
	 * selectLoginLockListCnt
	 * 검색 값에 따른 잠김계정 리스트데이터 개수 조회
	 * @param vo 잠김계정 데이터 검색조건값을 vo객체에 담아 전달
	 * @return int 조회된 잠김계정 데이터의 개수를 정수형으로 전달
	*/
	@Override
	public int selectLoginLockListCnt(Dm_member_vo vo) throws Exception {
		int result = loginLockMapper.selectLoginLockListCnt(vo);
		return result;
	}
	
	/**
	 * selectLoginLockList
	 * 검색 값에 따른 잠김계정 리스트데이터 조회
	 * @param vo 잠김계정 데이터 검색조건,페이징 값을 vo객체에 담아 전달
	 * @return List<Dm_member_vo> 조회된 잠김계정 데이터를 List 자료형으로 전달
	*/
	@Override
	public List<Dm_member_vo> selectLoginLockList(Dm_member_vo vo) throws Exception {
		List<Dm_member_vo> result = loginLockMapper.selectLoginLockList(vo);
		return result;
	}
	
	/**
	 * selectLockMember
	 * 잠김계정 PK값으로 1건의 잠김계정데이터 조회
	 * @param vo 잠김계정 PK값을 vo객체에 담아 전달
	 * @return Dm_member_vo 조회된 잠김계정데이터를 잠김계정데이터 vo객체에 담아 전달
	*/
	@Override
	public Dm_member_vo selectLockMember(Dm_member_vo vo) throws Exception {
		Dm_member_vo result = loginLockMapper.selectLockMember(vo);
		return result;
	}
	
	/**
	 * unlockMember
	 * 잠김계정 PK값으로 등록되어 있는 잠김계정 잠김해제 처리
	 * @param vo 잠김계정 PK값을 vo객체에 담아 전달
	 * @return void 잠김계정 잠김해제 update 기능만 담당하는 메소드
	*/
	@Override
	public void unlockMember(List<Dm_member_vo> list) throws Exception {
		if (list.size() > 0) {
			list.forEach(item -> {
				if (loginLockMapper.unlockMember(item) < 1) throw new RuntimeException(MessageCode.CMM_TRANSACTION_FAIL.getLog());
			});
		}
	}
}
