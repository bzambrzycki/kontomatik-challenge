package pl.zambrzyckib.connection;

import io.vavr.control.Try;
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
  public Response send(Request request) {
    return Try.of(
            () ->
                connection
                    .url(request.url)
                    .requestBody(request.body)
                    .method(Method.valueOf(request.method.toString()))
                    .headers(request.headers)
                    .cookies(request.cookies)
                    .execute())
        .map(
            response ->
                Response.of(
                    response.body(), response.statusCode(), response.headers(), response.cookies()))
        .onFailure(throwable -> System.out.println("[LOG/ERR] " + throwable.getMessage()))
        .get();
  }
}
