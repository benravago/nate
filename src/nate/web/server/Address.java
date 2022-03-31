package nate.web.server;

import java.net.InetSocketAddress;
import java.net.URI;

class Address {

  final InetSocketAddress inet;
  final URI uri;

  // URI components
  //   [scheme:][//[user:password@]host[:port]][/]path[?query][#fragment]

  Address(URI uri) {
    this.inet = new InetSocketAddress(hostname(uri),port(uri));
    this.uri = resolve(uri);
  }

  // NOTE: caller can check Address#isUnresolved() for hostname validity

  static String hostname(URI uri) {
    var host = uri.getHost();
    return host != null ? host : "localhost";
  }

  static int port(URI uri) {
    var def = supported(uri);
    var port = uri.getPort();
    return port < 0 ? def : port;
  }

  static int supported(URI uri) {
    var scheme = uri.getScheme();
    if (scheme == null) {
      throw new IllegalArgumentException("missing protocol: "+uri);
    }
    return switch (scheme.toLowerCase()) {
      case "http", "ws" -> 80;
      case "https", "wss" -> 443;
      default -> throw new IllegalArgumentException("unsupported protocol: "+uri);
    };
  }

  static URI resolve(URI uri) {
    var p = uri.getPath();
    return (p == null || p.isBlank()) ? uri.resolve("/") : uri;
  }

  static String toString(InetSocketAddress sa, String path) {
    return sa.getHostString() + ':' + sa.getPort() + path;
  }

  @Override
  public String toString() {
    return toString(inet,uri.getPath()); // ip.address:port/uri/path
  }

  private volatile int hashCode;

  @Override
  public int hashCode() {
    return hashCode > 0 ? hashCode : (hashCode = toString().hashCode());
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof Address that ? hashCode() == that.hashCode() : false;
  }

}