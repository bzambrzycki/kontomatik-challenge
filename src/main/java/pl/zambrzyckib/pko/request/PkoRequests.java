package pl.zambrzyckib.pko.request;

import com.google.gson.Gson;
import pl.zambrzyckib.connection.Request;
import pl.zambrzyckib.connection.Request.Method;
import pl.zambrzyckib.connection.Response;
import pl.zambrzyckib.pko.PkoSession;
import pl.zambrzyckib.pko.request.body.AccountsInfoRequestBody;
import pl.zambrzyckib.pko.request.body.LoginRequestBody;
import pl.zambrzyckib.pko.request.body.PasswordRequestBody;
import pl.zambrzyckib.pko.response.body.LoginResponseBody;

import java.util.Map;

public class PkoRequests {

  private static final Gson GSON = new Gson();

  private static final Request BASE_PKO_POST_REQUEST =
      Request.builder().baseUrl(PkoSession.HOME_URL).method(Method.POST).build();

  public static Request userLoginPostRequest(String login) {
    return BASE_PKO_POST_REQUEST.toBuilder()
        .endpoint(PkoSession.LOGIN_ENDPOINT)
        .body(GSON.toJson(new LoginRequestBody(login)))
        .build();
  }

  public static Request userPasswordPostRequest(
      String password, String sessionId, Response sendLoginResponse) {
    final var loginResponseBody =
        new Gson().fromJson(sendLoginResponse.body, LoginResponseBody.class);
    return BASE_PKO_POST_REQUEST.toBuilder()
        .endpoint(PkoSession.LOGIN_ENDPOINT)
        .headers(Map.of("x-session-id", sessionId))
        .body(GSON.toJson(new PasswordRequestBody(password, loginResponseBody)))
        .build();
  }

  public static Request accountsInfoPostRequest(String sessionId) {

    return BASE_PKO_POST_REQUEST.toBuilder()
        .endpoint(PkoSession.ACCOUNT_INFO_ENDPOINT)
        .headers(Map.of("x-session-id", sessionId))
        .body(GSON.toJson(new AccountsInfoRequestBody()))
        .build();
  }
}
