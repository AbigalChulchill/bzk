package net.bzk.flow.api.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.bzk.flow.model.Flow;
import net.bzk.flow.run.flow.FlowRuner.RunInfo;
import net.bzk.flow.run.flow.FlowRuner.State;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class FlowPoolInfo {

	private Flow flow;
	private List<RunInfo> runInfos;


}
