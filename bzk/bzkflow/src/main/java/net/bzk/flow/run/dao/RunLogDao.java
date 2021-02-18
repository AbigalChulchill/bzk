package net.bzk.flow.run.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.bzk.flow.model.RunLog;
@Repository
public interface RunLogDao extends CrudRepository<RunLog, Long> {

}
