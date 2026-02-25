package spring.batch.cake;

import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;


public class CakeJobTasklet implements Tasklet {

    int cakeCount = 0;
    int goal = 5;

    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        cakeCount++;
        System.out.println("make cake : " + cakeCount);

        if (cakeCount < goal) {
            return RepeatStatus.CONTINUABLE;
        }

        System.out.println("finish making cake");
        return RepeatStatus.FINISHED;
    }
}
