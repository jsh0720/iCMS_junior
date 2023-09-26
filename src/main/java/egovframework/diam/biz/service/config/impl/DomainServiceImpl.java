/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.service.config.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.diam.biz.db.config.ConfigMapper;
import egovframework.diam.biz.db.config.DomainMapper;
import egovframework.diam.biz.db.display.MenuMapper;
import egovframework.diam.biz.model.config.Dm_config_vo;
import egovframework.diam.biz.model.config.Dm_domain_list_vo;
import egovframework.diam.biz.model.display.Dm_menus_vo;
import egovframework.diam.biz.service.config.DomainService;
import egovframework.diam.cmm.util.MessageCode;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : DomainServiceImpl.java
 * @Description : 사용자페이지 표출에 사용되는 도메인 데이터 CRUD 메소드를 수행하는 Service Interface 구현 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

@Service("domainService")
public class DomainServiceImpl extends EgovAbstractServiceImpl implements DomainService {
	
	@Resource(name="domainMapper")
	private DomainMapper domainMapper;
	
	@Resource(name="menuMapper")
	private MenuMapper menuMapper;
	
	@Resource(name="configMapper")
	private ConfigMapper configMapper;
	
	/**
	 * selectDomainList
	 * 검색 값에 따른 도메인 리스트데이터 조회
	 * @param vo 도메인 데이터 검색조건,페이징 값을 vo객체에 담아 전달
	 * @return List<Dm_domain_list_vo> 조회된 도메인 데이터를 List 자료형으로 전달
	*/
	@Override
	public List<Dm_domain_list_vo> selectDomainList(Dm_domain_list_vo vo) throws Exception {
		List<Dm_domain_list_vo> result = domainMapper.selectDomainList(vo);
		return result;
	}
	
	/**
	 * selectDomainListCnt
	 * 검색 값에 따른 도메인 리스트데이터 개수 조회
	 * @param vo 도메인 데이터 검색조건값을 vo객체에 담아 전달
	 * @return int 조회된 도매인데이터의 개수를 정수형으로 전달
	*/
	@Override
	public int selectDomainListCnt(Dm_domain_list_vo vo) throws Exception {
		int result = domainMapper.selectDomainListCnt(vo);
		return result;
	}
	
	/**
	 * selectDomainByDmid
	 * 도메인 PK값으로 1건의 도메인데이터 조회
	 * @param vo 도메인 PK값을 vo객체에 담아 전달
	 * @return Dm_domain_list_vo 조회된 도메인데이터를 도메인데이터 vo객체에 담아 전달
	*/
	@Override
	public Dm_domain_list_vo selectDomainByDmid(Dm_domain_list_vo vo) throws Exception {
		Dm_domain_list_vo result = domainMapper.selectDomainByDmid(vo);
		return result;
	}
	
	/**
	 * deleteDomain
	 * 도메인 PK값으로 등록되어 있는 도메인 데이터 삭제
	 * @param vo 사용자가 삭제하고자 하는 도메인데이터의 PK값을 vo객체에 담아 전달
	 * @return void 도메인데이터 delete 기능만 담당하는 메소드
	*/
	@Override
	@Transactional
	public int deleteDomain(Dm_domain_list_vo vo) throws Exception {
		int result = 0;
		if (vo != null) {		
			domainMapper.deleteDomain(vo);
			
			menuMapper.deleteMenuByDomain(Dm_menus_vo.builder()
					.dm_domain(vo.getDm_id())
					.dm_delete_id(vo.getDm_delete_id())
					.build());
			
			result = 1;
		}
		return result;
	}
	
	/**
	 * insertDomain
	 * 사용자가 입력한 도메인데이터 DB에 insert
	 * @param vo 사용자가 입력한 도메인데이터를 vo객체에 담아 전달
	 * @return void 도메인데이터 insert 기능만 담당하는 메소드
	*/
	@Override
	@Transactional
	public int insertDomain(Dm_domain_list_vo vo) throws Exception {
		int result = domainMapper.insertDomain(vo);
		if (result > 0) {
			Dm_menus_vo menuVO = Dm_menus_vo.builder()
					.dm_create_id(vo.getDm_create_id())
					.dm_domain(vo.getDm_id())
					.build();
			result = menuMapper.insertMenuRoot(menuVO);
			if (result < 1) throw new RuntimeException(MessageCode.CMM_TRANSACTION_FAIL.getLog());
		}
		return result;
	}
	
	/**
	 * selectDomainMainCnt
	 * 메인 도메인 중복등록 방지를 위해 메인도메인으로 등록된 도메인의 개수 조회
	 * @return int 조회된 메인도메인 데이터의 개수를 정수형으로 전달
	*/
	@Override
	public int selectDomainMainCnt() throws Exception {
		int result = domainMapper.selectDomainMainCnt();
		return result;
	}
	
	/**
	 * selectDomainRootDupCnt
	 * 도메인 root폴더값으로 1건의 도메인데이터 조회
	 * @param vo 사용자가 입력한 도메인 root폴더를 vo객체에 담아 전달
	 * @return int 조회된 도메인 데이터의 개수를 정수형으로 전달
	*/
	@Override
	public int selectDomainRootDupCnt(Dm_domain_list_vo vo) throws Exception {
		int result = domainMapper.selectDomainRootDupCnt(vo);
		return result;
	}
	
	/**
	 * updateDomain
	 * 도메인 PK값으로 등록되어 있는 도메인 데이터 DB에 update
	 * @param vo 사용자가 입력한 도메인 데이터를 객체에 담아 전달
	 * @return void 도메인데이터 update 기능만 담당하는 메소드
	*/
	@Override
	public int updateDomain(Dm_domain_list_vo vo) throws Exception {
		return domainMapper.updateDomain(vo);
	}
	
	/**
	 * selectDomainMain
	 * 메인도메인으로 등록된 도메인 조회
	 * @return Dm_domain_list_vo 조회된 도메인데이터를 도메인데이터 vo객체에 담아 전달
	*/
	@Override
	public Dm_domain_list_vo selectDomainMain() throws Exception {
		Dm_domain_list_vo result = domainMapper.selectDomainMain();
		return result;
	}	
}
