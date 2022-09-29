package com.demo.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;

import static jakarta.ws.rs.core.MediaType.SERVER_SENT_EVENTS;

@ApplicationScoped
@Path("/sse")
public class SseController {

    @GET
    @Produces(SERVER_SENT_EVENTS)
    public void handleSse(@Context Sse sse, @Context SseEventSink sink) {
        try {
            var sseEvent = sse.newEventBuilder().data("Hello from Open Liberty through Server-Sent Events!").build();
            sink
                .send(sseEvent)
                .exceptionally(ex -> {
                    System.out.println("Exception from `exceptionally` block:");
                    ex.printStackTrace();
                    return null;
                });
        } catch (Throwable ex) {
            System.out.println("Exception from `catch` block:");
            ex.printStackTrace();
        }
    }
}
