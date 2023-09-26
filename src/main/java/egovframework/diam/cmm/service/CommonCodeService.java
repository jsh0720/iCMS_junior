/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.cmm.service;

import java.util.List;

import egovframework.diam.cmm.model.Dm_common_code_vo;

/**
 * @Class Name : CommonCodeService.java
 * @Description : 공통코드 데이터 CRUD 메소드를 수행하는 Service Interface
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

public interface CommonCodeService {
	
	/**
	 * selectCommonCodeListCnt
	 * 검색 값에 따른 공통코드 리스트데이터 개수 조회
	 * @param vo 공통코드 데이터 검색조건값을 vo객체에 담아 전달
	 * @return int 조회된 공통코드 데이터의 개수를 정수형으로 전달
	*/
	public int selectCommonCodeListCnt(Dm_common_code_vo vo) throws Exception;
	
	/**
	 * selectCommonCodeList
	 * 검색 값에 따른 공통코드 리스트데이터 조회
	 * @param vo 공통코드 데이터 검색조건,페이징 값을 vo객체에 담아 전달
	 * @return List<Dm_common_code_vo> 조회된 공통코드 데이터를 List 자료형으로 전달
	*/
	public List<Dm_common_code_vo> selectCommonCodeList(Dm_common_code_vo vo) throws Exception;
	
	/**
	 * selectCommonCode
	 * 공통코드 PK값으로 1건의공통코드 데이터 조회
	 * @param vo 공통코드 PK값을 vo객체에 담아 전달
	 * @return Dm_common_code_vo 조회된 공통코드데이터를 공통코드데이터 vo객체에 담아 전달
	*/
	public Dm_common_code_vo selectCommonCode(Dm_common_code_vo vo) throws Exception;
	
	/**
	 * deleteCommonCode
	 * 공통코드 PK값으로 등록되어 있는 공통코드 데이터 삭제
	 * @param vo 사용자가 삭제하고자 하는 공통코드 데이터의 PK값을 vo객체에 담아 전달
	 * @return void 공통코드 데이터 delete 기능만 담당하는 메소드
	*/
	public void deleteCommonCode(List<Dm_common_code_vo> list) throws Exception;
	
	/**
	 * insertCommonCode
	 * 사용자가 입력한 공통코드 데이터 DB에 insert
	 * @param vo 사용자가 입력한 공통코드 데이터를 vo객체에 담아 전달
	 * @return void 공통코드 데이터 insert 기능만 담당하는 메소드
	*/
	public int insertCommonCode(Dm_common_code_vo vo) throws Exception;
	
	/**
	 * updateCommonCode
	 * 공통코드 PK값으로 등록되어 있는 공통코드 데이터 DB에 update
	 * @param vo 사용자가 입력한 공통코드 데이터를 객체에 담아 전달
	 * @return void 공통코드 데이터 update 기능만 담당하는 메소드
	*/
	public int updateCommonCode(Dm_common_code_vo vo) throws Exception;
	
	/**
	 * selectCommonCodeValueDup
	 * 공통코드 그룹, value값을 검증하여 동일 그룹에 중복된 값 조회
	 * @param vo 공통코드 그룹, value값을 vo객체에 담아 전달
	 * @return Dm_common_code_vo 조회된 공통코드데이터의 개수를 정수형으로 전달
	*/
	public int selectCommonCodeValueDup(Dm_common_code_vo vo) throws Exception;
	
	/**
	 * selectCommonCodeValueDup
	 * 공통코드 그룹, 이름값을 검증하여 동일 그룹에 중복된 값 조회
	 * @param vo 공통코드 그룹, 이름값을 vo객체에 담아 전달
	 * @return Dm_common_code_vo 조회된 공통코드데이터의 개수를 정수형으로 전달
	*/
	public int selectCommonCodeNameDup(Dm_common_code_vo vo) throws Exception;
	
	/**
	 * selectCommonCodeValueDup
	 * 공통코드 그룹, 밸류 값으로 코드 명 조회
	 * @param vo 공통코드 그룹, value값을 vo객체에 담아 전달
	 * @return Dm_common_code_vo
	*/
	public Dm_common_code_vo selectCodeNameByValue(Dm_common_code_vo vo) throws Exception;
}
