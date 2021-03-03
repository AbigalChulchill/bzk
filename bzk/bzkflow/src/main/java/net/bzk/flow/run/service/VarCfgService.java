package net.bzk.flow.run.service;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import net.bzk.flow.BzkFlowUtils;
import net.bzk.flow.Constant;
import net.bzk.flow.model.VarCfg;
import net.bzk.flow.model.var.VarMap;
import net.bzk.flow.run.dao.VarCfgDao;
import net.bzk.infrastructure.JsonUtils;
import net.bzk.infrastructure.ex.BzkRuntimeException;

@Service
public class VarCfgService {

	public static final String initDataPath = "/bzk/model/varcfg/";
	
	@Inject
	private VarCfgDao dao;
	
	@PostConstruct
	public void loadInitData() {
		var dir = new File(initDataPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		var ffs = FileUtils.listFiles(dir, new String[] { "json" }, false);
		ffs.forEach(this::importByFile);
	}
	
	@Transactional
	private void importByFile(File f) {
		try {
			VarMap v = BzkFlowUtils.getFlowJsonMapper().readValue(f, VarMap.class);
			VarCfg cfg = new VarCfg();
			cfg.setName(f.getName());
			cfg.setContent(v);
			create(cfg);
		} catch (IOException e) {
			throw new BzkRuntimeException(e);
		}
	}
	
	@Transactional
	public VarCfg create(VarCfg ans) {
		ans.setUid(RandomStringUtils.randomAlphanumeric(Constant.MODEL_UID_SIZE));
		replaceHex(ans);
		return dao.save(ans);
	}
	
	@Transactional
	public VarCfg save(VarCfg c) {
		c = replaceHex(c);
		return dao.save(c);
	}
	
	@Transactional
	public void remove(String uid) {
		var ist = dao.findById(uid).get();
		dao.delete(ist);
	}
	
	
	private VarCfg replaceHex(VarCfg c) {
		VarMap v = c.getContent();
		String jss = JsonUtils.toJson(v);
		String shex= org.apache.commons.codec.digest.DigestUtils.sha256Hex(jss);
		c.setSha256(shex);
		return c;
		
	}
	
	

}
