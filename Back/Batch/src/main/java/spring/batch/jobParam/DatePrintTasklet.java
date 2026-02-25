package spring.batch.jobParam;

import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope // 먼저 외부 Job Parameter 값을 입력받은 뒤 스프링 컨테이너에 빈으로 등록
public class DatePrintTasklet implements Tasklet {

    private final String requestDate;

    public DatePrintTasklet(@Value("#{jobParameters['requestDate']}") String requestDate) {
        this.requestDate = requestDate;
    }

    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("job parameter data : " + requestDate);

        return RepeatStatus.FINISHED;
    }
}
