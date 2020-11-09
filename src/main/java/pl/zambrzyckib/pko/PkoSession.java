package pl.zambrzyckib.pko;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import pl.zambrzyckib.connection.BankConnection;
import pl.zambrzyckib.connection.JsoupConnection;

@Getter
public class PkoSession {

  public static final String HOME_URL = "https://www.ipko.pl/";
  public static final String LOGIN_ENDPOINT = "ipko3/login";
  public static final String ACCOUNT_INFO_ENDPOINT = "ipko3/init";

  private final BankConnection bankConnection;
  private final Map<String, String> headers;
  private final Map<String, String> cookies;

  public PkoSession() {
    this.bankConnection = new JsoupConnection(HOME_URL, true);
    this.headers = new HashMap<>();
    this.cookies = new HashMap<>();
  }

  public void addHeader(String key, String value) {
    headers.put(key, value);
  }
}
