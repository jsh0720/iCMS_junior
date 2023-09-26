/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.service.display.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.diam.biz.db.display.MenuMapper;
import egovframework.diam.biz.model.display.Dm_menus_vo;
import egovframework.diam.biz.service.display.MenuService;
import egovframework.diam.cmm.util.MessageCode;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : MenuServiceImpl.java
 * @Description : 사용자페이지 메인/서브 상단에 사용되는 메뉴 데이터 CRUD 메소드를 수행하는 Service Interface 구현 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

@Service("menuService")
public class MenuServiceImpl extends EgovAbstractServiceImpl implements MenuService {
	
	@Resource(name="menuMapper")
	private MenuMapper menuMapper;
	
	/**
	 * selectMenuList
	 * 등록된 메뉴표출 여부가 사용중인 사용자메뉴 리스트데이터 조회
	 * @param vo 사용자메뉴 도메인PK값을 vo객체에 담아 전달
	 * @return List<Dm_menus_vo> 조회된 사용자메뉴 데이터를 List 자료형으로 전달
	*/
	@Override
	public List<Dm_menus_vo> selectMenuList(Dm_menus_vo vo) throws Exception {
		List<Dm_menus_vo> result = menuMapper.selectMenuList(vo);
		return result;
	}
	
	/**
	 * selectMenuRootChild
	 * 등록된 root계정의 모든 하위메뉴 리스트데이터 조회
	 * @param vo 사용자메뉴 도메인PK값을 vo객체에 담아 전달
	 * @return List<Dm_menus_vo> 조회된 사용자메뉴 데이터를 List 자료형으로 전달
	*/
	@Override
	public List<Dm_menus_vo> selectMenuRootChild(Dm_menus_vo vo) throws Exception {
		List<Dm_menus_vo> result = menuMapper.selectMenuRootChild(vo);
		return result;
	}
	
	/**
	 * selectMenuByParentId
	 * 사용자메뉴 상위메뉴 PK값으로 1건의 사용자메뉴 데이터 조회
	 * @param vo 사용자메뉴 상위메뉴 PK값을 vo객체에 담아 전달
	 * @return Dm_menus_vo 조회된 사용자메뉴 데이터를 사용자메뉴 데이터 vo객체에 담아 전달
	*/
	@Override
	public Dm_menus_vo selectMenuByParentId(Dm_menus_vo vo) throws Exception {
		Dm_menus_vo result = menuMapper.selectMenuByParentId(vo);
		return result;
	}
	
	/**
	 * insertMenu
	 * 사용자가 입력한 사용자메뉴 데이터 DB에 insert
	 * @param vo 사용자가 입력한 사용자메뉴 데이터를 vo객체에 담아 전달
	 * @return void 사용자메뉴 데이터 insert 기능만 담당하는 메소드
	*/
	@Override
	public int insertMenu(Dm_menus_vo vo) throws Exception {
		return menuMapper.insertMenu(vo);
	}
		
	/**
	 * updateMenu
	 * 사용자메뉴 PK값으로 등록되어 있는 사용자메뉴 데이터 DB에 update
	 * @param vo 사용자가 입력한 사용자메뉴 데이터를 객체에 담아 전달
	 * @return void 사용자메뉴 데이터 update 기능만 담당하는 메소드
	*/
	@Override
	public int updateMenu(Dm_menus_vo vo) throws Exception {
		return menuMapper.updateMenu(vo);
	}
	
	/**
	 * selectMenuListByParentId
	 * 선택한 메뉴의 하위메뉴 리스트데이터 조회, 상위메뉴 삭제 시 하위메뉴 삭제를 위해 조회
	 * @param vo 사용자메뉴 PK값을 vo객체에 담아 전달
	 * @return List<Dm_menus_vo> 조회된 사용자메뉴 데이터를 List 자료형으로 전달
	*/
	@Override
	public List<Dm_menus_vo> selectMenuListByParentId(Dm_menus_vo vo) throws Exception {
		List<Dm_menus_vo> result = menuMapper.selectMenuListByParentId(vo);
		return result;
	}
	
	/**
	 * deleteMenuByDmid
	 * 사용자메뉴 PK값으로 등록되어 있는 사용자메뉴 데이터 삭제
	 * @param vo 사용자가 삭제하고자 하는 사용자메뉴 데이터의 PK값을 vo객체에 담아 전달
	 * @return void 사용자메뉴 데이터 delete 기능만 담당하는 메소드
	*/
	@Override
	@Transactional
	public void deleteMenuByDmid(List<Dm_menus_vo> list) throws Exception {
		if (list.size() > 0) {
			list.forEach(item -> {
				int result = menuMapper.deleteMenuByDmid(item);
				if (result < 1) throw new RuntimeException(MessageCode.CMS_DELETE_FAIL.getLog());
			});
		}
	}
	
	/**
	 * selectMenuListDesc
	 * 사용자가 설정한 메뉴정렬값의 내림차순으로 모든 메뉴리스트 조회
	 * @param vo 사용자메뉴 도메인PK값을 vo객체에 담아 전달
	 * @return List<Dm_menus_vo> 조회된 사용자메뉴 데이터를 List 자료형으로 전달
	*/
	@Override
	public List<Dm_menus_vo> selectMenuListDesc(Dm_menus_vo vo) throws Exception {
		List<Dm_menus_vo> result = menuMapper.selectMenuListDesc(vo);
		return result;
	}
	
	/**
	 * insertMenuRoot
	 * 도메인 신규생성 시 관리자페이지 메뉴관리에서 사용할 최상위 root메뉴 insert
	 * @param vo 사용자가 등록한 도메인 정보 중 PK값을 vo객체에 담아 전달
	 * @return void 사용자메뉴 중 최상위 root메뉴 데이터 insert 기능만 담당하는 메소드
	*/
	@Override
	public void insertMenuRoot(Dm_menus_vo vo) throws Exception {
		menuMapper.insertMenuRoot(vo);
	}
	
	/**
	 * selectMenuByDmId
	 * 사용자메뉴 PK값으로 1건의 사용자메뉴 데이터 조회
	 * @param vo 사용자메뉴 PK값을 vo객체에 담아 전달
	 * @return Dm_menus_vo 조회된 사용자메뉴 데이터를 사용자메뉴 데이터 vo객체에 담아 전달
	*/
	@Override
	public Dm_menus_vo selectMenuByDmId(Dm_menus_vo vo) throws Exception {
		Dm_menus_vo result = menuMapper.selectMenuByDmId(vo);
		return result;
	}
	
	/**
	 * deleteMenuByDomain
	 * 도메인 삭제 시 해당하는 도메인의 메뉴로 등록되어 있는 사용자메뉴 모두 삭제
	 * @param vo 사용자가 삭제한 도메인 정보 중 PK값을 vo객체에 담아 전달
	 * @return void 도메인PK에 해당하는 사용자메뉴 데이터 delete 기능만 담당하는 메소드
	*/
	@Override
	public void deleteMenuByDomain(Dm_menus_vo vo) throws Exception {
		menuMapper.deleteMenuByDomain(vo);
	}

	@Override
	public List<Dm_menus_vo> selectTreeMenu(Dm_menus_vo vo) throws Exception {
		return menuMapper.selectTreeMenu(vo);
	}
}
