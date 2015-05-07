package ch.sky.detector.util;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ch.sky.detector.service.HackerDetectorService;

/**
 * This class extends the runnable interface and wraps the {@link WatchService}
 * API in the run method. This run method listens for periodical changes on the
 * log file.
 * 
 * @author asemota stephan
 *
 */
@Component
public class FileMonitor implements Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(FileMonitor.class);

	private Path path = Paths.get("log/");

	private HackerDetectorService hackerDetectorService;

	/**
	 * Default init construtor
	 */
	public FileMonitor() {
	}

	/**
	 * Second constructor
	 * 
	 * @param hackerDetectorService
	 *            {@link HackerDetectorService}
	 */
	public FileMonitor(HackerDetectorService hackerDetectorService) {
		this.hackerDetectorService = hackerDetectorService;
	}

	// print the events and the affected file
	private void printEvent(WatchEvent<?> event) {
		Kind<?> kind = event.kind();
		if (kind.equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
			Path pathModified = (Path) event.context();
			logger.info("Log file was modified {} :", pathModified, "");
			FailedAttempt failed = hackerDetectorService.parseLine(null);
			if (failed != null) {
				logger.info(
						"Current print has a minimum of 5 failed login attempts from IP {} :",
						failed.getIpAddress(), "");
			}
		}
	}

	@Override
	public void run() {
		try {
			WatchService watchService = path.getFileSystem().newWatchService();
			path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

			// loop forever to watch directory
			while (true) {
				WatchKey watchKey;
				// this call is blocking until
				// events are present
				watchKey = watchService.take();

				// poll for file system events on the WatchKey
				for (final WatchEvent<?> event : watchKey.pollEvents()) {
					printEvent(event);
				}

				// if the watched directed gets deleted, get out of run method
				if (!watchKey.reset()) {
					logger.info("No longer valid");
					watchKey.cancel();
					watchService.close();
					break;
				}
			}
		} catch (InterruptedException ex) {
			logger.error("interrupted. Goodbye");
			return;
		} catch (IOException ex) {
			logger.error(ex.getMessage());
			return;
		}
	}
}
