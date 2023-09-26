/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.db.display;

import java.util.List;

import egovframework.diam.biz.model.display.Dm_pages_vo;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

/**
 * @Class Name : PageMapper.java
 * @Description : 사용자 서브페이지에 사용되는 페이지 데이터 CRUD 메소드를 수행하는 Mapper Interface
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

@Mapper("pageMapper")
public interface PageMapper {
	
	/**
	 * selectPageListCnt
	 * 검색 값에 따른 페이지 리스트데이터 개수 조회
	 * @param vo 페이지데이터 검색조건값을 vo객체에 담아 전달
	 * @return int 조회된 페이지데이터의 개수를 정수형으로 전달
	*/
	public int selectPageListCnt(Dm_pages_vo vo);
	
	/**
	 * selectPageList
	 * 검색 값에 따른 페이지 리스트데이터 조회
	 * @param vo 페이지데이터 검색조건,페이징 값을 vo객체에 담아 전달
	 * @return List<Dm_pages_vo> 조회된 페이지데이터를 List 자료형으로 전달
	*/
	public List<Dm_pages_vo> selectPageList(Dm_pages_vo vo);
	
	/**
	 * selectPage
	 * 페이지 PK값으로 1건의 페이지데이터 조회
	 * @param vo 페이지 PK값을 vo객체에 담아 전달
	 * @return Dm_pages_vo 조회된 페이지데이터를 페이지데이터 vo객체에 담아 전달
	*/
	public Dm_pages_vo selectPage(Dm_pages_vo vo);
	
	/**
	 * selectPageDmUid
	 * 페이지 PK값으로 1건의 페이지데이터 조회
	 * @param vo 페이지 Uid값을 vo객체에 담아 전달
	 * @return Dm_pages_vo 조회된 페이지데이터를 페이지데이터 vo객체에 담아 전달
	*/
	public Dm_pages_vo selectPageDmUid(Dm_pages_vo vo);
	
	/**
	 * selectPageMainContent
	 * 접속한 URL에 해당하는 도메인의 메인페이지 조회
	 * @param vo 도메인PK값을 vo객체에 담아 전달
	 * @return Dm_pages_vo 조회된 페이지데이터를 페이지데이터 vo객체에 담아 전달
	*/
	public Dm_pages_vo selectPageMainContent(Dm_pages_vo vo);
	
	/**
	 * selectPageHistory
	 * 페이지 Uid 값에 해당하는 등록된 페이지 버전정보 조회
	 * @param vo 페이지 Uid값을 vo객체에 담아 전달
	 * @return List<Dm_pages_vo> 조회된 페이지데이터를 List 자료형으로 전달
	*/
	public List<Dm_pages_vo> selectPageHistory(Dm_pages_vo vo);
	
	/**
	 * selectPageVersion
	 * 페이지 Uid 값에 해당하는 등록된 페이지 중 최상위 버전정보 조회
	 * @param vo 페이지 Uid값을 vo객체에 담아 전달
	 * @return String 조회된 페이지 버전정보를 문자열에 담아 전달
	*/
	public String selectPageVersion(Dm_pages_vo vo);
	
	/**
	 * updatePageDmStatus
	 * 페이지정보 update 시 이전버전 페이지정보 비활성화
	 * @param vo 페이지 Uid값을 vo객체에 담아 전달
	 * @return void 이전버전 페이지정보 update 기능만 담당하는 메소드
	*/
	public int updatePageDmStatus(Dm_pages_vo vo);
	
	/**
	 * insertPage
	 * 사용자가 입력한 페이지 데이터 DB에 insert
	 * @param vo 사용자가 입력한 페이지 데이터를 vo객체에 담아 전달
	 * @return void 페이지데이터 insert 기능만 담당하는 메소드
	*/
	public int insertPage(Dm_pages_vo vo);
	
	public int updatePage(Dm_pages_vo vo);
		
	/**
	 * updatePageDmStatusByDmuid
	 * 페이지 버전변경 시 페이지Uid에 해당하는 페이지를 모두 비활성화
	 * @param vo 페이지 Uid값을 vo객체에 담아 전달
	 * @return void Uid에 해당하는 페이지데이터 update 기능만 담당하는 메소드
	*/
	public int updatePageDmStatusByDmuid(Dm_pages_vo vo);
	
	/**
	 * updatePageDmStatusByDmid
	 * 페이지 버전변경 시 페이지PK에 해당하는 페이지를 활성화
	 * @param vo 페이지 PK값을 vo객체에 담아 전달
	 * @return void 페이지 PK에 해당하는 페이지데이터 update 기능만 담당하는 메소드
	*/
	public int updatePageDmStatusByDmid(Dm_pages_vo vo);
	
	/**
	 * deletePage
	 * 페이지 PK값으로 등록되어 있는 페이지데이터 삭제
	 * @param vo 사용자가 삭제하고자 하는 페이지데이터의 PK값을 vo객체에 담아 전달
	 * @return void 페이지데이터 delete 기능만 담당하는 메소드
	*/
	public int deletePage(Dm_pages_vo vo);
	
	/**
	 * selectPageListCombo
	 * 사용자메뉴 등록 시 페이지 선택 콤보박스에 사용될 도메인PK에 해당하는 페이지 중 사용중이고 메인페이지가 아닌 페이지 조회
	 * @param vo 도메인PK값을 vo객체에 담아 전달
	 * @return List<Dm_pages_vo> 조회된 페이지 데이터를 List 자료형으로 전달
	*/
	public List<Dm_pages_vo> selectPageListCombo(Dm_pages_vo vo);
	
	/**
	 * selectPageMainCnt
	 * 메인페이지 등록 시 중복등록 방지를 위해 도메인에 해당하는 사용중이 메인페이지 개수 조회
	 * @param vo 도메인PK값, 페이지 Uid값을 vo객체에 담아 전달
	 * @return int 조회된 페이지 데이터의 개수를 정수형으로 전달
	*/
	public int selectPageMainCnt(Dm_pages_vo vo);
	
	/**
	 * selectPageBoardChk
	 * 게시판 타입 페이지 등록시 동일 게시판 중복등록 방지를 위해 게시판페이지 개수 조회
	 * @param vo 도메인PK값, 페이지 Uid값을 vo객체에 담아 전달
	 * @return int 조회된 페이지 데이터의 개수를 정수형으로 전달
	*/
	public int selectPageBoardChk(Dm_pages_vo vo);
	
	/**
	 * selectPageDmUidCnt
	 * 페이지 등록 시 Uid값 위변조 방지를 위해 전달받은 Uid에 해당하는 페이지 개수 조회
	 * @param vo 도메인PK값, 페이지 Uid값을 vo객체에 담아 전달
	 * @return int 조회된 페이지 데이터의 개수를 정수형으로 전달
	*/
	public int selectPageDmUidCnt(Dm_pages_vo vo);
	
	/**
	 * selectPageMaxVersionByDmUid
	 * 페이지 삭제 시 전달받은 Uid에 해당하는 최상위 버전정보 조회
	 * @param vo 페이지 Uid값을 vo객체에 담아 전달
	 * @return String 조회된 페이지 버전정보를 문자열에 담아 전달
	*/
	public String selectPageMaxVersionByDmUid(Dm_pages_vo vo);
	
	/**
	 * updatePageDmStatusAfterDel
	 * 페이지 삭제 시 최상위 버전의 페이지를 활성화
	 * @param vo 페이지 Uid값, 페이지 버전값을 vo객체에 담아 전달
	 * @return void 페이지Uid에 해당하는 최상위버전의 페이지정보 update 기능만 담당하는 메소드
	*/
	public void updatePageDmStatusAfterDel(Dm_pages_vo vo);
	
	/**
	 * selectPageByVersion
	 * 페이지 Uid, 버전 정보로 1건의 페이지데이터 조회
	 * @param vo 페이지 Uid값, 페이지 버전값을 vo객체에 담아 전달
	 * @return Dm_pages_vo 조회된 페이지데이터를 페이지데이터 vo객체에 담아 전달
	*/
	public Dm_pages_vo selectPageByVersion(Dm_pages_vo vo);

	/**
	 * selectPageUtilCnt
	 * 로그인/회원가입 등 기능 페이지 등록 시 중복등록 방지를 위해 도메인에 해당하는 사용중인 기능 페이지 개수 조회
	 * @param vo 도메인PK값, 페이지 Uid값, 페이지타입을 vo객체에 담아 전달
	 * @return int 조회된 페이지 데이터의 개수를 정수형으로 전달
	*/
	public int selectPageUtilCnt(Dm_pages_vo vo);
	
	/**
	 * selectPageUtilUid
	 * 사용자페이지 상단에 사용될 로그인/회원가입/정보수정 버튼 링크에 필요한 고유UID값 조회
	 * @param domain_id 도메인PK값을 문자열에 담아 전달
	 * @param page_type 페이지타입을 문자열에 담아 전달
	 * @return String 조회된 페이지의 고유 UID값을 문자열로 전달
	*/
	public String selectPageUtilUid(String domain_id, String page_type);
}
