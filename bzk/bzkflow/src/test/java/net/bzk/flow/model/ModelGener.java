package net.bzk.flow.model;

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
import net.bzk.flow.model.Box;
import net.bzk.flow.model.Entry;
import net.bzk.flow.model.Flow;
import net.bzk.flow.model.Link;
import net.bzk.flow.model.Action.NodejsAction;
import net.bzk.flow.model.Entry.FixedRateEntry;
import net.bzk.flow.run.dao.RunFlowPool;
import net.bzk.infrastructure.CommUtils;


public class ModelGener {

	private Flow model = new Flow(); 
	
	

	@Test
	void testCreateByMode() {
		Flow flowM = genModel();
		String fmjs = CommUtils.toJson(flowM);
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
		e.setInitUnit(ChronoUnit.SECONDS);
		e.setPeriod(300000);
		return e;
	}

	public Box genBox() {
		Box ans = new Box();
		List<Action> actions = new ArrayList<>();
//		Action act = genAction();
//		actions.add(act);
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



}
