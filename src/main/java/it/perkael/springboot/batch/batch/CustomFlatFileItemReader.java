package it.perkael.springboot.batch.batch;

import it.perkael.springboot.batch.utils.ByteUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class CustomFlatFileItemReader<T> extends FlatFileItemReader<T> implements StepExecutionListener {

    private byte[] stream;

    @Value("${httpUrl.path}")
    private String uri;

    @Override
    public void beforeStep(StepExecution stepExecution) {

        stream = ByteUtils.getFileAndUnzip(uri);

        this.setResource(null);
    }

    @Override
    public void setResource(Resource resource) {

        // Convert byte array to input stream
        InputStream is = new ByteArrayInputStream(stream);

        // Create springbatch input stream resource
        InputStreamResource res = new InputStreamResource(is);

        // Set resource
        super.setResource(res);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

}
