package nate.web.server;

import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import java.net.Authenticator;
import java.net.CookieHandler;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;

import java.net.http.HttpClient.Version;

import nate.http.HttpServer;
import nate.http.HttpServer.Builder;
import nate.http.HttpServer.Feature;

public class Factory implements HttpServer.Builder {

  Version version;
  Authenticator authenticator;
  CookieHandler cookieHandler;
  Executor executor;
  Set<Feature<?>> features;
  SSLContext sslContext;
  SSLParameters sslParameters;

  @Override
  public HttpServer build() {
    var ws = new Driver();
    init(ws);
    reset();
    return ws;
  }

  void init(Driver ws) {
    ws.version = version != null ? version : Version.HTTP_1_1;
    ws.authenticator = authenticator;
    ws.cookieHandler = cookieHandler;
    ws.executor = executor != null ? executor : Executors.newFixedThreadPool(2);
    ws.features = features != null ? features : new HashSet<>();
    ws.sslContext = sslContext;
    ws.sslParameters = sslParameters;
  }

  void reset() {
    version = null;
    authenticator = null;
    cookieHandler = null;
    executor = null;
    features = null;
    sslContext = null;
    sslParameters = null;
  }

  @Override
  public Builder version(Version _version) {
    version = _version; return this;
  }
  @Override
  public Builder authenticator(Authenticator _authenticator) {
    authenticator = _authenticator; return this;
  }
  @Override
  public Builder cookieHandler(CookieHandler _cookieHandler) {
    cookieHandler = _cookieHandler; return this;
  }
  @Override
  public Builder executor(Executor _executor) {
    executor = _executor; return this;
  }
  @Override
  public Builder sslContext(SSLContext _sslContext) {
    sslContext = _sslContext; return this;
  }
  @Override
  public Builder sslParameters(SSLParameters _sslParameters) {
    sslParameters = _sslParameters; return this;
  }
  @Override
  public Builder feature(Feature<?> feature) {
    if (features == null) features = new HashSet<>();
    features.add(feature); return this;
  }

}
