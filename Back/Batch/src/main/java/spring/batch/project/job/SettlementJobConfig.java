package spring.batch.project.job;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.database.JpaPagingItemReader;
import org.springframework.batch.infrastructure.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.infrastructure.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import spring.batch.project.domain.Orders;
import spring.batch.project.domain.Settlement;

import java.time.LocalDate;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class SettlementJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory emf;

    @Bean
    public Job settlementJob() {
        return new JobBuilder("settlementJob", jobRepository)
                .start(settlementStep())
                .build();
    }

    @Bean
    public Step settlementStep() {
        return new StepBuilder("settlementStep", jobRepository)
                .<Orders, Settlement>chunk(1000)
                .transactionManager(txManager)
                .reader(ordersReader(null))
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean // 입력 데이터 : Orders
    @StepScope
    public JpaPagingItemReader<Orders> ordersReader(@Value("#{jobParameters['targetDate']}") String targetDate) {
        System.out.println("[Reader] 정산 집계 대상 날짜 : " + targetDate);
        // JpaPagingItemReader : Jpa 페이징 처리를 포함한 ItemReader
        return new JpaPagingItemReaderBuilder<Orders>()
                .name("ordersReader")
                .entityManagerFactory(emf)
                .pageSize(1000) // 1000개씩 데이터를 가지고 온다, 기본값 10개 => 여러 번 메서드가 호출되어 배치가 느려질 수 있다.
                .queryString("SELECT o FROM Orders o WHERE o.orderDate = :targetDate ORDER BY o.id")
                .parameterValues(Collections.singletonMap("targetDate", LocalDate.parse(targetDate))) // targetDate에 어떤 값을 넣을지 결정
                .build();
    }

    @Bean // 입력 데이터 : Orders, 출력 데이터 : Settlement
    public ItemProcessor<Orders, Settlement> processor() {
        return item -> {
            int fee = (int) (item.getAmount() * 0.03);
            int settlementAmount = item.getAmount() - fee;

            return new Settlement(item.getId(), item.getStoreName(), settlementAmount, LocalDate.now());
        };
    }

    @Bean // 입력 데이터 : Settlement
    public JpaItemWriter<Settlement> writer() {
        return new JpaItemWriterBuilder<Settlement>()
                .entityManagerFactory(emf)
                .build();
    }


}
