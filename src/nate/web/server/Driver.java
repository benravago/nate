package nate.web.server;

import java.nio.ByteBuffer;

import java.util.Set;
import java.util.concurrent.Flow;

import java.net.http.WebSocket;

import nate.http.HttpServer;
import nate.http.HttpContext;

public class Driver extends Configuration implements HttpServer {

  @Override public void startup() {}
  @Override public void shutdown() {}

  @Override public Endpoint create(String address, Flow.Processor<ByteBuffer,ByteBuffer> implementor) { return null; }
  @Override public void publish(Endpoint endpoint) {}

  @Override public Set<Endpoint> endpoints() { return null; }

  @Override public WebSocket.Builder newWebSocketBuilder() { return null; }
  @Override public HttpContext.Builder newHttpContextBuilder() { return null; }

  @Override public Endpoint create(String address, HttpContext.Listener listener) { return null; }
  @Override public Endpoint create(String address, WebSocket.Listener listener) { return null; }

}
