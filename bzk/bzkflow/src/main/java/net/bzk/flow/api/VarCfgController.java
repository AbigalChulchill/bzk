package net.bzk.flow.api;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import net.bzk.flow.model.VarCfg;
import net.bzk.flow.run.dao.VarCfgDao;
import net.bzk.flow.run.service.VarCfgService;

@CrossOrigin(maxAge = 3600, methods = { RequestMethod.GET,RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.POST, RequestMethod.PATCH,
		RequestMethod.OPTIONS, RequestMethod.HEAD }, allowedHeaders = "*", origins = "*")
@Validated
@Controller
@RequestMapping(value = "varcfg/")
public class VarCfgController {
	@Inject
	private VarCfgService service;
	@Inject
	private VarCfgDao dao;

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "all", method = RequestMethod.GET)
	public List<VarCfg> listAll() {
		var ib = dao.findAll();
		return StreamSupport.stream(ib.spliterator(), false).collect(Collectors.toList());
	}

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public VarCfg save(@RequestBody VarCfg v) throws IOException {
		return service.save(v,true);
	}


	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "{uid}", method = RequestMethod.DELETE)
	public void remove(@PathVariable String uid) {
		service.remove(uid);
	}

}
