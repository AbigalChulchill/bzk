package net.bzk.flow.model;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bzk.flow.api.dto.VarKeyReflect;
import net.bzk.flow.model.var.VarLv;
import net.bzk.flow.model.var.VarLv.VarKey;
import net.bzk.infrastructure.CommUtils;
import net.bzk.infrastructure.JsonUtils;

@Data
@EqualsAndHashCode(callSuper = false)
public class Action extends BzkObj {

	private String name = "Action";
	private boolean tryErrorble;

	@JsonIgnore
	public String getErrorVarKey() {
		StringBuilder sb = new StringBuilder("bzk.error.");
		sb.append(StringUtils.isBlank(name) ? getUid() : name);
		return sb.toString();
	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class NodejsAction extends Action {
		private boolean alwayCleanWorkDir = false;
		private String code = "";
		private Set<String> installs = new ConcurrentSkipListSet<String>();
		private Map<String, String> dependencies = new ConcurrentHashMap<>();
		private Map<String, String> devDependencies = new ConcurrentHashMap<>();

		@JsonIgnore
		public String getProjectSha1Code() {
			NodejsAction co = JsonUtils.toByJson(this, NodejsAction.class);
			co.setCode(null);
			String js = JsonUtils.toJson(co);
			String sha1 = CommUtils.sha1(js, 12);
			return sha1;
		}

	}

	public static enum Polyglot {
		js, R, ruby, python
	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class PolyglotAction extends Action {
		private String code = "";
		private Polyglot polyglot;
		private VarLv resultLv;
		private String resultKey;
	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class MXparserAction extends Action {
		private String code = "";
		private VarLv resultLv;
		private String resultKey;
	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class VarModifyAction extends Action {
//		private Polyglot polyglot = Polyglot.js;
		private List<KVPair> flatData;
	}

	@Data
	public static class KVPair {
		private String key;
		private String val;
		
	
	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class JSONPathAction extends Action {
		private String syntax;
		private VarKey source;
		private VarKey target;

	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class SubFlowAction extends Action {
		private String flowUid;
		private boolean asynced;
		private List<KVPair> inputData;
		private List<VarKeyReflect> outputReflects;
	}

	public static enum ConvertMethod {
		ToJSONText;
	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class ConvertAction extends Action {
		private Polyglot polyglot = Polyglot.js;
		private ConvertMethod method;
		private String code;
		private VarKey output;
	}
	
	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class WaitAction extends Action {
		private ChronoUnit unit;
		private long step;
	}
	
	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class MailAction extends Action {
		private String smtpHost= "smtp.gmail.com";
		private String smtpPort = "465";
		private String username;
		private String password;
		private String toMail;
		private String subject;
		private String body;
		

		
	}
	
	

}
