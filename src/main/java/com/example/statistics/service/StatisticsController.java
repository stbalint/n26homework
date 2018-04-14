package com.example.statistics.service;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.statistics.pojo.TransactionReport;
import com.example.statistics.pojo.TransactionStatistics;
import com.example.statistics.store.TransactionStore;

@RestController
@ControllerAdvice
public class StatisticsController {

	private static final Logger logger = LoggerFactory.getLogger(StatisticsController.class);

	@Autowired
	private TransactionStore transactionStore;
	@Autowired
	private TransactionInputValidator transactionInputValidator;

	@RequestMapping("/statistics")
	public TransactionStatistics getStatistics() {
		return transactionStore.getStatistics();
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/transactions", method = RequestMethod.POST)
	public ResponseEntity newTrasaction(@RequestBody TransactionReport transactionReport) {
		logger.debug("New transaction : {}", transactionReport);
		transactionInputValidator.validate(transactionReport);
		boolean isInTime = transactionStore.update(transactionReport);
		if (isInTime) {
			return new ResponseEntity(HttpStatus.CREATED);
		} else {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

	}

	@SuppressWarnings("rawtypes")
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity exception(HttpServletRequest req, Exception ex) {
		logger.warn(ex.getMessage());
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

}
