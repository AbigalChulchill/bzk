package net.bzk.flow.api;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;
import javax.transaction.Transactional;

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

import net.bzk.flow.model.Flow;
import net.bzk.flow.model.SavedFlow;
import net.bzk.flow.run.dao.SavedFlowDao;
import net.bzk.flow.run.service.SavedFlowService;

@CrossOrigin(maxAge = 3600, methods = { RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST, RequestMethod.PATCH,
		RequestMethod.OPTIONS, RequestMethod.HEAD }, allowedHeaders = "*", origins = "*")
@Validated
@Controller
@RequestMapping(value = "saved/flow/")
public class SavedFlowController {

	@Inject
	private SavedFlowDao dao;
	@Inject
	private SavedFlowService service;

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "all", method = RequestMethod.GET)
	public List<SavedFlow> listAll() {
		return StreamSupport.stream(dao.findAll().spliterator(), false).collect(Collectors.toList());
	}

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "{uid}", method = RequestMethod.GET)
	public SavedFlow getByUid(@PathVariable String uid) {
		return dao.findById(uid).get();
	}
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "{uid}", method = RequestMethod.DELETE)
	public void remove(String uid) {
		service.remove(uid);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public SavedFlow save(@RequestBody Flow f) {
		return service.save(f);
	}
	
	

}
