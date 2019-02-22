package it.perkael.springboot.batch.batch;

import it.perkael.springboot.batch.entity.User;
import it.perkael.springboot.batch.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Component
public class CustomFlatFileItemReader<T> extends FlatFileItemReader<T> implements StepExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomFlatFileItemReader.class);

    private byte[] stream;

    @Value("${httpUrl.path}")
    private String uri;

    public CustomFlatFileItemReader() {
        super();
        this.setLineMapper((LineMapper<T>) this.lineMapper());
    }

    public CustomFlatFileItemReader(LineMapper<T> tLineMapper) {
        super();
        this.setLineMapper(tLineMapper);
    }

    @Retryable(
            value = {Exception.class},
            maxAttempts = 2,
            backoff = @Backoff(delay = 5000))
    @Override
    public void beforeStep(StepExecution stepExecution) {
        LOGGER.info("##### RETRYBLE METHOD ########");
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

    public LineMapper<User> lineMapper() {
        DefaultLineMapper<User> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[]{"id", "name", "dept", "salary"});

        BeanWrapperFieldSetMapper<User> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(User.class);

        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

}
