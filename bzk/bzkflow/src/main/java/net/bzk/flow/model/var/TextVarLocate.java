package net.bzk.flow.model.var;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.bzk.infrastructure.JsonUtils;
import net.bzk.infrastructure.PlaceholderUtils;

public class TextVarLocate {
//	private static final String TAG = "<BZK_VAR>";
	private String text;
	private List<VarVal> vars = new ArrayList<>();

	public TextVarLocate init(String text) {
		this.text = text;
		parse();
		return this;
	}

	private void parse() {
		vars = PlaceholderUtils.listStringSubstitutorKeys(text).stream().map(json -> JsonUtils.loadByJson(json, VarVal.class))
				.collect(Collectors.toList());
	}

	public VarValSet list() {
		return new VarValSet(vars);
	}

}
