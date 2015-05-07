package ch.sky.detector.service;

import ch.sky.detector.util.FailedAttempt;

public interface HackerDetectorService {
	/**
	 * This reads the last line of the log file and returns the ip address
	 * 
	 * @param line
	 *            this parameter is ignored
	 * @return {@link FailedAttempt} which is the ip address
	 */
	public FailedAttempt parseLine(String line);
}
