package com.example.statistics.service.store;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.example.statistics.StatisticsRefreshScheduler;
import com.example.statistics.pojo.TransactionReport;
import com.example.statistics.pojo.TransactionStatistics;
import com.example.statistics.store.TimeTresholdProvider;
import com.example.statistics.store.TransactionStore;

public class TransactionStoreTest {

	@InjectMocks
	private TransactionStore target;

	@Mock
	private TimeTresholdProvider timeTresholdProvider;

	@Mock
	private StatisticsRefreshScheduler statisticsRefreshScheduler;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void tooOldReportTest() {
		TransactionReport oldReport = new TransactionReport(10.1, 1000L);

		Mockito.when(timeTresholdProvider.getTreshold()).thenReturn(1500L);

		assertFalse(target.update(oldReport));
	}

	@Test
	public void updateTest() {
		TransactionReport oldReport = new TransactionReport(10.1, 1000L);

		Mockito.when(timeTresholdProvider.getTreshold()).thenReturn(500L);

		boolean isSuccesful = target.update(oldReport);
		TransactionStatistics statistics = target.getStatistics();

		assertTrue(isSuccesful);
		Assert.assertEquals(10.1, statistics.getAvg(), 0.0001);
	}

	@Test
	public void updateIntegrationTest() {
		TransactionReport report1 = new TransactionReport(10.0, 1000L);
		TransactionReport report2 = new TransactionReport(20.0, 2000L);
		TransactionReport report3 = new TransactionReport(30.0, 3000L);

		Mockito.when(timeTresholdProvider.getTreshold()).thenReturn(500L).thenReturn(500L).thenReturn(1500L)
				.thenReturn(1500L).thenReturn(2500L).thenReturn(2500L);

		target.update(report1);
		target.cleanupQueueAndUpdateStatistics();
		TransactionStatistics statistics = target.getStatistics();

		Assert.assertEquals(10.0, statistics.getAvg(), 0.0001);
		Assert.assertEquals(1, statistics.getCount());

		target.update(report2);
		target.cleanupQueueAndUpdateStatistics();
		statistics = target.getStatistics();

		Assert.assertEquals(20.0, statistics.getAvg(), 0.0001);
		Assert.assertEquals(1, statistics.getCount());

		target.update(report3);
		target.cleanupQueueAndUpdateStatistics();
		statistics = target.getStatistics();

		Assert.assertEquals(30.0, statistics.getAvg(), 0.0001);
		Assert.assertEquals(1, statistics.getCount());
	}

	@Test
	public void initilizeSchedulerTest() {
		TransactionReport report1 = new TransactionReport(10.1, 1000L);
		TransactionReport report2 = new TransactionReport(10.1, 1500L);

		Mockito.when(timeTresholdProvider.getTreshold()).thenReturn(500L).thenReturn(1500L).thenReturn(2000L);

		target.update(report1);
		target.update(report2);

		Mockito.verify(statisticsRefreshScheduler)
				.resetScheduler(1000 + TimeTresholdProvider.ENABLED_TIME_INTERVAL + 1);

		target.cleanupQueueAndUpdateStatistics();

		Mockito.verifyNoMoreInteractions(statisticsRefreshScheduler);
	}

	@Test
	public void resetSchedulerTest() {
		TransactionReport report1 = new TransactionReport(10.1, 1000L);
		TransactionReport report2 = new TransactionReport(10.1, 1500L);

		Mockito.when(timeTresholdProvider.getTreshold()).thenReturn(500L).thenReturn(1000L).thenReturn(1499L);

		target.update(report1);
		target.update(report2);

		Mockito.verify(statisticsRefreshScheduler)
				.resetScheduler(1000 + TimeTresholdProvider.ENABLED_TIME_INTERVAL + 1);

		target.cleanupQueueAndUpdateStatistics();

		Mockito.verify(statisticsRefreshScheduler)
				.resetScheduler(1500 + TimeTresholdProvider.ENABLED_TIME_INTERVAL + 1);
	}

}
