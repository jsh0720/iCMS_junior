package egovframework.diam.ui.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.diam.biz.model.board.Dm_board_vo;
import egovframework.diam.biz.model.statistics.Dm_statistics_board_vo;
import egovframework.diam.biz.service.board.BoardService;
import egovframework.diam.biz.service.statistics.StatisticsBoardService;
import egovframework.diam.cmm.util.CommonUtil;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
public class StatisticsBoardController {
	
	@Resource(name="boardService") private BoardService boardService;
	@Resource(name="statisticsBoardService") private StatisticsBoardService statisticsBoardService;

	@RequestMapping("/adm/statistics_board.do")
	public ResponseEntity<?> statistics_board(@RequestParam(value="mode", required=false) String mode) throws Exception {
		Map<String, Object> result = new HashMap<>();
		CommonUtil commonUtil = new CommonUtil();
		List<Dm_statistics_board_vo> list = new ArrayList<>();
		Dm_statistics_board_vo vo = new Dm_statistics_board_vo();
		if (commonUtil.isNullOrEmpty(mode)) {
			result.put("notice", "게시판 통계 정보가 없습니다.");
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		}
		try {
			if("dash_board".equals(mode)) {
				list = statisticsBoardService.selectStatisticsBoardCount(vo);
			}
			result.put("result",  "success");
			result.put("rows",  list);
		} catch(DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			result.put("notice", MessageCode.CMM_DATA_ERROR.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch(Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			result.put("notice", MessageCode.CMM_SYSTEM_ERROR.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
