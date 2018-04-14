package com.example.statistics.service;

import org.springframework.stereotype.Component;

import com.example.statistics.pojo.TransactionReport;

@Component
public class TransactionInputValidator {

	public void validate(TransactionReport report) {
		if (report.getTimestamp() > System.currentTimeMillis()) {
			throw new IllegalArgumentException("Transaction from the future dropped: " + report.toString());
		}
	}
}
