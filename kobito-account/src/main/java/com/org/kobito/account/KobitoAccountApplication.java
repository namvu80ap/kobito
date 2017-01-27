package com.org.kobito.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories;

@SpringBootApplication()
public class KobitoAccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(KobitoAccountApplication.class, args);
	}
}