/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.service.admin;

import java.util.List;

import egovframework.diam.biz.model.admin.Dm_admin_vo;

/**
 * @Class Name : AdminService.java
 * @Description : 관리자 계정정보 CRUD 메소드를 수행하는 Service Interface
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

public interface AdminService {
	/**
	 * selectAdminListCnt
	 * 검색 값에 따른 관리자계정 리스트데이터 개수 조회
	 * @param vo 관리자계정 데이터 검색조건값을 vo객체에 담아 전달
	 * @return int 조회된 관리자계정 데이터의 개수를 정수형으로 전달
	*/
	public int selectAdminListCnt(Dm_admin_vo vo) throws Exception;
	
	/**
	 * selectAdminList
	 * 검색 값에 따른 관리자계정 리스트데이터 조회
	 * @param vo 관리자계정 데이터 검색조건,페이징 값을 vo객체에 담아 전달
	 * @return List<Dm_admin_vo> 조회된 관리자계정 데이터를 List 자료형으로 전달
	*/
	public List<Dm_admin_vo> selectAdminList(Dm_admin_vo vo) throws Exception;
	
	/**
	 * selectAdmin
	 * 관리자계정 PK값으로 1건의 관리자계정 데이터 조회
	 * @param vo 관리자계정 PK값을 vo객체에 담아 전달
	 * @return Dm_admin_vo 조회된 관리자계정 데이터를 관리자계정 데이터 vo객체에 담아 전달
	*/
	public Dm_admin_vo selectAdmin(Dm_admin_vo vo) throws Exception;
	
	/**
	 * deleteAdmin
	 * 관리자계정 PK값으로 등록되어 있는 관리자계정 데이터 삭제
	 * @param vo 사용자가 삭제하고자 하는 관리자계정 데이터의 PK값을 vo객체에 담아 전달
	 * @return void 관리자계정 데이터 delete 기능만 담당하는 메소드
	*/
	public void deleteAdmin(List<Dm_admin_vo> list) throws Exception;
	
	/**
	 * insertAdmin
	 * 사용자가 입력한 관리자계정 데이터 DB에 insert
	 * @param vo 사용자가 입력한 관리자계정 데이터를 vo객체에 담아 전달
	 * @return void 관리자계정 데이터 insert 기능만 담당하는 메소드
	*/
	public void insertAdmin(Dm_admin_vo vo) throws Exception;
	
	/**
	 * updateAdmin
	 * 관리자계정 PK값으로 등록되어 있는 관리자계정 데이터 DB에 update
	 * @param vo 사용자가 입력한 관리자계정 데이터를 객체에 담아 전달
	 * @return void 관리자계정 데이터 update 기능만 담당하는 메소드
	*/
	public void updateAdmin(Dm_admin_vo vo) throws Exception;
	
	/**
	 * selectAdminDupChk
	 * 관리자계정 등록 시 중복된 아이디가 있는지 검증
	 * @param vo 관리자계정 데이터 아이디 값을 vo객체에 담아 전달
	 * @return Dm_admin_vo 조회된 관리자계정 데이터를 vo객체에 담아 전달
	*/
	public Dm_admin_vo selectAdminDupChk(Dm_admin_vo vo) throws Exception;
}
