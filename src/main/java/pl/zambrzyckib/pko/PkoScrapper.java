package pl.zambrzyckib.pko;

import io.vavr.collection.List;
import io.vavr.control.Option;
import java.util.Map;
import org.json.JSONObject;
import pl.zambrzyckib.dto.AccountInfoDTO;
import pl.zambrzyckib.BankConnection;
import pl.zambrzyckib.BankScrapper;
import pl.zambrzyckib.JsoupConnection;
import pl.zambrzyckib.KontomatikChallengeApp;
import pl.zambrzyckib.dto.RequestDTO;
import pl.zambrzyckib.dto.RequestDTO.Method;
import pl.zambrzyckib.dto.ResponseDTO;

public class PkoScrapper implements BankScrapper {

  private final String homeUrl = "https://www.ipko.pl/";
  private final String loginEndpoint = "ipko3/login";
  private final String accountInfoEndpoint = "ipko3/init";
  private final BankConnection bankConnection = new JsoupConnection(homeUrl, true);
  private final JSONObject requestJson = new JSONObject();
  private final RequestDTO genericRequest =
      RequestDTO.builder()
          .url(homeUrl + loginEndpoint)
          .method(Method.POST)
          .headers(Map.of())
          .cookies(Map.of())
          .build();

  @Override
  public Option<List<AccountInfoDTO>> getAccountsInfo() {
    return postUserLogin()
        .peek(response -> PkoResponseHandler.verifyCredentialsResponse(response.getBody(), "login"))
        .flatMap(this::postUserPassword)
        .peek(
            response ->
                PkoResponseHandler.verifyCredentialsResponse(response.getBody(), "password"))
        .flatMap(this::postAccountInfo)
        .map(response -> PkoResponseHandler.mapAccountsInfoResponse(response.getBody()));
  }

  private Option<ResponseDTO> postUserLogin() {
    System.out.println("Podaj login");
    final var userLogin = KontomatikChallengeApp.scanner.nextLine();
    return Option.of(
            bankConnection.send(
                genericRequest.toBuilder()
                    .body(
                        requestJson
                            .put("action", "submit")
                            .put("data", new JSONObject().put("login", userLogin))
                            .put("state_id", "login")
                            .toString())
                    .build()))
        .peek(ignored -> System.out.println("Wysłano login użytkownika"));
  }

  private Option<ResponseDTO> postUserPassword(final ResponseDTO response) {
    System.out.println("Podaj hasło");
    final var password = KontomatikChallengeApp.scanner.nextLine();
    final var responseJson = new JSONObject(response.getBody());
    return Option.of(
            bankConnection.send(
                genericRequest.toBuilder()
                    .headers(Map.of("x-session-id", response.getHeader("X-Session-Id")))
                    .body(
                        requestJson
                            .put("data", new JSONObject().put("password", password))
                            .put("flow_id", responseJson.get("flow_id"))
                            .put("state_id", "password")
                            .put("token", responseJson.get("token"))
                            .toString())
                    .build()))
        .peek(ignored -> System.out.println("Wysłano hasło"));
  }

  private Option<ResponseDTO> postAccountInfo(final ResponseDTO response) {
    return Option.of(
            bankConnection.send(
                genericRequest.toBuilder()
                    .url(homeUrl + accountInfoEndpoint)
                    .body(
                        requestJson
                            .put(
                                "data",
                                new JSONObject()
                                    .put("account_ids", new JSONObject())
                                    .put("accounts", new JSONObject()))
                            .toString())
                    .build()))
        .peek(ignored -> System.out.println("Pobrano dane o kontach"));
  }
}
