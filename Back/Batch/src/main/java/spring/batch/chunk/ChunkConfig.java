package spring.batch.chunk;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class ChunkConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;

    // step 실행
    @Bean
    public Job makeJob() {
        return new JobBuilder("makeJob", jobRepository)
                .start(makeStep())
                .build();
    }

    // itemReader, Processor, Writer 동작
    @Bean
    public Step makeStep() {
        return new StepBuilder("chunkStep", jobRepository)
                .<String, String>chunk(2) // < , > : 읽고 쓸 데이터
                .reader(dataReader())
                .processor(dataProcessor())
                .writer(dataWriter())
                .build();
    }

    // 1. itemReader
    @Bean
    public ItemReader<String> dataReader() {
        // <> : 읽을 데이터 타입, Member .. 등
        return new ListItemReader<>(
                Arrays.asList("hello", "world", "this", "is", "lower", "case", "data")
        );
    }

    // 2. itemProcessor
    @Bean
    public ItemProcessor<String, String> dataProcessor() {
        // < , > : 받을 데이터 타입, 보낼 데이터 타입, Member, Member 등
        return item -> item.toUpperCase();
    }

    @Bean
    public ItemWriter<String > dataWriter() {
        // <> : 처리할 데이터 타입, Member .. 등

        return items -> {
            System.out.println("Start to write chunk");

            for (String s : items) {
                System.out.println("Result : " + s);
            }
            System.out.println("End to write chunk");
        };
    }
}
