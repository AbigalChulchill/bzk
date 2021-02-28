package net.bzk.flow.api;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import net.bzk.flow.api.dto.ActionDebugData;
import net.bzk.flow.api.dto.FlowPoolInfo;
import net.bzk.flow.model.Flow;
import net.bzk.flow.model.demo.ModelBuilder;
import net.bzk.flow.run.flow.FlowRuner.RunInfo;
import net.bzk.flow.run.service.JobsService;
import net.bzk.flow.run.service.RunFlowService;

@CrossOrigin(maxAge = 3600, methods = { RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST, RequestMethod.PATCH,
		RequestMethod.OPTIONS, RequestMethod.HEAD }, allowedHeaders = "*", origins = "*")
@Validated
@Controller
@RequestMapping(value = "/flow/")
public class FlowController {

	@Inject
	private ModelBuilder modelBuilder;
	@Inject
	private RunFlowService runFlowService;
	@Inject
	private JobsService jobsService;

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "demo", method = RequestMethod.GET)
	public Flow getDemoModel() {
		return modelBuilder.init().getModel();
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "register", method = RequestMethod.POST)
	public void register(@RequestBody List< Flow> fs) {
		fs.forEach(runFlowService::register);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "{uid}/run", method = RequestMethod.POST, params = "type=manual")
	public void runManual(@PathVariable String uid ) {
		runFlowService.runManual(uid);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "{uid}/register", method = RequestMethod.POST)
	public void register(@PathVariable String uid) {
		var sfs = jobsService.listDepends(uid);
		List< Flow> fs = sfs.stream().map(sf-> sf.getFlow()).collect(Collectors.toList());
		register(fs);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "{uid}/test", method = RequestMethod.POST)
	public RunInfo testFlow(@PathVariable String uid ) {
		var sfs = jobsService.listDepends(uid);
		List< Flow> fs = sfs.stream().map(sf-> sf.getFlow()).collect(Collectors.toList());
		return runFlowService.test(uid, fs);
	}

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<FlowPoolInfo> listFlowPoolInfo() {
		return runFlowService.listFlowPoolInfo();
	}
	
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "{uid}", method = RequestMethod.GET)
	public FlowPoolInfo getPoolInfo(@PathVariable String uid ) {
		return runFlowService.getFlowPoolInfo(uid);
	}

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "pool/{fuid}/remove", method = RequestMethod.POST, params = "type=force")
	public void forceRemovePool(@PathVariable String fuid) {
		runFlowService.forceRemove(fuid);
	}
	

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "debug/action", method = RequestMethod.POST)
	public void debugAction(@RequestBody ActionDebugData data, @RequestParam int delDelay) throws InterruptedException {
		runFlowService.debugAction(data, delDelay);
	}

}
