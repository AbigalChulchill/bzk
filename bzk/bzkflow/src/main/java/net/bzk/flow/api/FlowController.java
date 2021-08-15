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

import net.bzk.flow.api.dto.FlowPoolInfo;
import net.bzk.flow.model.Flow;
import net.bzk.flow.model.demo.ModelBuilder;
import net.bzk.flow.run.dao.JobsDao;
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
	private JobsDao jobsDao;

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "demo", method = RequestMethod.GET)
	public Flow getDemoModel() {
		return modelBuilder.init().getModel();
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "register", method = RequestMethod.POST)
	public void register(@RequestBody List<Flow> fs) {
		fs.forEach(runFlowService::register);
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "{uid}/register", method = RequestMethod.POST)
	public void register(@PathVariable String uid) {
		runFlowService.registerDepends(uid);
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
	public FlowPoolInfo getPoolInfo(@PathVariable String uid) {
		return runFlowService.getFlowPoolInfo(uid);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "archive/runinfo/{uid}", method = RequestMethod.GET)
	public List<RunInfo> listArchiveRunInfo(@PathVariable String uid) {
		return runFlowService.listArchiveRunInfo(uid);
	}

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "pool/{fuid}/remove", method = RequestMethod.POST, params = "type=force")
	public void forceRemovePool(@PathVariable String fuid) {
		runFlowService.forceRemove(fuid);
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "{fuid}/debug/action/{auid}", method = RequestMethod.POST)
	public void testAction(@PathVariable String fuid, @PathVariable String auid, @RequestParam int delDelay)
			throws InterruptedException {
		runFlowService.testAction(fuid, auid, delDelay);
	}

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "{uid}/run", method = RequestMethod.POST, params = "type=manual")
	public RunInfo runManual(@PathVariable String uid) {
		return runFlowService.runManual(uid).getInfo();
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "pool/{uid}/reload", method = RequestMethod.POST)
	public void reloadPool(@PathVariable String uid){
		 runFlowService.reloadPool(uid);
	}

}
