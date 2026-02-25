package spring.batch.file;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class FileCleanUpConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;

    @Bean
    public Tasklet fileCleanUpTasklet() {
        return new FileCleanUpTasklet("./test-logs", 30);
    }

    @Bean
    public Job fileCleanUpJob() {
        return new JobBuilder("fileCleanUpJob", jobRepository)
                .start(fileCleanUpStep())
                .build();
    }

    @Bean
    public Step fileCleanUpStep() {
        return new StepBuilder("fileCleanUpStep", jobRepository)
                .tasklet(fileCleanUpTasklet(), txManager)
                .build();
    }
}
