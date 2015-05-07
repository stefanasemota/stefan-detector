package ch.sky.detector;

import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * The Authentication Interceptor class. This class implements the spring
 * HandlerInteceptor to processing requests into three steps:<br>
 * - pre-handle<br>
 * - post-handle<br>
 * - after-completition
 * 
 * @see org.springframework.web.servlet.HandlerInterceptor
 * @author asemota stephan
 * 
 */
public class AuthenticationInterceptor implements HandlerInterceptor {

	private static final Logger log = LoggerFactory
			.getLogger(AuthenticationInterceptor.class);
	public static final String LOGIN_STATUS_OK = "SIGNIN_SUCCESS";
	public static final String LOGIN_STATUS_FAIL = "SIGNIN_FAILURE";
	static String bundle = "configuration";
	public static ResourceBundle settings = ResourceBundle.getBundle(bundle);

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if (log.isDebugEnabled()) {
			log.info("Interceptor: Pre-handle");
		}

		// Avoid a redirect loop for some urls
		if (!request.getRequestURI().equals("/stefan-detector/")
				&& !request.getRequestURI().equals("/stefan-detector/login.do")
				&& !request.getRequestURI().equals(
						"/stefan-detector/login.failed")) {
			LoginForm userData = (LoginForm) request.getSession().getAttribute(
					LOGIN_STATUS_OK);// ("LOGGEDIN_USER");
			if (userData == null) {
				response.sendRedirect("/stefan-detector/");
				return false;
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Post-handle");
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("After-completion");
		}
	}
}