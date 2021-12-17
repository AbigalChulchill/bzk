package net.bzk.flow.model.demo;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import lombok.Getter;
import net.bzk.flow.model.Action;
import net.bzk.flow.model.Action.NodejsAction;
import net.bzk.flow.model.Box;
import net.bzk.flow.model.Entry;
import net.bzk.flow.model.Entry.FixedRateEntry;
import net.bzk.flow.model.Flow;
import net.bzk.flow.model.Link;
import net.bzk.flow.model.Transition;

@Service
public class ModelBuilder {
	
	@Getter
	private Flow model = new Flow(); 
	
	public ModelBuilder init() {
		model.setBoxs(createBoxs());
		model.setEntry(createEntry());
		return this;
	}

	private Entry createEntry() {
		FixedRateEntry ans = new FixedRateEntry();
		ans.setBoxUid(model.getBoxs().stream().findFirst().get().getUid());
		ans.setInitialDelay(2);
		ans.setPeriod(-1);
		ans.setCronExpression("0 0/10 * * * *");
		ans.setUnit(ChronoUnit.SECONDS);
		return ans;
	}

	private Set<Box> createBoxs() {
		Set<Box> ans = new HashSet<>();
		ans.add(createBox());
		
		return ans;
	}

	private Box createBox() {
		Box ans = new Box();
		ans.setActions(createActions());
		ans.setLinks(createLinks());
		List<String> sort = new ArrayList<String>();
		sort.add(ans.getActions().get(0).getUid());
		sort.add(ans.getLinks().get(0).getUid());
		ans.setTaskSort(sort);
		ans.setTransition(new Transition());
//		ans.setWhileJudgment(ConditionNum.gen());
		return ans;
	}

	private List<Link> createLinks() {
		List<Link> ans = new ArrayList<>();
		ans.add(createLink());
		return ans;
	}

	private Link createLink() {
		Link ans = new Link();
		ans.setName("TODOLink");
		ans.getTransition().setEndTag("TestEnd");
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
