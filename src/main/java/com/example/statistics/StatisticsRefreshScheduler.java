package com.example.statistics;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import com.example.statistics.store.TransactionStore;

@Component
public class StatisticsRefreshScheduler {

	private static final Logger logger = LoggerFactory.getLogger(StatisticsRefreshScheduler.class);

	@Autowired
	private TransactionStore transactionStore;

	@Autowired
	private TaskScheduler taskScheduler;

	private ScheduledFuture<?> future;

	public void resetScheduler(long time) {
		if (future != null && !future.isCancelled()) {
			future.cancel(false);
		}

		Date scheduledDate = new Date(time);

		future = taskScheduler.schedule(() -> {
			transactionStore.cleanupQueueAndUpdateStatistics();
		}, scheduledDate);

		logger.debug("Refresh scheduled to: {}", scheduledDate);
	}

}
