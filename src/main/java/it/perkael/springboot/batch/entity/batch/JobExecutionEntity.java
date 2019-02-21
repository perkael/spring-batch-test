package it.perkael.springboot.batch.entity.batch;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "BATCH_STEP_EXECUTION")
@Data
@NoArgsConstructor
public class JobExecutionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "STEP_EXECUTION_ID")
    private long stepExecutionId;

    @Column(name = "VERSION")
    private int version;

    @Column(name = "STEP_NAME")
    private String stepName;

    @Column(name = "JOB_EXECUTION_ID")
    private long jobExecutionId;

    @Temporal(TemporalType.DATE)
    @Column(name = "START_TIME")
    private Date startTime;

    @Temporal(TemporalType.DATE)
    @Column(name = "END_TIME")
    private Date endTime;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "EXIT_MESSAGE")
    private String exitMessage;

}
