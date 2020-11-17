package net.bzk.flow.run.dao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.inject.Provider;

import org.springframework.stereotype.Service;

import net.bzk.flow.model.Box;
import net.bzk.flow.model.var.VarMap.VarsDao;
import net.bzk.flow.run.flow.BoxRuner;

@Service
public class RunBoxDao implements VarsDao {

	@Inject
	private Provider<BoxRuner> boxRunerProvider;

	private Map<String, BoxRuner> map = new ConcurrentHashMap<>();

	public BoxRuner create(BoxRuner.Bundle bb, Box b) {
		BoxRuner ans = boxRunerProvider.get().init(bb, b);
		map.put(b.getUid(), ans);
		return ans;
	}

	@Override
	public BoxRuner getByUid(String uid) {
		return map.get(uid);
	}

	public void remove(BoxRuner currentBoxRuner) {
		map.remove(currentBoxRuner.getUid());
	}

}
