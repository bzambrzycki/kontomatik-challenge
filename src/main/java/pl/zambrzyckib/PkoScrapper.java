package pl.zambrzyckib;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.json.JSONObject;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

public class PkoScrapper implements BankScrapper {

  private final String homeUrl = "https://www.ipko.pl/";
  private final String loginUrl = "ipko3/login";
  private final String accountInfoUrl = "ipko3/init";

  private Option<Response> postUserLogin() {
    System.out.println("Podaj login");
    final var userLogin = KontomatikChallengeApp.scanner.nextLine();
    final var requestJson = new JSONObject()
        .put("action", "submit")
        .put("data", new JSONObject()
            .put("login", userLogin))
        .put("state_id", "login");
    return Try.of(() ->
        Jsoup.connect(homeUrl + loginUrl)
            .method(Method.POST)
            .ignoreContentType(true)
            .requestBody(requestJson.toString())
            .execute())
        .onFailure(throwable -> System.out.println("[LOG/ERR] " + throwable.getMessage()))
        .toOption()
        .peek(ignored -> System.out.println("Wysłano login użytkownika"));
  }

  private Option<Response> postUserPassword(final Response response) {
    System.out.println("Podaj hasło");
    final var password = KontomatikChallengeApp.scanner.nextLine();
    final var responseJson = new JSONObject(response.body());
    final var requestJson = new JSONObject()
        .put("action", "submit")
        .put("data", new JSONObject()
            .put("password", password))
        .put("flow_id", responseJson.get("flow_id"))
        .put("state_id", "password")
        .put("token", responseJson.get("token"));
    return Try.of(() ->
        Jsoup.connect(homeUrl + loginUrl)
            .method(Method.POST)
            .ignoreContentType(true)
            .header("x-session-id", response.header("X-Session-Id"))
            .requestBody(requestJson.toString())
            .execute())
        .onFailure(throwable -> System.out.println("[LOG/ERR] " + throwable.getMessage()))
        .toOption()
        .peek(ignored -> System.out.println("Wysłano hasło"));


  }

  private Option<Response> postAccountInfo(final Response response) {
    final var requestJson = new JSONObject()
        .put("data", new JSONObject()
            .put("account_ids", new JSONObject())
            .put("accounts", new JSONObject()));
    return Try.of(() ->
        Jsoup.connect(homeUrl + accountInfoUrl)
            .method(Method.POST)
            .ignoreContentType(true)
            .header("x-session-id", response.header("X-Session-Id"))
            .header("content-type", "charset=UTF-16LE")
            .requestBody(requestJson.toString())
            .execute())
        .onFailure(throwable -> System.out.println("[LOG/ERR] " + throwable.getMessage()))
        .toOption();
  }

  public Option<HashMap<String, String>> parseAccountsInfoJson(final JSONObject responseJson) {
    return Try.of(() -> responseJson
        .getJSONObject("response")
        .getJSONObject("data"))
        .map(responseDataJson -> HashMap
            .ofEntries(List.ofAll(responseDataJson.getJSONArray("account_ids").toList())
                .map(accountId -> Map.entry(
                    responseDataJson.getJSONObject("accounts").getJSONObject(accountId.toString())
                        .get("name").toString(),
                    responseDataJson.getJSONObject("accounts").getJSONObject(accountId.toString())
                        .get("balance").toString()))))
        .onFailure(throwable -> System.out.println("[LOG/ERR] " + throwable.getMessage()))
        .toOption();
  }

  @Override
  public Option<HashMap<String, String>> getAccountsInfo() {
    return
        postUserLogin()
            .flatMap(this::postUserPassword)
            .flatMap(this::postAccountInfo)
            .flatMap(response -> parseAccountsInfoJson(new JSONObject(response.body())));
  }
}







