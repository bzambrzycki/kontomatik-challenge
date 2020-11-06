package pl.zambrzyckib;

import java.io.IOException;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

public class JsoupConnection {

  public final Connection connection;

  public JsoupConnection(final String url, final Boolean ignoreContentType) {
    this.connection = Jsoup.connect(url);
    this.connection.ignoreContentType(ignoreContentType);
  }

  public Response send(final RequestDTO requestDTO) throws IOException {
    return connection
        .url(requestDTO.getUrl())
        .requestBody(requestDTO.getBody())
        .method(Method.valueOf(requestDTO.getMethod().toString()))
        .headers(requestDTO.getHeaders())
        .cookies(requestDTO.getCookies())
        .execute();
  }
}
