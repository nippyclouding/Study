package spring.batch.batchBasic;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class CafeJobConfig {
    private final JobRepository jobRepository; // 배치의 메타 데이터를 DB에 저장하는 repository
    private final PlatformTransactionManager txManager; // 배치 설계 시 트랜잭션이 필요할 때 사용

    /*
    .tasklet (step, transactionManager)
    파라미터 - step : tasklet, chunk 두 방식으로 step 생성 가능
    파라미터 - transactionManager : 필드로 선언한 txManager

    RepeatStatus.FINISHED : step을 이 곳에서 종료
    RepeatStatus.CONTINUABLE : step을 이어서 실행
     */

    @Bean
    public Job cafeJob() {
        return new JobBuilder("cafeJob", jobRepository)
                .start(step1())
                .next(step2())
                .next(step3())
                .build();
    }

    // 1. open cafe
    @Bean
    public Step step1() {
        return new StepBuilder("step1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("[1] open cafe");
                    return RepeatStatus.FINISHED;
                }, txManager)
                .build();
    }
    // 2. make coffee
    int goal = 5; // 커피 5잔 완성을 목표
    int currentCount = 0; // 현재 커피 완성 개수

    @Bean
    public Step step2() {
        return new StepBuilder("step2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("[2] make coffee");
                    currentCount++;
                    System.out.println("[2] current coffee count : " + currentCount);

                    if (currentCount < goal) { // 5잔을 완성하지 않았을 경우
                        return RepeatStatus.CONTINUABLE;
                    }

                    return RepeatStatus.FINISHED;
                }, txManager)
                .build();
    }

    // 3. get off work
    @Bean
    public Step step3() {
        return new StepBuilder("step3", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("[3] get off work");
                    return RepeatStatus.FINISHED;
                }, txManager)
                .build();
    }
}
