/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.db.board;

import java.util.List;

import egovframework.diam.biz.model.board.Dm_write_vo;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

/**
 * @Class Name : WriteMapper.java
 * @Description : 관리자/사용자페이지 게시물 페이지에서 사용하는 게시물 데이터 CRUD 메소드를 수행하는 Mapper Interface
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
 */

@Mapper("writeMapper")
public interface WriteMapper {	
	/**
	 * selectWriteList
	 * 검색 값에 따른 게시물 리스트데이터 조회
	 * @param vo 게시물데이터 검색조건,페이징 값을 vo객체에 담아 전달
	 * @return List<Dm_write_vo> 조회된 게시물데이터를 List 자료형으로 전달
	*/
	public List<Dm_write_vo> selectWriteList(Dm_write_vo vo);
	
	/**
	 * selectWrite
	 * 게시물 PK값과 게시판 아이디로 1건의 게시물데이터 조회
	 * @param vo 게시물 PK값과 게시판 아이디를 vo객체에 담아 전달
	 * @return Dm_write_vo 조회된 게시물데이터를 게시물데이터 vo객체에 담아 전달
	*/
	public Dm_write_vo selectWrite(Dm_write_vo vo);
	
	/**
	 * insertWrite
	 * 사용자가 입력한 게시물데이터 DB에 insert
	 * @param vo 사용자가 입력한 게시물데이터를 vo객체에 담아 전달
	 * @return void 게시물데이터 insert 기능만 담당하는 메소드
	*/
	public int insertWrite(Dm_write_vo vo);
	
	/**
	 * updateWrnumParent
	 * 게시물 등록 후 등록된 게시물 PK값을 음수처리하여 게시물 일련번호에 update
	 * @param vo 등록된 게시물 데이터의 PK값을 vo객체에 담아 전달
	 * @return void 게시물 일련번호 update 기능만 담당하는 메소드
	*/
	public int updateWrnumParent(Dm_write_vo vo) throws Exception;
	/**
	 * updateWrite
	 * 게시물 PK값으로 등록되어 있는 게시물데이터 DB에 update
	 * @param vo 사용자가 입력한 게시물데이터를 객체에 담아 전달
	 * @return void 게시물데이터 update 기능만 담당하는 메소드
	*/
	public int updateWrite(Dm_write_vo vo);
	
	public int updateWriteMove(Dm_write_vo vo);
	
	/**
	 * deleteWrite
	 * 게시물 PK값으로 등록되어 있는 게시물데이터 삭제
	 * @param vo 사용자가 삭제하고자 하는 게시물데이터의 PK값을 vo객체에 담아 전달
	 * @return void 게시물데이터 delete 기능만 담당하는 메소드
	*/
	public int deleteWrite(Dm_write_vo vo);
	
	public int selectWriteReplyCount(Dm_write_vo vo);
	
	/**
	 * selectWriteCommentReplyCnt
	 * 게시물 이동 시 댓글/답글이 존재하는지 조회
	 * @param vo 등록된 게시물 데이터를 vo객체에 담아 전달
	 * @return int 조회된 댓글/답글의 개수를 정수형으로 전달
	*/
	public int selectWriteCommentReplyCnt(Dm_write_vo vo);
	
	/**
	 * selectWriteListCnt
	 * 검색 값에 따른 게시물 리스트데이터 개수 조회
	 * @param vo 게시물데이터 검색조건값을 vo객체에 담아 전달
	 * @return int 조회된 게시물데이터의 개수를 정수형으로 전달
	*/
	public int selectWriteListCnt(Dm_write_vo vo);
	
	public List<Dm_write_vo> selectWriteListForWeb(Dm_write_vo vo);
	
	public int selectWriteCountForWeb(Dm_write_vo vo);
	
	public List<Dm_write_vo> selectWriteNoticeForWeb(Dm_write_vo vo);
	
	/**
	 * selectReplyListForWeb
	 * 사용자페이지 답글 리스트 조회
	 * @param vo 게시물데이터 그룹값, 게시판아이디를 vo객체에 담아 전달
	 * @return List<Dm_write_vo> 조회된 답글데이터를 List 자료형으로 전달
	*/
	public List<Dm_write_vo> selectReplyListForWeb(Dm_write_vo vo);
	
	/**
	 * selectWriteCheckPassword
	 * 사용자페이지 게시물 리스트에서 비밀글 접근 시 사용자가 입력한 비밀번호 검증
	 * @param vo 게시물데이터 PK, 게시판아이디, 입력한 비밀번호 값을 vo객체에 담아 전달
	 * @return Dm_write_vo 조회된 게시물데이터를 게시물데이터 vo객체에 담아 전달
	*/
	public Dm_write_vo selectWriteCheckPassword(Dm_write_vo vo);
		
	/**
	 * updateWriteHits
	 * 사용자가 게시물을 읽을 시 게시판설정의 조회수 설정에 해당하는 수치만큼 게시물조회수 update
	 * @param vo 게시물데이터 PK, 게시판아이디 값을 vo객체에 담아 전달
	 * @return void 게시물 조회수 update 기능만 담당하는 메소드
	*/
	public void updateWriteHits(Dm_write_vo vo);
	
	/**
	 * deleteWriteBoth
	 * 게시글 삭제 시 게시판 옵션에서 답글도 같이 삭제하는 옵션 선택 시 삭제할 게시글의 작성된 답글도 삭제
	 * @param vo 등록된 게시물 데이터를 vo객체에 담아 전달
	 * @return void 게시글 삭제 시 작성된 답글도 delete 하는 기능만 담당하는 메소드
	*/
	public void deleteWriteBoth(Dm_write_vo vo);

	
	/**
	 * selectWriteOri
	 * 게시글 수정 시 답글/댓글일 경우를 대비해 답급/댓글의 원글데이터를 조회
	 * @param vo 게시물 정렬값, 게시판아이디 값을 vo객체에 담아 전달
	 * @return Dm_write_vo 조회된 게시물데이터를 게시물데이터 vo객체에 담아 전달
	*/
	public Dm_write_vo selectWriteOri(Dm_write_vo vo);
	
	/**
	 * selectMaxReplyReply
	 * 답글 작성 시 현재 등록된 답변의 정렬값의 최대값을 조회
	 * @param vo 게시물 그룹 값, 게시판아이디 값을 vo객체에 담아 전달
	 * @return int 조회된 답글 작성 시 현재 등록된 답변의 정렬값의 최대값을 정수형으로 전달
	*/
	public int selectMaxReplyReply(Dm_write_vo vo);
	
	/**
	 * updateReplyReply
	 * 답글 작성 시 현재 등록된 답변정렬값을 업데이트 
	 * @param vo 등록된 게시물 데이터를 vo객체에 담아 전달
	 * @return void 답글 작성 시 현재 등록된 답변정렬값을 update 하는 기능만 담당하는 메소드
	*/
	public void updateReplyReply(Dm_write_vo vo);
		
	/**
	 * selectMaxCommentReply
	 * 댓글 작성 시 현재 등록된 댓글의 정렬값의 최대값을 조회
	 * @param vo 게시물 그룹 값, 게시판아이디 값을 vo객체에 담아 전달
	 * @return int 조회된 댓글 작성 시 현재 등록된 댓글의 정렬값의 최대값을 정수형으로 전달
	*/
	public int selectMaxCommentReply(Dm_write_vo vo);
	
	public List<Dm_write_vo> selectCommentList(Dm_write_vo vo);
	
	public List<Dm_write_vo> selectParentComment(Dm_write_vo vo);
	
	public List<Dm_write_vo> selectCommentChildrenAll(Dm_write_vo vo);
	
	public int updateComment(Dm_write_vo vo);
	
	public int insertComment(Dm_write_vo vo);
	
	public List<Dm_write_vo> selectReplyList(Dm_write_vo vo);
	
	public List<Dm_write_vo> selectMainWriteList(Dm_write_vo vo);
}
