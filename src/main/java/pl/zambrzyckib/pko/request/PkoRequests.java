package pl.zambrzyckib.pko.request;

import com.google.gson.Gson;
import lombok.experimental.UtilityClass;
import pl.zambrzyckib.connection.Request;
import pl.zambrzyckib.connection.Request.Method;
import pl.zambrzyckib.connection.Response;
import pl.zambrzyckib.pko.PkoSession;
import pl.zambrzyckib.pko.request.body.AccountsInfoRequestBody;
import pl.zambrzyckib.pko.request.body.LoginRequestBody;
import pl.zambrzyckib.pko.request.body.PasswordRequestBody;
import pl.zambrzyckib.pko.response.body.LoginResponseBody;

import java.util.Map;

@UtilityClass
public class PkoRequests {

  private final Gson GSON = new Gson();

  public Request userLoginPostRequest(String login) {
    return pkoRequestBuilder()
        .endpoint(PkoSession.LOGIN_ENDPOINT)
        .body(GSON.toJson(new LoginRequestBody(login)))
        .build();
  }

  public Request userPasswordPostRequest(
      String password, String sessionId, Response sendLoginResponse) {
    LoginResponseBody loginResponseBody =
        new Gson().fromJson(sendLoginResponse.body, LoginResponseBody.class);
    return pkoRequestBuilder()
        .endpoint(PkoSession.LOGIN_ENDPOINT)
        .headers(Map.of("x-session-id", sessionId))
        .body(GSON.toJson(new PasswordRequestBody(password, loginResponseBody)))
        .build();
  }

  public Request accountsInfoPostRequest(String sessionId) {
    return pkoRequestBuilder()
        .endpoint(PkoSession.ACCOUNT_INFO_ENDPOINT)
        .headers(Map.of("x-session-id", sessionId))
        .body(GSON.toJson(new AccountsInfoRequestBody()))
        .build();
  }

  private Request.RequestBuilder pkoRequestBuilder() {
    return Request.builder()
        .baseUrl(PkoSession.HOME_URL)
        .method(Method.POST)
        .headers(Map.of())
        .cookies(Map.of());
  }
}
