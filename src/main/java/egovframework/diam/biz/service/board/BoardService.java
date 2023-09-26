/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.service.board;

import java.util.List;

import egovframework.diam.biz.model.board.Dm_board_vo;

/**
 * @Class Name : BoardService.java
 * @Description : 게시판관리 게시판 데이터 CRUD 메소드를 수행하는 Service Interface
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

public interface BoardService {
	/**
	 * selectBoardList
	 * 검색 값에 따른 게시판 리스트데이터 조회
	 * @param vo 게시판데이터 검색조건,페이징 값을 vo객체에 담아 전달
	 * @return List<Dm_board_vo> 조회된 게시판데이터를 List 자료형으로 전달
	*/
	public List<Dm_board_vo> selectBoardList(Dm_board_vo vo) throws Exception;
	
	/**
	 * selectBoard
	 * 게시판 PK값으로 1건의 게시판데이터 조회
	 * @param vo 게시판 PK값을 vo객체에 담아 전달
	 * @return Dm_board_vo 조회된 게시판데이터를 게시판데이터 vo객체에 담아 전달
	*/
	public Dm_board_vo selectBoard(Dm_board_vo vo) throws Exception;
	
	/**
	 * insertBoard
	 * 사용자가 입력한 게시판 설정데이터 DB에 insert
	 * @param vo 사용자가 입력한 게시판 설정데이터를 vo객체에 담아 전달
	 * @return void 게시판데이터 insert 기능만 담당하는 메소드
	*/	
	public int insertBoard(Dm_board_vo vo) throws Exception;
	
	/**
	 * updateBoard
	 * 게시판 PK값으로 등록되어 있는 게시판 설정데이터 DB에 update
	 * @param vo 사용자가 입력한 게시판 설정데이터를 객체에 담아 전달
	 * @return void 게시판데이터 update 기능만 담당하는 메소드
	*/
	public int updateBoard(Dm_board_vo vo) throws Exception;
	
	/**
	 * deleteBoard
	 * 게시판 PK값으로 등록되어 있는 게시판 설정데이터 삭제
	 * @param vo 사용자가 삭제하고자 하는 게시판데이터의 PK값을 vo객체에 담아 전달
	 * @return void 게시판데이터 delete 기능만 담당하는 메소드
	*/
	public void deleteBoard(List<Dm_board_vo> list) throws Exception;
	
	/**
	 * selectBoardListCnt
	 * 검색 값에 따른 게시판 리스트데이터 개수 조회
	 * @param vo 게시판데이터 검색조건값을 vo객체에 담아 전달
	 * @return int 조회된 게시판데이터의 개수를 정수형으로 전달
	*/
	public int selectBoardListCnt(Dm_board_vo vo) throws Exception;
	
	/**
	 * selectBoardListForWrite
	 * 게시글 리스트 조회 시 필요한 게시판리스트 데이터 조회
	 * @param vo 검색 필터링 시 게시판 PK값을 vo객체에 담아 전달
	 * @return List<Dm_board_vo> 조회된 게시판데이터의 개수를 List 자료형으로 전달
	*/
	public List<Dm_board_vo> selectBoardListForWrite(Dm_board_vo vo) throws Exception;
	
	/**
	 * selectBoardbyDmTable
	 * 게시판 아이디값으로 1건의 게시판데이터 조회
	 * @param vo 게시판 아이디값을 vo객체에 담아 전달
	 * @return Dm_board_vo 조회된 게시판데이터를 게시판데이터 vo객체에 담아 전달
	*/	
	public Dm_board_vo selectBoardByDmtable(Dm_board_vo vo) throws Exception;
	
	public List<Dm_board_vo> selectMainBoardList(Dm_board_vo vo) throws Exception;
}
