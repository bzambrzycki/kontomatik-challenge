package pl.zambrzyckib.pko;

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

  private String sessionId;

  public PkoSession() {
    this.httpAgent = new JsoupConnection(HOME_URL, true);
  }

  Response sendLoginRequest(String login) {
    final Response loginResponse = httpAgent.send(PkoRequests.userLoginPostRequest(login));
    PkoResponseParser.assertLoginCorrect(loginResponse);
    saveSessionId(loginResponse);
    return loginResponse;
  }

  Response sendPasswordRequest(Response sendLoginResponse, String password) {
    final Response passwordResponse =
        httpAgent.send(PkoRequests.userPasswordPostRequest(password, sessionId, sendLoginResponse));
    PkoResponseParser.assertPasswordCorrect(passwordResponse);
    return passwordResponse;
  }

  Response sendAccountsInfoRequest() {
    return httpAgent.send(PkoRequests.accountsInfoPostRequest(sessionId));
  }

  private void saveSessionId(Response response) {
    final String sessionId = response.headers.get("X-Session-Id");
    if (sessionId != null) this.sessionId = response.headers.get("X-Session-Id");
    else throw new RuntimeException("SessionID not received");
  }
}
