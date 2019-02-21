package it.perkael.springboot.batch.business.impl;

import it.perkael.springboot.batch.business.BatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BatchServiceImpl implements BatchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchServiceImpl.class);

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    JobExplorer jobExplorer;

    @Autowired
    Job job;

    @Retryable(
            value = {Exception.class},
            maxAttempts = 2,
            backoff = @Backoff(delay = 5000))
    @Override
    public Long startJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        LOGGER.info("##### RETRY ########");
        Map<String, JobParameter> maps = new HashMap<>();
        maps.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters parameters = new JobParameters(maps);
        JobExecution jobExecution = jobLauncher.run(job, parameters);
        System.out.println("JobExecution: " + jobExecution.getStatus());
        return jobExecution.getJobId();
    }

    @Override
    public String jobStatus(Long idJob) {
        JobExecution jobExecution = jobExplorer.getJobExecution(idJob);
        return jobExecution.getStatus().toString();
    }
}
