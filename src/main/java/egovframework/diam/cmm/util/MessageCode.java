package egovframework.diam.cmm.util;

public enum MessageCode {
	CMS_SELECT_SUCCESS(1000, "데이터를 조회 하였습니다."),
	CMS_UPSERT_SUCCESS(1001, "데이터를 등록/수정 하였습니다."),
	CMS_INSERT_SUCCESS(1002, "데이터를 등록 하였습니다."),
	CMS_UPDATE_SUCCESS(1003, "데이터를 수정 하였습니다."),
	CMS_DELETE_SUCCESS(1004, "데이터를 삭제하였습니다."),
	CMS_INSERT_FAIL(1005, "데이터 등록을 실패하였습니다."),
	CMS_UPDATE_FAIL(1006,"데이터 수정을 실패하였습니다."),
	CMS_DELETE_FAIL(1007, "데이터 삭제를 실패하였습니다."),
	CMS_SELECT_NODATA(1009, "조건에 맞는 데이터가 없습니다."),
	CMM_LOGIN_ALREADY(5000, "이미 로그인 중입니다."),
	CMM_ACCESS_DENIED(5001, "접근 불가능한 계정입니다."),
	CMM_LOGIN_SUCCESS(5002, "로그인에 성공하였습니다."),
	CMM_LOGIN_FAIL(5003, "로그인에 실패하였습니다."),
	CMM_LOGOUT_SUCCESS(5004, "로그아웃 하였습니다."),
	CMM_SYSTEM_ERROR(5005, "오류가 발생하였습니다. 관리자에게 문의주세요."),
	CMM_DATA_ERROR(5006, "SQL 구문 오류가 발생하였습니다. 관리자에게 문의주세요."),
	CMM_ENCRYPT_EXPIRED(5007, "암호화 키가 만료되었습니다. 새로고침 후 다시 진행하시기 바랍니다."),
	CMM_FILE_ERROR(5008, "파일 처리중 오류가 발생했습니다. 관리자에게 문의주세요."),
	CMM_REQUEST_BADREQUEST(5009, "잘못된 요청입니다."),
	CMM_SESSION_EXPIRED(5010, "로그인 세션이 만료되었습니다."),
	CMM_TRANSACTION_FAIL(5011, "트랜잭션 처리 중 오류가 발생했습니다. 관리자에게 문의주세요."),
	CMM_JSON_ERROR(5012, "JSON 데이터 변환 중 오류가 발생했습니다. 관리자에게 문의주세요."),
	CMM_NULL_ERROR(5013, "값이 존재하지 않습니다.")
	;
	
	private int code;
	private String message;
	
	public String getMessage() {
		return new String(message);
	}
	
	public int getCode() {
		return code;
	}
	
	private MessageCode(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public String getLog() {
		String result = "[code : "+ code +"] [message : " + message + "]"; 
		return result;
	}
}
