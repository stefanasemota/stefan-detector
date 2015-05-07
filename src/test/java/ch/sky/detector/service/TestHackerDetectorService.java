package ch.sky.detector.service;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.sky.detector.service.impl.HackerDetectorServiceImpl;
import ch.sky.detector.util.FailedAttempt;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class TestHackerDetectorService {
	private static final Logger logger = LoggerFactory
			.getLogger(TestHackerDetectorService.class);
	@Autowired
	private HackerDetectorServiceImpl hackerTestSubject;

	@Test//(expected = junit.framework.AssertionFailedError.class)
	public void test() {
		Assert.assertNotNull(hackerTestSubject.parseLine(null));
	}

	@Test
	public void testNormalIpAddress() {
		logger.error("|203.000.113.195,1418075897397,SIGNIN_FAILURE,normalUser|");
		logger.error("|203.000.113.195,1418075897397,SIGNIN_FAILURE,normalUser|");
		logger.error("|203.000.113.195,1418075897397,SIGNIN_FAILURE,normalUser|");
		logger.error("|203.000.113.195,1418075897397,SIGNIN_FAILURE,normalUser|");
		logger.error("|203.000.113.195,1418075897397,SIGNIN_FAILURE,normalUser|");
		FailedAttempt failedAttempt = hackerTestSubject.parseLine(null);
		Assert.assertNotNull(failedAttempt);
		Assert.assertEquals("203.000.113.195", failedAttempt.getIpAddress());
	}

	@Test
	public void testPrivateIpAddress() {
		logger.error("|172.31.255.255,1418075897397,SIGNIN_FAILURE,privateIpAddresseUser|");
		logger.error("|172.31.255.255,1418075897397,SIGNIN_FAILURE,privateIpAddresseUser|");
		logger.error("|172.31.255.255,1418075897397,SIGNIN_FAILURE,privateIpAddresseUser|");
		logger.error("|172.31.255.255,1418075897397,SIGNIN_FAILURE,privateIpAddresseUser|");
		logger.error("|172.31.255.255,1418075897397,SIGNIN_FAILURE,privateIpAddresseUser|");
		FailedAttempt failedAttempt = hackerTestSubject.parseLine(null);
		Assert.assertNotNull(failedAttempt);
		Assert.assertEquals("172.31.255.255", failedAttempt.getIpAddress());
	}
}
