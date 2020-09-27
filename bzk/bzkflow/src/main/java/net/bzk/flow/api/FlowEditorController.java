package net.bzk.flow.api;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import net.bzk.flow.model.Flow;
import net.bzk.flow.model.demo.ModelBuilder;

@CrossOrigin(maxAge = 3600, methods = { RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST, RequestMethod.PATCH,
		RequestMethod.OPTIONS, RequestMethod.HEAD }, allowedHeaders = "*", origins = "*")
@Validated
@Controller
@RequestMapping(value = "/flow/editor/")
public class FlowEditorController {

	@Inject
	private ModelBuilder modelBuilder;

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "demo", method = RequestMethod.GET)
	public Flow getDemoModel() {
		return modelBuilder.init().getModel();
	}

}
