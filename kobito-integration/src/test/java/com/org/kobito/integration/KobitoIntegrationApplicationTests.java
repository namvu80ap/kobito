package com.org.kobito.integration;

import com.org.kobito.integration.services.TradeTweetService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KobitoIntegrationApplicationTests {

	@Autowired
	TradeTweetService tradeTweetService;

	@Test
	public void contextLoads() {
		//Flux<TradeTweet> list = tradeTweetService.searchByCreatedAt();
		//list.subscribe(value -> System.out.println(value.getId()) );
	}

}
