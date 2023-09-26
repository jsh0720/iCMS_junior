/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.db.config;

import egovframework.diam.biz.model.config.Dm_config_vo;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

/**
 * @Class Name : ConfigMapper.java
 * @Description : 도메인 기본설정 데이터 CRUD 메소드를 수행하는 Mapper Interface
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

@Mapper("configMapper")
public interface ConfigMapper {
	
	/**
	 * selectDmConfig
	 * 도메인 PK값으로 1건의 도메인 기본설정데이터 조회
	 * @param vo 도메인PK값을 vo객체에 담아 전달
	 * @return Dm_config_vo 조회된 도메인 기본설정 데이터를 도메인 기본설정 데이터 vo객체에 담아 전달
	*/
	public Dm_config_vo selectDmConfig(Dm_config_vo vo);
	
	/**
	 * insertDmConfig
	 * 사용자가 입력한 도메인 기본설정 데이터 DB에 insert
	 * @param vo 사용자가 입력한도메인 기본설정 데이터를 vo객체에 담아 전달
	 * @return void 도메인 기본설정 데이터 insert 기능만 담당하는 메소드
	*/
	public int insertDmConfig(Dm_config_vo vo);
	
	/**
	 * updateDmConfig
	 * 도메인 기본설정 PK값으로 등록되어 있는 도메인 기본설정 데이터 DB에 update
	 * @param vo 사용자가 입력한 도메인 기본설정 데이터를 객체에 담아 전달
	 * @return void 도메인 기본설정 데이터 update 기능만 담당하는 메소드
	*/
	public int updateDmConfig(Dm_config_vo vo);
	
	/**
	 * selectDmConfigByUrl
	 * 접속한 URL정보로 등록되어 있는 도메인 기본설정 데이터 조회
	 * @param vo 접속한 URL값을 vo객체에 담아 전달
	 * @return Dm_config_vo 조회된 도메인 기본설정 데이터를 도메인 기본설정 데이터 vo객체에 담아 전달
	*/
	public Dm_config_vo selectDmConfigByUrl(Dm_config_vo vo);
		
	/**
	 * selectDmConfigDuplicate
	 * 도메인 PK값으로 1건의 도메인 기본설정데이터 개수 조회
	 * @param vo 도메인PK값을 vo객체에 담아 전달
	 * @return int 조회된 도메인 기본설정 데이터의 개수를 정수형으로 전달
	*/
	public int selectDmConfigDuplicate(Dm_config_vo vo);
	
	/**
	 * updateDmPage
	 * 도메인 기본페이지설정 PK값으로 등록되어 있는 도메인 기본페이지설정 데이터 DB에 update
	 * @param vo 사용자가 입력한 도메인 기본페이지설정 데이터를 객체에 담아 전달
	 * @return void 도메인 기본페이지설정 데이터 update 기능만 담당하는 메소드
	*/
	public int updateDmPage(Dm_config_vo vo);
	
	public int selectDmConfigDuplicateUrl(Dm_config_vo vo);
}
