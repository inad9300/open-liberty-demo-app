package com.demo.rest;

import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ManagedScheduledExecutorService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

import static jakarta.ws.rs.core.MediaType.SERVER_SENT_EVENTS;
import static java.lang.System.currentTimeMillis;
import static java.util.concurrent.TimeUnit.SECONDS;

@ApplicationScoped
@Path("/sse")
public class SseController {

    @Resource
    private ManagedScheduledExecutorService scheduledExecutor;

    private Sse sse;
    private final List<SseEventSink> sinks = new CopyOnWriteArrayList<>();
    private ScheduledFuture<?> scheduledPing;

    void onInit(@Observes @Initialized(ApplicationScoped.class) Object __) {
        scheduledPing = scheduledExecutor.scheduleAtFixedRate(() -> {
            if (this.sse == null) return;

            var ping = sse.newEventBuilder().name("PING").data(currentTimeMillis()).build();
            System.out.println("Sending pings to " + sinks.size() + " clients...");
            for (var sink : sinks) {
                System.out.println("sink.isClosed() = " + sink.isClosed());
                try {
                    sink
                        .send(ping)
                        .exceptionally(ex -> {
                            System.out.println("Exception from `exceptionally` block: " + ex.getMessage());
                            sinks.remove(sink);
                            return null;
                        });
                } catch (Throwable ex) {
                    System.out.println("Exception from `catch` block: " + ex.getMessage());
                    sinks.remove(sink);
                }
            }
        }, 0, 1, SECONDS);
    }

    void onDestroy(@Observes @Destroyed(ApplicationScoped.class) Object __) {
        if (scheduledPing != null) scheduledPing.cancel(true);
    }

    @GET
    @Produces(SERVER_SENT_EVENTS)
    public void handleSse(@Context Sse sse, @Context SseEventSink sink) {
        this.sse = sse;
        this.sinks.add(sink);
    }
}
