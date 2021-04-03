package net.bzk.flow.run.action;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import net.bzk.flow.model.HttpAction;
import net.bzk.flow.model.HttpAction.Headers;
import net.bzk.flow.model.var.VarValSet;
import net.bzk.infrastructure.JsonUtils;

import org.springframework.http.HttpMethod;

@Service("net.bzk.flow.model.HttpAction")
@Scope("prototype")
@Slf4j
public class HttpActionCall extends ActionCall<HttpAction> {


	public HttpActionCall() {
		super(HttpAction.class);
	}

	private RestTemplate restTemplate = new RestTemplate();

	@Override
	public ActionCall initBase(Uids _uids, HttpAction a) {
		ActionCall ans = super.initBase(_uids, a);
		combindToHeader();
		return ans;
	}


	
	private void combindToHeader() {
		HttpAction a = getModel();
		if(a.getHeaders() == null) {
			a.setHeaders(new Headers());
		}
		Map<String, String> m = a.headersFlat();
		for(String fk : m.keySet()) {
			String fv = m.get(fk);
			String[] vs = fv.split(",");
			Arrays.stream(vs).forEach(v-> a.getHeaders().add(fk,v) );
		}
	}

	@Override
	public VarValSet call() throws Exception {
		Object body = getModel().body();
		Headers hs = getModel().getHeaders();
		String url = getModel().url();
		HttpMethod mt = getModel().method();
		var logm = new HashMap<String,Object>();
		logm.put("body", body);
		logm.put("headers", hs);
		logm.put("url", url);
		logm.put("method", mt);
		logUtils.logActionCall( getUids(), JsonUtils.toJson(logm));
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(body, hs);
		ResponseEntity<String> o = restTemplate.exchange(url, mt, requestEntity,
				String.class, getModel().getUriVariables());
		Object rob =  JsonUtils.stringToValue(o.getBody());
		return VarValSet.genSingle(getModel().getKey().getKey(), getModel().getKey().getLv(), rob);
	}

}
