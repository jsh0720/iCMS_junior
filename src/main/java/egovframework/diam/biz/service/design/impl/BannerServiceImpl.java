/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.biz.service.design.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.diam.biz.db.design.BannerMapper;
import egovframework.diam.biz.model.design.Dm_banners_vo;
import egovframework.diam.biz.service.design.BannerService;
import egovframework.diam.cmm.util.MessageCode;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : BannerServiceImpl.java
 * @Description : 사용자페이지 메인페이지에 표출되는 배너 데이터 CRUD 메소드를 수행하는 Service Interface 구현 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

@Service("bannerService")
public class BannerServiceImpl extends EgovAbstractServiceImpl implements BannerService {
	
	@Resource(name="bannerMapper")
	private BannerMapper bannerMapper;
	
	/**
	 * selectBannerListCnt
	 * 검색 값에 따른 배너 리스트데이터 개수 조회
	 * @param vo 배너데이터 검색조건값을 vo객체에 담아 전달
	 * @return int 조회된 배너데이터의 개수를 정수형으로 전달
	*/
	@Override
	public int selectBannerListCnt(Dm_banners_vo vo) throws Exception {
		int result = bannerMapper.selectBannerListCnt(vo);
		return result;
	}
	
	/**
	 * selectBannerList
	 * 검색 값에 따른 배너 리스트데이터 조회
	 * @param vo 배너데이터 검색조건,페이징 값을 vo객체에 담아 전달
	 * @return List<Dm_banners_vo> 조회된 배너데이터를 List 자료형으로 전달
	*/
	@Override
	public List<Dm_banners_vo> selectBannerList(Dm_banners_vo vo) throws Exception {
		List<Dm_banners_vo> result = bannerMapper.selectBannerList(vo);
		return result;
	}
	
	/**
	 * selectBanner
	 * 배너 PK값으로 1건의 배너데이터 조회
	 * @param vo 배너 PK값을 vo객체에 담아 전달
	 * @return Dm_banners_vo 조회된 배너데이터를 배너데이터 vo객체에 담아 전달
	*/
	@Override
	public Dm_banners_vo selectBanner(Dm_banners_vo vo) throws Exception {
		Dm_banners_vo result = bannerMapper.selectBanner(vo);
		return result;
	}
	
	/**
	 * deleteBanner
	 * 배너 PK값으로 등록되어 있는 배너데이터 삭제
	 * @param vo 사용자가 삭제하고자 하는 배너데이터의 PK값을 vo객체에 담아 전달
	 * @return void 배너데이터 delete 기능만 담당하는 메소드
	*/
	@Override
	@Transactional
	public void deleteBanner(List<Dm_banners_vo> list) throws Exception {
		if (list.size() > 0) {
			list.forEach(item -> {
				int result = bannerMapper.deleteBanner(item);
				if (result < 1) throw new RuntimeException(MessageCode.CMS_DELETE_FAIL.getLog());
			});
		}
	}
	
	/**
	 * insertBanner
	 * 사용자가 입력한 배너 데이터 DB에 insert
	 * @param vo 사용자가 입력한 배너 데이터를 vo객체에 담아 전달
	 * @return void 배너데이터 insert 기능만 담당하는 메소드
	*/
	@Override
	public int insertBanner(Dm_banners_vo vo) throws Exception {
		return bannerMapper.insertBanner(vo);
	}
	
	/**
	 * updateBanner
	 * 배너 PK값으로 등록되어 있는 배너데이터 DB에 update
	 * @param vo 사용자가 입력한 배너데이터를 객체에 담아 전달
	 * @return void 배너데이터 update 기능만 담당하는 메소드
	*/
	@Override
	public int updateBanner(Dm_banners_vo vo) throws Exception {
		return bannerMapper.updateBanner(vo);
	}
	
	/**
	 * selectBannerListForWeb
	 * 사용자페이지 메인페이지에 표출할 게시기간이 지나지 않거나,기간무제한인 배너 리스트데이터 조회
	 * @param vo 배너 도메인PK값을 vo객체에 담아 전달
	 * @return List<Dm_main_visual_vo> 조회된 배너 데이터를 List 자료형으로 전달
	*/
	@Override
	public List<Dm_banners_vo> selectBannerListForWeb(Dm_banners_vo vo) throws Exception {
		List<Dm_banners_vo> result = bannerMapper.selectBannerListForWeb(vo);
		return result;
	}
}
