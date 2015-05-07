package ch.sky.detector.service.impl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ch.sky.detector.service.HackerDetectorService;
import ch.sky.detector.util.FailedAttempt;

/**
 * This class defines a single method 'parseLine'. The method should take one
 * line of the log file at a time and return the IP address if any suspicious
 * activity is identified or null if the activity appears to be normal.
 * 
 * @author asemota stefan
 *
 */
@Service("hackerDetectorService")
public class HackerDetectorServiceImpl implements HackerDetectorService {
	private static final Logger logger = LoggerFactory
			.getLogger(HackerDetectorService.class);

	// log file location
	private String LOG_FILE_LOCATION = "log/checkLog.log";

	@Override
	public FailedAttempt parseLine(String line) {
		LineIterator it;
		try {
			it = FileUtils.lineIterator(new File(LOG_FILE_LOCATION), "UTF-8");
			String lastLine = "";
			try {
				while (it.hasNext()) {
					// save last line
					lastLine = it.nextLine();
				}
			} finally {
				LineIterator.closeQuietly(it);
			}
			// do something with last line
			String stripedValue = StringUtils.substringBetween(lastLine, "|",
					"|");
			if (StringUtils.isNotEmpty(stripedValue)) {
				String[] items = StringUtils.split(stripedValue, ",");
				return new FailedAttempt(items[0], items[3]);
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Ops Error: ", e);
		}
		return null;
	}
}
