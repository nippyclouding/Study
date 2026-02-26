package spring.batch.jobListener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.stereotype.Component;

@Component
public class AnnotationListener {

    @BeforeJob
    public void start(JobExecution execution) {
        System.out.println("start Job : " + execution.getJobInstanceId());
    }

    @AfterJob
    public void finish(JobExecution execution) {
        System.out.println("End Job, id : " + execution.getJobInstanceId());
        if (execution.getStatus() == BatchStatus.FAILED) {
            System.out.println("fail to finish job");
        } else {
            System.out.println("success to finish job");
        }
    }
}
