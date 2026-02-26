package spring.batch.jobListener;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailJobListener {
    private final EmailProvider emailProvider;

    @BeforeJob
    public void before(JobExecution execution) {
        System.out.println("start batch job, id : " + execution.getJobInstanceId());
    }

    @AfterJob
    public void after(JobExecution execution) {
        if (execution.getStatus() == BatchStatus.FAILED) {
            emailProvider.send("admin@google.com", "fail to finish job", "id : " + execution.getJobInstanceId());
        } else {
            System.out.println("success batch job");
        }
    }
}
