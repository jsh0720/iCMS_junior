/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.cmm.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.diam.cmm.db.GroupMapper;
import egovframework.diam.cmm.model.Dm_group_vo;
import egovframework.diam.cmm.service.GroupService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;

/**
 * @Class Name : GroupServiceImpl.java
 * @Description : 관리자그룹 데이터 CRUD 메소드를 수행하는 Service Interface 구현 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

@Service("groupService")
public class GroupServiceImpl extends EgovAbstractServiceImpl implements GroupService {
	
	@Resource(name="groupMapper")
	private GroupMapper groupMapper;
	
	@Resource(name="egovDiamGroupIdGnrService")
	private EgovIdGnrService egovDiamGroupIdGnrService;
	
	/**
	 * selectGroupList
	 * 검색 값에 따른 그룹 리스트데이터 조회
	 * @param vo 그룹데이터 검색조건,페이징 값을 vo객체에 담아 전달
	 * @return List<Dm_board_vo> 조회된 그룹데이터를 List 자료형으로 전달
	*/
	@Override
	public List<Dm_group_vo> selectGroupList(Dm_group_vo vo) throws Exception {
		List<Dm_group_vo> result = groupMapper.selectGroupList(vo);
		return result;
	}
	
	/**
	 * selectGroupListCnt
	 * 검색 값에 따른 그룹 리스트데이터 개수 조회
	 * @param vo 그룹데이터 검색조건값을 vo객체에 담아 전달
	 * @return int 조회된 그룹데이터의 개수를 정수형으로 전달
	*/
	@Override
	public int selectGroupListCnt(Dm_group_vo vo) throws Exception {
		int result = groupMapper.selectGroupListCnt(vo);
		return result;
	}
	
	/**
	 * selectGroup
	 * 그룹 PK값으로 1건의 그룹데이터 조회
	 * @param vo 그룹 PK값을 vo객체에 담아 전달
	 * @return Dm_group_vo 조회된 그룹데이터를 그룹데이터 vo객체에 담아 전달
	*/
	@Override
	public Dm_group_vo selectGroup(Dm_group_vo vo) throws Exception {
		Dm_group_vo result = groupMapper.selectGroup(vo);
		return result;
	}
	
	/**
	 * deleteGroup
	 * 그룹 PK값으로 등록되어 있는 그룹데이터 삭제
	 * @param vo 사용자가 삭제하고자 하는 그룹데이터의 PK값을 vo객체에 담아 전달
	 * @return void 그룹데이터 delete 기능만 담당하는 메소드
	*/
	@Override
	@Transactional
	public void deleteGroup(List<Dm_group_vo> list) throws Exception {
		if (list.size() > 0) {
			list.forEach(item -> {
				int result = groupMapper.deleteGroup(item);
				if (result < 1) throw new RuntimeException();
			});
		}
	}
	
	/**
	 * insertGroup
	 * 사용자가 입력한 그룹 설정데이터 DB에 insert
	 * @param vo 사용자가 입력한 그룹 설정데이터를 vo객체에 담아 전달
	 * @return void 그룹데이터 insert 기능만 담당하는 메소드
	*/
	@Override
	public int insertGroup(Dm_group_vo vo) throws Exception {
		vo.setDm_group_id(egovDiamGroupIdGnrService.getNextStringId());
		return groupMapper.insertGroup(vo);
	}
	
	/**
	 * selectGroupTable
	 * 관리자 등록 시 관리자그룹 콤보박스에 사용되는 등록된 전체 그룹리스트  조회
	 * @param vo 관리자그룹 데이터를 vo객체에 담아 전달, 현재는 아무값도 넘기지 않으나 추후에 이용할 수 있어 추가
	 * @return List<Dm_group_vo> 조회된 페이지 데이터를 List 자료형으로 전달
	*/
	@Override
	public List<Dm_group_vo> selectGroupTable(Dm_group_vo vo) throws Exception {
		List<Dm_group_vo> result = groupMapper.selectGroupTable(vo);
		return result;
	}
	
	/**
	 * updateGroup
	 * 그룹 PK값으로 등록되어 있는 그룹데이터 DB에 update
	 * @param vo 사용자가 입력한 그룹데이터를 객체에 담아 전달
	 * @return void 그룹데이터 update 기능만 담당하는 메소드
	*/
	@Override
	public int updateGroup(Dm_group_vo vo) throws Exception {
		return groupMapper.updateGroup(vo);
	}

}
