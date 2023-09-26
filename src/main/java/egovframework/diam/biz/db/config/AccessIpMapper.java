/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.db.config;

import java.util.List;

import egovframework.diam.biz.model.config.Dm_access_ip_vo;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

/**
 * @Class Name : AccessIpMapper.java
 * @Description : 관리자페이지 접근아이피 데이터 CRUD 메소드를 수행하는 Mapper Interface
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
 */

@Mapper("accessIpMapper")
public interface AccessIpMapper {
	
	/**
	 * selectAccessIpListCnt
	 * 검색 값에 따른 접근아이피 리스트데이터 개수 조회
	 * @param vo 접근아이피 데이터 검색조건값을 vo객체에 담아 전달
	 * @return int 조회된 접근아이피 데이터의 개수를 정수형으로 전달
	*/
	public int selectAccessIpListCnt(Dm_access_ip_vo vo);
	
	/**
	 * selectAccessIpList
	 * 검색 값에 따른 접근아이피 리스트데이터 조회
	 * @param vo 접근아이피 데이터 검색조건,페이징 값을 vo객체에 담아 전달
	 * @return List<Dm_access_ip_vo> 조회된 접근아이피 데이터를 List 자료형으로 전달
	*/
	public List<Dm_access_ip_vo> selectAccessIpList(Dm_access_ip_vo vo);
	
	/**
	 * selectAccessIp
	 * 접근아이피 PK값으로 1건의 접근아이피 데이터 조회
	 * @param vo 접근아이피 PK값을 vo객체에 담아 전달
	 * @return Dm_access_ip_vo 조회된 접근아이피 데이터를 접근아이피 데이터 vo객체에 담아 전달
	*/
	public Dm_access_ip_vo selectAccessIp(Dm_access_ip_vo vo);
	
	/**
	 * deleteAccessIp
	 * 접근아이피 PK값으로 등록되어 있는 접근아이피 데이터 삭제
	 * @param vo 사용자가 삭제하고자 하는 접근아이피 데이터의 PK값을 vo객체에 담아 전달
	 * @return void 접근아이피 데이터 delete 기능만 담당하는 메소드
	*/
	public int deleteAccessIp(Dm_access_ip_vo vo);
	
	/**
	 * insertAccessIp
	 * 사용자가 입력한 접근아이피 데이터 DB에 insert
	 * @param vo 사용자가 입력한 접근아이피 데이터를 vo객체에 담아 전달
	 * @return void 접근아이피 데이터 insert 기능만 담당하는 메소드
	*/
	public int insertAccessIp(Dm_access_ip_vo vo);
	
	/**
	 * selectAccessIpAll
	 * 등록된 접근아이피를 모두 조회
	 * @return List<Dm_access_ip_vo> 조회된 접근아이피 데이터를 List 자료형으로 전달
	*/
	public List<Dm_access_ip_vo> selectAccessIpAll();
	
	/**
	 * updateAccessIp
	 * 접근아이피 PK값으로 등록되어 있는 접근아이피 데이터 DB에 update
	 * @param vo 사용자가 입력한 접근아이피 데이터를 객체에 담아 전달
	 * @return void 접근아이피 데이터 update 기능만 담당하는 메소드
	*/
	public int updateAccessIp(Dm_access_ip_vo vo);
	
	/**
	 * selectAccessIpDuplicate
	 * 사용자가 입력한 접근아이피 값 중복여부 조회
	 * @return int 조회된 접근아이피 데이터의 개수를 정수형으로 전달
	*/
	public int selectAccessIpDuplicate(Dm_access_ip_vo vo);
}
