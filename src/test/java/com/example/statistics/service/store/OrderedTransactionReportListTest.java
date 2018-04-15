package com.example.statistics.service.store;

import org.junit.Assert;
import org.junit.Test;

import com.example.statistics.pojo.TransactionReport;
import com.example.statistics.store.OrderedTransactionReportList;

public class OrderedTransactionReportListTest {

	@Test
	public void addTest() {
		OrderedTransactionReportList list = new OrderedTransactionReportList();

		list.addInOrder(new TransactionReport(10.0, 1000L));
		list.addInOrder(new TransactionReport(11.0, 1100L));
		list.addInOrder(new TransactionReport(11.0, 1050L));
		list.addInOrder(new TransactionReport(11.0, 500L));
		list.addInOrder(new TransactionReport(11.0, 1500L));

		Assert.assertEquals(5, list.size());

		Assert.assertEquals(500, list.get(0).getTimestamp().longValue());
		Assert.assertEquals(1000, list.get(1).getTimestamp().longValue());
		Assert.assertEquals(1050, list.get(2).getTimestamp().longValue());
		Assert.assertEquals(1100, list.get(3).getTimestamp().longValue());
		Assert.assertEquals(1500, list.get(4).getTimestamp().longValue());
	}

}
