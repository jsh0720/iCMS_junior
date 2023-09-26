/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.service.config.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.diam.biz.db.config.ConfigMapper;
import egovframework.diam.biz.model.config.Dm_config_vo;
import egovframework.diam.biz.service.config.ConfigService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : ConfigServiceImpl.java
 * @Description : 도메인 기본설정 데이터 CRUD 메소드를 수행하는 Service Interface 구현 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

@Service("configService")
public class ConfigServiceImpl extends EgovAbstractServiceImpl implements ConfigService {
	
	@Resource(name="configMapper")
	private ConfigMapper mapper;
	
	/**
	 * selectDmConfig
	 * 도메인 PK값으로 1건의 도메인 기본설정데이터 조회
	 * @param vo 도메인PK값을 vo객체에 담아 전달
	 * @return Dm_config_vo 조회된 도메인 기본설정 데이터를 도메인 기본설정 데이터 vo객체에 담아 전달
	*/
	@Override
	public Dm_config_vo selectDmConfig(Dm_config_vo vo) throws Exception {
		Dm_config_vo result = mapper.selectDmConfig(vo);
		return result;
	}
	
	/**
	 * insertDmConfig
	 * 사용자가 입력한 도메인 기본설정 데이터 DB에 insert
	 * @param vo 사용자가 입력한도메인 기본설정 데이터를 vo객체에 담아 전달
	 * @return void 도메인 기본설정 데이터 insert 기능만 담당하는 메소드
	*/
	@Override
	public int insertDmConfig(Dm_config_vo vo) throws Exception {
		return mapper.insertDmConfig(vo);
	}
	
	/**
	 * updateDmConfig
	 * 도메인 기본설정 PK값으로 등록되어 있는 도메인 기본설정 데이터 DB에 update
	 * @param vo 사용자가 입력한 도메인 기본설정 데이터를 객체에 담아 전달
	 * @return void 도메인 기본설정 데이터 update 기능만 담당하는 메소드
	*/
	@Override
	public int updateDmConfig(Dm_config_vo vo) throws Exception {
		return mapper.updateDmConfig(vo);
	}
	
	/**
	 * selectDmConfigByUrl
	 * 접속한 URL정보로 등록되어 있는 도메인 기본설정 데이터 조회
	 * @param vo 접속한 URL값을 vo객체에 담아 전달
	 * @return Dm_config_vo 조회된 도메인 기본설정 데이터를 도메인 기본설정 데이터 vo객체에 담아 전달
	*/
	@Override
	public Dm_config_vo selectDmConfigByUrl(Dm_config_vo vo) throws Exception {
		Dm_config_vo result = mapper.selectDmConfigByUrl(vo);
		return result;
	}
	
	/**
	 * selectDmConfigDuplicate
	 * 도메인 PK값으로 1건의 도메인 기본설정데이터 개수 조회
	 * @param vo 도메인PK값을 vo객체에 담아 전달
	 * @return int 조회된 도메인 기본설정 데이터의 개수를 정수형으로 전달
	*/
	@Override
	public int selectDmConfigDuplicate(Dm_config_vo vo) throws Exception {
		int result = mapper.selectDmConfigDuplicate(vo);
		return result;
	}
	
	/**
	 * updateDmPage
	 * 도메인 기본페이지설정 PK값으로 등록되어 있는 도메인 기본페이지설정 데이터 DB에 update
	 * @param vo 사용자가 입력한 도메인 기본페이지설정 데이터를 객체에 담아 전달
	 * @return void 도메인 기본페이지설정 데이터 update 기능만 담당하는 메소드
	*/
	@Override
	public int updateDmPage(Dm_config_vo vo) throws Exception {
		return mapper.updateDmPage(vo);
	}

	@Override
	public int selectDmConfigDuplicateUrl(Dm_config_vo vo) throws Exception {
		return mapper.selectDmConfigDuplicateUrl(vo);
	}
}
