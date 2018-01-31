package com.org.kobito.selenium;

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
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@SpringBootApplication
@Configuration
@EnableBatchProcessing
public class KobitoSeleniumApplication {

	public static void main(String[] args) {
		SpringApplication.run(KobitoSeleniumApplication.class, args);
	}

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	public DataSource dataSource;

	// tag::readerwriterprocessor[]
	@Bean
	public FlatFileItemReader<Person> reader() {
		FlatFileItemReader<Person> reader = new FlatFileItemReader<Person>();
		reader.setResource(new ClassPathResource("data.csv"));
		reader.setLineMapper(new DefaultLineMapper<Person>() {{
			setLineTokenizer(new DelimitedLineTokenizer() {{
				setNames(new String[] { "firstName", "lastName" });
			}});
			setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
				setTargetType(Person.class);
			}});
		}});
		return reader;
	}

	@Bean
	public PersonItemProcessor processor() {
		return new PersonItemProcessor();
	}

	@Bean
	public ItemWriter<Person> writer() {
		FlatFileItemWriter<Person> writer = new FlatFileItemWriter<Person>();
		writer.setResource(new ClassPathResource("person.csv"));
		DelimitedLineAggregator<Person> delLineAgg = new DelimitedLineAggregator<Person>();
		delLineAgg.setDelimiter(",");
		BeanWrapperFieldExtractor<Person> fieldExtractor = new BeanWrapperFieldExtractor<Person>();
		fieldExtractor.setNames(new String[] {"firstName", "lastName"});
		delLineAgg.setFieldExtractor(fieldExtractor);
		writer.setLineAggregator(delLineAgg);
		return writer;
	}

//	@Bean
//	public JdbcBatchItemWriter<Person> writer() {
//		JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<Person>();
//		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>());
//		writer.setSql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)");
//		writer.setDataSource(dataSource);
//		return writer;
//	}
	// end::readerwriterprocessor[]

	// tag::jobstep[]
	@Bean
	public Job importUserJob(JobCompletionNotificationListener listener) {
		return jobBuilderFactory.get("importUserJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.flow(step1())
				.end()
				.build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.<Person, Person> chunk(10)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.build();
	}

}
