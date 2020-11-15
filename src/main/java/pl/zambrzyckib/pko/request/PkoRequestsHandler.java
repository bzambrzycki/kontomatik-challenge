package pl.zambrzyckib.pko.request;

import pl.zambrzyckib.connection.Response;
import pl.zambrzyckib.pko.PkoSession;

public class PkoRequestsHandler {

  private final PkoSession pkoSession;

  public PkoRequestsHandler(PkoSession pkoSession) {
    this.pkoSession = pkoSession;
  }

  public Response sendLoginRequest(String login) {
    return pkoSession
        .getBankConnection()
        .send(PkoRequests.userLoginPostRequest(login, pkoSession));
  }

  public Response sendPasswordRequest(Response sendLoginResponse, String password) {
    return pkoSession
        .getBankConnection()
        .send(PkoRequests.userPasswordPostRequest(password, pkoSession, sendLoginResponse));
  }

  public Response sendAccountsInfoRequest() {
    return pkoSession.getBankConnection().send(PkoRequests.accountsInfoPostRequest(pkoSession));
  }
}
