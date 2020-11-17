package net.bzk.flow.run.dao;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import net.bzk.flow.model.Action;
import net.bzk.flow.model.Action.NodejsAction;
import net.bzk.flow.model.Box;
import net.bzk.flow.model.Entry;
import net.bzk.flow.model.Entry.FixedRateEntry;
import net.bzk.flow.model.Flow;
import net.bzk.flow.model.Link;
import net.bzk.infrastructure.JsonUtils;

@SpringBootTest
public class RunFlowPoolTest {

	@Inject
	private Provider<RunFlowPool> runFlowPoolProvider;

	@Test
	void testCreateByMode() {
		Flow flowM = genModel();
		String fmjs = JsonUtils.toJson(flowM);
		System.out.println(fmjs);

	}

	private Flow genModel() {
		Flow ans = new Flow();
		Set<Box> boxs = new HashSet<>();
		Box box = genBox();
		boxs.add(box);
		ans.setBoxs(boxs);
		Entry entry=genEntry(box.getUid());
		ans.setEntry(entry);
		return ans;
	}

	private Entry genEntry(String boxUid) {
		FixedRateEntry e=new FixedRateEntry();
		e.setBoxUid(boxUid);
		e.setInitialDelay(1);
		e.setUnit(ChronoUnit.SECONDS);
		e.setPeriod(300000);
		return e;
	}

	private Box genBox() {
		Box ans = new Box();
		List<Action> actions = new ArrayList<>();
		Action act = genAction();
		actions.add(act);
		ans.setActions(actions);
		return ans;
	}
	
	private List<Link> genLinks(){
		List<Link> links=new ArrayList<>();
		Link link=genLink();
		links.add(link);
		return links;
	}

	private Link genLink() {
		// TODO Auto-generated method stub
		return null;
	}

	private Action genAction() {
		NodejsAction na = new NodejsAction();
		Map<String, String> dm = new HashMap<>();
		dm.put("moment", "^2.26.0");
		na.setDependencies(dm);
		na.setDevDependencies(new HashMap<>());
		na.setCode("console.log($bzk.init)");
		return na;
	}

}
