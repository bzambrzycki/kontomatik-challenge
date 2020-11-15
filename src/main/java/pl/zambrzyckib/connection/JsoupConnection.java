package pl.zambrzyckib.connection;

import io.vavr.control.Try;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;

public class JsoupConnection implements BankConnection {

  private final Connection connection;

  public JsoupConnection(final String url, final Boolean ignoreContentType) {
    this.connection = Jsoup.connect(url);
    this.connection.ignoreContentType(ignoreContentType);
  }

  @Override
  public Response send(final Request request) {
    return Try.of(
            () ->
                connection
                    .url(request.getUrl())
                    .requestBody(request.getBody())
                    .method(Method.valueOf(request.getMethod().toString()))
                    .headers(request.getHeaders())
                    .cookies(request.getCookies())
                    .execute())
        .map(
            response ->
                Response.of(
                    response.body(), response.statusCode(), response.headers(), response.cookies()))
        .onFailure(throwable -> System.out.println("[LOG/ERR] " + throwable.getMessage()))
        .get();
  }
}