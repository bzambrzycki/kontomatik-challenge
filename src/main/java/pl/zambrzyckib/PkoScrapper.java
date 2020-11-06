package pl.zambrzyckib;

import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.util.Map;
import org.json.JSONObject;
import org.jsoup.Connection.Response;
import pl.zambrzyckib.RequestDTO.Method;

public class PkoScrapper implements BankScrapper {

  private final String homeUrl = "https://www.ipko.pl/";
  private final String loginUrl = "ipko3/login";
  private final String accountInfoUrl = "ipko3/init";
  private final JsoupConnection connection = new JsoupConnection(homeUrl, true);
  private final JSONObject requestJson = new JSONObject();

  @Override
  public Option<List<AccountInfoDTO>> getAccountsInfo() {
    return postUserLogin()
        .peek(response -> PkoResponseHandler.verifyCredentialsResponse(response.body(), "login"))
        .flatMap(this::postUserPassword)
        .peek(response -> PkoResponseHandler.verifyCredentialsResponse(response.body(), "password"))
        .flatMap(this::postAccountInfo)
        .map(response -> PkoResponseHandler.mapAccountsInfoResponse(response.body()));
  }

  private Option<Response> postUserLogin() {
    System.out.println("Podaj login");
    final var userLogin = KontomatikChallengeApp.scanner.nextLine();
    return Try.of(
            () ->
                connection.send(
                    RequestDTO.builder()
                        .url(homeUrl + loginUrl)
                        .method(Method.POST)
                        .headers(Map.of())
                        .cookies(Map.of())
                        .body(
                            requestJson
                                .put("action", "submit")
                                .put("data", new JSONObject().put("login", userLogin))
                                .put("state_id", "login")
                                .toString())
                        .build()))
        .onFailure(throwable -> System.out.println("[LOG/ERR] " + throwable.getMessage()))
        .peek(ignored -> System.out.println("Wysłano login użytkownika"))
        .toOption();
  }

  private Option<Response> postUserPassword(final Response response) {
    System.out.println("Podaj hasło");
    final var password = KontomatikChallengeApp.scanner.nextLine();
    final var responseJson = new JSONObject(response.body());
    return Try.of(
            () ->
                connection.send(
                    RequestDTO.builder()
                        .url(homeUrl + loginUrl)
                        .method(Method.POST)
                        .headers(Map.of("x-session-id", response.header("X-Session-Id")))
                        .cookies(Map.of())
                        .body(
                            requestJson
                                .put("data", new JSONObject().put("password", password))
                                .put("flow_id", responseJson.get("flow_id"))
                                .put("state_id", "password")
                                .put("token", responseJson.get("token"))
                                .toString())
                        .build()))
        .onFailure(throwable -> System.out.println("[LOG/ERR] " + throwable.getMessage()))
        .toOption()
        .peek(ignored -> System.out.println("Wysłano hasło"));
  }

  private Option<Response> postAccountInfo(final Response response) {
    return Try.of(
            () ->
                connection.send(
                    RequestDTO.builder()
                        .url(homeUrl + accountInfoUrl)
                        .method(Method.POST)
                        .headers(Map.of())
                        .cookies(Map.of())
                        .body(
                            requestJson
                                .put(
                                    "data",
                                    new JSONObject()
                                        .put("account_ids", new JSONObject())
                                        .put("accounts", new JSONObject()))
                                .toString())
                        .build()))
        .onFailure(throwable -> System.out.println("[LOG/ERR] " + throwable.getMessage()))
        .toOption()
        .peek(ignored -> System.out.println("Pobrano dane o kontach"));
  }
}
