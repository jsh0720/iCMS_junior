/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.cmm.model;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Class Name : Dm_common_se2_vo.java
 * @Description : 네이버 스마트에디터 2.0 사진 첨부 시 사용변수 정의 VO 클래스
 * @author 디자인아이엠 개발팀 한연재
 * @since 2021.11.16
 * @version 1.0
*/

public class Dm_common_se2_vo implements Serializable {
	
	/** VO 직렬화 시 version uid 값 */
	private static final long serialVersionUID = 3911508782682057014L;

	/** 네이버 스마트에디터2 업로드 이미지파일 */
	private MultipartFile filedata;
	
	/** 네이버 스마트에디터2 업로드 후 이동할 URL */
	private String callback;
	
	/** 네이버 스마트에디터2 업로드 후 실행될 함수명 */
	private String callback_func;

	/**
	 * getFiledata
	 * 네이버 스마트에디터2 업로드 이미지파일을 전달
	 * @return filedata vo객체의 네이버 스마트에디터2 업로드 이미지파일에 해당하는 지역변수 값을 return
	*/
	public MultipartFile getFiledata() {
		return filedata;
	}

	/**
	 * setFiledata
	 * 네이버 스마트에디터2 업로드 이미지파일을 전달받은 MultipartFile객체로 세팅
	 * @param filedata 네이버 스마트에디터2 업로드 이미지파일을 MultipartFile객체로 전달
	*/
	public void setFiledata(MultipartFile filedata) {
		this.filedata = filedata;
	}

	/**
	 * getCallback
	 * 네이버 스마트에디터2 업로드 후 이동할 URL을 전달
	 * @return callback vo객체의 네이버 스마트에디터2 업로드 후 이동할 URL에 해당하는 지역변수 값을 return
	*/
	public String getCallback() {
		return callback;
	}

	/**
	 * setCallback
	 * 네이버 스마트에디터2 업로드 후 이동할 URL을 전달받은 문자열로 세팅
	 * @param callback 네이버 스마트에디터2 업로드 후 이동할 URL을 문자열로 전달
	*/
	public void setCallback(String callback) {
		this.callback = callback;
	}

	/**
	 * getCallback_func
	 * 네이버 스마트에디터2 업로드 후 실행될 함수명을 전달
	 * @return callback_func vo객체의 네이버 스마트에디터2 업로드 후 실행될 함수명에 해당하는 지역변수 값을 return
	*/
	public String getCallback_func() {
		return callback_func;
	}

	/**
	 * setCallback_func
	 * 네이버 스마트에디터2 업로드 후 실행될 함수명을 전달받은 문자열로 세팅
	 * @param callback_func 네이버 스마트에디터2 업로드 후 실행될 함수명을 문자열로 전달
	*/
	public void setCallback_func(String callback_func) {
		this.callback_func = callback_func;
	}	
}