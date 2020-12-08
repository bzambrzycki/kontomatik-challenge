package pl.zambrzyckib.connection;

import static pl.zambrzyckib.KontomatikChallengeApp.USER_INTERFACE;

import io.vavr.control.Try;
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
  public Response send(Request request) {
    final Map<String, String> headers = request.headers != null ? request.headers : Map.of();
    final Map<String, String> cookies = request.cookies != null ? request.cookies : Map.of();
    return Try.of(
            () ->
                connection
                    .url(request.baseUrl + request.endpoint)
                    .requestBody(request.body)
                    .method(Method.valueOf(request.method.toString()))
                    .headers(headers)
                    .cookies(cookies)
                    .execute())
        .map(
            response ->
                Response.of(
                    response.body(), response.statusCode(), response.headers(), response.cookies()))
        .onFailure(USER_INTERFACE::logThrowable)
        .get();
  }
}
