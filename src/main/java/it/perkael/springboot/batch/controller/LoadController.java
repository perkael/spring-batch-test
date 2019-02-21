package it.perkael.springboot.batch.controller;

import it.perkael.springboot.batch.business.BatchService;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoadController {

    @Autowired
    private BatchService batchService;


    @RequestMapping(value = "startJob", method = RequestMethod.GET)
    public String load() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        String ret = "JOB ID: " + batchService.startJob();
        return ret;

    }

    @RequestMapping(value = "jobStatus/{idJob}", method = RequestMethod.GET)
    public String jobStatus(@RequestParam("idJob") Long idJob) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        return batchService.jobStatus(idJob);
    }
}
