package pl.zambrzyckib.pko;

import io.vavr.collection.Stream;
import lombok.Setter;
import pl.zambrzyckib.connection.HttpAgent;
import pl.zambrzyckib.connection.JsoupConnection;
import pl.zambrzyckib.connection.Response;
import pl.zambrzyckib.pko.request.PkoRequests;
import pl.zambrzyckib.pko.response.PkoResponseUtils;

public class PkoSession {

  public static final String HOME_URL = "https://www.ipko.pl/";
  public static final String LOGIN_ENDPOINT = "ipko3/login";
  public static final String ACCOUNT_INFO_ENDPOINT = "ipko3/init";

  private final HttpAgent httpAgent;

  @Setter private String sessionId;

  public PkoSession() {
    this.httpAgent = new JsoupConnection(HOME_URL, true);
  }

  public Response sendLoginRequest(String login) {
    return Stream.of(httpAgent.send(PkoRequests.userLoginPostRequest(login)))
        .peek(PkoResponseUtils::verifyLoginResponse)
        .get();
  }

  public Response sendPasswordRequest(Response sendLoginResponse, String password) {
    return Stream.of(
            httpAgent.send(
                PkoRequests.userPasswordPostRequest(password, sessionId, sendLoginResponse)))
        .peek(PkoResponseUtils::verifyPasswordResponse)
        .get();
  }

  public Response sendAccountsInfoRequest() {
    return httpAgent.send(PkoRequests.accountsInfoPostRequest(sessionId));
  }
}
