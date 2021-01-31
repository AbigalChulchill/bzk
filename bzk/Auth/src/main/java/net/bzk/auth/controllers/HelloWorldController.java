package net.bzk.auth.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import net.bzk.auth.aop.CurrentUser;
import net.bzk.auth.model.Account;
import net.bzk.infrastructure.JsonUtils;

@CrossOrigin(maxAge = 3600, methods = { RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST, RequestMethod.PATCH,
		RequestMethod.OPTIONS, RequestMethod.HEAD }, allowedHeaders = "*", origins = "*")
@Controller
public class HelloWorldController {

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public String firstPage(@CurrentUser Account a) {
		return "Hello World:" + JsonUtils.toJson(a);
	}

}