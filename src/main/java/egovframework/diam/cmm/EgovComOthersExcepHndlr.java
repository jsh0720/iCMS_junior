/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.cmm;

import egovframework.rte.fdl.cmmn.exception.handler.ExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Class Name : EgovComOthersExcepHndlr.java
 * @Description : Exception 발생시 실행되는 Handler 인터페이스
 * @author 공통서비스 개발팀
 */

public class EgovComOthersExcepHndlr implements ExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(EgovComOthersExcepHndlr.class);
	
	/**
	 * occur
	 * basic Exception외 기타 Exception 발생 시 어떤 Exception이 어떤 package에서 발생하였는지 로그저장기능 수행
	 * @param ex Exception발생정보를 Exception객체에 담아 전달
	 * @param packageName Exception이 발생한 패키지명을 문자열에 담아 전달
	 * @return String Exception 정보 로그저장기능만 수행
	*/
    public void occur(Exception exception, String packageName) {
    	//log.debug(" EgovServiceExceptionHandler run...............");
    	LOGGER.error(packageName, exception);
    }
}
