package net.bzk.flow.run.dao;

import net.bzk.flow.model.RunLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface RunLogDao extends MongoRepository<RunLog, String> {

    Page<RunLog> findByRunFlowUidOrderByCreateAtAsc(String uid, Pageable pageable);

    Page<RunLog> findByActionUidOrderByCreateAtAsc(String uid,Pageable pageable);

    Page<RunLog> findByCreateAtBefore(Date date,Pageable pageable);

//    findTopByOrderByIdDesc

    Optional<RunLog> findTopByRefRunFlowUidOrderByUidDesc(String uid);

    @Transactional
    @Modifying
    List<RunLog> deleteByCreateAtBefore(Date date);


}
