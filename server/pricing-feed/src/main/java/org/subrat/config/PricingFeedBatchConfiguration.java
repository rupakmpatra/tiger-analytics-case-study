package org.subrat.config;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.*;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.subrat.model.PricingFeed;
import org.subrat.repository.PricingFeedRepository;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class PricingFeedBatchConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final PricingFeedRepository pricingFeedRepository;
    private final ResourceLoader resourceLoader;

    @Bean
    @StepScope
    public FlatFileItemReader<PricingFeed> reader(@Value("#{jobParameters['input.file.path']}") String inputFilePath,
                                                  @Value("#{jobParameters['input.file.loader']}") String inputResourceLoader) {
        Resource inputResource = resourceLoader.getResource("file:" + inputFilePath);
        FlatFileItemReader<PricingFeed> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(inputResource);
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    private LineMapper<PricingFeed> lineMapper() {
        DefaultLineMapper<PricingFeed> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("storeId", "sku", "productName", "price", "date");

        CustomPricingFeedFieldSetMapper fieldSetMapper = new CustomPricingFeedFieldSetMapper();
        fieldSetMapper.setTargetType(PricingFeed.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public ItemProcessor<PricingFeed, PricingFeed> processor() {
        return pricingFeed -> pricingFeed;
    }
    @Bean
    public ItemWriter<PricingFeed> writer() {
        return list -> {
            pricingFeedRepository.saveAll(list);
        };
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }

    @Bean
    public Step pricingFeedStep() throws Exception {
        return stepBuilderFactory.get("pricingFeedStep")
                .<PricingFeed, PricingFeed>chunk(100)
                .reader(reader(null, null))
                .processor(processor())
                .writer(writer())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Job pricingFeedJob() throws Exception {
        return jobBuilderFactory.get("pricingFeedJob")
                .incrementer(new RunIdIncrementer())
                .flow(pricingFeedStep())
                .end()
                .build();
    }

}
