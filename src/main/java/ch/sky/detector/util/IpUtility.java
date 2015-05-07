package ch.sky.detector.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Helper class to retrieve the ip address
 * 
 * @author asemota stephan
 *
 */
public class IpUtility {

	/**
	 * Returns the correct ip from the request
	 * 
	 * @param request
	 *            {@link HttpServletRequest}
	 * @return ip as {@link String}
	 */
	public static String getIpFromRequest(HttpServletRequest request) {
		// is client behind something?
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		return ipAddress;
	}
}
