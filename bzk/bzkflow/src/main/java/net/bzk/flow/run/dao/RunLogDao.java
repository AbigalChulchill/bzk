package net.bzk.flow.run.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.bzk.flow.model.RunLog;

import javax.transaction.Transactional;

@Repository
public interface RunLogDao extends CrudRepository<RunLog, Long> {

    List<RunLog> findByRunFlowUidOrderByCreateAtAsc(String uid);

    List<RunLog> findByActionUidOrderByCreateAtAsc(String uid);

    @Transactional
    @Modifying
    List<RunLog> deleteByCreateAtBefore(Date date);

}
