package net.bzk.flow.run.dao;

import lombok.extern.slf4j.Slf4j;
import net.bzk.flow.dto.FlowRunEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
@Slf4j
public class EventListenerDao implements ApplicationListener<FlowRunEvent> {

    private List<FlowEventListener> listeners = new CopyOnWriteArrayList<FlowEventListener>();

    @Override
    public void onApplicationEvent(FlowRunEvent event) {
        listeners.stream().forEach(l -> {
            try {
                l.onEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
                log.warn(l + " is fails", e);
            }
        });
    }

    public void add(FlowEventListener l) {
        listeners.add(l);
    }

    public void remove(FlowEventListener l) {
        listeners.remove(l);
    }


    @FunctionalInterface
    public static interface FlowEventListener {
        void onEvent(FlowRunEvent e) throws Exception;
    }


}
