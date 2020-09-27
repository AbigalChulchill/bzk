package net.bzk.gists.ervice;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GitClientService {

	private static final String HOST = "https://github.com/";
	@Inject
	private RestTemplate rest;
	@Value("${github.app.client.id}")
	private String appId;
	@Value("${github.app.client.secret}")
	private String appSecret;

	public String oauthToken(String code) {
		Map<String, Object> reb = new HashMap<>();
		reb.put("client_id", appId);
		reb.put("client_secret", appSecret);
		reb.put("code", code);
		String url = HOST + "login/oauth/access_token";
		Map ob = rest.postForObject(url, reb, Map.class);
		return ob.get("access_token") + "";
	}

}
