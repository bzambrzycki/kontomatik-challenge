package pl.zambrzyckib.pko;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import pl.zambrzyckib.connection.BankConnection;
import pl.zambrzyckib.connection.JsoupConnection;
import pl.zambrzyckib.connection.Response;
import pl.zambrzyckib.pko.request.PkoRequests;

public class PkoSession {

  public static final String HOME_URL = "https://www.ipko.pl/";
  public static final String LOGIN_ENDPOINT = "ipko3/login";
  public static final String ACCOUNT_INFO_ENDPOINT = "ipko3/init";

  private final BankConnection bankConnection;

  @Setter
  private String sessionId;

  public PkoSession() {
    this.bankConnection = new JsoupConnection(HOME_URL, true);
  }

  public Response sendLoginRequest(String login) {
    return bankConnection
            .send(PkoRequests.userLoginPostRequest(login));
  }

  public Response sendPasswordRequest(Response sendLoginResponse, String password) {
    return bankConnection
            .send(PkoRequests.userPasswordPostRequest(password, sessionId, sendLoginResponse));
  }

  public Response sendAccountsInfoRequest() {
    return bankConnection.send(PkoRequests.accountsInfoPostRequest(sessionId));
  }
}
