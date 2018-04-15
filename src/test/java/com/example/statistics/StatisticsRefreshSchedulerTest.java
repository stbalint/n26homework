package com.example.statistics;

import static com.example.statistics.store.TimeTresholdProvider.ENABLED_TIME_INTERVAL;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.TaskScheduler;

import com.example.statistics.pojo.TransactionReport;
import com.example.statistics.store.TransactionStore;

public class StatisticsRefreshSchedulerTest {

	@InjectMocks
	private StatisticsRefreshScheduler target;

	@Mock
	private TransactionStore transactionStore;

	@Mock
	private TaskScheduler taskScheduler;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);

	}

	@Test
	public void scheduleTest() {
		TransactionReport report = new TransactionReport(10.0, 1000L);
		Date reportOutdateTime = new Date(report.getTimestamp() + ENABLED_TIME_INTERVAL + 1);

		Mockito.when(taskScheduler.schedule(Mockito.any(), Mockito.eq(reportOutdateTime))).then((invocation) -> {
			Runnable task = invocation.getArgument(0);
			task.run();
			return null;
		});

		target.resetScheduler(report);

		Mockito.verify(transactionStore).cleanupQueueAndUpdateStatistics();
	}
}
