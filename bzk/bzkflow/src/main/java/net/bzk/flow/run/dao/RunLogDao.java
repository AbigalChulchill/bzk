package net.bzk.flow.run.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.bzk.flow.model.RunLog;

import javax.transaction.Transactional;

@Repository
public interface RunLogDao extends MongoRepository<RunLog, String> {

    Page<RunLog> findByRunFlowUidOrderByCreateAtAsc(String uid, Pageable pageable);

    Page<RunLog> findByActionUidOrderByCreateAtAsc(String uid,Pageable pageable);

    Page<RunLog> findByCreateAtBefore(Date date,Pageable pageable);

    @Transactional
    @Modifying
    List<RunLog> deleteByCreateAtBefore(Date date);


}
