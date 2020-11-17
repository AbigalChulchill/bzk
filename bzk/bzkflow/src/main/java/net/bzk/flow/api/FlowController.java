package net.bzk.flow.api;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.RandomStringUtils;
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

import net.bzk.flow.Constant;
import net.bzk.flow.api.dto.ActionDebugData;
import net.bzk.flow.api.dto.RegisteredFlow;
import net.bzk.flow.model.Entry.FixedRateEntry;
import net.bzk.flow.model.Flow;
import net.bzk.flow.model.demo.ModelBuilder;
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

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "demo", method = RequestMethod.GET)
	public Flow getDemoModel() {
		return modelBuilder.init().getModel();
	}

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "register", method = RequestMethod.POST)
	public Flow register(@RequestBody Flow f) {
		runFlowService.register(f);
		return f;
	}

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "registers", method = RequestMethod.GET)
	public List<RegisteredFlow> listRegisters() {
		return runFlowService.listRegisters();
	}

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "{fuid}/remove", method = RequestMethod.POST, params = "type=force")
	public void forceRemove(@PathVariable String fuid) {
		runFlowService.forceRemove(fuid);
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "debug/action", method = RequestMethod.POST)
	public void debugAction(@RequestBody ActionDebugData data, @RequestParam int delDelay) throws InterruptedException {
		runFlowService.debugAction(data, delDelay);
	}

}
