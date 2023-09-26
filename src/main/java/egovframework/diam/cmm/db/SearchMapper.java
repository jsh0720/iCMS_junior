package egovframework.diam.cmm.db;

import java.util.List;

import egovframework.diam.biz.model.board.Dm_board_vo;
import egovframework.diam.biz.model.board.Dm_write_vo;
import egovframework.diam.biz.model.display.Dm_pages_vo;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("searchMapper")
public interface SearchMapper {

	public List<Dm_board_vo> selectBoardList(Dm_pages_vo vo);
	
	public List<Dm_write_vo> selectWriteList(Dm_board_vo vo);
	
	public int selectWriteListCnt(Dm_board_vo vo);

}
