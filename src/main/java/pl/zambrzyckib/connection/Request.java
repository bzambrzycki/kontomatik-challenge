package pl.zambrzyckib.connection;

import java.util.Map;
import lombok.Builder;

@Builder
public class Request {

  public final String url;
  public final String body;
  public final Method method;
  public final Map<String, String> headers;
  public final Map<String, String> cookies;

  public enum Method {
    POST
  }
}
