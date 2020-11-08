package pl.zambrzyckib.pko;

import io.vavr.control.Option;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import pl.zambrzyckib.BankConnection;
import pl.zambrzyckib.JsoupConnection;

@Getter
public class PkoSession {

  static final String HOME_URL = "https://www.ipko.pl/";
  static final String LOGIN_ENDPOINT = "ipko3/login";
  static final String ACCOUNT_INFO_ENDPOINT = "ipko3/init";

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
