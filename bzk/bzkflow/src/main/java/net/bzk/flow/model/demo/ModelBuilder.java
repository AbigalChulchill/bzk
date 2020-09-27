package net.bzk.flow.model.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import lombok.Getter;
import net.bzk.flow.model.Action;
import net.bzk.flow.model.Box;
import net.bzk.flow.model.Flow;
import net.bzk.flow.model.Action.NodejsAction;

@Service
public class ModelBuilder {
	
	@Getter
	private Flow model = new Flow(); 
	
	public ModelBuilder init() {
		model.setBoxs(createBoxs());
		return this;
	}

	private Set<Box> createBoxs() {
		Set<Box> ans = new HashSet<>();
		ans.add(createBox());
		
		return ans;
	}

	private Box createBox() {
		Box ans = new Box();
		ans.setActions(createActions());
		List<String> sort = new ArrayList<String>();
		sort.add(ans.getActions().get(0).getUid());
		ans.setTaskSort(sort);
		return ans;
	}

	private List<Action> createActions() {
		List<Action> ans = new ArrayList<>();
		ans.add(createAction());
		return ans;
	}

	private Action createAction() {
		NodejsAction na =new NodejsAction();
		Map<String,String> dm = new HashMap<>();
		dm.put("moment", "^2.26.0");
		na.setDependencies(dm);
		na.setDevDependencies(new HashMap<>());
		na.setCode("console.log($bzk.init)");
		return na;
	}

}
