package net.bzk.flow.run.dao;

import net.bzk.flow.model.ArchiveRun;
import net.bzk.flow.run.flow.FlowRuner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ArchiveRunDao extends CrudRepository<ArchiveRun, String> {
	
//	List<ArchiveRun> findByFlowUid(String uid);

	Page<ArchiveRun> findByFlowUidOrderByCreateAtDesc(String uid, Pageable pageable);

	Long countByFlowUidAndState(String flowUid, FlowRuner.State state);

	Optional<ArchiveRun> findTopByFlowUidOrderByCreateAtDesc(String flowUid);

	@Transactional
	@Modifying
	List<ArchiveRun> deleteByCreateAtBefore(Date date);
}
