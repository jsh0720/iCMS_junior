/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.service.design;

import java.util.List;

import egovframework.diam.biz.model.design.Dm_popup_vo;

/**
 * @Class Name : PopupService.java
 * @Description : 사용자페이지 메인페이지에 표출되는 팝업 데이터 CRUD 메소드를 수행하는 Service Interface
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

public interface PopupService {
	
	/**
	 * selectPopupListCnt
	 * 검색 값에 따른 팝업 리스트데이터 개수 조회
	 * @param vo 팝업데이터 검색조건값을 vo객체에 담아 전달
	 * @return int 조회된 팝업데이터의 개수를 정수형으로 전달
	*/
	public int selectPopupListCnt(Dm_popup_vo vo) throws Exception;
	
	/**
	 * selectPopupList
	 * 검색 값에 따른 팝업 리스트데이터 조회
	 * @param vo 팝업데이터 검색조건,페이징 값을 vo객체에 담아 전달
	 * @return List<Dm_popup_vo> 조회된 팝업데이터를 List 자료형으로 전달
	*/
	public List<Dm_popup_vo> selectPopupList(Dm_popup_vo vo) throws Exception;
	
	/**
	 * insertPopup
	 * 사용자가 입력한 팝업 설정데이터 DB에 insert
	 * @param vo 사용자가 입력한 팝업데이터를 vo객체에 담아 전달
	 * @return void 팝업데이터 insert 기능만 담당하는 메소드
	*/
	public int insertPopup(Dm_popup_vo vo) throws Exception;
	
	/**
	 * updatePopup
	 * 팝업 PK값으로 등록되어 있는 팝업데이터 DB에 update
	 * @param vo 사용자가 입력한 팝업데이터를 객체에 담아 전달
	 * @return void 팝업데이터 update 기능만 담당하는 메소드
	*/
	public int updatePopup(Dm_popup_vo vo) throws Exception;	
	
	/**
	 * deletePopup
	 * 팝업 PK값으로 등록되어 있는 팝업데이터 삭제
	 * @param vo 사용자가 삭제하고자 하는 팝업데이터의 PK값을 vo객체에 담아 전달
	 * @return void 팝업데이터 delete 기능만 담당하는 메소드
	*/
	public void deletePopup(List<Dm_popup_vo> list) throws Exception;
	
	/**
	 * selectPopup
	 * 팝업 PK값으로 1건의 팝업데이터 조회
	 * @param vo 팝업 PK값을 vo객체에 담아 전달
	 * @return Dm_popup_vo 조회된 팝업데이터를 팝업데이터 vo객체에 담아 전달
	*/
	public Dm_popup_vo selectPopup(Dm_popup_vo vo) throws Exception;
	
	/**
	 * selectPopupListForWeb
	 * 사용자페이지 메인페이지에서 표출되는 사용중인 팝업 리스트데이터 조회
	 * @param vo 도메인PK 값을 vo객체에 담아 전달
	 * @return List<Dm_popup_vo> 조회된 팝업데이터를 List 자료형으로 전달
	*/
	public List<Dm_popup_vo> selectPopupListForWeb(Dm_popup_vo vo) throws Exception;
	
}
