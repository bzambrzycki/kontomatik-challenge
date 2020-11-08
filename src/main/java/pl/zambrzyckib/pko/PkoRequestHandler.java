package pl.zambrzyckib.pko;

import org.json.JSONObject;
import pl.zambrzyckib.KontomatikChallengeApp;
import pl.zambrzyckib.dto.ResponseDTO;

public class PkoRequestHandler {

  private final PkoSession pkoSession;

  public PkoRequestHandler(PkoSession pkoSession) {
    this.pkoSession = pkoSession;
  }

  public ResponseDTO sendUserLoginRequest() {
    System.out.println("Podaj login");
    final var userLogin = KontomatikChallengeApp.scanner.nextLine();
    return pkoSession
        .getBankConnection()
        .send(PkoRequests.userLoginPostRequest(userLogin, pkoSession));
  }

  public ResponseDTO sendUserPasswordRequest(final ResponseDTO postLoginResponse) {
    System.out.println("Podaj hasło");
    final var password = KontomatikChallengeApp.scanner.nextLine();
    final var postLoginResponseJson = new JSONObject(postLoginResponse.getBody());
    final var token = postLoginResponseJson.get("token").toString();
    final var flowId = postLoginResponseJson.get("flow_id").toString();
    return pkoSession
        .getBankConnection()
        .send(PkoRequests.userPasswordPostRequest(password, pkoSession, flowId, token));
  }

  public ResponseDTO sendAccountsInfoRequest() {
    return pkoSession.getBankConnection().send(PkoRequests.accountsInfoPostRequest(pkoSession));
  }
}
