package net.bzk.flow.run.dao;

import net.bzk.flow.dto.FlowRunEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class EventListenerDao implements ApplicationListener<FlowRunEvent> {

    private List<FlowEventListener> listeners = new CopyOnWriteArrayList<FlowEventListener>();

    @Override
    public void onApplicationEvent(FlowRunEvent event) {
        listeners.stream().forEach(l -> l.onEvent(event));
    }

    public void add(FlowEventListener l) {
        listeners.add(l);
    }

    public void remove(FlowEventListener l) {
        listeners.remove(l);
    }


    @FunctionalInterface
    public static interface FlowEventListener {
        void onEvent(FlowRunEvent e);
    }


}
