package net.bzk.auth.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.NullHandling;
import org.springframework.data.domain.Sort.Order;
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

import lombok.Data;
import net.bzk.auth.dao.PayerDao;
import net.bzk.auth.model.Account;
import net.bzk.auth.model.Bill;
import net.bzk.auth.model.Payer;
import net.bzk.auth.model.Payer.State;
import net.bzk.auth.service.PayerService;

@CrossOrigin(maxAge = 3600, methods = { RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST, RequestMethod.PATCH,
		RequestMethod.OPTIONS, RequestMethod.HEAD }, allowedHeaders = "*", origins = "*")
@Validated
@Controller
@RequestMapping(value = "/admin/payer/")
public class AdminPayerController {

	@Inject
	private PayerService service;
	@Inject
	private PayerDao dao;

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Payer create(@RequestBody Payer p) {
		return service.create(p);
	}

	@Data
	public static class QueryDto {
		private State state;
		private int page;
		private int size;
		private List<OrderDto> orders;

	}

	@Data
	public static class OrderDto {
		private Direction direction;
		private String property;
		private NullHandling nullHandling;
	}

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.POST)
	public List<Payer> findAllByState(QueryDto q) {
		List<Order> orders = q.orders.stream().map(od -> new Order(od.direction, od.property, od.nullHandling))
				.collect(Collectors.toList());
		return dao.findAllByState(q.state, PageRequest.of(q.page, q.size, Sort.by(orders)));
	}

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "{accUid}/become", method = RequestMethod.POST)
	public Payer becomePayer(@PathVariable String accUid) {
		return service.become(accUid);
	}

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "{payerUid}/bill/create", method = RequestMethod.POST)
	public Bill createBill(@PathVariable String payerUid, Bill b) {
		return service.createBill(payerUid, b);
	}

}
