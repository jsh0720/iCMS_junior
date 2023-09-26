/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.cmm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.diam.cmm.db.LoginAuthMapper;
import egovframework.diam.cmm.model.LoginVO;
import egovframework.diam.cmm.service.LoginAuthService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : LoginAuthServiceImpl.java
 * @Description : 관리자/사용자 페이지 로그인 프로세스 메소드를 수행하는 Service Interface 구현 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

@Service("loginAuthService")
public class LoginAuthServiceImpl extends EgovAbstractServiceImpl implements LoginAuthService {
	
	@Resource(name="loginAuthMapper")
	private LoginAuthMapper loginAuthMapper;
	
	/**
	 * actionLogin
	 * 사용자가 로그인 폼에 입력한 아이디/비밀번호 정보로 등록되어있는 회원 데이터 조회
	 * @param vo 로그인 아이디/비밀번호값을 vo객체에 담아 전달
	 * @return LoginVO 조회된 회원데이터를 회원데이터 vo객체에 담아 전달
	*/
	@Override
	public LoginVO actionLogin(LoginVO vo) throws Exception {
		LoginVO result = loginAuthMapper.actionLogin(vo);
		return result;
	}
	
	/**
	 * insertLoginLog
	 * 사용자가 로그인 성공/실패한 아이디,아이피,시간대 등 로그인과 관련된 로그인로그 데이터 DB에 insert
	 * @param vo 로그인 요청한 데이터를 vo객체에 담아 전달
	 * @return void 로그인로그 데이터 insert 기능만 담당하는 메소드
	*/
	@Override
	public void insertLoginLog(LoginVO vo) throws Exception {
		loginAuthMapper.insertLoginLog(vo);
	}
	
	/**
	 * selectLoginInfo
	 * 회원 아이디값으로 1건의 회원데이터 조회
	 * @param vo 회원 아이디값을 vo객체에 담아 전달
	 * @return LoginVO 조회된 회원데이터를 회원데이터 vo객체에 담아 전달
	*/
	@Override
	public LoginVO selectLoginInfo(LoginVO vo) throws Exception {
		LoginVO result = loginAuthMapper.selectLoginInfo(vo);
		return result;
	}
		
	/**
	 * selectLoginFailCnt
	 * 회원 아이디값으로 해당 아이디의 로그인 실패건수를 조회
	 * @param vo 회원 아이디값을 vo객체에 담아 전달
	 * @return int 조회된 로그인 실패건수 데이터의 개수를 정수형으로 전달
	*/
	@Override
	public int selectLoginFailCnt(LoginVO vo) throws Exception {
		int result = loginAuthMapper.selectLoginFailCnt(vo);
		return result;
	}
	
	/**
	 * updateMemberUnlock
	 * 잠김회원 관리에서 잠김처리된 회원의 정보를 잠김해제 처리
	 * @param vo 회원 아이디값을 vo객체에 담아 전달
	 * @return void 회원데이터 잠김처리 해제 update 기능만 담당하는 메소드
	*/
	@Override
	public void updateMemberUnlock(LoginVO vo) throws Exception {
		loginAuthMapper.updateMemberUnlock(vo);
	}
	
	/**
	 * updateLoginFailCnt
	 * 로그인 실패 시 로그인 실패회수를 +1 처리
	 * @param vo 회원 아이디값을 vo객체에 담아 전달
	 * @return void 로그인 실패한 회원데이터 로그인 실패건수 update 기능만 담당하는 메소드
	*/
	@Override
	public void updateLoginFailCnt(LoginVO vo) throws Exception {
		loginAuthMapper.updateLoginFailCnt(vo);
	}
	
	/**
	 * updateLoginFailCntLock
	 * 로그인 5회 이상 실패시 계정 잠금처리 + 실패회수 +1, 실패시간 최신화 처리
	 * @param vo 회원 아이디값을 vo객체에 담아 전달
	 * @return void 로그인 5회 이상 실패시 정보 update 기능만 담당하는 메소드
	*/
	@Override
	public void updateLoginFailCntLock(LoginVO vo) throws Exception {
		loginAuthMapper.updateLoginFailCntLock(vo);
	}
	
	/**
	 * updateMemberPasswordChange
	 * 초기비밀번호 세팅된 계정일 시 비밀번호 변경페이지에서 변경할 경우 비밀번호 정보 update
	 * @param vo 회원 아이디값,입력한 비밀번호를 vo객체에 담아 전달
	 * @return void 로그인한 계정의 비밀번호 정보 update 기능만 담당하는 메소드
	*/
	@Override
	public void updateMemberPasswordChange(LoginVO vo) throws Exception {
		loginAuthMapper.updateMemberPasswordChange(vo);
	}	
}
