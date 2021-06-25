package net.bzk.flow.run.dao;

import lombok.extern.slf4j.Slf4j;
import net.bzk.flow.dto.FlowRunEvent;
import net.bzk.infrastructure.ex.BzkRuntimeException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

@Repository
@Slf4j
public class EventListenerDao implements ApplicationListener<FlowRunEvent> {

    @Inject
    @Qualifier("threadPoolTaskExecutor")
    private ExecutorService executorService;

    private List<FlowEventListener> listeners = new CopyOnWriteArrayList<FlowEventListener>();

    @Override
    public void onApplicationEvent(FlowRunEvent event) {
        listeners.stream().forEach(l -> {
            pushListener(l, event);
        });
    }

    private void pushListener(FlowEventListener l, FlowRunEvent event) {
        executorService.submit(() -> {
            try {
                l.onEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
                log.warn(l + " is fails", e);
                throw new BzkRuntimeException(e);
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
