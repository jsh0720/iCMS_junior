/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.db.board;

import java.util.List;

import egovframework.diam.biz.model.board.Dm_faq_vo;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

/**
 * @Class Name : FaqMapper.java
 * @Description : 관리자/사용자 FAQ 페이지에서 사용하는 FAQ데이터 CRUD 메소드를 수행하는 Mapper Interface
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
 */

@Mapper("faqMapper")
public interface FaqMapper {
	/**
	 * selectFaqList
	 * 검색 값에 따른 FAQ 리스트데이터 조회
	 * @param vo FAQ데이터 검색조건,페이징 값을 vo객체에 담아 전달
	 * @return List<Dm_faq_vo> 조회된 FAQ데이터를 List 자료형으로 전달
	*/
	public List<Dm_faq_vo> selectFaqList(Dm_faq_vo vo);
	
	/**
	 * selectFaq
	 * FAQ PK값으로 1건의 게시판데이터 조회
	 * @param vo FAQ PK값을 vo객체에 담아 전달
	 * @return Dm_faq_vo 조회된 FAQ데이터를 FAQ데이터 vo객체에 담아 전달
	*/
	public Dm_faq_vo selectFaq(Dm_faq_vo vo);
	
	/**
	 * insertFaq
	 * 사용자가 입력한 FAQ데이터 DB에 insert
	 * @param vo 사용자가 입력한 FAQ데이터를 vo객체에 담아 전달
	 * @return void FAQ데이터 insert 기능만 담당하는 메소드
	*/
	public int insertFaq(Dm_faq_vo vo);
	
	/**
	 * updateFaq
	 * FAQ PK값으로 등록되어 있는 FAQ데이터 DB에 update
	 * @param vo 사용자가 입력한 FAQ데이터를 객체에 담아 전달
	 * @return void FAQ데이터 update 기능만 담당하는 메소드
	*/
	public int updateFaq(Dm_faq_vo vo);
	
	/**
	 * deleteFaq
	 * FAQ PK값으로 등록되어 있는 FAQ데이터 삭제
	 * @param vo 사용자가 삭제하고자 하는 FAQ데이터의 PK값을 vo객체에 담아 전달
	 * @return void FAQ데이터 delete 기능만 담당하는 메소드
	*/
	public int deleteFaq(Dm_faq_vo vo);
	
	/**
	 * selectFaqListCnt
	 * 검색 값에 따른 FAQ 리스트데이터 개수 조회
	 * @param vo FAQ데이터 검색조건값을 vo객체에 담아 전달
	 * @return int 조회된 FAQ데이터의 개수를 정수형으로 전달
	*/
	public int selectFaqListCnt(Dm_faq_vo vo);
	
	/**
	 * selectFaqListForWeb
	 * 사용자 FAQ페이지에 출력될 FAQ 리스트데이터 조회
	 * @param vo 사용자페이지에서 전달받은 도메인의 PK값을 vo객체에 담아 전달
	 * @return List<Dm_faq_vo> 조회된 FAQ데이터를 List 자료형으로 전달
	*/
	public List<Dm_faq_vo> selectFaqListForWeb(Dm_faq_vo vo);	
}
