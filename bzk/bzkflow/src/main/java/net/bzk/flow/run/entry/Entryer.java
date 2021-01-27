package net.bzk.flow.run.entry;

import javax.inject.Inject;

import org.springframework.scheduling.TaskScheduler;

import lombok.Getter;
import net.bzk.flow.model.Entry;

public abstract  class Entryer<T extends Entry> implements Runnable  {

	@Getter
	@Inject
	private TaskScheduler scheduler;
	@Getter
	private T model;
	private Runnable postRun;
	
	
	public void init(T e) {
		model =e;
		
	}
	
	
	public void schedule(Runnable p) {
		postRun = p;
		registerSchedule(scheduler);
	}


	protected abstract void registerSchedule(TaskScheduler s);
	
	public abstract void unregister();


	@Override
	public void run() {
		postRun.run();
	}
	
	

	
}
