package net.bzk.gists.controller;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.bzk.infrastructure.AES256Util;
import net.bzk.infrastructure.CommUtils;

@CrossOrigin(maxAge = 3600, methods = { RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST, RequestMethod.PATCH,
		RequestMethod.OPTIONS, RequestMethod.HEAD }, allowedHeaders = "*", origins = "*")
@Validated
@Controller
@RequestMapping(value = "/secret/")
public class SecretController {

	@Value("${gist.secret.pass}")
	private String pass;

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "encrypt", method = RequestMethod.POST)
	public Text encrypt(@RequestBody InPlain ep) throws Exception {
		String fp = pass + ranName(ep.name + ep.passHash);
		String ans = AES256Util.encrypt(fp, ep.getPlain());
		return new Text(SMethod.encrypt, ans);
	}

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "decrypt", method = RequestMethod.POST)
	public Text decrypt(@RequestBody InPlain ep) throws Exception {
		String fp = pass + ranName(ep.name + ep.passHash);
		String ans = AES256Util.decrypt(fp, ep.getPlain());
		return new Text(SMethod.decrypt, ans);
	}

	private String ranName(String name) {
		return CommUtils.sha1(name, 7);
	}

	public static enum SMethod {
		encrypt, decrypt
	}

	@SuppressWarnings("serial")
	@Data
	public static class InPlain implements Serializable {
		private String plain;
		private String name;
		private String passHash;
	}

	@SuppressWarnings("serial")
	@Data
	@AllArgsConstructor(access = AccessLevel.PUBLIC)
	public static class Text implements Serializable {
		private SMethod method;
		private String data;
	}
}
