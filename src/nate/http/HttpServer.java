package nate.http;

import java.nio.ByteBuffer;

import java.net.URI;
import java.net.Authenticator;
import java.net.CookieHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;

import java.net.http.WebSocket;
import java.net.http.HttpClient.Version;

import java.util.Set;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.Flow;
import java.util.concurrent.Executor;

public interface HttpServer {

  static HttpServer newHttpServer() {
    return newBuilder().build();
  }

  void startup();
  void shutdown();

  Version version();
  Optional<Authenticator> authenticator();
  Optional<CookieHandler> cookieHandler();
  Executor executor();
  Set<Feature<?>> features();

  Optional<SSLContext> sslContext();
  Optional<SSLParameters> sslParameters();

  static Builder newBuilder() {
    return ServiceLoader.load(Builder.class).findFirst().get();
  }

  interface Builder {

    HttpServer build();

    Builder version(Version version);

    Builder authenticator(Authenticator authenticator);
    Builder cookieHandler(CookieHandler cookieHandler);
    Builder executor(Executor executor);
    Builder feature(Feature<?> feature);

    Builder sslContext(SSLContext sslContext);
    Builder sslParameters(SSLParameters sslParameters);
  }

  interface Feature<F> {
    String key();
    F value();
  }

  Endpoint create(String bindingId, Flow.Processor<ByteBuffer,ByteBuffer> implementor);
  void publish(Endpoint endpoint);
  
  Set<Endpoint> endpoints();

  interface Endpoint {

    URI uri();
    <T> T implementor();

    boolean isPublished();
    void stop();
  }

  WebSocket.Builder newWebSocketBuilder();
  HttpContext.Builder newHttpContextBuilder();

  // wraps *.Listener with a protocol adapter
  Endpoint create(String address, HttpContext.Listener listener);
  Endpoint create(String address, WebSocket.Listener listener);

  default void publish(String address, HttpContext.Listener listener) { publish(create(address,listener)); }
  default void publish(String address, WebSocket.Listener listener) { publish(create(address,listener)); }

}
