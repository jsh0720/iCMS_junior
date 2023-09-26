/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.service.admin.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.diam.biz.db.admin.AdminMapper;
import egovframework.diam.biz.model.admin.Dm_admin_vo;
import egovframework.diam.biz.service.admin.AdminService;
import egovframework.diam.cmm.util.MessageCode;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;

/**
 * @Class Name : AdminServiceImpl.java
 * @Description : 관리자 계정정보 CRUD 메소드를 수행하는 Service Interface 구현 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

@Service("adminService")
public class AdminServiceImpl extends EgovAbstractServiceImpl implements AdminService {
	
	@Resource(name="adminMapper")
	private AdminMapper adminMapper;
	
	@Resource(name="egovDiamEsntlIdGnrService")
	private EgovIdGnrService egovDiamEsntlIdGnrService;
	
	/**
	 * selectAdminListCnt
	 * 검색 값에 따른 관리자계정 리스트데이터 개수 조회
	 * @param vo 관리자계정 데이터 검색조건값을 vo객체에 담아 전달
	 * @return int 조회된 관리자계정 데이터의 개수를 정수형으로 전달
	*/
	@Override
	public int selectAdminListCnt(Dm_admin_vo vo) throws Exception{
		int result = adminMapper.selectAdminListCnt(vo);
		return result;
	}
	
	/**
	 * selectAdminList
	 * 검색 값에 따른 관리자계정 리스트데이터 조회
	 * @param vo 관리자계정 데이터 검색조건,페이징 값을 vo객체에 담아 전달
	 * @return List<Dm_admin_vo> 조회된 관리자계정 데이터를 List 자료형으로 전달
	*/
	@Override
	public List<Dm_admin_vo> selectAdminList(Dm_admin_vo vo) throws Exception {
		List<Dm_admin_vo> result = adminMapper.selectAdminList(vo);
		return result;
	}	
	
	/**
	 * selectAdmin
	 * 관리자계정 PK값으로 1건의 관리자계정 데이터 조회
	 * @param vo 관리자계정 PK값을 vo객체에 담아 전달
	 * @return Dm_admin_vo 조회된 관리자계정 데이터를 관리자계정 데이터 vo객체에 담아 전달
	*/
	@Override
	public Dm_admin_vo selectAdmin(Dm_admin_vo vo) throws Exception {
		Dm_admin_vo result = adminMapper.selectAdmin(vo);
		return result;
	}

	/**
	 * deleteAdmin
	 * 관리자계정 PK값으로 등록되어 있는 관리자계정 데이터 삭제
	 * @param vo 사용자가 삭제하고자 하는 관리자계정 데이터의 PK값을 vo객체에 담아 전달
	 * @return void 관리자계정 데이터 delete 기능만 담당하는 메소드
	*/
	@Override
	@Transactional
	public void deleteAdmin(List<Dm_admin_vo> list) throws Exception {
		if(list.size() > 0) {
			list.forEach(item -> {
				int result = adminMapper.deleteAdmin(item);
				if (result < 1) throw new RuntimeException(MessageCode.CMS_DELETE_FAIL.getLog());
			});
		}
	}
	
	/**
	 * insertAdmin
	 * 사용자가 입력한 관리자계정 데이터 DB에 insert
	 * @param vo 사용자가 입력한 관리자계정 데이터를 vo객체에 담아 전달
	 * @return void 관리자계정 데이터 insert 기능만 담당하는 메소드
	*/
	@Override
	public void insertAdmin(Dm_admin_vo vo) throws Exception {
		vo.setEsntl_id(egovDiamEsntlIdGnrService.getNextStringId());
		adminMapper.insertAdmin(vo);
	}
	
	/**
	 * updateAdmin
	 * 관리자계정 PK값으로 등록되어 있는 관리자계정 데이터 DB에 update
	 * @param vo 사용자가 입력한 관리자계정 데이터를 객체에 담아 전달
	 * @return void 관리자계정 데이터 update 기능만 담당하는 메소드
	*/
	@Override
	public void updateAdmin(Dm_admin_vo vo) throws Exception {
		adminMapper.updateAdmin(vo);
	}
	
	/**
	 * selectAdminDupChk
	 * 관리자계정 등록 시 중복된 아이디가 있는지 검증
	 * @param vo 관리자계정 데이터 아이디 값을 vo객체에 담아 전달
	 * @return Dm_admin_vo 조회된 관리자계정 데이터를 vo객체에 담아 전달
	*/
	@Override
	public Dm_admin_vo selectAdminDupChk(Dm_admin_vo vo) throws Exception {
		Dm_admin_vo result = adminMapper.selectAdminDupChk(vo);
		return result;
	}
}
