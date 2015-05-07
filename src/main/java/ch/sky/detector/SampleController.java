package ch.sky.detector;

import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ch.sky.detector.service.HackerDetectorService;
import ch.sky.detector.util.FileMonitor;
import ch.sky.detector.util.IpUtility;

/**
 * Handles requests for the application pages.
 * 
 * @author asemota stefan
 */
@Controller
public class SampleController {

	private static final Logger logger = LoggerFactory
			.getLogger(SampleController.class);
	private static String bundle = "configuration";
	private static ResourceBundle settings = ResourceBundle.getBundle(bundle);
	private static final int MAX_FAILED_LOGIN_ATTEMPTS = 5;
	private static int ipOccurences = 0;
	private static String previousIp = "";

	// Log file monitor
	private FileMonitor fileMonitor;

	@Autowired
	private HackerDetectorService hackerDetectorService;

	@PostConstruct
	public void startFileMonitor() {
		fileMonitor = new FileMonitor(hackerDetectorService);
		Thread dirWatcherThread = new Thread(fileMonitor);
		dirWatcherThread.start();
	}

	/**
	 * The request mapper for welcome page
	 * 
	 * @return {@link String}
	 */
	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public String welcome() {
		return "welcome";
	}

	/**
	 * Simply selects the login view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String showLogin(Model model, LoginForm loginform) {
		if (!model.containsAttribute("error")) {
			model.addAttribute("error", false);
		}
		model.addAttribute("loginAttribute", loginform);
		return "login";
	}

	/**
	 * The POST method to submit login credentials.
	 * 
	 * @throws Exception
	 * 
	 */
	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public String login(Model model, LoginForm loginform, Locale locale,
			HttpServletRequest request) throws Exception {

		boolean loginFailed = true;

		// Get ip
		String userIpAdresse = IpUtility.getIpFromRequest(request);
		if (userIpAdresse.equals(previousIp)) {
			ipOccurences++;
		} else {
			ipOccurences = 0;
		}
		Date date = new Date();

		// returns the epoch time in milliseconds.
		String epoch = String.valueOf(date.getTime());

		String username = loginform.getUsername();
		String password = loginform.getPassword();

		// set previous ip
		previousIp = userIpAdresse;
		// A simple authentication manager
		if (username != null && password != null) {

			if (username.equals(settings.getString("username"))
					&& password.equals(settings.getObject("password"))) {
				// Set a session attribute to check authentication then redirect
				// to the welcome uri;
				request.getSession().setAttribute(
						AuthenticationInterceptor.LOGIN_STATUS_OK, loginform);

				// log msg to rolling daily log file if debug enabled
				if (logger.isDebugEnabled()) {
					logger.info(
							"{},{}",
							appendLogMsg(userIpAdresse, epoch),
							appendLogMsg(
									AuthenticationInterceptor.LOGIN_STATUS_OK,
									username));
				}
				loginFailed = false;
				return "redirect:/welcome";
			} else {

				// check for max failed login attempts
				if (loginFailed && ipOccurences >= MAX_FAILED_LOGIN_ATTEMPTS) {
					// log msg to rolling daily log file
					logger.error(
							"|{},{}|",
							appendLogMsg(userIpAdresse, epoch),
							appendLogMsg(
									AuthenticationInterceptor.LOGIN_STATUS_FAIL,
									username));
				}
				return "redirect:/login.failed";
			}
		} else {
			// check for max failed login attempts
			if (loginFailed && ipOccurences >= MAX_FAILED_LOGIN_ATTEMPTS) {
				// log msg to rolling daily log file
				logger.error(
						"|{},{}|",
						appendLogMsg(userIpAdresse, epoch),
						appendLogMsg(
								AuthenticationInterceptor.LOGIN_STATUS_FAIL,
								username));
			}
			return "redirect:/login.failed";
		}
	}

	/**
	 * Constructs log messages with the supplied values
	 * 
	 * @param value
	 * @param value2
	 * @return
	 */
	private String appendLogMsg(String value, String value2) {
		StringBuilder sb = new StringBuilder();
		sb.append(value);
		sb.append(",");
		sb.append(value2);
		return sb.toString();
	}

	/**
	 * The login failed controller
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/login.failed", method = RequestMethod.GET)
	public String loginFailed(Model model, LoginForm loginForm) {
		// logger.debug("Showing the login failed page");
		model.addAttribute("error", true);
		model.addAttribute("loginAttribute", loginForm);
		return "login";
	}
}
