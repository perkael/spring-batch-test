package it.perkael.springboot.batch.business.impl;

import it.perkael.springboot.batch.business.BatchService;
import it.perkael.springboot.batch.entity.batch.JobExecutionEntity;
import it.perkael.springboot.batch.repository.JobExecutionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BatchServiceImpl implements BatchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchServiceImpl.class);

    @Autowired
    private JobExecutionRepository jobExcecutionRepository;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    private Job job;

    @Async
    @Override
    public Long startJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        long currentTimeMillis = System.currentTimeMillis();
        Map<String, JobParameter> maps = new HashMap<>();
        maps.put("time", new JobParameter(currentTimeMillis));
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

    @Transactional(readOnly = true)
    @Override
    public String getLastJobStatus() {
        List<JobExecutionEntity> latest = jobExcecutionRepository.findLatest();
        String ret = "";
        if (latest != null && !latest.isEmpty()) {
            JobExecutionEntity job = latest.get(0);
            ret = "ID JOB: " + job.getJobExecutionId() + " - " + job.getStatus();
            if (job.getStatus().equalsIgnoreCase("FAILED")) {
                ret = ret + " - ERROR: " + job.getExitMessage();
            }
        }
        return ret;
    }

}
