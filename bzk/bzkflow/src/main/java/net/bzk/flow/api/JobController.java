package net.bzk.flow.api;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

import net.bzk.flow.dto.JobRunInfo;
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
import net.bzk.flow.model.Job;
import net.bzk.flow.model.demo.ModelBuilder;
import net.bzk.flow.run.dao.JobsDao;
import net.bzk.flow.run.service.JobsService;

@CrossOrigin(maxAge = 3600, methods = { RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST, RequestMethod.PATCH,
		RequestMethod.OPTIONS, RequestMethod.HEAD }, allowedHeaders = "*", origins = "*")
@Validated
@Controller
@RequestMapping(value = "job/")
public class JobController {
	@Inject
	private ModelBuilder modelBuilder;
	@Inject
	private JobsDao dao;
	@Inject
	private JobsService service;

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "all", method = RequestMethod.GET)
	public List<Job> listAll() {
		return StreamSupport.stream(dao.findAll().spliterator(), false).collect(Collectors.toList());
	}

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "{uid}", method = RequestMethod.GET)
	public Job getByUid(@PathVariable String uid) {
		return dao.findById(uid).get();
	}
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "{uid}", method = RequestMethod.DELETE)
	public void remove(@PathVariable String uid) {
		service.remove(uid);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public Job save(@RequestBody Flow f) throws IOException {
		return service.save(f,true);
	}
	
	
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "new", method = RequestMethod.POST)
	public Job save() throws IOException {
		return service.save(modelBuilder.init().getModel(),true);
	}

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "{uid}/info", method = RequestMethod.GET)
	public JobRunInfo info(@PathVariable String uid){
		return service.getInfo(uid);
	}
	
	

}
