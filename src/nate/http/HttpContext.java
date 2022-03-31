package nate.http;

import java.nio.ByteBuffer;

import java.net.URI;
import java.net.InetSocketAddress;
import javax.net.ssl.SSLSession;

import java.net.http.HttpHeaders;
import java.net.http.HttpClient.Version;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Flow;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

// see javax.xml.ws.spi.http.HttpContext

public interface HttpContext {

  // see javax.xml.ws.spi.http.HttpExchange

  InetSocketAddress getLocalAddress();
  InetSocketAddress getRemoteAddress();

  Optional<SSLSession> sslSession();

  Response.Builder responseBuilder();

  // see java.net.http.WebSocket

  void abort();
  boolean isInputClosed();
  boolean isOutputClosed();
  void request(long n);

  CompletableFuture<HttpContext> sendClose(int statusCode, String reason);
  CompletableFuture<HttpContext> sendResponse(Response response);

  interface Listener {

    default void onOpen(HttpContext session) { session.request(1); }
    default void onError(HttpContext session, Throwable error) {}

    default CompletionStage<?> onClose(HttpContext session, int statusCode, String reason) { return null; }

    CompletionStage<?> onRequest(HttpContext session, Request<?> request);
  }

  interface Builder {

    CompletableFuture<HttpContext> buildAsync(URI uri, HttpContext.Listener listener);

    Builder bodyHandler(BodyHandler<?> bodyHandler);
  }

  // see java.net.http.Http{Request,Response}

  interface Request<T> {

    String method();
    URI uri();
    Version version();
    HttpHeaders headers();
    T body();
  }

  interface BodyHandler<T> {
    BodySubscriber<T> apply(String method, URI uri, HttpHeaders headers);
  }

  interface BodySubscriber<T> extends Flow.Subscriber<List<ByteBuffer>> {

    CompletionStage<T> getBody();

    /* Flow.Subscriber */
    // void onSubscribe(Flow.Subscription subscription);
    // void onNext(ByteBuffer item);
    // void onComplete();
    // void onError(Throwable throwable);

    /* Flow.Subscription */
    // void cancel()
    // void request(long n)
  }

  interface Response {

    Optional<Version> version();
    int statusCode();
    String statusReason();
    HttpHeaders headers();
    Optional<BodyPublisher> bodyPublisher();

    interface Builder {

      Response build();

      Builder version(Version v);
      Builder status(int code, String reason);
      Builder headers(Object...pairs); // key/value pairs
      Builder header(String name, String value);
      Builder setHeader(String name, String value);
      Builder body(BodyPublisher publisher);
    }
  }

  interface BodyPublisher extends Flow.Publisher<ByteBuffer> {

    long contentLength();

    /* Flow.Publisher */
    // void subscribe(Flow.Subscriber<? super ByteBuffer> subscriber)
  }

}
