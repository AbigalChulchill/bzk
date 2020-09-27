package net.bzk.flow.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.bzk.flow.model.Action;
import net.bzk.flow.model.Box;
import net.bzk.flow.model.Action.NodejsAction;

public class BoxModelGener {
	private  Box model = new Box();

	public BoxModelGener genBox() {
		List<Action> actions = new ArrayList<>();
		Action act = genAction();
		actions.add(act);
		model.setActions(actions);
		return this;
	}
	
	public Action genAction() {
		NodejsAction na = new NodejsAction();
		Map<String, String> dm = new HashMap<>();
		dm.put("moment", "^2.26.0");
		na.setDependencies(dm);
		na.setDevDependencies(new HashMap<>());
		na.setCode("console.log($bzk.init)");
		return na;
	}
	
}
