package net.bzk.flow.api.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.bzk.flow.model.Flow;
import net.bzk.flow.run.flow.FlowRuner.State;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class RegisteredFlow {

	private Flow flow;
	private List<RunInfo> runInfos;

	@Data
	@AllArgsConstructor(access = AccessLevel.PUBLIC)
	public static class RunInfo {
		private String runUid;
		private State state;
		private String endTag;
		private String endLinkUid;
	}

}
