/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.cmm.db;

import java.util.List;

import egovframework.diam.cmm.model.Dm_common_code_vo;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

/**
 * @className : CommonCodeMapper.java
 * @author : (주)디자인아이엠
 * @Date : 2023. 3. 21.
 * @Description : 공통코드 매퍼
 */
@Mapper("commonCodeMapper")
public interface CommonCodeMapper {

	/**
	 * @Method : selectCommonCodeListCnt
	 * @Description : 공통코드 리스트 수 조회
	 * @param vo
	 * @return int
	 */
	public int selectCommonCodeListCnt(Dm_common_code_vo vo);
	
	/**
	 * @Method : selectCommonCodeList
	 * @Description : 공통코드 리스트 조회
	 * @param vo
	 * @return list
	 */
	public List<Dm_common_code_vo> selectCommonCodeList(Dm_common_code_vo vo);
	
	/**
	 * @Method : selectCommonCode
	 * @Description : 공통코드 상세
	 * @param vo
	 * @return Dm_common_code_vo
	 */
	public Dm_common_code_vo selectCommonCode(Dm_common_code_vo vo);
	
	/**
	 * @Method : deleteCommonCode
	 * @Description : 공통 코드 삭제
	 * @param vo
	 * @return int
	 */
	public int deleteCommonCode(Dm_common_code_vo vo);
	
	/**
	 * @Method : insertCommonCode
	 * @Description : 공통코드 등록
	 * @param vo
	 * @return int
	 */
	public int insertCommonCode(Dm_common_code_vo vo);
	
	/**
	 * @Method : updateCommonCode
	 * @Description : 공통코드 수정
	 * @param vo
	 * @return int
	 */
	public int updateCommonCode(Dm_common_code_vo vo);
	
	/**
	 * @Method : selectCommonCodeValueDup
	 * @Description : 공통코드 중복체크
	 * @param vo
	 * @return int
	 */
	public int selectCommonCodeValueDup(Dm_common_code_vo vo);
	
	/**
	 * @Method : selectCommonCodeNameDup
	 * @Description : 공통코드 그룹, 이름 중복 체크
	 * @param vo
	 * @return int
	 */
	public int selectCommonCodeNameDup(Dm_common_code_vo vo);
	
	/**
	 * @Method : selectCommonCodeNameDup
	 * @Description : 그룹, 값으로 코드명 조회
	 * @param vo
	 * @return Dm_common_code_vo
	 */
	public Dm_common_code_vo selectCodeNameByValue(Dm_common_code_vo vo);
}
