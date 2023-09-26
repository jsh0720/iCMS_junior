/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.service.display.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.diam.biz.db.display.LayoutMapper;
import egovframework.diam.biz.model.display.Dm_layout_vo;
import egovframework.diam.biz.service.display.LayoutService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : LayoutServiceImpl.java
 * @Description : 사용자페이지에서 사용하는 레이아웃 데이터 CRUD 메소드를 수행하는 Service Interface 구현 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

@Service("layoutService")
public class LayoutServiceImpl extends EgovAbstractServiceImpl implements LayoutService {
	
	@Resource(name="layoutMapper")
	private LayoutMapper layoutMapper;
	
	/**
	 * selectLayoutListCnt
	 * 검색 값에 따른 레이아웃 리스트데이터 개수 조회
	 * @param vo 레이아웃 데이터 검색조건값을 vo객체에 담아 전달
	 * @return int 조회된 레이아웃 데이터의 개수를 정수형으로 전달
	*/
	@Override
	public int selectLayoutListCnt(Dm_layout_vo vo) throws Exception {
		return layoutMapper.selectLayoutListCnt(vo);
	}
	
	/**
	 * selectLayoutList
	 * 검색 값에 따른 레이아웃 리스트데이터 조회
	 * @param vo 레이아웃 데이터 검색조건,페이징 값을 vo객체에 담아 전달
	 * @return List<Dm_layout_vo> 조회된 레이아웃 데이터를 List 자료형으로 전달
	*/
	@Override
	public List<Dm_layout_vo> selectLayoutList(Dm_layout_vo vo) throws Exception {
		return layoutMapper.selectLayoutList(vo);
	}
	
	/**
	 * selectLayout
	 * 레이아웃 PK값으로 1건의 레이아웃 데이터 조회
	 * @param vo 레이아웃 PK값을 vo객체에 담아 전달
	 * @return Dm_layout_vo 조회된 레이아웃 데이터를 레이아웃 데이터 vo객체에 담아 전달
	*/
	@Override
	public Dm_layout_vo selectLayout(Dm_layout_vo vo) throws Exception {
		return layoutMapper.selectLayout(vo);
	}
	
	/**
	 * deleteLayout
	 * 레이아웃 PK값으로 등록되어 있는 레이아웃 설정데이터 삭제
	 * @param vo 사용자가 삭제하고자 하는 레이아웃 데이터의 PK값을 vo객체에 담아 전달
	 * @return void 레이아웃 데이터 delete 기능만 담당하는 메소드
	*/
	@Override
	@Transactional
	public int deleteLayout(Dm_layout_vo vo) throws Exception {
		return layoutMapper.deleteLayout(vo);
	}
	
	/**
	 * insertLayout
	 * 사용자가 입력한 레이아웃 설정데이터 DB에 insert
	 * @param vo 사용자가 입력한 레이아웃 설정데이터를 vo객체에 담아 전달
	 * @return void 레이아웃 데이터 insert 기능만 담당하는 메소드
	*/
	@Override
	public int insertLayout(Dm_layout_vo vo) throws Exception {
		return layoutMapper.insertLayout(vo);
	}
	
	/**
	 * updateLayout
	 * 레이아웃 PK값으로 등록되어 있는 레이아웃 데이터 DB에 update
	 * @param vo 사용자가 입력한 레이아웃 데이터를 객체에 담아 전달
	 * @return void 레이아웃 데이터 update 기능만 담당하는 메소드
	*/
	@Override
	public int updateLayout(Dm_layout_vo vo) throws Exception {
		return layoutMapper.updateLayout(vo);
	}
	
	/**
	 * selectLayoutCombo
	 * 페이지 등록 시 레이아웃 선택 콤보박스에 사용될 현재 등록된 레이아웃 모두 조회
	 * @param vo 레이아웃 데이터를 vo객체에 담아 전달, 현재는 아무값도 넘기지 않으나 추후에 이용할 수 있어 추가
	 * @return List<Dm_layout_vo> 조회된 레이아웃 데이터를 List 자료형으로 전달
	*/
	@Override
	public List<Dm_layout_vo> selectLayoutCombo(Dm_layout_vo vo) throws Exception {
		return layoutMapper.selectLayoutCombo(vo);
	}

	@Override
	public int selectLayoutFolderDupCnt(Dm_layout_vo vo) throws Exception {
		return layoutMapper.selectLayoutFolderDupCnt(vo);
	}
}
