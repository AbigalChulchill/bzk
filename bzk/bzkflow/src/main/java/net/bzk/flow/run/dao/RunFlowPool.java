package net.bzk.flow.run.dao;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import lombok.Getter;
import net.bzk.flow.model.ArchiveRun;
import net.bzk.flow.model.Flow;
import net.bzk.flow.run.entry.Entryer;
import net.bzk.flow.run.flow.FlowRuner;
import net.bzk.flow.run.flow.FlowRuner.RunInfo;

/*
 * @Link https://docs.spring.io/spring/docs/4.2.x/spring-framework-reference/html/scheduling.html
 */
@Service
@Scope("prototype")
public class RunFlowPool {

	@Getter
	private Flow model;
	@Inject
	private RunFlowDao runFlowDao;
	@Inject
	private ApplicationContext context;
	@Getter
	private ThreadPoolExecutor threadPool;
	@Inject
	private ArchiveRunDao archiveRunDao;

	private Map<String, FlowRuner> map = new ConcurrentHashMap<>();
	private Entryer entryer;

	public RunFlowPool init(Flow fm) {
		model = fm;
		configThreadPool();
		return this;
	}

	public void launch() {
		schedule();
	}

	private void configThreadPool() {
		threadPool = new ThreadPoolExecutor(model.getThreadCfg().getCorePoolSize(),
				model.getThreadCfg().getMaximumPoolSize(), model.getThreadCfg().getKeepAliveTime(),
				model.getThreadCfg().getAliveUnit(), new ArrayBlockingQueue<Runnable>(100),
				new ThreadPoolExecutor.CallerRunsPolicy());
	}

	public Map<String, FlowRuner> getMap() {
		return map;
	}

	public FlowRuner create() {
		FlowRuner ans = runFlowDao.create(model, r -> onEndFlowRuner(r));
		map.put(ans.getInfo().getUid(), ans);
		return ans;
	}

	private void onEndFlowRuner(FlowRuner r) {
		var info = r.getInfo();
		ArchiveRun ar = new ArchiveRun();
		ar.setFlowUid(r.getModel().getUid());
		ar.setFlowRunUid(info.getUid());
		ar.setInfo(info);
		ar.setState(info.getState());
		archiveRunDao.save(ar);
		map.remove(r.getInfo().getUid());
	}

	public FlowRuner createAndStart() {
		FlowRuner ans = create();
		ans.start(threadPool);
		return ans;
	}

	private void schedule() {
		String beanName = getModel().getEntry().getClazz();
		entryer = context.getBean(beanName, Entryer.class);
		entryer.init(getModel().getEntry());
		entryer.schedule(() -> createAndStart());
	}

	public List<RunInfo> listRunInfos(boolean includeArchive) {
		List<RunInfo> ans = map.values().stream().map(FlowRuner::getInfo).collect(Collectors.toList());
		if (includeArchive) {
			ans.addAll(listArchiveRunInfo());
		}
		return ans;
	}

	public List<RunInfo> listArchiveRunInfo() {
		return archiveRunDao.findByFlowUid(model.getUid()).stream().map(ar -> ar.getInfo())
				.collect(Collectors.toList());
	}

	public void forceCancelAll() {
		entryer.unregister();
		map.values().forEach(v -> {
			if (v.getTask() != null) {
				v.getTask().cancel(true);
			}
		});

	}

}
