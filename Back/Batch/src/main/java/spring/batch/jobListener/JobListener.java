package spring.batch.jobListener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class JobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("Start Job, id : " + jobExecution.getJobInstanceId());

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("End Job, id : " + jobExecution.getJobInstanceId());
        if (jobExecution.getStatus() == BatchStatus.FAILED) {
            System.out.println("fail to finish job");
        } else {
            System.out.println("success to finish job");
        }
    }
}
