package net.bzk.flow.model.var;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import net.bzk.infrastructure.CommUtils;
import net.bzk.infrastructure.PlaceholderUtils;


public class TextVarLocate {
	private static final String TAG = "<BZK_VAR>";
	private String text;
	private List<VarVal> vars = new ArrayList<>();

	public TextVarLocate init(String text) {
		this.text = text;
		parse();
		return this;
	}

	private void parse() {
		vars = PlaceholderUtils.getByTag(TAG, text).stream().map(json -> CommUtils.loadByJson(json, VarVal.class))
				.collect(Collectors.toList());
	}
	
	public VarValSet list(){
		return new VarValSet(vars);
	}

}
