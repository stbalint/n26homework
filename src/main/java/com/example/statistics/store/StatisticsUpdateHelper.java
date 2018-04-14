package com.example.statistics.store;

import java.util.Queue;

import com.example.statistics.pojo.TransactionReport;
import com.example.statistics.pojo.TransactionStatistics;

public class StatisticsUpdateHelper {

	public static TransactionStatistics calculateStatistics(Queue<TransactionReport> reportQueue) {
		TransactionStatistics statistics = new TransactionStatistics.Builder().build();
		for (TransactionReport report : reportQueue) {
			statistics = addReportToStatistics(report, statistics);
		}
		return statistics;
	}

	public static TransactionStatistics addReportToStatistics(TransactionReport report,
			TransactionStatistics oldStatistics) {
		long count = oldStatistics.getCount() + 1;
		double sum = oldStatistics.getSum() + report.getAmount();
		double min;

		if (oldStatistics.getCount() == 0) {
			min = report.getAmount();
		} else {
			min = report.getAmount() < oldStatistics.getMin() ? report.getAmount() : oldStatistics.getMin();
		}

		return new TransactionStatistics.Builder().setCount(count).setSum(sum)
				.setMax(report.getAmount() > oldStatistics.getMax() ? report.getAmount() : oldStatistics.getMax())
				.setMin(min).setAvg(sum / count).build();
	}

}
