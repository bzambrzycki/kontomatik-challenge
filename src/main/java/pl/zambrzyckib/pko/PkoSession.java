package pl.zambrzyckib.pko;

import io.vavr.collection.List;
import pl.zambrzyckib.connection.HttpAgent;
import pl.zambrzyckib.connection.JsoupConnection;
import pl.zambrzyckib.connection.Response;
import pl.zambrzyckib.model.AccountSummary;
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

  void sendPasswordRequestAndVerifyResponse(Response sendLoginResponse, String password) {
    Response passwordResponse =
        httpAgent.send(PkoRequests.userPasswordPostRequest(password, sessionId, sendLoginResponse));
    sessionLoggedIn = PkoResponseParser.assertPasswordCorrectAndCheckLoginStatus(passwordResponse);
  }

  List<AccountSummary> fetchAccounts() {
    if (!sessionLoggedIn) throw new RuntimeException("Session not logged in");
    else {
      Response accountsInfoResponse =
          httpAgent.send(PkoRequests.accountsInfoPostRequest(sessionId));
      return PkoResponseParser.parseAccountSummaries(accountsInfoResponse);
    }
  }

  private void saveSessionId(Response response) {
    this.sessionId = response.headers.get("X-Session-Id");
  }
}
