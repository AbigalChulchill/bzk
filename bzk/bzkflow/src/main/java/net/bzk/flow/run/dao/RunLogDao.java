package net.bzk.flow.run.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.bzk.flow.model.RunLog;
@Repository
public interface RunLogDao extends CrudRepository<RunLog, Long> {
	
	List<RunLog> findByRunFlowUidOrderByCreateAtAsc(String uid);
	
	List<RunLog> findByActionUidOrderByCreateAtAsc(String uid);

	List<RunLog> deleteByCreateAtBefore(Date date);
	
}
