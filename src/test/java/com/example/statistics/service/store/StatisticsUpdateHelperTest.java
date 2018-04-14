package com.example.statistics.service.store;

import static com.example.statistics.store.StatisticsUpdateHelper.addReportToStatistics;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.Assert;
import org.junit.Test;

import com.example.statistics.pojo.TransactionReport;
import com.example.statistics.pojo.TransactionStatistics;
import com.example.statistics.store.StatisticsUpdateHelper;

public class StatisticsUpdateHelperTest {

	@Test
	public void addFirstReportTest() {
		TransactionStatistics oldStatistics = new TransactionStatistics.Builder().build();

		TransactionStatistics newStatistics = addReportToStatistics(new TransactionReport(10.1, 1000L), oldStatistics);

		Assert.assertEquals(10.1, newStatistics.getAvg(), 0.0001);
		Assert.assertEquals(1, newStatistics.getCount());
		Assert.assertEquals(10.1, newStatistics.getMax(), 0.0001);
		Assert.assertEquals(10.1, newStatistics.getMin(), 0.0001);
		Assert.assertEquals(10.1, newStatistics.getSum(), 0.0001);
	}

	@Test
	public void addHigherAmountTest() {
		TransactionStatistics firstStatistics = new TransactionStatistics.Builder().build();

		TransactionStatistics secondStatistics = addReportToStatistics(new TransactionReport(10.0, 1000L),
				firstStatistics);
		TransactionStatistics newStatistics = addReportToStatistics(new TransactionReport(20.0, 1000L),
				secondStatistics);

		Assert.assertEquals(15.0, newStatistics.getAvg(), 0.0001);
		Assert.assertEquals(2, newStatistics.getCount());
		Assert.assertEquals(20.0, newStatistics.getMax(), 0.0001);
		Assert.assertEquals(10.0, newStatistics.getMin(), 0.0001);
		Assert.assertEquals(30.0, newStatistics.getSum(), 0.0001);
	}

	@Test
	public void addLowerAmountTest() {
		TransactionStatistics firstStatistics = new TransactionStatistics.Builder().build();

		TransactionStatistics secondStatistics = addReportToStatistics(new TransactionReport(10.0, 1000L),
				firstStatistics);
		TransactionStatistics newStatistics = addReportToStatistics(new TransactionReport(0.0, 1000L),
				secondStatistics);

		Assert.assertEquals(5.0, newStatistics.getAvg(), 0.0001);
		Assert.assertEquals(2, newStatistics.getCount());
		Assert.assertEquals(10.0, newStatistics.getMax(), 0.0001);
		Assert.assertEquals(0.0, newStatistics.getMin(), 0.0001);
		Assert.assertEquals(10.0, newStatistics.getSum(), 0.0001);
	}

	@Test
	public void negativeAmountTest() {
		TransactionStatistics firstStatistics = new TransactionStatistics.Builder().build();

		TransactionStatistics secondStatistics = addReportToStatistics(new TransactionReport(10.0, 1000L),
				firstStatistics);
		TransactionStatistics newStatistics = addReportToStatistics(new TransactionReport(-20.0, 1000L),
				secondStatistics);

		Assert.assertEquals(-5.0, newStatistics.getAvg(), 0.0001);
		Assert.assertEquals(2, newStatistics.getCount());
		Assert.assertEquals(10.0, newStatistics.getMax(), 0.0001);
		Assert.assertEquals(-20.0, newStatistics.getMin(), 0.0001);
		Assert.assertEquals(-10.0, newStatistics.getSum(), 0.0001);
	}

	@Test
	public void calculateStatisticsTest() {
		Queue<TransactionReport> queue = new LinkedList<>();
		queue.add(new TransactionReport(20.0, 1000L));
		queue.add(new TransactionReport(10.0, 1000L));
		queue.add(new TransactionReport(15.0, 1000L));

		TransactionStatistics statistics = StatisticsUpdateHelper.calculateStatistics(queue);

		Assert.assertEquals(15.0, statistics.getAvg(), 0.0001);
		Assert.assertEquals(3, statistics.getCount());
		Assert.assertEquals(20.0, statistics.getMax(), 0.0001);
		Assert.assertEquals(10.0, statistics.getMin(), 0.0001);
		Assert.assertEquals(45.0, statistics.getSum(), 0.0001);
	}
}
