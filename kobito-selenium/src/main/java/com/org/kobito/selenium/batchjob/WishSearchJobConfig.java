package com.org.kobito.selenium.batchjob;

import com.org.kobito.selenium.batchjob.obj.Person;
import com.org.kobito.selenium.batchjob.obj.WishSearch;
import com.org.kobito.selenium.batchjob.processor.PersonItemProcessor;
import com.org.kobito.selenium.batchjob.processor.WishSearchProcessor;
import org.openqa.selenium.WebElement;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.util.List;

@Configuration
public class WishSearchJobConfig extends BatchConfiguration {

    @Bean(name="wishSearchJob")
    public Job wishSearchJob() {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<WishSearch, List<WebElement>> chunk(1)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public FlatFileItemReader<WishSearch> reader() {
        FlatFileItemReader<WishSearch> reader = new FlatFileItemReader<WishSearch>();
        reader.setResource(new ClassPathResource("data.csv"));
        reader.setLineMapper(new DefaultLineMapper<WishSearch>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "marketType", "param" });
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<WishSearch>() {{
                setTargetType(WishSearch.class);
            }});
        }});
        return reader;
    }

    @Bean
    public WishSearchProcessor processor() {
        return new WishSearchProcessor();
    }

    @Bean
    public ItemWriter<List<WebElement>> writer() {
        return new ItemWriter<List<WebElement>>() {
            @Override
            public void write(List<? extends List<WebElement>> items) throws Exception {
                for (List<WebElement> list: items
                     ) {
                    for ( WebElement e : list
                         ) {
                        System.out.println( e.getAttribute("innerHTML") );
                    }
                }
            }
        };
    }

}
