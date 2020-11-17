package net.bzk.flow.model;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bzk.flow.model.var.VarLv;
import net.bzk.infrastructure.JsonUtils.DataType;

@Data
@EqualsAndHashCode(callSuper = false)
public class Action extends BzkObj {

	private String name = "Action";

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class RestAction extends Action {

	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class NodejsAction extends Action {
		private boolean alwayCleanWorkDir = false;
		private String code = "";
		private Set<String> installs = new ConcurrentSkipListSet<String>();
		private Map<String, String> dependencies = new ConcurrentHashMap<>();
		private Map<String, String> devDependencies = new ConcurrentHashMap<>();;
	}

	public static enum Polyglot {
		js, R, ruby, python
	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class PolyglotAction extends Action {
		private String code = "";
		private Polyglot polyglot;
		private DataType resultType;
		private VarLv resultLv;
		private String resultKey;
	}
	
	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class MXparserAction extends Action{
		private String code = "";
		private VarLv resultLv;
		private String resultKey;
	}

}
