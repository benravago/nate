package nate.web.server;

import java.util.Set;
import java.util.Optional;
import java.util.concurrent.Executor;

import java.net.Authenticator;
import java.net.CookieHandler;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;

import java.net.http.HttpClient.Version;

import nate.http.HttpServer.Feature;

abstract class Configuration {

  Version version;
  Authenticator authenticator;
  CookieHandler cookieHandler;
  Executor executor;
  Set<Feature<?>> features;
  SSLContext sslContext;
  SSLParameters sslParameters;

  public Version version() { return version; }
  public Executor executor() { return executor; }
  public Set<Feature<?>> features() { return features; }
  public Optional<Authenticator> authenticator() { return Optional.ofNullable(authenticator); }
  public Optional<CookieHandler> cookieHandler() { return Optional.ofNullable(cookieHandler); }
  public Optional<SSLContext> sslContext() { return Optional.ofNullable(sslContext); }
  public Optional<SSLParameters> sslParameters() { return Optional.ofNullable(sslParameters); }

}
