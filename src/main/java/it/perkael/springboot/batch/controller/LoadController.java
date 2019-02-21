package it.perkael.springboot.batch.controller;

import it.perkael.springboot.batch.business.BatchService;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "batch")
public class LoadController {

    @Autowired
    private BatchService batchService;


    @RequestMapping(value = "startJob", method = RequestMethod.GET)
    public String load() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        batchService.startJob();
        String ret = "JOB STARTED";
        return ret;

    }

    @RequestMapping(value = "jobStatus/{idJob}", method = RequestMethod.GET)
    public String jobStatus(@RequestHeader("idJob") Long idJob) {
        return batchService.jobStatus(idJob);
    }

    @RequestMapping(value = "getLastJobStatus", method = RequestMethod.GET)
    public String getLastJobStatus() {
        return batchService.getLastJobStatus();
    }
}
