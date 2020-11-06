package pl.zambrzyckib;

import io.vavr.collection.Stream;
import java.io.IOException;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;

public class JsoupConnection {

  public final Connection connection;

  public JsoupConnection(final String url, final Boolean ignoreContentType) {
    this.connection = Jsoup.connect(url);
    this.connection.ignoreContentType(ignoreContentType);
  }

  public ResponseDTO send(final RequestDTO requestDTO) throws IOException {
    return Stream.of(
            connection
                .url(requestDTO.getUrl())
                .requestBody(requestDTO.getBody())
                .method(Method.valueOf(requestDTO.getMethod().toString()))
                .headers(requestDTO.getHeaders())
                .cookies(requestDTO.getCookies())
                .execute())
        .map(response -> ResponseDTO.of(response.body(), response.headers(), response.cookies()))
        .get();
  }
}
