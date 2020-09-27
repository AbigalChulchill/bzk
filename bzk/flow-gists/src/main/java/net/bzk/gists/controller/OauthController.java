package net.bzk.gists.controller;

import java.net.MalformedURLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import net.bzk.gists.ervice.GitClientService;

@CrossOrigin(maxAge = 3600, methods = { RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST, RequestMethod.PATCH,
		RequestMethod.OPTIONS, RequestMethod.HEAD }, allowedHeaders = "*", origins = "*")
@Validated
@Controller
@RequestMapping(value = "/oauth")
public class OauthController {

	@Inject
	private GitClientService gitClientService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView receiveCode(@RequestParam String code, HttpServletRequest request)
			throws MalformedURLException {
		Map<String, Object> ans = new HashMap<>();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			ans.put(key, value);
		}
		ans.put("code", code);
		String token = gitClientService.oauthToken(code);
		ans.put("token", token);
		String referer = request.getHeader("referer");
		String rUrl = UriComponentsBuilder.fromUriString(referer).queryParam("token", token).build().toUri().toString();
		ans.put("rUrl", rUrl);
		System.out.println(ans);
		return new ModelAndView("redirect:" + rUrl);
	}

}
