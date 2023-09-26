package egovframework.diam.xss;

import com.navercorp.lucy.security.xss.servletfilter.defender.Defender;
import com.nhncorp.lucy.security.xss.XssPreventer;

public class XssPreventerDefender implements Defender {
	/**
	 * @param values String[]
	 * @return void
	 */
	@Override
	public void init(String[] values) {
	}

	/**
	 * @param value String
	 * @return String
	 */
	@Override
	public String doFilter(String value) {
		String tmp_value = XssPreventer.escape(value).replaceAll("&amp;", "&");
		return tmp_value;		
	}
}
