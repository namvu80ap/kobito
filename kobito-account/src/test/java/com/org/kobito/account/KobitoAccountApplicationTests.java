package com.org.kobito.account;

import com.org.kobito.account.model.Account;
import com.org.kobito.account.repository.AccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.cassandra.core.ReactiveCassandraTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KobitoAccountApplicationTests {

	@Autowired
	AccountRepository accountRepository;

	@Test
	public void some() {

		accountRepository.save(new Account("Skyler", "White", "45"));

	}

}
