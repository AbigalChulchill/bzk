package net.bzk.flow.run.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import net.bzk.flow.model.ArchiveRun;

public interface ArchiveRunDao extends CrudRepository<ArchiveRun, String> {
	
	List<ArchiveRun> findByFlowUid(String uid);

}
