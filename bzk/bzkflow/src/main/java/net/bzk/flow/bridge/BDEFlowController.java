package net.bzk.flow.bridge;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import net.bzk.flow.run.dao.RunFlowDao;

@Validated
@Controller
@RequestMapping(value = "/bridge/flow/")
public class BDEFlowController {
	
	@Inject
	private RunFlowDao dao;
	
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "{uid}/var/{key}", method = RequestMethod.GET)	
	public String getVar(String uid,String key) {
		return dao.getVarMapByUid(uid).getByPath(key)+"";
	}
	
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "{uid}/var/{key}", method = RequestMethod.POST)	
	public void putVar(String uid,String key,String val) {
		 dao.getVarMapByUid(uid).putByPath(key, val);
	}

}
 