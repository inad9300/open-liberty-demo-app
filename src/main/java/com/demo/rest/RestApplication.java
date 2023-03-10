package com.demo.rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.nio.ByteBuffer;

@ApplicationPath("/api")
public class RestApplication extends Application {

  // Uncomment to produce massive memory leak.
  // static final ByteBuffer buf = ByteBuffer.allocate(100_000_000); // Allocate 100 MB.
}
