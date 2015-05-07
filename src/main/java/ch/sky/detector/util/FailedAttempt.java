package ch.sky.detector.util;

/**
 * Models the login failed attempts
 * 
 * @author asemota stephan
 *
 */
public class FailedAttempt {
	private String ipAddress;
	private String username;
	private int failedAttempts = 0;

	public FailedAttempt(String ipadresse, String username) {
		this.ipAddress = ipadresse;
		this.username = username;
	}

	public void increaseAttempts() {
		this.failedAttempts++;
	}

	/**
	 * 
	 * @return ip address as {@link String}
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * 
	 * @return user name as {@link String}
	 */
	public String getUsername() {
		return username;
	}

	public int getFailedAttempts() {
		return failedAttempts;
	}

}
