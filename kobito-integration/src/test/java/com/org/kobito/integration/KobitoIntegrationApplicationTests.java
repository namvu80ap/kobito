package com.org.kobito.integration;

import com.org.kobito.integration.model.TradeTweet;
import com.org.kobito.integration.services.TradeTwetService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KobitoIntegrationApplicationTests {
	@Autowired
	TradeTwetService tradeTwetService;

	@Test
	public void contextLoads() {
		List<TradeTweet> list = tradeTwetService.searchByCreatedAt();
		list.stream().forEach( item -> System.out.println( item.getId() ) );
	}

}
