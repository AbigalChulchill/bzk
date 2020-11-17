package net.bzk.flow.run.action;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.bzk.flow.model.Action.NodejsAction;
import net.bzk.flow.model.var.TextVarLocate;
import net.bzk.flow.model.var.VarValSet;
import net.bzk.flow.utils.LogUtils;
import net.bzk.infrastructure.CommUtils;
import net.bzk.infrastructure.JsonUtils;
import net.bzk.infrastructure.StrPlacer;
import net.bzk.infrastructure.ex.BzkRuntimeException;
import net.bzk.infrastructure.process.ProcessUtils;

@Service("net.bzk.flow.model.Action$NodejsAction")
@Scope("prototype")
@Slf4j
public class NodejsActionCall extends ActionCall<NodejsAction> {

	
	@Value("classpath:action/nodejs/package.jsontemp")
	private Resource packageTemp;
	@Value("classpath:action/nodejs/app.jstemp")
	private Resource appJsTemp;
	public static final String ROOT_DIR = "/bzk/action/nodejs/";

	private File projectDir;
	private String npm;

	@Override
	public ActionCall initBase(Uids _uids, NodejsAction model) {
		super.initBase(_uids, model);
		model.getInstalls().add("bzk");
		NodejsActionCall ans = (NodejsActionCall) super.initBase(_uids, model);
		npm = CommUtils.isWindows() ? "npm.cmd" : "npm";
		return ans;
	}

	private void initProject() throws IOException {
		String propath = ROOT_DIR + getModel().getUid() + "/";
		projectDir = new File(propath);
		if(projectDir.exists() && getModel().isAlwayCleanWorkDir()) {
			FileUtils.deleteDirectory(projectDir);
		}
		if(!projectDir.exists()) {
			genProject();
		}
	}
	
	
	private void genProject() throws IOException {

		projectDir.mkdirs();
		File packfile = new File(projectDir, "package.json");
		packfile.createNewFile();
		FileUtils.writeStringToFile(packfile, getPackageBody(), StandardCharsets.UTF_8);

		File appjsfile = new File(projectDir, "app.js");
		appjsfile.createNewFile();
		FileUtils.writeStringToFile(appjsfile, getAppJsBody(), StandardCharsets.UTF_8);
		
		runCmd(npm, "install");
		for (String ic : getModel().getInstalls()) {
			runCmd(npm, "install", ic);
		}
	}

	private void runCmd(String... cmd) {
		runCmd(s -> {
		}, cmd);
	}

	private void runCmd(Consumer<String> c, String... cmd) {
//		log.info("runCmd:" + cmd);
		logUtils.logActionCall(log, getUids(), "runCmd:" + cmd);
		int ec = ProcessUtils.exec(projectDir, cains -> {
			logUtils.logActionCall(log, getUids(),cains);
			c.accept(cains);
		}, cmd);
		if (ec != 0)
			throw new BzkRuntimeException(
					JsonUtils.toJson(getModel()) + " , " + JsonUtils.toJson(getUids()) + " exit code=" + ec);
	}

	@Override
	public VarValSet call() throws Exception {
		initProject();
		StringBuilder sb = new StringBuilder();
		runCmd(l -> {
			sb.append(l + "\n").append(System.getProperty("line.separator"));
		}, npm, "start");

		return new TextVarLocate().init(sb.toString()).list();
	}

	public String getPackageBody() {
		String ans = CommUtils.loadBy(packageTemp);
		ans = StrPlacer.build(ans).place("dependencies", JsonUtils.toJson(getModel().getDependencies()))
				.place("devDependencies", JsonUtils.toJson(getModel().getDevDependencies())).replace();
		return ans;
	}

	public String getAppJsBody() {
		String ans = CommUtils.loadBy(appJsTemp);
		RpcObj ro = new RpcObj();
		ro.setHost("http://127.0.0.1:8080");
		ro.setUids(getUids());
		ans = StrPlacer.build(ans).place("initJson", JsonUtils.toJson(ro)).place("content", getModel().getCode())
				.replace();
		return ans;
	}

}
