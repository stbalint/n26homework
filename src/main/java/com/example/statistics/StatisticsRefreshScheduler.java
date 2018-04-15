package com.example.statistics;

import static com.example.statistics.store.TimeTresholdProvider.ENABLED_TIME_INTERVAL;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import com.example.statistics.pojo.TransactionReport;
import com.example.statistics.store.TransactionStore;

@Component
public class StatisticsRefreshScheduler {

	private static final Logger logger = LoggerFactory.getLogger(StatisticsRefreshScheduler.class);

	@Autowired
	private TransactionStore transactionStore;

	@Autowired
	private TaskScheduler taskScheduler;

	private ScheduledFuture<?> future;

	public void resetScheduler(TransactionReport report) {
		if (future != null && !future.isDone()) {
			future.cancel(false);
		}

		Date scheduledDate = getReportOutdateTime(report);

		future = taskScheduler.schedule(() -> {
			transactionStore.cleanupQueueAndUpdateStatistics();
		}, scheduledDate);

		logger.debug("Refresh scheduled to: {}", scheduledDate);
	}

	private Date getReportOutdateTime(TransactionReport report) {
		return new Date(report.getTimestamp() + ENABLED_TIME_INTERVAL + 1);
	}

}
