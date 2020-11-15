package pl.zambrzyckib.connection;

import io.vavr.control.Option;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class Response {

  private String body;
  private Integer statusCode;
  private Map<String, String> headers;
  private Map<String, String> cookies;

  public String getHeader(String name) {
    return Option.of(headers.get(name)).getOrElse("");
  }

  public String getCookie(String name) {
    return Option.of(cookies.get(name)).getOrElse("");
  }
}
