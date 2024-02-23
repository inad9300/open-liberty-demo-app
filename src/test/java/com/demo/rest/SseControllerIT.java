package com.demo.rest;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.sse.SseEventSource;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;

class SseControllerIT {

    private static final SSLContext TRUST_ALL_SSL_CONTEXT;
    static {
        try {
            TRUST_ALL_SSL_CONTEXT = SSLContext.getInstance("TLS");
            TRUST_ALL_SSL_CONTEXT.init(null, new TrustManager[] {
                new X509TrustManager() {
                    @Override public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                    @Override public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                    @Override public X509Certificate[] getAcceptedIssuers() { return null; }
                }
            }, null);
        } catch (GeneralSecurityException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static final String API_URL = "https://localhost:4443/api";

    @Test
    void passingTest_but_lotsOfNoisyLogsProduced() throws InterruptedException {
        try (
            var client = ClientBuilder.newBuilder().sslContext(TRUST_ALL_SSL_CONTEXT).build();
            var source = SseEventSource.target(client.target(API_URL + "/sse")).build()
        ) {
            source.register(ev -> System.out.println("Event received: " + ev.getName()));
            source.open();
            Thread.sleep(5_000);
        }
    }
}
