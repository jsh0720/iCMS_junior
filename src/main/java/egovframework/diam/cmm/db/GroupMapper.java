/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.cmm.db;

import java.util.List;

import egovframework.diam.cmm.model.Dm_group_vo;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

/**
 * @Class Name : GroupMapper.java
 * @Description : 관리자그룹 데이터 CRUD 메소드를 수행하는 Mapper Interface
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

@Mapper("groupMapper")
public interface GroupMapper {
	
	/**
	 * selectGroupList
	 * 검색 값에 따른 그룹 리스트데이터 조회
	 * @param vo 그룹데이터 검색조건,페이징 값을 vo객체에 담아 전달
	 * @return List<Dm_board_vo> 조회된 그룹데이터를 List 자료형으로 전달
	*/
	public List<Dm_group_vo> selectGroupList(Dm_group_vo vo);
	
	/**
	 * selectGroupListCnt
	 * 검색 값에 따른 그룹 리스트데이터 개수 조회
	 * @param vo 그룹데이터 검색조건값을 vo객체에 담아 전달
	 * @return int 조회된 그룹데이터의 개수를 정수형으로 전달
	*/
	public int selectGroupListCnt(Dm_group_vo vo);
	
	/**
	 * selectGroup
	 * 그룹 PK값으로 1건의 그룹데이터 조회
	 * @param vo 그룹 PK값을 vo객체에 담아 전달
	 * @return Dm_group_vo 조회된 그룹데이터를 그룹데이터 vo객체에 담아 전달
	*/
	public Dm_group_vo selectGroup(Dm_group_vo vo);
	
	/**
	 * deleteGroup
	 * 그룹 PK값으로 등록되어 있는 그룹데이터 삭제
	 * @param vo 사용자가 삭제하고자 하는 그룹데이터의 PK값을 vo객체에 담아 전달
	 * @return void 그룹데이터 delete 기능만 담당하는 메소드
	*/
	public int deleteGroup(Dm_group_vo vo);
	
	/**
	 * insertGroup
	 * 사용자가 입력한 그룹 설정데이터 DB에 insert
	 * @param vo 사용자가 입력한 그룹 설정데이터를 vo객체에 담아 전달
	 * @return void 그룹데이터 insert 기능만 담당하는 메소드
	*/
	public int insertGroup(Dm_group_vo vo);
	
	/**
	 * selectGroupTable
	 * 관리자 등록 시 관리자그룹 콤보박스에 사용되는 등록된 전체 그룹리스트  조회
	 * @param vo 관리자그룹 데이터를 vo객체에 담아 전달, 현재는 아무값도 넘기지 않으나 추후에 이용할 수 있어 추가
	 * @return List<Dm_group_vo> 조회된 페이지 데이터를 List 자료형으로 전달
	*/
	public List<Dm_group_vo> selectGroupTable(Dm_group_vo vo);
	
	/**
	 * updateGroup
	 * 그룹 PK값으로 등록되어 있는 그룹데이터 DB에 update
	 * @param vo 사용자가 입력한 그룹데이터를 객체에 담아 전달
	 * @return void 그룹데이터 update 기능만 담당하는 메소드
	*/
	public int updateGroup(Dm_group_vo vo);
}
