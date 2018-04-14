package com.example.statistics.store;

import static com.example.statistics.store.StatisticsUpdateHelper.addReportToStatistics;
import static com.example.statistics.store.TimeTresholdProvider.ENABLED_TIME_INTERVAL;

import java.util.LinkedList;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.statistics.StatisticsRefreshScheduler;
import com.example.statistics.pojo.TransactionReport;
import com.example.statistics.pojo.TransactionStatistics;

@Component
public class TransactionStore {

	private static final Logger logger = LoggerFactory.getLogger(TransactionStore.class);

	@Autowired
	private TimeTresholdProvider timeTresholdProvider;

	@Autowired
	private StatisticsRefreshScheduler statisticsRefreshScheduler;

	private Queue<TransactionReport> reportQueue = new LinkedList<>();

	private TransactionStatistics transactionStatistics = new TransactionStatistics.Builder().build();

	public synchronized boolean update(TransactionReport transactionReport) {
		long timeTreshold = timeTresholdProvider.getTreshold();
		if (checkIfTransactionIsOld(transactionReport, timeTreshold)) {
			return false;
		}
		initializeSchedulerIfRequired(transactionReport);
		reportQueue.add(transactionReport);
		transactionStatistics = addReportToStatistics(transactionReport, transactionStatistics);
		return true;
	}

	public synchronized void cleanupQueueAndUpdateStatistics() {
		long timeTreshold = timeTresholdProvider.getTreshold();
		boolean isReportRemoved = false;
		TransactionReport reportInQueue;
		while ((reportInQueue = reportQueue.peek()) != null) {
			if (checkIfTransactionIsOld(reportInQueue, timeTreshold)) {
				isReportRemoved = true;
				reportQueue.poll();
				logger.debug("Trasaction outdated: {}", reportInQueue);
			} else {
				break;
			}
		}
		if (isReportRemoved) {
			transactionStatistics = StatisticsUpdateHelper.calculateStatistics(reportQueue);
			resetScheduler();
		}
	}

	private void resetScheduler() {
		TransactionReport report = reportQueue.peek();
		if (report != null) {
			statisticsRefreshScheduler.resetScheduler(report.getTimestamp() + ENABLED_TIME_INTERVAL + 1);
		}
	}

	private void initializeSchedulerIfRequired(TransactionReport report) {
		if (reportQueue.isEmpty()) {
			statisticsRefreshScheduler.resetScheduler(report.getTimestamp() + ENABLED_TIME_INTERVAL + 1);
		}
	}

	private boolean checkIfTransactionIsOld(TransactionReport transactionReport, long timeTreshold) {
		return timeTreshold > transactionReport.getTimestamp();
	}

	public TransactionStatistics getStatistics() {
		return transactionStatistics;
	}
}
