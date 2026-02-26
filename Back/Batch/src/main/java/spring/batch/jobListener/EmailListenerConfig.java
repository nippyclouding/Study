package spring.batch.jobListener;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class EmailListenerConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EmailJobListener listener;

    @Bean
    public Job emailJob() {
        return new JobBuilder("emailJob", jobRepository)
                .start(emailStep())
                .listener(listener)
                .build();
    }

    @Bean
    public Step emailStep() {
        return new StepBuilder("emailStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("finish error");

                    Thread.sleep(3000);
                    throw new RuntimeException("error for test");
                }, txManager)
                .build();
    }
}
