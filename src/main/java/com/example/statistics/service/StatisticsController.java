package com.example.statistics.service;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.statistics.Greeting;

@RestController
public class StatisticsController {
	
	private static final Logger logger = LoggerFactory.getLogger(StatisticsController.class);

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
    
    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/transactions", method = RequestMethod.POST)
    public ResponseEntity newTrasaction(@RequestBody TransactionReport transactionReport) {
    	logger.debug("New transaction : {}", transactionReport);
        return new ResponseEntity(HttpStatus.CREATED);
    }
    
    
}
