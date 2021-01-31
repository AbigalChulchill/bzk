package net.bzk.auth.controllers;

import java.util.List;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import net.bzk.auth.aop.CurrentUser;
import net.bzk.auth.dao.CosignDao;
import net.bzk.auth.model.Account;
import net.bzk.auth.model.Cosign;
import net.bzk.auth.service.CosignCreator;
import net.bzk.auth.service.CosignService;

@CrossOrigin(maxAge = 3600, methods = { RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST, RequestMethod.PATCH,
		RequestMethod.OPTIONS, RequestMethod.HEAD }, allowedHeaders = "*", origins = "*")
@Validated
@Controller
@RequestMapping(value = "/cosign/")
public class CosignController {

	@Inject
	private CosignService service;
	@Inject
	private CosignDao dao;
	
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public CosignCreator.Bundle create(@CurrentUser Account ca, @RequestBody Cosign cs) {
		return service.create(ca, cs);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<Cosign> listBySigner(@CurrentUser Account ca){
		return dao.listByAccountId(ca.getUid());
	}

}
