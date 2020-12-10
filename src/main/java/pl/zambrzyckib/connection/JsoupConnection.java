package pl.zambrzyckib.connection;

import lombok.SneakyThrows;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;

import java.util.Map;

public class JsoupConnection implements HttpAgent {

  private final Connection connection;

  public JsoupConnection(String url, Boolean ignoreContentType) {
    this.connection = Jsoup.connect(url);
    this.connection.ignoreContentType(ignoreContentType);
  }

  @Override
  @SneakyThrows
  public Response send(Request request) {
    final Map<String, String> headers = request.headers != null ? request.headers : Map.of();
    final Map<String, String> cookies = request.cookies != null ? request.cookies : Map.of();
    final Connection.Response jsoupResponse =
        connection
            .url(request.baseUrl + request.endpoint)
            .requestBody(request.body)
            .method(Method.valueOf(request.method.toString()))
            .headers(headers)
            .cookies(cookies)
            .execute();
    return Response.of(
        jsoupResponse.body(), jsoupResponse.statusCode(), jsoupResponse.headers(), jsoupResponse.cookies());
  }
}
