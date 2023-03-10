package com.demo.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.nio.ByteBuffer;

@ApplicationPath("/api")
public class RestApplication extends Application {

  // Uncomment to produce massive memory leak.
  // static final ByteBuffer buf = ByteBuffer.allocate(100_000_000); // Allocate 100 MB.

  public void onInit(@Observes @Initialized(ApplicationScoped.class) Object __) {
    Void npe = null;
    // Uncomment to break Open Liberty's ability to restart upon source code changes.
    // npe.toString();
  }
}
