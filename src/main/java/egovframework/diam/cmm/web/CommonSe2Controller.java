/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.cmm.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.diam.biz.model.board.Dm_board_vo;
import egovframework.diam.biz.service.board.BoardService;
import egovframework.diam.cmm.model.Dm_common_se2_vo;
import egovframework.diam.cmm.util.CommonUtil;
import egovframework.diam.cmm.util.MessageCode;
import lombok.extern.log4j.Log4j2;

/**
 * @Class Name : CommonSe2Controller.java
 * @Description : 네이버 스마트에디터2 사진업로드 및 썸네일 생성기능 구현 Controller 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
 */

@Controller
@Log4j2
public class CommonSe2Controller implements ApplicationContextAware, InitializingBean {
	private ApplicationContext applicationContext;

	public void afterPropertiesSet() throws Exception {}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		log.info("CommonSe2Controller setApplicationContext method has called!");
	}
	
	/**
	 * se2_single_uploader
	 * 네이버 스마트에디터2 단일 이미지 파일 업로드를 수행, muiltiuploader 사용으로 현재는 미사용
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 BindingResult객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/se2_single_uploader.do")
	public String se2_single_uploader(HttpServletRequest request, HttpServletResponse response,
			Dm_common_se2_vo dm_common_se2_vo, MultipartHttpServletRequest multirequest) throws IOException {
		String callback = dm_common_se2_vo.getCallback();
		String callback_func = dm_common_se2_vo.getCallback_func();
		String file_result = "";
		String result = "";
		//MultipartFile upload_file = dm_common_se2_vo.getFiledata(); 
		MultipartFile upload_file = multirequest.getFile("Filedata");
		       
        String file_alt = request.getParameter("file_alt");
        String FILE_PATH = request.getServletContext().getRealPath("/resources/se2/");
        
        File folder = new File(FILE_PATH);
        
        try {
        	if(upload_file != null && upload_file.getOriginalFilename() != null && !"".equals(upload_file.getOriginalFilename())) {
        		CommonUtil commonUtil = new CommonUtil();
    			String original_file_name = URLDecoder.decode(upload_file.getOriginalFilename(), "UTF-8");
    			String ext = original_file_name.substring(original_file_name.indexOf(".") + 1);
    			String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    			String upload_name = today + "_" + commonUtil.convertSHA256(original_file_name)+"."+ext;
    			String upload_path = FILE_PATH + upload_name;
    			if (!folder.exists()) {
    				folder.mkdirs();
    			}
				upload_file.transferTo(new File(upload_path));
				file_result += "&bNewLine=true";
				file_result += "&sFileName="+original_file_name;
				file_result += "&sFileURL=/resources/se2/"+upload_name;
				file_result += "&sFileAlt="+file_alt;
			} else {
    			file_result += "&errstr=error";
    		}
		} catch (IOException ioe) {
			log.error(MessageCode.CMM_FILE_ERROR.getLog());
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println(MessageCode.CMM_FILE_ERROR.getMessage());
			out.flush();
			out.close();
		} catch (Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println(MessageCode.CMM_SYSTEM_ERROR.getMessage());
			out.flush();
			out.close();			
		}
		result += "redirect:"+callback+"?callback_func="+callback_func+file_result;
		return result;
	}
	
	/**
	 * se2_multi_uploader
	 * 네이버 스마트에디터2 이미지 업로드 시 서버에 업로드한 이미지 저장 후 썸네일 생성
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 BindingResult객체
	 * @param response 메소드의 수행결과를 화면에 전달하기 위해 사용하는 HttpServletResponse 객체
	 * @return void response객체를 통하여 ajax 결과값 전송
	*/
	@RequestMapping("/adm/se2_multi_uploader.do")
	public @ResponseBody void se2_multi_uploader (HttpServletRequest request, HttpServletResponse response) throws Exception {
		BoardService boardService = (BoardService) applicationContext.getBean("boardService");
		try {
			CommonUtil commonUtil = new CommonUtil();
			
			//파일정보
	        String sFileInfo = "";
	        
	        //알트텍스트 입력한 정보 받는다.
	        String file_alt = URLDecoder.decode(request.getHeader("file-alt"),"UTF-8");
	        String board_id = request.getHeader("file-boardid");
	        	        
	        //파일명을 받는다 = 일반 원본파일
	        String original_file_name = URLDecoder.decode(request.getHeader("file-name"),"UTF-8");
        	String FILE_PATH = request.getServletContext().getRealPath("/resources/se2/");
        	String FILE_PATH2 = request.getServletContext().getRealPath("/resources/se2/thumb");
        	
        	File folder = new File(FILE_PATH);
        	        	
        	String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        	String upload_name = today + "_" + commonUtil.convertSHA256(original_file_name) + original_file_name.substring(original_file_name.lastIndexOf("."));
        	String upload_path = FILE_PATH + upload_name;
        	if (!folder.exists()) {
        		if (folder.mkdirs()) {
        			folder = new File(FILE_PATH2);
        			if(!folder.exists()) folder.mkdirs();
        		}
        	}
        	
        	InputStream is = request.getInputStream();
        	OutputStream os = new FileOutputStream(upload_path);
        	int numRead;
        	byte[] b = null;
        	if (Integer.parseInt(request.getHeader("file-size")) > 0) {
        		b = new byte[Integer.parseInt(request.getHeader("file-size"))];
        		while ((numRead = is.read(b,0,b.length)) != -1)  {
            		os.write(b, 0, numRead);
            	}
            	if(is != null) {
            		is.close();
            	}
            	os.flush();
            	os.close();
            	if (!"".equals(board_id) && board_id != null) {
            		Dm_board_vo boardVO = new Dm_board_vo();
                	boardVO.setDm_id(board_id);
                	boardVO = boardService.selectBoard(boardVO);
                	
                	commonUtil.makeThumbnailForEditor(FILE_PATH, FILE_PATH2, upload_name, boardVO.getDm_gallery_width(), boardVO.getDm_gallery_height());
            	}
            	
            	sFileInfo += "&bNewLine=true";
            	sFileInfo += "&sFileName="+original_file_name;
            	sFileInfo += "&sFileURL=/resources/se2/"+upload_name;
            	sFileInfo += "&sFileAlt="+file_alt;
            	
            	PrintWriter print = response.getWriter();
            	print.print(sFileInfo);
            	print.flush();
            	print.close();
        	} else {
        		response.setContentType("text/html; charset=UTF-8");
    			PrintWriter out = response.getWriter();
    			out.println("파일용량 오류입니다. 업로드한 파일을 확인해주세요.");
    			out.flush();
    			out.close();
        	}        	
		} catch (IOException ioe) {
			log.error(MessageCode.CMM_FILE_ERROR.getLog());
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println(MessageCode.CMM_FILE_ERROR.getMessage());
			out.flush();
			out.close();
		} catch(DataAccessException dae) {
			log.error(MessageCode.CMM_DATA_ERROR.getLog());
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println(MessageCode.CMM_DATA_ERROR.getMessage());
			out.flush();
			out.close();
		} catch (Exception e) {
			log.error(MessageCode.CMM_SYSTEM_ERROR.getLog());
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println(MessageCode.CMM_SYSTEM_ERROR.getMessage());
			out.flush();
			out.close();			
		}
	}
}