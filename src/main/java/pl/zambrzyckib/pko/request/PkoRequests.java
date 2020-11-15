package pl.zambrzyckib.pko.request;

import com.google.gson.Gson;
import pl.zambrzyckib.connection.Request;
import pl.zambrzyckib.connection.Request.Method;
import pl.zambrzyckib.connection.Response;
import pl.zambrzyckib.pko.PkoScraper;
import pl.zambrzyckib.pko.PkoSession;
import pl.zambrzyckib.pko.request.body.AccountsInfoRequestBody;
import pl.zambrzyckib.pko.request.body.LoginRequestBody;
import pl.zambrzyckib.pko.request.body.PasswordRequestBody;
import pl.zambrzyckib.pko.response.body.LoginResponseBody;

import java.util.Map;

public class PkoRequests {

  public static Request userLoginPostRequest(String login) {
    return Request.builder()
        .url(PkoSession.HOME_URL + PkoSession.LOGIN_ENDPOINT)
        .method(Method.POST)
        .headers(Map.of())
        .cookies(Map.of())
        .body(PkoScraper.GSON.toJson(new LoginRequestBody(login)))
        .build();
  }

  public static Request userPasswordPostRequest(String password, String sessionId, Response sendLoginResponse) {
    final var loginResponseBody =
        new Gson().fromJson(sendLoginResponse.getBody(), LoginResponseBody.class);
    return Request.builder()
        .url(PkoSession.HOME_URL + PkoSession.LOGIN_ENDPOINT)
        .method(Method.POST)
        .headers(Map.of("x-session-id", sessionId))
        .cookies(Map.of())
        .body(PkoScraper.GSON.toJson(new PasswordRequestBody(password, loginResponseBody)))
        .build();
  }

  public static Request accountsInfoPostRequest(String sessionId) {

    return Request.builder()
        .url(PkoSession.HOME_URL + PkoSession.ACCOUNT_INFO_ENDPOINT)
        .method(Method.POST)
        .headers(Map.of("x-session-id", sessionId))
        .cookies(Map.of())
        .body(PkoScraper.GSON.toJson(new AccountsInfoRequestBody()))
        .build();
  }
}
