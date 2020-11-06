package pl.zambrzyckib;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RequestDTO {

  private String url;
  private String body;
  private Method method;
  private Map<String, String> headers;
  private Map<String, String> cookies;

  public enum Method{
    POST
  }

}
