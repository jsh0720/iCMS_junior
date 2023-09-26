/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.service.board;

import java.util.List;
import java.util.Map;

import egovframework.diam.biz.model.board.Dm_board_vo;
import egovframework.diam.biz.model.board.Dm_write_vo;

/**
 * @Class Name : WriteService.java
 * @Description : 관리자/사용자페이지 게시물 페이지에서 사용하는 게시물 데이터 CRUD 메소드를 수행하는 Service Interface
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

public interface WriteService {
	/**
	 * selectWriteList
	 * 검색 값에 따른 게시물 리스트데이터 조회
	 * @param vo 게시물데이터 검색조건,페이징 값을 vo객체에 담아 전달
	 * @return List<Dm_write_vo> 조회된 게시물데이터를 List 자료형으로 전달
	*/
	public List<Dm_write_vo> selectWriteList(Dm_write_vo vo) throws Exception;
	
	/**
	 * selectWrite
	 * 게시물 PK값과 게시판 아이디로 1건의 게시물데이터 조회
	 * @param vo 게시물 PK값과 게시판 아이디를 vo객체에 담아 전달
	 * @return Dm_write_vo 조회된 게시물데이터를 게시물데이터 vo객체에 담아 전달
	*/
	public Dm_write_vo selectWrite(Dm_write_vo vo) throws Exception;
	
	/**
	 * insertWrite
	 * 사용자가 입력한 게시물데이터 DB에 insert
	 * @param vo 사용자가 입력한 게시물데이터를 vo객체에 담아 전달
	 * @return void 게시물데이터 insert 기능만 담당하는 메소드
	*/
	public int insertWrite(Dm_write_vo vo) throws Exception;
	
	/**
	 * updateWrite
	 * 게시물 PK값으로 등록되어 있는 게시물데이터 DB에 update
	 * @param vo 사용자가 입력한 게시물데이터를 객체에 담아 전달
	 * @return void 게시물데이터 update 기능만 담당하는 메소드
	*/
	public int updateWrite(Dm_write_vo vo) throws Exception;
	
	/**
	 * deleteWrite
	 * 게시물 PK값으로 등록되어 있는 게시물데이터 삭제
	 * @param vo 사용자가 삭제하고자 하는 게시물데이터의 PK값을 vo객체에 담아 전달
	 * @return void 게시물데이터 delete 기능만 담당하는 메소드
	*/
	public void deleteWrite(List<Dm_write_vo> list) throws Exception;
	
	
	public int selectWriteReplyCount(Dm_write_vo vo) throws Exception;
	/**
	 * selectWriteCommentReplyCnt
	 * 게시물 이동 시 댓글/답글이 존재하는지 조회
	 * @param vo 등록된 게시물 데이터를 vo객체에 담아 전달
	 * @return int 조회된 댓글/답글의 개수를 정수형으로 전달
	*/
	public int selectWriteCommentReplyCnt(Dm_write_vo vo) throws Exception;
	
	/**
	 * selectWriteListCnt
	 * 검색 값에 따른 게시물 리스트데이터 개수 조회
	 * @param vo 게시물데이터 검색조건값을 vo객체에 담아 전달
	 * @return int 조회된 게시물데이터의 개수를 정수형으로 전달
	*/
	public int selectWriteListCnt(Dm_write_vo vo) throws Exception;
	
	/**
	 * moveWrite
	 * 특정 게시물을 다른 게시판으로 이동 시 기존 게시판의 게시물을 delete, 이동할 게시판에 게시물데이터 insert한 후 변경된 PK로 부모아이디 업데이트
	 * @param vo 사용자가 이동하고자 하는 게시물데이터를 vo객체에 담아 전달
	 * @return void 게시물데이터 이동 기능만 담당하는 메소드
	*/
	public void moveWrite(List<Dm_write_vo> list) throws Exception;
	
	
	public int selectWriteCountForWeb(Dm_write_vo vo) throws Exception;
	
	/**
	 * selectWriteListForWeb
	 * 사용자페이지 게시물 리스트 검색,정렬 값에 따른 게시물 리스트데이터 조회
	 * @param vo 게시물데이터 검색조건,페이징,정렬 값을 vo객체에 담아 전달
	 * @return List<Dm_write_vo> 조회된 게시물데이터를 List 자료형으로 전달
	*/
	public List<Dm_write_vo> selectWriteListForWeb(Dm_write_vo vo) throws Exception;
	
	
	public List<Dm_write_vo> selectWriteNoticeForWeb(Dm_write_vo vo) throws Exception;
	/**
	 * selectWriteCheckPassword
	 * 사용자페이지 게시물 리스트에서 비밀글 접근 시 사용자가 입력한 비밀번호 검증
	 * @param vo 게시물데이터 PK, 게시판아이디, 입력한 비밀번호 값을 vo객체에 담아 전달
	 * @return Dm_write_vo 조회된 게시물데이터를 게시물데이터 vo객체에 담아 전달
	*/
	public Dm_write_vo selectWriteCheckPassword(Dm_write_vo vo) throws Exception;

	/**
	 * selectWriteAndUpdateHits
	 * 사용자가 게시물 상세보기 시 게시물데이터 조회와 조회수 업데이트를 동시에 수행
	 * @param vo 게시물데이터 PK, 게시판아이디, 증가할 조회수를 vo객체에 담아 전달
	 * @return Dm_write_vo 조회된 게시물데이터를 게시물데이터 vo객체에 담아 전달
	*/
	public Dm_write_vo selectWriteAndUpdateHits(Dm_write_vo vo) throws Exception;

	/**
	 * selectWriteOri
	 * 게시글 수정 시 답글/댓글일 경우를 고려해 답급/댓글의 원글데이터를 조회
	 * @param vo 게시물 정렬값, 게시판아이디 값을 vo객체에 담아 전달
	 * @return Dm_write_vo 조회된 게시물데이터를 게시물데이터 vo객체에 담아 전달
	*/
	public Dm_write_vo selectWriteOri(Dm_write_vo vo) throws Exception;
	
	public int insertComment(Dm_write_vo vo) throws Exception;
	
	public List<Dm_write_vo> selectParentComment(Dm_write_vo vo) throws Exception;
	
	public List<Dm_write_vo> selectCommentChildrenAll(Dm_write_vo vo) throws Exception;
	
	public void deleteComment(List<Dm_write_vo> list) throws Exception;
	
	public int selectMaxCommentReply(Dm_write_vo vo) throws Exception;
	
	public int updateComment(Dm_write_vo vo) throws Exception;
	
	public List<Dm_write_vo> selectReplyList(Dm_write_vo vo) throws Exception;
	
	public int selectMaxReplyReply(Dm_write_vo vo) throws Exception;
	
	public Map<String, List<Dm_write_vo>> selectMainWriteList(List<Dm_board_vo> list) throws Exception;
}
