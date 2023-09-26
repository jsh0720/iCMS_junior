/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.service.config.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.diam.biz.db.config.AccessIpMapper;
import egovframework.diam.biz.model.config.Dm_access_ip_vo;
import egovframework.diam.biz.service.config.AccessIpService;
import egovframework.diam.cmm.util.MessageCode;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : AccessIpServiceImpl.java
 * @Description : 관리자페이지 접근아이피 데이터 CRUD 메소드를 수행하는 Service Interface 구현 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

@Service("accessIpService")
public class AccessIpServiceImpl extends EgovAbstractServiceImpl implements AccessIpService {
	
	@Resource(name="accessIpMapper")
	private AccessIpMapper accessIpMapper;
	
	/**
	 * selectAccessIpListCnt
	 * 검색 값에 따른 접근아이피 리스트데이터 개수 조회
	 * @param vo 접근아이피 데이터 검색조건값을 vo객체에 담아 전달
	 * @return int 조회된 접근아이피 데이터의 개수를 정수형으로 전달
	*/
	@Override
	public int selectAccessIpListCnt(Dm_access_ip_vo vo) throws Exception {
		int result = accessIpMapper.selectAccessIpListCnt(vo);
		return result;
	}
	
	/**
	 * selectAccessIpList
	 * 검색 값에 따른 접근아이피 리스트데이터 조회
	 * @param vo 접근아이피 데이터 검색조건,페이징 값을 vo객체에 담아 전달
	 * @return List<Dm_access_ip_vo> 조회된 접근아이피 데이터를 List 자료형으로 전달
	*/
	@Override
	public List<Dm_access_ip_vo> selectAccessIpList(Dm_access_ip_vo vo) throws Exception {
		List<Dm_access_ip_vo> result = accessIpMapper.selectAccessIpList(vo);
		return result;
	}

	/**
	 * selectAccessIp
	 * 접근아이피 PK값으로 1건의 접근아이피 데이터 조회
	 * @param vo 접근아이피 PK값을 vo객체에 담아 전달
	 * @return Dm_access_ip_vo 조회된 접근아이피 데이터를 접근아이피 데이터 vo객체에 담아 전달
	*/
	@Override
	public Dm_access_ip_vo selectAccessIp(Dm_access_ip_vo vo) throws Exception {
		Dm_access_ip_vo result = accessIpMapper.selectAccessIp(vo);
		return result;
	}
	
	/**
	 * deleteAccessIp
	 * 접근아이피 PK값으로 등록되어 있는 접근아이피 데이터 삭제
	 * @param List<Dm_access_ip_vo> 삭제할 PK가 담긴 VO를 컬렉션에 담아 전달
	 * @return void 접근아이피 데이터 delete 기능만 담당하는 메소드
	*/
	@Override
	@Transactional
	public void deleteAccessIp(List<Dm_access_ip_vo> list) throws Exception {
		if (list.size() > 0) {
			list.forEach(item -> {
				int result = accessIpMapper.deleteAccessIp(item);
				if (result < 1) throw new RuntimeException(MessageCode.CMS_DELETE_FAIL.getLog());
			});
		}
	}
	
	/**
	 * insertAccessIp
	 * 사용자가 입력한 접근아이피 데이터 DB에 insert
	 * @param vo 사용자가 입력한 접근아이피 데이터를 vo객체에 담아 전달
	 * @return void 접근아이피 데이터 insert 기능만 담당하는 메소드
	*/
	@Override
	public int insertAccessIp(Dm_access_ip_vo vo) throws Exception {
		return accessIpMapper.insertAccessIp(vo);
	}
	
	/**
	 * selectAccessIpAll
	 * 등록된 접근아이피를 모두 조회
	 * @return List<Dm_access_ip_vo> 조회된 접근아이피 데이터를 List 자료형으로 전달
	*/
	@Override
	public List<Dm_access_ip_vo> selectAccessIpAll() throws Exception {
		List<Dm_access_ip_vo> result = accessIpMapper.selectAccessIpAll();
		return result;
	}
	
	/**
	 * updateAccessIp
	 * 접근아이피 PK값으로 등록되어 있는 접근아이피 데이터 DB에 update
	 * @param vo 사용자가 입력한 접근아이피 데이터를 객체에 담아 전달
	 * @return void 접근아이피 데이터 update 기능만 담당하는 메소드
	*/
	@Override
	public int updateAccessIp(Dm_access_ip_vo vo) throws Exception {
		return accessIpMapper.updateAccessIp(vo);
	}
	
	/**
	 * selectAccessIpDuplicate
	 * 사용자가 입력한 접근아이피 값 중복여부 조회
	 * @return int 조회된 접근아이피 데이터의 개수를 정수형으로 전달
	*/
	@Override
	public int selectAccessIpDuplicate(Dm_access_ip_vo vo) throws Exception {
		int result = accessIpMapper.selectAccessIpDuplicate(vo);
		return result;
	}

}
