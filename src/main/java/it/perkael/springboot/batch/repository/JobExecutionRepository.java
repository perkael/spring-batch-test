package it.perkael.springboot.batch.repository;

import it.perkael.springboot.batch.entity.batch.JobExecutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobExecutionRepository extends JpaRepository<JobExecutionEntity, Integer> {

    @Query(value = "select b from JobExecutionEntity b order by b.jobExecutionId desc ")
    public List<JobExecutionEntity> findLatest();
}
