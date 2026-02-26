package spring.batch.project.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.InvalidJobParametersException;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.launch.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class JobScheduler {

    private final JobOperator operator;
    private final Job settlementJob;

    @Scheduled(cron = "0 17 21 * * *")
    public void runJob() throws JobInstanceAlreadyCompleteException, InvalidJobParametersException, JobExecutionAlreadyRunningException, JobRestartException {
        JobParameters parameters = new JobParametersBuilder()
                .addString("targetDate", LocalDate.now().minusDays(7).toString())
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        System.out.println("Run scheduler, start batch");

        operator.start(settlementJob, parameters);
    }
}
