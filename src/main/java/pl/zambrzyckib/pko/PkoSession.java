package pl.zambrzyckib.pko;

import io.vavr.control.Option;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import pl.zambrzyckib.BankConnection;
import pl.zambrzyckib.JsoupConnection;
import pl.zambrzyckib.KontomatikChallengeApp;
import pl.zambrzyckib.dto.RequestDTO;
import pl.zambrzyckib.dto.RequestDTO.Method;
import pl.zambrzyckib.dto.ResponseDTO;

public class PkoSession {

  private final String homeUrl = "https://www.ipko.pl/";
  private final String loginEndpoint = "ipko3/login";
  private final String accountInfoEndpoint = "ipko3/init";
  private final BankConnection bankConnection;
  private final Map<String, String> headers;
  private final Map<String, String> cookies;

  public PkoSession() {
    this.bankConnection = new JsoupConnection(homeUrl, true);
    this.headers = new HashMap<>();
    this.cookies = new HashMap<>();
  }

  public void addHeader(String key, String value) {
    headers.put(key, value);
  }

  public Option<ResponseDTO> getAccountsInfo() {
    login();
    return fetchAccountsInfo();
  }

  private void login() {
    sendUserLogin()
        .peek(
            responseDTO ->
                PkoResponseUtils.verifyCredentialsResponse(responseDTO.getBody(), "login"))
        .peek(responseDTO -> addHeader("x-session-id", responseDTO.getHeader("X-Session-Id")))
        .flatMap(this::sendUserPassword)
        .peek(
            responseDTO ->
                PkoResponseUtils.verifyCredentialsResponse(responseDTO.getBody(), "password"));
  }

  private Option<ResponseDTO> sendUserLogin() {
    System.out.println("Podaj login");
    final var userLogin = KontomatikChallengeApp.scanner.nextLine();
    return Option.of(
            bankConnection.send(
                RequestDTO.builder()
                    .url(homeUrl + loginEndpoint)
                    .method(Method.POST)
                    .headers(headers)
                    .cookies(cookies)
                    .body(
                        new JSONObject()
                            .put("action", "submit")
                            .put("data", new JSONObject().put("login", userLogin))
                            .put("state_id", "login")
                            .toString())
                    .build()))
        .peek(ignored -> System.out.println("Wysłano login użytkownika"));
  }

  private Option<ResponseDTO> sendUserPassword(final ResponseDTO response) {
    System.out.println("Podaj hasło");
    final var password = KontomatikChallengeApp.scanner.nextLine();
    final var responseJson = new JSONObject(response.getBody());
    return Option.of(
            bankConnection.send(
                RequestDTO.builder()
                    .url(homeUrl + loginEndpoint)
                    .method(Method.POST)
                    .headers(headers)
                    .cookies(cookies)
                    .body(
                        new JSONObject()
                            .put("action", "submit")
                            .put("data", new JSONObject().put("password", password))
                            .put("flow_id", responseJson.get("flow_id"))
                            .put("state_id", "password")
                            .put("token", responseJson.get("token"))
                            .toString())
                    .build()))
        .peek(ignored -> System.out.println("Wysłano hasło"));
  }

  private Option<ResponseDTO> fetchAccountsInfo() {
    return Option.of(
            bankConnection.send(
                RequestDTO.builder()
                    .url(homeUrl + accountInfoEndpoint)
                    .method(Method.POST)
                    .headers(headers)
                    .cookies(cookies)
                    .body(
                        new JSONObject()
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
