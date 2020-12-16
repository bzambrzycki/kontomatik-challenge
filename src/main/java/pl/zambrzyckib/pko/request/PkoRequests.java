package pl.zambrzyckib.pko.request;

import com.google.gson.Gson;
import lombok.experimental.UtilityClass;
import pl.zambrzyckib.connection.Request;
import pl.zambrzyckib.connection.Request.Method;
import pl.zambrzyckib.connection.Response;
import pl.zambrzyckib.pko.request.body.AccountsInfoRequestBody;
import pl.zambrzyckib.pko.request.body.LoginRequestBody;
import pl.zambrzyckib.pko.request.body.PasswordRequestBody;
import pl.zambrzyckib.pko.response.body.LoginResponseBody;

import java.util.Map;

@UtilityClass
public class PkoRequests {

  private static final String HOME_URL = "https://www.ipko.pl/";
  private static final String LOGIN_ENDPOINT = "ipko3/login";
  private static final String ACCOUNT_INFO_ENDPOINT = "ipko3/init";

  private final Gson GSON = new Gson();

  public Request userLoginPostRequest(String login) {
    return pkoRequestBuilder(LOGIN_ENDPOINT).body(GSON.toJson(new LoginRequestBody(login))).build();
  }

  public Request userPasswordPostRequest(
      String password, String sessionId, Response sendLoginResponse) {
    LoginResponseBody loginResponseBody =
        new Gson().fromJson(sendLoginResponse.body, LoginResponseBody.class);
    return pkoRequestBuilder(LOGIN_ENDPOINT)
        .headers(Map.of("x-session-id", sessionId))
        .body(GSON.toJson(new PasswordRequestBody(password, loginResponseBody)))
        .build();
  }

  public Request accountsInfoPostRequest(String sessionId) {
    return pkoRequestBuilder(ACCOUNT_INFO_ENDPOINT)
        .headers(Map.of("x-session-id", sessionId))
        .body(GSON.toJson(new AccountsInfoRequestBody()))
        .build();
  }

  private Request.RequestBuilder pkoRequestBuilder(String endpoint) {
    return Request.builder()
        .url(HOME_URL + endpoint)
        .method(Method.POST)
        .headers(Map.of())
        .cookies(Map.of());
  }
}
