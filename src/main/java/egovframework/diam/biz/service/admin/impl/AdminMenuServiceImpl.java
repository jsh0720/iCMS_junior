/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.service.admin.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.diam.biz.db.admin.AdminMenuMapper;
import egovframework.diam.biz.model.admin.Dm_access_admin_menu_vo;
import egovframework.diam.biz.service.admin.AdminMenuService;
import egovframework.diam.cmm.util.MessageCode;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : AdminMenuServiceImpl.java
 * @Description : 관리자페이지 메뉴 CRUD 메소드를 수행하는 Service Interface 구현 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

@Service("adminMenuService")
public class AdminMenuServiceImpl extends EgovAbstractServiceImpl implements AdminMenuService {
	
	@Resource(name="adminMenuMapper")
	private AdminMenuMapper adminMenuMapper;
	
	/**
	 * selectAdminMenuList
	 * 등록된 모든 관리자메뉴 리스트데이터 조회
	 * @param vo 관리자메뉴 데이터를 vo객체에 담아 전달, 현재는 아무값도 넘기지 않으나 추후에 이용할 수 있어 추가
	 * @return List<Dm_access_admin_menu_vo> 조회된 관리자메뉴 데이터를 List 자료형으로 전달
	*/
	@Override
	public List<Dm_access_admin_menu_vo> selectAdminMenuList(Dm_access_admin_menu_vo vo) throws Exception {
		List<Dm_access_admin_menu_vo> result = adminMenuMapper.selectAdminMenuList(vo);
		return result;
	}

	/**
	 * selectAdminMenuRootChild
	 * 등록된 root계정의 모든 하위메뉴 리스트데이터 조회 
	 * @param vo 관리자메뉴 데이터를 vo객체에 담아 전달, 현재는 아무값도 넘기지 않으나 추후에 이용할 수 있어 추가
	 * @return List<Dm_access_admin_menu_vo> 조회된 관리자메뉴 데이터를 List 자료형으로 전달
	*/
	@Override
	public List<Dm_access_admin_menu_vo> selectAdminMenuRootChild(Dm_access_admin_menu_vo vo) throws Exception {
		List<Dm_access_admin_menu_vo> result = adminMenuMapper.selectAdminMenuRootChild(vo);
		return result;
	}
	
	/**
	 * insertBoard
	 * 사용자가 입력한 관리자메뉴 데이터 DB에 insert
	 * @param vo 사용자가 입력한 관리자메뉴 데이터를 vo객체에 담아 전달
	 * @return void 관리자메뉴 데이터 insert 기능만 담당하는 메소드
	*/
	@Override
	public int insertAdminMenu(Dm_access_admin_menu_vo vo) throws Exception {
		return adminMenuMapper.insertAdminMenu(vo);
	}
	
	/**
	 * updateAdminMenu
	 * 관리자메뉴 PK값으로 등록되어 있는 관리자메뉴 데이터 DB에 update
	 * @param vo 사용자가 입력한 관리자메뉴 데이터를 객체에 담아 전달
	 * @return void 관리자메뉴 데이터 update 기능만 담당하는 메소드
	*/
	@Override
	public int updateAdminMenu(Dm_access_admin_menu_vo vo) throws Exception {
		return adminMenuMapper.updateAdminMenu(vo);
	}
	
	/**
	 * selectAdminMenuListByParentId
	 * 선택한 메뉴의 하위메뉴 리스트데이터 조회, 상위메뉴 삭제 시 하위메뉴 삭제를 위해 조회
	 * @param vo 관리자메뉴 PK값을 vo객체에 담아 전달
	 * @return List<Dm_access_admin_menu_vo> 조회된 관리자메뉴 데이터를 List 자료형으로 전달
	*/
	@Override
	public List<Dm_access_admin_menu_vo> selectAdminMenuListByParentId(Dm_access_admin_menu_vo vo) throws Exception {
		List<Dm_access_admin_menu_vo> result = adminMenuMapper.selectAdminMenuListByParentId(vo);
		return result;
	}
	
	/**
	 * deleteAdminMenuByDmid
	 * 관리자메뉴 PK값으로 등록되어 있는 관리자메뉴 데이터 삭제
	 * @param vo 사용자가 삭제하고자 하는 관리자메뉴 데이터의 PK값을 vo객체에 담아 전달
	 * @return void 관리자메뉴 데이터 delete 기능만 담당하는 메소드
	*/
	@Override
	@Transactional
	public void deleteAdminMenuByDmid(List<Dm_access_admin_menu_vo> list) throws Exception {
		if (list.size() > 0) {
			list.forEach(item -> {
				int result = adminMenuMapper.deleteAdminMenuByDmid(item);
				if (result < 1) throw new RuntimeException(MessageCode.CMS_DELETE_FAIL.getLog());
			});
		}
	}
	
	/**
	 * selectAdminMenuListMain
	 * 관리자페이지 메인 왼쪽영역에 표출할 관리자 권한레벨별 관리자메뉴 리스트 조회
	 * @param vo 관리자메뉴 상위메뉴PK, 관리자계정 권한레벨을 vo객체에 담아 전달
	 * @return List<Dm_access_admin_menu_vo> 조회된 관리자메뉴 데이터를 List 자료형으로 전달
	*/
	@Override
	public List<Dm_access_admin_menu_vo> selectAdminMenuListMain(Dm_access_admin_menu_vo vo) throws Exception {
		List<Dm_access_admin_menu_vo> result = adminMenuMapper.selectAdminMenuListMain(vo);
		return result;
	}	
	
	/**
	 * selectAdminMenuByParentId
	 * 관리자메뉴 상위메뉴 PK값으로 1건의 게시판데이터 조회
	 * @param vo 관리자메뉴 상위메뉴 PK값을 vo객체에 담아 전달
	 * @return Dm_access_admin_menu_vo 조회된 관리자메뉴 데이터를 관리자메뉴 데이터 vo객체에 담아 전달
	*/
	@Override
	public Dm_access_admin_menu_vo selectAdminMenuByParentId(Dm_access_admin_menu_vo vo) throws Exception {
		Dm_access_admin_menu_vo result = adminMenuMapper.selectAdminMenuByParentId(vo);
		return result;
	}
	
	/**
	 * selectAdminMenuByDmId
	 * 관리자메뉴 PK값으로 1건의 게시판데이터 조회
	 * @param vo 관리자메뉴 PK값을 vo객체에 담아 전달
	 * @return Dm_access_admin_menu_vo 조회된 관리자메뉴 데이터를 관리자메뉴 데이터 vo객체에 담아 전달
	*/
	@Override
	public Dm_access_admin_menu_vo selectAdminMenuByDmId(Dm_access_admin_menu_vo vo) throws Exception {
		Dm_access_admin_menu_vo result = adminMenuMapper.selectAdminMenuByDmId(vo);
		return result;
	}

	@Override
	public List<Dm_access_admin_menu_vo> selectAdminTreeMenu(Dm_access_admin_menu_vo vo) throws Exception {
		return adminMenuMapper.selectAdminTreeMenu(vo);
	}

	@Override
	public int selectAdminMenuUrlDupCheck(Dm_access_admin_menu_vo vo) throws Exception {
		return adminMenuMapper.selectAdminMenuUrlDupCheck(vo);
	}

}
