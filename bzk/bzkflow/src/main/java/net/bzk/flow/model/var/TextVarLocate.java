package net.bzk.flow.model.var;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import net.bzk.infrastructure.CommUtils;
import net.bzk.infrastructure.JsonUtils;
import net.bzk.infrastructure.PlaceholderUtils;

public class TextVarLocate {
//	private static final String TAG = "<BZK_VAR>";
	private List<VarVal> vars = new ArrayList<>();
	@Getter
	private String startTag = "<BZK!VAR@Start>";
	@Getter
	private String endTag = "<BZK@VAR#END>";

	public TextVarLocate setStartTag(String t) {
		startTag = t;
		return this;
	}

	public TextVarLocate setEndTag(String t) {
		endTag = t;
		return this;
	}

	public void parseLn(String txt) {
		PlaceholderUtils.listPlaceHolderKeys(txt, startTag, endTag, ".").forEach(s -> {
			vars.add(JsonUtils.loadByJson(s, VarVal.class));
		});
		
	}

	public VarValSet list() {
		return new VarValSet(vars);
	}

}
