/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.cmm.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.diam.biz.model.board.Dm_board_vo;
import egovframework.diam.biz.model.config.Dm_domain_list_vo;
import egovframework.diam.biz.model.display.Dm_layout_vo;
import egovframework.diam.cmm.db.CommonMapper;
import egovframework.diam.cmm.model.Dm_common_code_vo;
import egovframework.diam.cmm.service.CommonService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : CommonServiceImpl.java
 * @Description : CMS 기능별 공통으로 사용하는 조회 메소드를 수행하는 Service Interface 구현 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

@Service("commonService")
public class CommonServiceImpl extends EgovAbstractServiceImpl implements CommonService {
	
	@Resource(name="commonMapper")
	private CommonMapper mapper;
	
	/**
	 * selectDm_common_code
	 * 파라미터로 넘겨받은 공통코드 PK/그룹/value/이름 등의 정보로 공통코드 리스트 조회
	 * @param vo 공통코드 PK/그룹/value/이름 등의 값을 vo객체에 담아 전달
	 * @return List<Dm_common_code_vo> 조회된 공통코드 데이터의 개수를 List 자료형으로 전달
	*/
	@Override
	public List<Dm_common_code_vo> selectDm_common_code(Dm_common_code_vo vo) throws Exception {
		List<Dm_common_code_vo> result = mapper.selectDm_common_code(vo);
		return result;
	}
	
	/**
	 * selectBoardListAll
	 * 등록되어 있는 전체 게시판 리스트데이터 조회
	 * @param vo 게시판 데이터를 vo객체에 담아 전달, 현재는 아무값도 넘기지 않으나 추후에 이용할 수 있어 추가
	 * @return List<Dm_board_vo> 조회된 게시판 데이터의 개수를 List 자료형으로 전달
	*/
	@Override
	public List<Dm_board_vo> selectBoardListAll(Dm_board_vo vo) throws Exception {
		List<Dm_board_vo> result = mapper.selectBoardListAll(vo);
		return result;
	}
	
	/**
	 * selectDomainList
	 * 등록되어 있는 사용중인 도메인 리스트데이터 조회
	 * @param vo 도메인 데이터를 vo객체에 담아 전달, 현재는 아무값도 넘기지 않으나 추후에 이용할 수 있어 추가
	 * @return List<Dm_domain_list_vo> 조회된 도메인 데이터의 개수를 List 자료형으로 전달
	*/
	@Override
	public List<Dm_domain_list_vo> selectDomainList(Dm_domain_list_vo vo) throws Exception{
		List<Dm_domain_list_vo> result = mapper.selectDomainList(vo);
		return result;
	}

	@Override
	public List<Dm_layout_vo> selectLayoutList(Dm_layout_vo vo) throws Exception {
		return mapper.selectLayoutList(vo);
	}

}
