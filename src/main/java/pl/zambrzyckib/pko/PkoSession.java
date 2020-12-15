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

  AuthenticatedPkoSession sendPasswordRequest(Response sendLoginResponse, String password) {
    Response passwordResponse =
        httpAgent.send(PkoRequests.userPasswordPostRequest(password, sessionId, sendLoginResponse));
    PkoResponseParser.assertPasswordCorrect(passwordResponse);
    if (PkoResponseParser.checkIsSessionSignedIn(passwordResponse)) {
      return new AuthenticatedPkoSession();
    } else throw new RuntimeException("Session not signed in");
  }

  private void saveSessionId(Response response) {
    this.sessionId = response.headers.get("X-Session-Id");
  }

  public class AuthenticatedPkoSession {
    List<AccountSummary> fetchAccounts() {
      Response accountsInfoResponse =
          httpAgent.send(PkoRequests.accountsInfoPostRequest(sessionId));
      return PkoResponseParser.parseAccountSummaries(accountsInfoResponse);
    }
  }
}
