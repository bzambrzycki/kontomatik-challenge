package pl.zambrzyckib.connection;

import java.util.Map;
import lombok.AllArgsConstructor;

@AllArgsConstructor(staticName = "of")
public class Response {

  public final String body;
  public final Integer statusCode;
  public final Map<String, String> headers;
  public final Map<String, String> cookies;

}
