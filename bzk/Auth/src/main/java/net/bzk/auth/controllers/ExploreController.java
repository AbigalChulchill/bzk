package net.bzk.auth.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
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

import net.bzk.auth.aop.CurrentUser;
import net.bzk.auth.dao.BillDao;
import net.bzk.auth.dao.EndUserDao;
import net.bzk.auth.dao.PayerDao;
import net.bzk.auth.dao.PromoteTileDao;
import net.bzk.auth.model.Account;
import net.bzk.auth.model.Bill;
import net.bzk.auth.model.EndUser;
import net.bzk.auth.model.Payer;
import net.bzk.auth.model.PromoteTile;
import net.bzk.auth.service.EndUserService;

@CrossOrigin(maxAge = 3600, methods = { RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST, RequestMethod.PATCH,
		RequestMethod.OPTIONS, RequestMethod.HEAD }, allowedHeaders = "*", origins = "*")
@Validated
@Controller
@RequestMapping(value = "/explore/")
public class ExploreController {

	@Inject
	private BillDao billDao;
	@Inject
	private PayerDao payerDao;
	@Inject
	private PromoteTileDao promoteTileDao;
	@Inject
	private EndUserDao endUserDao;
	@Inject
	private EndUserService endUserService;
	
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "bill/{state}", method = RequestMethod.GET)
	public List<Bill> listDoneBill(@PathVariable Bill.State state) {
		return billDao.findAllByState(state);
	}

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "{uids}/payer", method = RequestMethod.GET)
	public List<Payer> listPayerByPayUids(@PathVariable String[] uids) {
		System.out.println("uids:" + uids);
		return StreamSupport.stream(payerDao.findAllById(Arrays.asList(uids)).spliterator(), false)
				.collect(Collectors.toList());
	}

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "promote/tiles", method = RequestMethod.GET)
	public List<PromoteTile> listPromoteTile() {
		return StreamSupport.stream(promoteTileDao.findAll().spliterator(), false).collect(Collectors.toList());
	}

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "enduser", method = RequestMethod.GET)
	public EndUser getEndUser(@CurrentUser Account a) {
		return endUserDao.findByAccountOid(a.getUid()).get();
	}
	
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "enduser", method = RequestMethod.PUT)
	public EndUser putEndUser(@CurrentUser Account a , @RequestBody EndUser eu) {
		return endUserService.putEndUser(a,eu);
	}

}
