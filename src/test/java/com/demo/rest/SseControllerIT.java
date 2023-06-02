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

    @Test
    void test() throws InterruptedException {
        var client = ClientBuilder.newBuilder().sslContext(trustAllSslContext).build();
        var target = client.target("https://localhost:4443/api/sse");
        var source = SseEventSource.target(target).build();
        source.register(ev -> System.out.println("ev.getName() = " + ev.getName()));
        source.open();
        source.close();
        client.close();
    }

    private static final SSLContext trustAllSslContext;
    static {
        try {
            trustAllSslContext = SSLContext.getInstance("TLS");
            trustAllSslContext.init(null, new TrustManager[] {
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
}
