/**
 * Description Date(Format: 2021/11/16)
 */
package egovframework.diam.cmm;

import egovframework.rte.fdl.cmmn.trace.handler.TraceHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Class Name : EgovComTraceHandler.java
 * @Description : 공통서비스의 trace 처리 클래스
 * @Modification Information
 *
 *    수정일       수정자         수정내용
 *    -------        -------     -------------------
 *    2011. 09. 30.     JJY
 *
 * @author JJY
 * @since 2011.9.30.
 */
public class EgovComTraceHandler implements TraceHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(EgovComTraceHandler.class);

	/**
	 * todo
	 * Exception 발생 시 프로퍼티 파일에 등록된 메시지파일의 내용과 발생한 클래스명 로그저장기능 수행
	 * @param clazz Exception이 발생한 클래스 정보를 Class객체에 담아 전달
	 * @param message 프로퍼티 파일에 등록된 메시지 내용을 문자열에 담아 전달
	 * @return String Exception 정보 로그저장기능만 수행
	*/
    public void todo(Class<?> clazz, String message) {
    	//System.out.println("log ==> DefaultTraceHandler run...............");
    	LOGGER.debug("[TRACE]CLASS::: {}", clazz.getName());
    	LOGGER.debug("[TRACE]MESSAGE::: {}", message);
    	//이곳에서 후속처리로 필요한 액션을 취할 수 있다.
    }
}
