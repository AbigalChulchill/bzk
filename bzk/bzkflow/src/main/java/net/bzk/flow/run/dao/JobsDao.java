package net.bzk.flow.run.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.bzk.flow.model.Job;
@Repository
public interface JobsDao extends CrudRepository<Job, String> {

}
