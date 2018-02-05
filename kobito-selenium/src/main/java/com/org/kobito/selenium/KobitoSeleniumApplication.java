package com.org.kobito.selenium;

import com.org.kobito.selenium.dto.Wish;
import com.org.kobito.selenium.repositories.WishRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;

@SpringBootApplication
public class KobitoSeleniumApplication {

	public static void main(String[] args) {
		SpringApplication.run(KobitoSeleniumApplication.class, args);
	}


	@Autowired
	WishRepository repository;

	@EventListener
	public void seed(ContextRefreshedEvent event) {
		repository.save(new Wish( 1, "sort_order=price_asc&keyword=camera&category_root=1&category_child=&brand_name=11&brand_id=&size_group=3&price_min=10000&price_max=30000&item_condition_id=1&shipping_payer_id=1&status_trading_sold_out=1" ));
		repository.save(new Wish( 1, "sort_order=price_asc&keyword=bag&category_root=1&category_child=&brand_name=11&brand_id=&size_group=3&price_min=10000&price_max=30000&item_condition_id=1&shipping_payer_id=1&status_trading_sold_out=1" ));
		System.out.println("---------------------------------------------");
		System.out.println(repository.count());
	}
}
