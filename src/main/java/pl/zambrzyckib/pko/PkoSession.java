package pl.zambrzyckib.pko;

import lombok.Setter;
import pl.zambrzyckib.connection.HttpAgent;
import pl.zambrzyckib.connection.JsoupConnection;
import pl.zambrzyckib.connection.Response;
import pl.zambrzyckib.pko.request.PkoRequests;
import pl.zambrzyckib.pko.response.PkoResponseParser;

public class PkoSession {

  public static final String HOME_URL = "https://www.ipko.pl/";
  public static final String LOGIN_ENDPOINT = "ipko3/login";
  public static final String ACCOUNT_INFO_ENDPOINT = "ipko3/init";

  private final HttpAgent httpAgent;

  @Setter private String sessionId;

  public PkoSession() {
    this.httpAgent = new JsoupConnection(HOME_URL, true);
  }

  Response sendLoginRequest(String login) {
    final Response loginResponse = httpAgent.send(PkoRequests.userLoginPostRequest(login));
    PkoResponseParser.verifyLoginResponse(loginResponse);
    return loginResponse;
  }

  Response sendPasswordRequest(Response sendLoginResponse, String password) {
    final Response passwordResponse =
        httpAgent.send(PkoRequests.userPasswordPostRequest(password, sessionId, sendLoginResponse));
    PkoResponseParser.verifyPasswordResponse(passwordResponse);
    return passwordResponse;
  }

  Response sendAccountsInfoRequest() {
    return httpAgent.send(PkoRequests.accountsInfoPostRequest(sessionId));
  }
}
