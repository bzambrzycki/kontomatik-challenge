package pl.zambrzyckib.pko;

import io.vavr.control.Option;
import org.json.JSONObject;
import pl.zambrzyckib.KontomatikChallengeApp;
import pl.zambrzyckib.dto.ResponseDTO;

public class PkoSessionHandler {

  private final PkoSession pkoSession = new PkoSession();

  public Option<ResponseDTO> getAccountsInfo() {
    login();
    return fetchAccountsInfo();
  }

  public void login() {
    sendUserLogin()
        .peek(
            responseDTO ->
                PkoResponseUtils.verifyCredentialsResponse(responseDTO.getBody(), "login"))
        .peek(
            responseDTO ->
                pkoSession.addHeader("x-session-id", responseDTO.getHeader("X-Session-Id")))
        .flatMap(this::sendUserPassword)
        .peek(
            responseDTO ->
                PkoResponseUtils.verifyCredentialsResponse(responseDTO.getBody(), "password"));
  }

  private Option<ResponseDTO> sendUserLogin() {
    System.out.println("Podaj login");
    final var userLogin = KontomatikChallengeApp.scanner.nextLine();
    return Option.of(
            pkoSession
                .getBankConnection()
                .send(PkoRequest.userLoginPostRequest(userLogin, pkoSession)))
        .peek(ignored -> System.out.println("Wysłano login użytkownika"));
  }

  private Option<ResponseDTO> sendUserPassword(final ResponseDTO response) {
    System.out.println("Podaj hasło");
    final var password = KontomatikChallengeApp.scanner.nextLine();
    final var responseJson = new JSONObject(response.getBody());
    final var token = responseJson.get("token").toString();
    final var flowId = responseJson.get("flow_id").toString();
    return Option.of(
            pkoSession
                .getBankConnection()
                .send(PkoRequest.userPasswordPostRequest(password, pkoSession, flowId, token)))
        .peek(ignored -> System.out.println("Wysłano hasło"));
  }

  private Option<ResponseDTO> fetchAccountsInfo() {
    return Option.of(
            pkoSession.getBankConnection().send(PkoRequest.accountInfoPostRequest(pkoSession)))
        .peek(ignored -> System.out.println("Pobrano dane o kontach"));
  }
}
