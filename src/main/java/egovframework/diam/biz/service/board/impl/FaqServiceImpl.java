/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.service.board.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.diam.biz.db.board.FaqMapper;
import egovframework.diam.biz.model.board.Dm_faq_vo;
import egovframework.diam.biz.service.board.FaqService;
import egovframework.diam.cmm.util.MessageCode;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : FaqServiceImpl.java
 * @Description : 관리자/사용자 FAQ 페이지에서 사용하는 FAQ데이터 CRUD 메소드를 수행하는 Service Interface 구현 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

@Service("faqService")
public class FaqServiceImpl extends EgovAbstractServiceImpl implements FaqService {
	
	@Resource(name="faqMapper")
	private FaqMapper mapper;
	
	/**
	 * selectFaqList
	 * 검색 값에 따른 FAQ 리스트데이터 조회
	 * @param vo FAQ데이터 검색조건,페이징 값을 vo객체에 담아 전달
	 * @return List<Dm_faq_vo> 조회된 FAQ데이터를 List 자료형으로 전달
	*/
	@Override
	public List<Dm_faq_vo> selectFaqList(Dm_faq_vo vo) throws Exception {
		List <Dm_faq_vo> faqList = mapper.selectFaqList(vo);
		return faqList;
	}
	
	/**
	 * selectFaq
	 * FAQ PK값으로 1건의 게시판데이터 조회
	 * @param vo FAQ PK값을 vo객체에 담아 전달
	 * @return Dm_faq_vo 조회된 FAQ데이터를 FAQ데이터 vo객체에 담아 전달
	*/
	@Override
	public Dm_faq_vo selectFaq(Dm_faq_vo vo) throws Exception {
		return mapper.selectFaq(vo);
	}
	
	/**
	 * insertFaq
	 * 사용자가 입력한 FAQ데이터 DB에 insert
	 * @param vo 사용자가 입력한 FAQ데이터를 vo객체에 담아 전달
	 * @return void FAQ데이터 insert 기능만 담당하는 메소드
	*/
	@Override
	public int insertFaq(Dm_faq_vo vo) throws Exception {
		return mapper.insertFaq(vo);
	}
	
	/**
	 * updateFaq
	 * FAQ PK값으로 등록되어 있는 FAQ데이터 DB에 update
	 * @param vo 사용자가 입력한 FAQ데이터를 객체에 담아 전달
	 * @return void FAQ데이터 update 기능만 담당하는 메소드
	*/
	@Override
	public int updateFaq(Dm_faq_vo vo) throws Exception {
		return mapper.updateFaq(vo);
	}
	
	/**
	 * deleteFaq
	 * FAQ PK값으로 등록되어 있는 FAQ데이터 삭제
	 * @param vo 사용자가 삭제하고자 하는 FAQ데이터의 PK값을 vo객체에 담아 전달
	 * @return void FAQ데이터 delete 기능만 담당하는 메소드
	*/
	@Override
	@Transactional
	public void deleteFaq(List<Dm_faq_vo> list) throws Exception {
		if (list.size() > 0) {
			list.forEach(item -> {
				int result = mapper.deleteFaq(item);
				if (result < 1) throw new RuntimeException(MessageCode.CMS_DELETE_FAIL.getLog());
			});
		}
	}
	
	/**
	 * selectFaqListCnt
	 * 검색 값에 따른 FAQ 리스트데이터 개수 조회
	 * @param vo FAQ데이터 검색조건값을 vo객체에 담아 전달
	 * @return int 조회된 FAQ데이터의 개수를 정수형으로 전달
	*/
	@Override
	public int selectFaqListCnt(Dm_faq_vo vo) throws Exception {
		int result = mapper.selectFaqListCnt(vo);
		return result;
	}
	
	/**
	 * selectFaqListForWeb
	 * 사용자 FAQ페이지에 출력될 FAQ 리스트데이터 조회
	 * @param vo 사용자페이지에서 전달받은 도메인의 PK값을 vo객체에 담아 전달
	 * @return List<Dm_faq_vo> 조회된 FAQ데이터를 List 자료형으로 전달
	*/
	@Override
	public List<Dm_faq_vo> selectFaqListForWeb(Dm_faq_vo vo) throws Exception {
		List<Dm_faq_vo> result = mapper.selectFaqListForWeb(vo);
		return result;
	}

}
