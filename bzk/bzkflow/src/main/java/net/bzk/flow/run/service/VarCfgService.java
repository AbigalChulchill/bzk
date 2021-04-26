package net.bzk.flow.run.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.bzk.flow.BzkFlowUtils;
import net.bzk.flow.model.VarCfg;
import net.bzk.flow.model.var.VarMap;
import net.bzk.flow.run.dao.VarCfgDao;
import net.bzk.infrastructure.JsonUtils;
import net.bzk.infrastructure.ex.BzkRuntimeException;

@Service
public class VarCfgService {

	@Value("${init.cfg.path}")
	public String initDataPath ;
	@Value("${init.cfg.saveback}")
	private boolean saveBackInited = false;

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
			save(cfg,false);
		} catch (IOException e) {
			throw new BzkRuntimeException(e);
		}
	}

	@Transactional
	public VarCfg save(VarCfg c,boolean saveFile) throws IOException {
		c = replaceHex(c);
		if (saveBackInited && saveFile ) {
			FileUtils.write(new File(initDataPath + c.getName() + ".json"), JsonUtils.toJson(c.getContent()),
					Charset.forName("UTF-8"));
		}
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
		String shex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(jss);
		c.setSha256(shex);
		return c;

	}

}
