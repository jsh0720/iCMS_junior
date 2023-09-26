/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.cmm.service;

import java.util.List;

import egovframework.diam.biz.model.member.Dm_member_vo;

/**
 * @Class Name : LoginLockService.java
 * @Description : 관리자 페이지 로그인 실패 시 잠금 프로세스 메소드를 수행하는 Service Interface
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

public interface LoginLockService {
	
	/**
	 * selectLoginLockListCnt
	 * 검색 값에 따른 잠김계정 리스트데이터 개수 조회
	 * @param vo 잠김계정 데이터 검색조건값을 vo객체에 담아 전달
	 * @return int 조회된 잠김계정 데이터의 개수를 정수형으로 전달
	*/
	public int selectLoginLockListCnt(Dm_member_vo vo) throws Exception;
	
	/**
	 * selectLoginLockList
	 * 검색 값에 따른 잠김계정 리스트데이터 조회
	 * @param vo 잠김계정 데이터 검색조건,페이징 값을 vo객체에 담아 전달
	 * @return List<Dm_member_vo> 조회된 잠김계정 데이터를 List 자료형으로 전달
	*/
	public List<Dm_member_vo> selectLoginLockList(Dm_member_vo vo) throws Exception;
	
	/**
	 * selectLockMember
	 * 잠김계정 PK값으로 1건의 잠김계정데이터 조회
	 * @param vo 잠김계정 PK값을 vo객체에 담아 전달
	 * @return Dm_member_vo 조회된 잠김계정데이터를 잠김계정데이터 vo객체에 담아 전달
	*/
	public Dm_member_vo selectLockMember(Dm_member_vo vo) throws Exception;
	
	/**
	 * unlockMember
	 * 잠김계정 PK값으로 등록되어 있는 잠김계정 잠김해제 처리
	 * @param vo 잠김계정 PK값을 vo객체에 담아 전달
	 * @return void 잠김계정 잠김해제 update 기능만 담당하는 메소드
	*/
	public void unlockMember(List<Dm_member_vo> list) throws Exception;
}
