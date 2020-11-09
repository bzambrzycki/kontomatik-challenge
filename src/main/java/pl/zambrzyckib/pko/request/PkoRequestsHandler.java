package pl.zambrzyckib.pko.request;

import pl.zambrzyckib.KontomatikChallengeApp;
import pl.zambrzyckib.connection.Response;
import pl.zambrzyckib.pko.PkoSession;

public class PkoRequestsHandler {

  private final PkoSession pkoSession;

  public PkoRequestsHandler(PkoSession pkoSession) {
    this.pkoSession = pkoSession;
  }

  public Response sendLoginRequest() {
    System.out.println("Podaj login");
    final var userLogin = KontomatikChallengeApp.scanner.nextLine();
    return pkoSession
        .getBankConnection()
        .send(PkoRequests.userLoginPostRequest(userLogin, pkoSession));
  }

  public Response sendPasswordRequest(final Response sendLoginResponse) {
    System.out.println("Podaj hasło");
    final var password = KontomatikChallengeApp.scanner.nextLine();
    return pkoSession
        .getBankConnection()
        .send(PkoRequests.userPasswordPostRequest(password, pkoSession, sendLoginResponse));
  }

  public Response sendAccountsInfoRequest() {
    return pkoSession.getBankConnection().send(PkoRequests.accountsInfoPostRequest(pkoSession));
  }
}
