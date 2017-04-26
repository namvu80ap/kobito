package com.org.kobito.integration;

import com.org.kobito.integration.model.TradeTweet;
import com.org.kobito.integration.services.TradeTwetService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KobitoIntegrationApplicationTests {
	@Autowired
	TradeTwetService tradeTwetService;

	@Test
	public void contextLoads() {
		Flux<TradeTweet> list = tradeTwetService.searchByCreatedAt();
		list.subscribe(value -> System.out.println(value.getId()) );
	}

}
