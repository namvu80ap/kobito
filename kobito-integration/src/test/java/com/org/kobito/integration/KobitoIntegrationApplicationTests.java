package com.org.kobito.integration;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest({"spring.data.cassandra.port=9042","spring.data.cassandra.contact-points=35.201.24.119",
		"spring.data.cassandra.username=cassandra",
		"spring.data.cassandra.password=RLpPLb23xjMx",
		"spring.data.cassandra.keyspace-name=kobito_dev"})
public abstract class KobitoIntegrationApplicationTests {

}