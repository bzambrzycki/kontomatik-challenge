package pl.zambrzyckib.connection;

import lombok.SneakyThrows;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;

public class JsoupConnection implements HttpAgent {

  private final Connection connection;

  public JsoupConnection(String url, Boolean ignoreContentType) {
    this.connection = Jsoup.connect(url);
    this.connection.ignoreContentType(ignoreContentType);
  }

  @Override
  @SneakyThrows
  public Response send(Request request) {
    Connection.Response jsoupResponse =
        connection
            .url(request.url)
            .requestBody(request.body)
            .method(Method.valueOf(request.method.toString()))
            .headers(request.headers)
            .cookies(request.cookies)
            .execute();
    return Response.of(
        jsoupResponse.body(),
        jsoupResponse.statusCode(),
        jsoupResponse.headers(),
        jsoupResponse.cookies());
  }
}
