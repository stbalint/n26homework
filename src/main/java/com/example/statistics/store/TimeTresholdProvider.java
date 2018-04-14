package com.example.statistics.store;

import org.springframework.stereotype.Component;

@Component
public class TimeTresholdProvider {

	public static final long ENABLED_TIME_INTERVAL = 60 * 1000;

	public long getTreshold() {
		return System.currentTimeMillis() - ENABLED_TIME_INTERVAL;
	}

}
