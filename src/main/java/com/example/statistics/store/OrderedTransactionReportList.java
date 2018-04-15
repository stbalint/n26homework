package com.example.statistics.store;

import java.util.LinkedList;
import java.util.ListIterator;

import com.example.statistics.pojo.TransactionReport;

public class OrderedTransactionReportList extends LinkedList<TransactionReport> {

	public void addInOrder(TransactionReport transactionReport) {
		ListIterator<TransactionReport> iterator = listIterator();
		while (true) {
			if (!iterator.hasNext()) {
				iterator.add(transactionReport);
				break;
			}
			TransactionReport queueReport = iterator.next();
			if (queueReport.getTimestamp().longValue() > transactionReport.getTimestamp().longValue()) {
				iterator.previous();
				iterator.add(transactionReport);
				break;
			}
		}
	}
}
