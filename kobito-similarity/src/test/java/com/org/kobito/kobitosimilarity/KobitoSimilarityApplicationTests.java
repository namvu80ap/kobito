package com.org.kobito.kobitosimilarity;

import com.datastax.driver.core.utils.UUIDs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import com.org.kobito.kobitosimilarity.services.SimilarWordServices;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KobitoSimilarityApplicationTests {

	@MockBean
	private SimilarWordServices service;

	@Test
	public void contextLoads() {

	}

	@Test
	public void insertSimilarWord(){
		service.saveSimilarWord(UUIDs.timeBased(), "TESTE","TESTY",100);
		System.out.println("----------------------------------------------------------------------------------------");
		System.out.println("----------------------------------------------------------------------------------------");
		System.out.println("----------------------------------------------------------------------------------------");
		System.out.println(service.count());
		System.out.println("----------------------------------------------------------------------------------------");
		System.out.println("----------------------------------------------------------------------------------------");
		System.out.println("----------------------------------------------------------------------------------------");
		System.out.println("----------------------------------------------------------------------------------------");
	}

}
