package net.bzk.flow.run.dao;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import net.bzk.flow.model.ArchiveRun;
import net.bzk.flow.run.flow.FlowRuner.RunInfo;
import net.bzk.flow.run.flow.FlowRuner.State;

@SpringBootTest
public class TestArchiveRunDao {
	@Inject
	private ArchiveRunDao dao;
	
	@Test
	public void save() {
		ArchiveRun ar =new ArchiveRun();
		String fuid = "avc11";
		ar.setFlowUid(fuid);
		
		RunInfo info = new RunInfo();
		info.setUid("qqq");
		info.setState(State.Fail);
		ar.setInfo(info );
		dao.save(ar);
		
		var fo= dao.findById(fuid);
		
		assertEquals(fo.get().getInfo().getUid(), "qqq");
	}

}
