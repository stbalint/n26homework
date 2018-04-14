/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.statistics.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.statistics.pojo.TransactionReport;
import com.example.statistics.pojo.TransactionStatistics;
import com.example.statistics.store.TransactionStore;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StatisticsControllerTests {

	private static final String APP_JSON_UTF8 = "application/json;charset=UTF-8";
	private static final String CONTENT = "{\"amount\":10.0,\"timestamp\":1000}";

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private TransactionStore transactionStore;
	@MockBean
	private TransactionInputValidator transactionInputValidator;

	@Test
	public void statisticsInitialTest() throws Exception {
		Mockito.when(transactionStore.getStatistics()).thenReturn(new TransactionStatistics.Builder().build());

		this.mockMvc.perform(get("/statistics")).andExpect(status().isOk())
				.andExpect(content().contentType(APP_JSON_UTF8))
				.andExpect(content().json("{\"sum\":0.0,\"avg\":0.0,\"max\":0.0,\"min\":0.0,\"count\":0}"));
	}

	@Test
	public void tooOldTransactionTest() throws Exception {
		TransactionReport transactionReport = new TransactionReport(10.0, 1000L);
		Mockito.when(transactionStore.update(transactionReport)).thenReturn(false);
		this.mockMvc.perform(post("/transactions").contentType(APP_JSON_UTF8).content(CONTENT))
				.andExpect(status().is(204));
	}

	@Test
	public void updateTransactionTest() throws Exception {
		TransactionReport transactionReport = new TransactionReport(10.0, 1000L);
		Mockito.when(transactionStore.update(Mockito.eq(transactionReport))).thenReturn(true);
		this.mockMvc.perform(post("/transactions").contentType(APP_JSON_UTF8).content(CONTENT))
				.andExpect(status().is(201));
	}

	@Test
	public void futureTansactionTest() throws Exception {
		TransactionReport transactionReport = new TransactionReport(10.0, 1000L);
		Mockito.doThrow(new IllegalArgumentException()).when(transactionInputValidator)
				.validate(Mockito.eq(transactionReport));
		this.mockMvc.perform(post("/transactions").contentType(APP_JSON_UTF8).content(CONTENT))
				.andExpect(status().is(400));
	}

}
