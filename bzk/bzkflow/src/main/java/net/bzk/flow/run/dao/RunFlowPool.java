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
import net.bzk.flow.api.dto.RegisteredFlow.RunInfo;
import net.bzk.flow.model.Flow;
import net.bzk.flow.run.entry.Entryer;
import net.bzk.flow.run.flow.FlowRuner;

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

	private Map<String, FlowRuner> map = new ConcurrentHashMap<>();

	public RunFlowPool init(Flow fm) {
		model = fm;
		configThreadPool();
		schedule();
		return this;
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

	public FlowRuner createAndStart() {
		FlowRuner ans = runFlowDao.create( model);
		map.put(ans.getUid(), ans);
		ans.start(threadPool);
		return ans;
	}

	private void schedule() {
		String beanName = getModel().getEntry().getClazz();
		Entryer e = context.getBean(beanName, Entryer.class);
		e.init(getModel().getEntry());
		e.schedule(() -> createAndStart());
	}

	public List<RunInfo> listRunInfos() {
		return map.values().stream().map(FlowRuner::getRunInfo).collect(Collectors.toList());
	}

	public void forceCancelAll() {
		map.values().forEach(v -> v.getTask().cancel(true));

	}

}
