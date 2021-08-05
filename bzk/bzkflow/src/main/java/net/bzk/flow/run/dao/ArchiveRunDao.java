package net.bzk.flow.run.dao;

import java.util.List;
import java.util.Optional;

import net.bzk.flow.run.flow.FlowRuner;
import org.springframework.data.repository.CrudRepository;

import net.bzk.flow.model.ArchiveRun;

public interface ArchiveRunDao extends CrudRepository<ArchiveRun, String> {
	
	List<ArchiveRun> findByFlowUid(String uid);

	Long countByFlowUidAndState(String flowUid, FlowRuner.State state);

	Optional<ArchiveRun> findTopByFlowUidOrderByCreateAtDesc(String flowUid);
}
