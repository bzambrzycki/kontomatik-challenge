package pl.zambrzyckib.pko;

import pl.zambrzyckib.connection.HttpAgent;
import pl.zambrzyckib.connection.JsoupConnection;
import pl.zambrzyckib.connection.Response;
import pl.zambrzyckib.pko.request.PkoRequests;
import pl.zambrzyckib.pko.response.PkoResponseParser;

public class PkoSession {

  private final HttpAgent httpAgent;

  private String sessionId;
  private boolean sessionLoggedIn;

  public PkoSession() {
    String homeUrl = "https://www.ipko.pl/";
    this.httpAgent = new JsoupConnection(homeUrl, true);
  }

  Response sendLoginRequest(String login) {
    Response loginResponse = httpAgent.send(PkoRequests.userLoginPostRequest(login));
    PkoResponseParser.assertLoginCorrect(loginResponse);
    saveSessionId(loginResponse);
    return loginResponse;
  }

  Response sendPasswordRequest(Response sendLoginResponse, String password) {
    Response passwordResponse =
        httpAgent.send(PkoRequests.userPasswordPostRequest(password, sessionId, sendLoginResponse));
    sessionLoggedIn = PkoResponseParser.assertPasswordCorrectAndCheckLoginStatus(passwordResponse);
    return passwordResponse;
  }

  Response fetchAccounts() {
    if (!sessionLoggedIn) throw new RuntimeException("Session not logged in");
    else return httpAgent.send(PkoRequests.accountsInfoPostRequest(sessionId));
  }

  private void saveSessionId(Response response) {
    this.sessionId = response.headers.get("X-Session-Id");
  }
}
