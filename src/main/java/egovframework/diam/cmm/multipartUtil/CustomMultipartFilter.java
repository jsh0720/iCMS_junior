package egovframework.diam.cmm.multipartUtil;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.MultipartFilter;

public class CustomMultipartFilter extends MultipartFilter {

	@Override
	protected void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		CustomMultipartResolver multipartResolver = (CustomMultipartResolver) lookupMultipartResolver(request);
		HttpServletRequest processedRequest = request;
		
		try {
			if (multipartResolver.isMultipart(processedRequest)) {
				if (logger.isDebugEnabled()) {
					logger.debug("Resolving multipart request [" + processedRequest.getRequestURI() +
							"] with MultipartFilter");
				}
				processedRequest = multipartResolver.resolveMultipart(processedRequest);
			}
			else {
				// A regular request...
				if (logger.isDebugEnabled()) {
					logger.debug("Request [" + processedRequest.getRequestURI() + "] is not a multipart request");
				}
			}
			
			filterChain.doFilter(processedRequest, response);
		} catch (TotalSizeExceedException te) {
			if (isAjax(request)) {
				JSONObject obj = new JSONObject();
				try {
					obj.put("notice", "업로드 용량 초과입니다.<br>" + "최대 업로드 용량 : " + te.getMaxUploadSize() + "MB<br> 첨부된 용량 : " + te.getRequestSize() + "MB");
				} catch (JSONException e1) {
					
				}
				response.setStatus(413);
				response.setContentType("application/json");
				response.getWriter().write(obj.toString());
			} else {
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("<script>alert('업로드 용량 초과입니다.\\n최대 업로드 용량: " + te.getMaxUploadSize() + "MB\\n첨부된 용량: " + te.getRequestSize() + "MB'); history.go(-1);</script>");
				out.flush();
				out.close();
			}
		} catch (PerSizeExceedException pe) {
			if (isAjax(request)) {
				JSONObject obj = new JSONObject();
				try {
					obj.put("notice", "업로드 용량 초과입니다.<br>" + "각 파일별 최대 : " + pe.getMaxUploadSizePerFile() + "MB 까지만 첨부 가능합니다.");
				} catch (JSONException e1) {
					
				}
				response.setStatus(413);
				response.setContentType("application/json");
				response.getWriter().write(obj.toString());
			} else {
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("<script>alert('업로드 용량 초과입니다.\\n각 파일별 최대: " + pe.getMaxUploadSizePerFile() + "MB 첨부 가능합니다.'); history.go(-1);</script>");
				out.flush();
				out.close();
			}
			
		} finally {
			if (processedRequest instanceof MultipartHttpServletRequest) {
				multipartResolver.cleanupMultipart((MultipartHttpServletRequest) processedRequest);
			}
		}
	}
	
	private boolean isAjax(HttpServletRequest request) {
		if (request.getHeader("x-requested-with") != null) return true;
		else return false;
	}
}
