/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.db.config;

import java.util.List;

import egovframework.diam.biz.model.config.Dm_domain_list_vo;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

/**
 * @Class Name : DomainMapper.java
 * @Description : 사용자페이지 표출에 사용되는 도메인 데이터 CRUD 메소드를 수행하는 Mapper Interface
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

@Mapper("domainMapper")
public interface DomainMapper {
	
	/**
	 * selectDomainList
	 * 검색 값에 따른 도메인 리스트데이터 조회
	 * @param vo 도메인 데이터 검색조건,페이징 값을 vo객체에 담아 전달
	 * @return List<Dm_domain_list_vo> 조회된 도메인 데이터를 List 자료형으로 전달
	*/
	public List<Dm_domain_list_vo> selectDomainList(Dm_domain_list_vo vo);
	
	/**
	 * selectDomainListCnt
	 * 검색 값에 따른 도메인 리스트데이터 개수 조회
	 * @param vo 도메인 데이터 검색조건값을 vo객체에 담아 전달
	 * @return int 조회된 도매인데이터의 개수를 정수형으로 전달
	*/
	public int selectDomainListCnt(Dm_domain_list_vo vo);
	
	/**
	 * selectDomainByDmid
	 * 도메인 PK값으로 1건의 도메인데이터 조회
	 * @param vo 도메인 PK값을 vo객체에 담아 전달
	 * @return Dm_domain_list_vo 조회된 도메인데이터를 도메인데이터 vo객체에 담아 전달
	*/
	public Dm_domain_list_vo selectDomainByDmid(Dm_domain_list_vo vo);
	
	/**
	 * deleteDomain
	 * 도메인 PK값으로 등록되어 있는 도메인 데이터 삭제
	 * @param vo 사용자가 삭제하고자 하는 도메인데이터의 PK값을 vo객체에 담아 전달
	 * @return void 도메인데이터 delete 기능만 담당하는 메소드
	*/
	public int deleteDomain(Dm_domain_list_vo vo);
	
	/**
	 * insertDomain
	 * 사용자가 입력한 도메인데이터 DB에 insert
	 * @param vo 사용자가 입력한 도메인데이터를 vo객체에 담아 전달
	 * @return void 도메인데이터 insert 기능만 담당하는 메소드
	*/
	public int insertDomain(Dm_domain_list_vo vo);
	
	/**
	 * selectDomainMainCnt
	 * 메인 도메인 중복등록 방지를 위해 메인도메인으로 등록된 도메인의 개수 조회
	 * @return int 조회된 메인도메인 데이터의 개수를 정수형으로 전달
	*/
	public int selectDomainMainCnt();
	
	/**
	 * selectDomainRootDupCnt
	 * 도메인 root폴더값으로 1건의 도메인데이터 조회
	 * @param vo 사용자가 입력한 도메인 root폴더를 vo객체에 담아 전달
	 * @return int 조회된 도메인 데이터의 개수를 정수형으로 전달
	*/
	public int selectDomainRootDupCnt(Dm_domain_list_vo vo);
	
	/**
	 * updateDomain
	 * 도메인 PK값으로 등록되어 있는 도메인 데이터 DB에 update
	 * @param vo 사용자가 입력한 도메인 데이터를 객체에 담아 전달
	 * @return void 도메인데이터 update 기능만 담당하는 메소드
	*/
	public int updateDomain(Dm_domain_list_vo vo);
	
	/**
	 * selectDomainMain
	 * 메인도메인으로 등록된 도메인 조회
	 * @return Dm_domain_list_vo 조회된 도메인데이터를 도메인데이터 vo객체에 담아 전달
	*/
	public Dm_domain_list_vo selectDomainMain();
}
