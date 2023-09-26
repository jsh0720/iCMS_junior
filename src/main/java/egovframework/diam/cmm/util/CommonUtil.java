/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.cmm.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.codehaus.jettison.json.JSONObject;
import org.imgscalr.Scalr;
import org.jasypt.contrib.org.apache.commons.codec_1_3.binary.Base64;
import org.springframework.web.multipart.MultipartFile;

import com.ibatis.common.logging.Log;
import com.nhncorp.lucy.security.xss.XssSaxFilter;

import egovframework.diam.biz.model.config.Dm_config_vo;

/**
 * @Class Name : CommonUtil.java
 * @Description : 관리자/사용자 기능에서 공통으로 사용하는 메소드들을 정의한 유틸 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
 */

public class CommonUtil {
	
	public boolean isNullOrEmpty(String str) {
		if (str == null || str.isEmpty() || str.replaceAll(" ", "").length() < 1) return true;
		return false;
	}
	
	/**
	 * convertSHA256
	 * 전달받은 문자열을 SHA-256으로 암호화 후 암호화한 값을 문자열으로 전달
	 * @param str 암호화할 문자열 전달
	 * @return String SHA-256으로 암호화한 문자열을 return
	*/
	public String convertSHA256(String str) throws Exception {
		String result = "";
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(str.getBytes());
		byte byteData[] = md.digest();
		StringBuffer sb = new StringBuffer();
		for (int i = 0 ; i < byteData.length ; i++) {
			sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
		}
		result = sb.toString();		
		return result;
	}
	
	/**
	 * encryptPassword
	 * 회원/관리자 비밀번호 등록 시 아이디, 비밀번호 2가지를 조합하여 SHA-256으로 암호화 후 base64로 한번더 암호화 한 문자열 전달
	 * @param password 사용자가 입력한 비밀번호를 문자열로 전달
	 * @param id 사용자가 입력한 아이디를 문자열로 전달
	 * @return String SHA-256으로 암호화한 문자열을 base64로 한번더 암호화 후 return
	*/
	public String encryptPassword(String password, String id) throws Exception {
		if (password == null) {
			return "";
		}
		
		byte[] hashValue = null;
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		
		md.reset();
		md.update(id.getBytes());
		
		hashValue = md.digest(password.getBytes());
				
		return new String(Base64.encodeBase64(hashValue));
	}
	
	/**
	 * getUserIp
	 * 접속한 유저의 아이피를 구할 때, 다양한 서버의 종류와 네트워크 구성에 따라 아이피값 탐색 후 전달
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @return String 탐색한 아이피값을 문자열로 return
	*/
	public String getUserIp(HttpServletRequest request) {
		String userIp = request.getHeader("X-Forwarded-For");
		
		if (userIp == null || userIp.length() == 0 || "unknown".equalsIgnoreCase(userIp)) { 
			userIp = request.getHeader("Proxy-Client-IP"); 
        } 
        if (userIp == null || userIp.length() == 0 || "unknown".equalsIgnoreCase(userIp)) { 
            userIp = request.getHeader("WL-Proxy-Client-userIp"); 
        } 
        if (userIp == null || userIp.length() == 0 || "unknown".equalsIgnoreCase(userIp)) { 
            userIp = request.getHeader("HTTP_CLIENT_userIp");
        } 
        if (userIp == null || userIp.length() == 0 || "unknown".equalsIgnoreCase(userIp)) { 
            userIp = request.getHeader("HTTP_X_FORWARDED_FOR"); 
        }
        if (userIp == null || userIp.length() == 0 || "unknown".equalsIgnoreCase(userIp)) { 
            userIp = request.getHeader("X-Real-userIp"); 
        }
        if (userIp == null || userIp.length() == 0 || "unknown".equalsIgnoreCase(userIp)) { 
            userIp = request.getHeader("X-RealuserIp"); 
        }
        if (userIp == null || userIp.length() == 0 || "unknown".equalsIgnoreCase(userIp)) { 
            userIp = request.getHeader("REMOTE_ADDR");
        }
        if (userIp == null || userIp.length() == 0 || "unknown".equalsIgnoreCase(userIp)) { 
            userIp = request.getRemoteAddr(); 
        }        
		return userIp;
	}
	
	/**
	 * getBrowser
	 * 사용자 접속 시 접속한 브라우저 정보를 탐색 후 브라우저 정보를 문자열로 전달
	 * @param request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @return String 탐색한 브라우저 정보를 문자열로 return
	*/
	public String getBrowser(HttpServletRequest request) {
		String header = request.getHeader("User-Agent");
		if (header.indexOf("MSIE") > -1 || header.indexOf("Trident") > -1)
			return "MSIE";
		else if (header.indexOf("Chrome") > -1)
			return "Chrome";
		else if (header.indexOf("Opera") > -1)
			return "Opera";
		return "Firefox";
	}
	
	/**
	 * getDisposition
	 * 파일 다운로드 시 전달받은 파일명과 브라우저 정보로 disposition 정보 가공 후 문자열로 전달
	 * @param filename 다운로드시 명명할 파일이름을 문자열로 전달
	 * @param browser 브라우저 정보를 문자열로 전달
	 * @return String 가공한 disposition 정보를 문자열로 return
	*/
	public String getDisposition(String filename, String browser) throws Exception {
		String dispositionPrefix = "attachment;filename=";
		String encodedFilename = null;
		if (browser.equals("MSIE")) {
			encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll(
					"\\+", "%20");
		} else if (browser.equals("Firefox")) {
			encodedFilename = "\""
					+ new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
		} else if (browser.equals("Opera")) {
			encodedFilename = "\""
					+ new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
		} else if (browser.equals("Chrome")) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < filename.length(); i++) {
				char c = filename.charAt(i);
				if (c > '~') {
					sb.append(URLEncoder.encode("" + c, "UTF-8"));
				} else {
					sb.append(c);
				}
			}
			encodedFilename = sb.toString();
		}
		return dispositionPrefix + encodedFilename;
	}
	
	/**
	 * urlEncodeUTF8
	 * get방식으로 넘어온 한글파라미터를 UTF-8로 인코딩
	 * @param str 인코딩할 문자열 전달
	 * @return String 인코딩한 결과값을 문자열로 return
	*/
	public String urlEncodeUTF8(String str) throws Exception {
		return URLEncoder.encode(str, "UTF-8");
	}
	
	/**
	 * convertParam
	 * Map으로 받은 파라미터 값을 문자열로 변환
	 * @param map get방식으로 전달받은 파라미터들을 Map으로 전달 
	 * @return String 변환한 파라미터를 문자열로 return
	*/
	public String convertParam(Map<?, ?> map) throws Exception {
		StringBuilder sb = new StringBuilder();
		
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			if (entry.getValue() == null) {
        		continue;
        	}
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
        		entry.getKey().toString(),
                //entry.getValue().toString()
                //urlEncodeUTF8(entry.getKey().toString()),
                urlEncodeUTF8(entry.getValue().toString())
            ));
		}
		
		return sb.toString();
	}

	/**
	 * getPaging
	 * 게시판 리스트페이지 하단 페이징 html태그를 문자열로 가공 후 문자열로 전달
	 * @param rows 한 페이지당 출력한 게시물 수를 정수형으로 전달
	 * @param curPage 현재 페이지 정보를 문자열로 전달 
	 * @param total_page 전체 페이지 정보를 정수형으로 전달 
	 * @param paramMap 리스트 검색값이 포함되어 있는 파라미터를 Map으로 전달
	 * @param domain 도메인 url을 전달
	 * @return String 가공한 html태그를 문자열로 return
	*/
	public String getPaging(Integer rows, Integer curPage, int total_page, Map<String, Object> paramMap, String domain) throws Exception{
		
		domain = isNullOrEmpty(domain) ? "" : domain;
		domain = domain.split("/").length == 1 ? "" : "/" + domain.split("/")[domain.split("/").length -1];
		
		String pagingStr = "";
		int startPage = (((curPage - 1) / rows) * rows) + 1;		
		int endPage = startPage + rows - 1;
		
		if (total_page < 1) {
			pagingStr += "<nav class=\"pg_wrap\"><span class=\"pg\"></span></nav>";
		} else {
			pagingStr += "<nav class=\"pg_wrap\"><span class=\"pg\">";
		}
		
		if (curPage > 1) {
			paramMap.put("page", "1");
			pagingStr += "<a href=\""+ domain +"/index.do?"+convertParam(paramMap)+"\" class=\"pg_page pg_start\">처음</a>";
		} else {
			pagingStr += "<a class=\"pg_page pg_start pg_empty\">처음</a>";
		}
		
		if (startPage > 1) {
			String subStr = "";
			paramMap.put("page", Integer.toString(curPage - 1));
			if(curPage == startPage) subStr = "pg_must";
			pagingStr += "<a href=\""+ domain +"/index.do?"+convertParam(paramMap)+"\" class=\"pg_page pg_prev " + subStr + "\">";	
			pagingStr += "<span>이전</span><page-number>"+(startPage -1)+"</page-number><span>페이지</span></a>";
		} else {
			pagingStr += "<a class=\"pg_page pg_prev pg_empty\">이전</a>";
		}
		
		if (endPage >= total_page) {
			endPage = total_page;
		}
		
		if (total_page > 1) {
			for (int i = startPage ; i <= endPage ; i++) {
				if (curPage != i) {
					paramMap.put("page", Integer.toString(i));
					pagingStr += "<a href=\""+ domain +"/index.do?"+convertParam(paramMap)+"\" class=\"pg_page pg_must\"><page-number>"+i+"</page-number><span class=\"sr-only\">페이지</span></a>";
				} else {
					pagingStr += "<span class=\"pg_current pg_must\"><span class=\"sr-only\">열린</span><page-number>"+i+"</page-number><span class=\"sr-only\">페이지</span></span>";
				}
			}
		}
		
		if (total_page > endPage) {
			String subStr = "";
			paramMap.put("page", Integer.toString(endPage + 1));
			if(curPage == endPage) subStr = "pg_must";
			pagingStr += "<a href=\""+ domain +"/index.do?"+convertParam(paramMap)+"\" class=\"pg_page pg_next " + subStr + "\">";
			pagingStr += "<span>다음></span><page-number>"+(endPage+1)+"</page-number><span>페이지</span></a>";
		} else {
			pagingStr += "<a class=\"pg_page pg_next pg_empty\"></a>";
		}
		
		if (curPage < total_page && total_page > 1) {
			paramMap.put("page", Integer.toString(total_page));
			pagingStr += "<a href=\""+ domain +"/index.do?"+convertParam(paramMap)+"\" class=\"pg_page pg_end\">맨끝</a>";
		} else {
			pagingStr += "<a class=\"pg_page pg_end pg_empty\">맨끝</a>";
		}
		
		if(pagingStr != null && !pagingStr.isEmpty()) {
			if (total_page > 0) {
				pagingStr += "</span></nav>";
			}
			return pagingStr;
		} else {
			return "";
		}
	}
	
	/**
	 * getAuth
	 * 게시판 리스트/상세보기/쓰기/답변 등 게시판에 관련된 권한 체크
	 * @param type 그룹(1)/레벨(2)/모든 사용자(3) 3가지의 타입값을 문자열로 전달 
	 * @param level 로그인한 계정의 권한레벨을 문자열로 전달
	 * @param compareLevel 해당 게시판에 설정된 권한레벨을 문자열로 전달
	 * @param group 로그인한 계정의 그룹을 문자열로 전달
	 * @param compareGroup 해당 게시판에 설정된 그룹리스트를 List자료형으로 전달
	 * @param is_admin admin권한이 있는 계정인지 boolean 자료형으로 전달
	 * @return boolean 권한이 있을경우 true, 반대의 경우 false return
	*/
	public boolean getAuth(String type, String level, String compareLevel, String group, List<String> compareGroup, boolean is_admin) throws Exception{
		boolean result = false;		
		if ("1".equals(type)) {
			if (compareGroup.size() > 0) {
				if (!"".equals(compareGroup.get(0))) {
					if (compareGroup.contains(group)) {
						result = true;
					}
				} else {
					result = true;
				}					
			} else {
				result = true;
			}
		} else if ("2".equals(type)) {
			if (level != null && !level.isEmpty()) {
				if (Integer.parseInt(compareLevel) <= Integer.parseInt(level)) {
					result = true;
				}
			} else {
				if (Integer.parseInt(compareLevel) == 0) {
					result = true;
				}
			}			
		} else if ("3".equals(type)) {
			result = true;
		}			
		return result;
	}
	
	/**
	 * makeThumbnailForEditor
	 * 스마트에디터2 에서 업로드한 이미지파일 정보를 가져와서 썸네일 이미지 생성
	 * @param orifile_path 에디터에서 업로드한 원본 이미지파일이 저장된 경로를 문자열로 전달
	 * @param new_file_path 생성한 썸네일 이미지가 저장될 경로를 문자열로 전달
	 * @param name 에디터에서 업로드한 원본 이미지파일명을 문자열로 전달
	 * @param width 생성할 썸네일의 넓이값을 문자열로 전달
	 * @param height 생성할 썸네일의 높이값을 문자열로 전달
	 * @return void 에디터 업로드이미지 썸네일 이미지 생성기능만 수행
	*/
	public void makeThumbnailForEditor(String orifile_path, String new_file_path, String name, String width, String height) throws Exception{
		File folder = new File(new_file_path);
		if (!folder.exists()) {
			folder.mkdirs();			
		}
				
		String oPath = orifile_path + name; // 원본 경로
		File oFile = new File(oPath);

		int index = oPath.lastIndexOf(".");
		String ext = oPath.substring(index + 1); // 파일 확장자

		String tPath = oFile.getParent() + File.separator + "thumb" + File.separator + "t-" + oFile.getName(); // 썸네일저장 경로
		File tFile = new File(tPath);

		BufferedImage oImage = ImageIO.read(oFile); // 원본이미지
		int tWidth = Integer.parseInt(width); // 생성할 썸네일이미지의 너비
		int tHeight = Integer.parseInt(height); // 생성할 썸네일이미지의 높이

		// 썸네일의 너비와 높이 입니다.
		int dw = tWidth, dh = tHeight;

		// 원본 이미지의 너비와 높이 입니다.
		int ow = oImage.getWidth();
		int oh = oImage.getHeight();

		// 원본 너비를 기준으로 하여 썸네일의 비율로 높이를 계산합니다.
		int nw = ow; int nh = (ow * dh) / dw;

		// 계산된 높이가 원본보다 높다면 crop이 안되므로
		// 원본 높이를 기준으로 썸네일의 비율로 너비를 계산합니다.
		if(nh > oh) {
			nw = (oh * dw) / dh;
			nh = oh;
		}

		// 계산된 크기로 원본이미지를 가운데에서 crop 합니다.
		BufferedImage cropImg = Scalr.crop(oImage, 0, 0, nw, nh);
		//BufferedImage cropImg = Scalr.crop(oImage, (ow-nw)/2, (oh-nh)/2, nw, nh);

		// crop된 이미지로 썸네일을 생성합니다.
		BufferedImage tImage = Scalr.resize(cropImg, dw, dh);
		
		/*
		BufferedImage tImage = new BufferedImage(tWidth, tHeight, BufferedImage.TYPE_3BYTE_BGR); // 썸네일이미지
		Graphics2D graphic = tImage.createGraphics();
		if (oImage != null) {
			Image image = oImage.getScaledInstance(tWidth, tHeight, Image.SCALE_SMOOTH);
			graphic.drawImage(image, 0, 0, tWidth, tHeight, null);
		}
		graphic.dispose(); // 리소스를 모두 해제
		*/

		ImageIO.write(tImage, ext, tFile);

	}

	/**
	 * makeThumbnail
	 * 이미지파일 정보 가져와서 썸네일 이미지 생성
	 * @param filePath 에디터에서 업로드한 원본 이미지파일이 저장된 경로를 문자열로 전달
	 * @param fileName 업로드한 원본 이미지파일명을 문자열로 전달
	 * @param width 생성할 썸네일의 넓이값을 문자열로 전달
	 * @param height 생성할 썸네일의 높이값을 문자열로 전달
	 * @param type 썸네일이 저장될 폴더명을 문자열로 전달
	 * @return void 게시판 이미지 첨부파일 썸네일 이미지 생성기능만 수행
	*/
	public void makeThumbnail(String filePath, String fileName, String width, String height, String type) throws Exception {
		File folder = new File(filePath+File.separator+type);
		if (!folder.exists()) {
			folder.mkdirs();			
		}
		
		String oPath = filePath + fileName; // 원본 경로
		File oFile = new File(oPath);

		int index = oPath.lastIndexOf(".");
		String ext = oPath.substring(index + 1); // 파일 확장자

		String tPath = oFile.getParent() + File.separator + type + File.separator + "t-" + oFile.getName(); // 썸네일저장 경로
		File tFile = new File(tPath);
		
		BufferedImage oImage = ImageIO.read(oFile); // 원본이미지
		int tWidth = Integer.parseInt(width); // 생성할 썸네일이미지의 너비
		int tHeight = Integer.parseInt(height); // 생성할 썸네일이미지의 높이

		// 썸네일의 너비와 높이 입니다.
		int dw = tWidth, dh = tHeight;
		
		// 원본 이미지의 너비와 높이 입니다.
		int ow = oImage.getWidth();
		int oh = oImage.getHeight();
		

		// 원본 너비를 기준으로 하여 썸네일의 비율로 높이를 계산합니다.
		int nw = ow; int nh = (ow * dh) / dw;
		
		// 계산된 높이가 원본보다 높다면 crop이 안되므로
		// 원본 높이를 기준으로 썸네일의 비율로 너비를 계산합니다.
		if(nh > oh) {
			nw = (oh * dw) / dh;
			nh = oh;
		}

		// 계산된 크기로 원본이미지를 가운데에서 crop 합니다.
		//썸네일 상단 부터
		BufferedImage cropImg = Scalr.crop(oImage, 0, 0, nw, nh);
		//BufferedImage cropImg = Scalr.crop(oImage, (ow-nw)/2, (oh-nh)/2, nw, nh);

		// crop된 이미지로 썸네일을 생성합니다.
		BufferedImage tImage = Scalr.resize(cropImg, dw, dh);
		
		/*
		BufferedImage tImage = new BufferedImage(tWidth, tHeight, BufferedImage.TYPE_3BYTE_BGR); // 썸네일이미지
		Graphics2D graphic = tImage.createGraphics();
		if (oImage != null) {
			Image image = oImage.getScaledInstance(tWidth, tHeight, Image.SCALE_SMOOTH);
			graphic.drawImage(image, 0, 0, tWidth, tHeight, null);
		}
		graphic.dispose(); // 리소스를 모두 해제
		*/

		ImageIO.write(tImage, ext, tFile);		
	}
	
	/**
	 * patternCheck
	 * 정규표현식 패턴으로 문자열을 검증하여 정규표현식에 부합하는지 결과값 전달
	 * @param pattern 검증기준이 되는 정규표현식을 문자열로 전달
	 * @param value 검증할 문자열을 전달
	 * @return boolean 정규표현식에 부합할 경우 true, 반대의 경우 false return
	*/
	public boolean patternCheck(String pattern, String value) {
		Pattern pattern_chk = Pattern.compile(pattern);
		Matcher matcher = pattern_chk.matcher(value);
		return matcher.find();
	}
	
	/**
	 * prohibitExtCheck
	 * 첨부파일이 선언된 배열에 열거된 금지확장자가 전달받은 파일에 포함되는지 검증
	 * @param file 확장자를 검증받을 파일정보를 MultipartFile 객체로 전달받음
	 * @return boolean 금지확장자일 경우 true, 반대의 경우 false return
	*/
	public boolean prohibitExtCheck(MultipartFile file) {
		boolean result = false;
		final String[] ext_arr = {"jsp","cgi","php","asp","aspx","exe","com","html","htm","cab","php3","pl","java","class","js","css"};
		if (file != null && file.getSize() > 0) {
			String file_name = file.getOriginalFilename();
			String ext = file_name.substring(file_name.lastIndexOf(".") + 1);
			result = Arrays.asList(ext_arr).contains(ext.toLowerCase());
		}
		return result;
	}
	
	/**
	 * imageExtCheck
	 * 첨부파일이 선언된 배열에 열거된 이미지확장자에 포함되는지 검증
	 * @param file 확장자를 검증받을 파일정보를 MultipartFile 객체로 전달받음
	 * @return boolean 이미지 확장자일 경우 true, 반대의 경우 false return
	*/
	public boolean imageExtCheck(MultipartFile file) {
		boolean result = true;
		final String[] allowedExtensions = {
			"jpg","jpeg","gif","png"
		};
		String fileName = file.isEmpty() ? "" : file.getOriginalFilename();
        if (!isNullOrEmpty(fileName)) {
            String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
            result = Arrays.asList(allowedExtensions).contains(ext.toLowerCase());
        } 
		return result;
	}
	
	public boolean fileExtCheck(MultipartFile file) {
		boolean result = true;
        final String[] allowedExtensions = {
            "txt", "hwp", "hwpx", "pdf", "xls", "xlsx", "pptx", "zip", "tar"
        };
        String fileName = file.isEmpty() ? "" : file.getOriginalFilename();
        if (!isNullOrEmpty(fileName)) {
            String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
            result = Arrays.asList(allowedExtensions).contains(ext.toLowerCase());
        }
		return result;
	}
	
	/**
	 * layoutFileCopy
	 * 레이아웃 신규생성 시 basic레이아웃에 포함되어 있는 폴더를 복사
	 * @param selectFile 복사할 basic레이아웃 폴더를 File객체로 전달
	 * @param copyFile basic레이아웃을 복사할 신규 레이아웃 폴더를  File객체로 전달
	 * @return boolean 폴더복사가 정상적으로 완료될 경우 true, 반대의 경우 false return
	*/
	public Boolean layoutFileCopy(File selectFile, File copyFile) throws Exception {
		boolean result = false;
		
		FileInputStream fis = null;
		FileOutputStream fos = null;
		
		File[] fileList = selectFile.listFiles();
		if (fileList.length > 0) {
			for (File file : fileList) {
				File tmp = new File(copyFile.getAbsolutePath() + File.separator + file.getName());
								
				if (file.isDirectory()) {
					tmp.mkdirs();
					layoutFileCopy(file, tmp);
				} else {
					fis = new FileInputStream(file);
					fos = new FileOutputStream(tmp);
					byte[] b = new byte[4096];
					int cnt = 0;
					while((cnt = fis.read(b)) != -1) {
						fos.write(b,0,cnt);
					}
					fis.close();
					fos.close();
				}
			}
			result = true;
		}				

		if (fis != null) {
			fis.close();
		}			
		if (fis != null) {
			fos.close();
		}
		
		return result;
	}
	
	/**
	 * folderDeleteAll
	 * 레이아웃 삭제 시 레이아웃과 관련된 폴더 삭제
	 * @param path 삭제할 레이아웃 폴더를 File객체로 전달
	 * @return boolean 폴더삭제가 정상적으로 완료될 경우 true, 반대의 경우 false return
	*/
	public Boolean folderDeleteAll(File path) throws Exception {
		boolean result = false;
		if (!path.exists()) {
			return false;
		}
				
		File[] fileList = path.listFiles();
		if (fileList.length > 0) {
			for (File file : fileList) {
				if (file.isDirectory()) {
					folderDeleteAll(file);
				} else {
					FileDelete(file);
					//file.delete(); 시큐어코딩 결함으로 주석처리, 후에 제거필요
				}
			}
		}
		//if (path.delete()) {  시큐어코딩 결함으로 주석처리, 후에 제거필요
		if (FileDelete(path)) {
			result = true;
		}
		return result;
	}	
	
	/**
	 * initRsa
	 * RSA 공개키/비밀키 암호화키 생성 및 초기화
	 * @param request request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @param session_name RSA비밀키를 저장할 세션명을 문자열로 전달
	 * @return JSONObject 생성된 RSA암호화/복호화 키와 RSA암호화 키 생성결과를 JSONObject로 return
	*/
	public JSONObject initRsa(HttpServletRequest request, String session_name) throws Exception {
		HttpSession session = request.getSession();
		KeyPairGenerator generator;
		JSONObject result = new JSONObject();
		
        generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
 
        KeyPair keyPair = generator.genKeyPair();
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
 
        session.setAttribute(session_name, privateKey); // session에 RSA 개인키를 세션에 저장
 
        RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
        String publicKeyModulus = publicSpec.getModulus().toString(16);
        String publicKeyExponent = publicSpec.getPublicExponent().toString(16);
 
        result.put("result", "success");
        result.put("RSAModulus", publicKeyModulus);
        result.put("RSAExponent", publicKeyExponent);

		return result;
	}
	
	/**
	 * decryptRsa
	 * RSA 공개키로 암호화된 문자열을 비밀키로 복호화 후 문자열로 전달
	 * @param privateKey 세션에 담긴 RSA암호화 키를 PrivateKey 객체로 전달
	 * @param securedValue RSA 공개키로 암호화된 값을 문자열로 전달
	 * @return String 복호화된 문자열 return
	*/
	public String decryptRsa(PrivateKey privateKey, String securedValue) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        byte[] encryptedBytes = hexToByteArray(securedValue);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        String decryptedValue = new String(decryptedBytes, "utf-8"); // 문자 인코딩 주의.
        return decryptedValue;
    }
	
	/**
	 * hexToByteArray
	 * 문자열로 된 헥사값을 바이트배열로 변환
	 * @param hex 바이트배열로 변환할 헥사값을 문자열로 전달
	 * @return byte[] 바이트배열화 된 값 return
	*/
	public static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() % 2 != 0) { return new byte[] {}; }
 
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            byte value = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
            bytes[(int) Math.floor(i / 2)] = value;
        }
        return bytes;
    }
	
	/**
	 * FileDelete
	 * File 객체에 담긴 파일을 삭제
	 * @param file 삭제할 파일정보를 File 객체로 전달
	 * @return void 전달받은 File 객체의 파일데이터 삭제만 수행
	*/
	private synchronized boolean FileDelete(File file) {
		return file.delete();
	}
	
	/**
	 * xssSaxFiltering
	 * 에디터 사용하는 입력항목들을 lucy-xss-sax.xml에 정의한 필터링 설정값으로 필터링
	 * @param content 에디터에서 입력한 내용을 문자열로 전달
	 * @return String 필터링된 내용을 문자열로 return
	*/
	public String xssSaxFiltering(String content) throws Exception {
		XssSaxFilter filter = XssSaxFilter.getInstance("lucy-xss-sax.xml");
		String filteredContents = filter.doFilter(content);
		return filteredContents;
	}
	
	/**
	 * uploadFileNameFiltering
	 * 첨부파일 업로드 시 오류를 유발할 수 있는 특수문자 제거 및 공백문자 _(언더바) 처리
	 * @param file_name 업로드한 첨부파일의 파일명
	 * @return String 필터링된 파일명을 문자열로 return
	*/
	public String uploadFileNameFiltering(String file_name) throws Exception {
		
		String notExtensionName = FilenameUtils.getBaseName(file_name);
		String ext = FilenameUtils.getExtension(file_name);
		
		String pattern = "\\\\/:*?\\\"\'<>&@#$!.+|";
		String regExpr = "[" + pattern + "]+";
	    String tmp_file_name = notExtensionName.replaceAll(regExpr, "").replaceAll("[ ]", "_");
	    tmp_file_name += "." + ext;
		return tmp_file_name;
	}
	
	/**
	 * UserOsChk
	 * 사용자의 OS를 체크
	 * @param request request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @return String 필터링된 파일명을 문자열로 return
	*/
	public String UserOsChk(HttpServletRequest request) {
		String userAgent = request.getHeader("user-agent");
		System.out.println("userAgent : " + userAgent);
		String os = "";
		if (userAgent.toLowerCase().indexOf("android") > -1){
            os = "Android";
        } else if (userAgent.toLowerCase().indexOf("linux") > -1) {
            os = "linux";
        } else if(userAgent.toLowerCase().indexOf("iphone") > -1){
            os = "iPhone";
        } else if(userAgent.toLowerCase().indexOf("macintosh") > -1 || userAgent.indexOf("mac") > -1){
            os = "mac";
        } else if (userAgent.toLowerCase().indexOf("windows") > -1 || userAgent.indexOf("win32") > -1){
            os = "windows";
        } else {
            os = "Other";
        }

		return os;
	}
	
	/**
	 * isMobile
	 * 사용자의 단말기 체크
	 * @param request request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @return String 필터링된 파일명을 문자열로 return
	*/
	public boolean isMobile(HttpServletRequest request) {
		boolean isMobile = false;
	    String userAgent = request.getHeader("User-Agent").toLowerCase();
	    String[] agent = {"iPhone","iPod","Android","Blackberry", "Opera Mini", "Windows ce", "Nokia", "sony"};

	    for (String mobileArr : agent) {
			if(userAgent.indexOf(mobileArr) > -1) {
				isMobile = true;
			}
		}
	    return isMobile;
	}
	
	public boolean isMobile(String os) {
		boolean isMobile = false;
		
		if(os.equals("Android") || os.equals("iPhone")) {
			isMobile = true;
		}
		
		return isMobile;
	}
	
	/**
	 * UserBrowserChk
	 * 사용자의 사용 브라우저를 체크
	 * @param request request 메소드 수행 시 HttpServletRequest 객체에서 값을 꺼낼 때 사용하는 객체
	 * @return String 필터링된 파일명을 문자열로 return
	*/
	public String UserBrowserChk(HttpServletRequest request) {
		String userAgent = request.getHeader("user-agent");
		String browser = "";

		if(userAgent.indexOf("Trident") > -1 || userAgent.indexOf("MSIE") > -1) { //IE
			browser = "MSIE";
		}else if(userAgent.indexOf("Whale") > -1){ //네이버 WHALE
			browser = "Whale";
		}else if(userAgent.indexOf("Opera") > -1 || userAgent.indexOf("OPR") > -1){ //오페라
			if(userAgent.indexOf("Opera") > -1 || userAgent.indexOf("OPR") > -1) {
				browser = "Opera";
			}
		}else if(userAgent.indexOf("Firefox") > -1){ //파이어폭스
			browser = "Firefox";
		}else if(userAgent.indexOf("Safari") > -1 && userAgent.indexOf("Chrome") == -1 ){ //사파리
			browser = "Apple Safari";
		}else if(userAgent.indexOf("Chrome") > -1){ //크롬
			browser = "Google Chrome";
		} else {
			browser = "Other";
		}

		return browser;
	}
	
	public String randomPassword() {
		char[] charSet = new char[] {
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '!', '@', '#', '$', '%', '^', '&'	
		};
		
		StringBuffer sb = new StringBuffer();
        SecureRandom sr = new SecureRandom();
        sr.setSeed(new Date().getTime());
        
        int idx = 0;
        int len = charSet.length;
        for (int i=0; i<8; i++) {
            idx = sr.nextInt(len);
            sb.append(charSet[idx]);
        }

        return sb.toString();
	}
	
	public String removeHtml(String html) {
		if (isNullOrEmpty(html)) {
			return "";
		} else {
			html = html.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
			html = html.replaceAll("<(no)?script[^>]*>.*?</(no)?script>", "");
			html = html.replaceAll("&[^;]+;", "");
			html = html.replaceAll("\\s\\s+", "");
			return html;			
		}
	}
	
	public String getDomain(HttpServletRequest request) {
		Dm_config_vo configVO = (Dm_config_vo) request.getAttribute("CONFIG_INFO");	
		String domain = "";
		if (configVO.getDm_url() != null) {
			domain = configVO.getDm_url().contains("/") ? "/" + configVO.getDm_url().split("/")[configVO.getDm_url().split("/").length -1] : "";
		}
		return domain;
	}
}
