package net.bzk.flow.model;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bzk.flow.model.var.BaseVar;

@Data
@EqualsAndHashCode(callSuper = false)
public class Box extends BzkObj {

	private List<Action> actions;
	private List<Condition> conditions;
	private List<Link> links;
	private BaseVar vars;
	private List<String> taskSort;

}
