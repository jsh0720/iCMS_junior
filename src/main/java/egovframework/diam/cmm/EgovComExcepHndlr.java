/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.cmm;

import egovframework.rte.fdl.cmmn.exception.handler.ExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Class Name : EgovComExcepHndlr.java
 * @Description : 공통서비스의 exception 처리 클래스
 * @Modification Information
 *
 *    수정일       수정자         수정내용
 *    -------        -------     -------------------
 *    2009. 3. 13.     이삼섭
 *
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 3. 13.
 * @version
 */
public class EgovComExcepHndlr implements ExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(EgovComExcepHndlr.class);

	/**
	 * occur
	 * Java basic Exception발생 시 어떤 Exception이 어떤 package에서 발생하였는지 로그저장기능 수행
	 * @param ex Exception발생정보를 Exception객체에 담아 전달
	 * @param packageName Exception이 발생한 패키지명을 문자열에 담아 전달
	 * @return String Exception 정보 로그저장기능만 수행
	*/
    public void occur(Exception ex, String packageName) {
		LOGGER.debug("[HANDLER][PACKAGE]::: {}", packageName);
		LOGGER.debug("[HANDLER][Exception]:::", ex);
    }
}
