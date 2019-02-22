package it.perkael.springboot.batch.config;

import it.perkael.springboot.batch.batch.CustomFlatFileItemReader;
import it.perkael.springboot.batch.batch.DBWriter;
import it.perkael.springboot.batch.batch.Processor;
import it.perkael.springboot.batch.entity.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;


@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {

        Step step = stepBuilderFactory.get("ETL-STEP")
                .<User, User>chunk(100)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();

        Job job = jobBuilderFactory.get("ETL-JOB")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();

        return job;
    }

    @Retryable(
            value = {Exception.class},
            maxAttempts = 2,
            backoff = @Backoff(delay = 5000))
    @Bean
    public ItemReader<? extends User> itemReader() {
        CustomFlatFileItemReader<User> customFlatFileItemReader = new CustomFlatFileItemReader<>(lineMapper());
        customFlatFileItemReader.setName("CSV-Reader");
        customFlatFileItemReader.setLinesToSkip(1);
        return customFlatFileItemReader;
    }

    @Bean
    public ItemWriter<? super User> itemWriter() {
        return new DBWriter();
    }

    @Bean
    public ItemProcessor<? super User, ? extends User> itemProcessor() {
        return new Processor();
    }

    @Bean
    public LineMapper<User> lineMapper() {
        DefaultLineMapper<User> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[]{"id", "name", "dept", "salary"});

        BeanWrapperFieldSetMapper<User> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(User.class);

        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
    }

}
