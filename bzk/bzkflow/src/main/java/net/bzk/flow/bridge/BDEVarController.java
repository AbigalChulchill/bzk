package net.bzk.flow.bridge;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import net.bzk.flow.api.dto.DtoVarQuery;
import net.bzk.flow.run.service.RunVarService;

@Validated
@Controller
@RequestMapping(value = "/bridge/var/")
public class BDEVarController {
	
	@Inject
	private RunVarService service;
	
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.POST)
	public String get(@RequestBody DtoVarQuery q) {   
		return service.findByQuery(q).get();
	}

}
