package pl.zambrzyckib.pko.request;

import com.google.gson.Gson;
import org.json.JSONObject;
import pl.zambrzyckib.connection.Request;
import pl.zambrzyckib.connection.Request.Method;
import pl.zambrzyckib.connection.Response;
import pl.zambrzyckib.pko.PkoSession;
import pl.zambrzyckib.pko.request.body.AccountsInfoRequestBody;
import pl.zambrzyckib.pko.request.body.SendLoginRequestBody;
import pl.zambrzyckib.pko.request.body.SendPasswordRequestBody;
import pl.zambrzyckib.pko.response.body.AccountsInfoResponseBody;
import pl.zambrzyckib.pko.response.body.SendLoginResponseBody;

public class PkoRequests {

  private static final Gson gson = new Gson();

  static Request userLoginPostRequest(final String login, final PkoSession pkoSession) {
    return Request.builder()
        .url(PkoSession.HOME_URL + PkoSession.LOGIN_ENDPOINT)
        .method(Method.POST)
        .headers(pkoSession.getHeaders())
        .cookies(pkoSession.getCookies())
        .body(gson.toJson(new SendLoginRequestBody(login)))
        .build();
  }

  static Request userPasswordPostRequest(
      final String password, final PkoSession pkoSession, final Response sendLoginResponse) {
    final var sendLoginResponseBody =
        new Gson().fromJson(sendLoginResponse.getBody(), SendLoginResponseBody.class);
    return Request.builder()
        .url(PkoSession.HOME_URL + PkoSession.LOGIN_ENDPOINT)
        .method(Method.POST)
        .headers(pkoSession.getHeaders())
        .cookies(pkoSession.getCookies())
        .body(gson.toJson(new SendPasswordRequestBody(password, sendLoginResponseBody)))
        .build();
  }

  static Request accountsInfoPostRequest(final PkoSession pkoSession) {

    return Request.builder()
        .url(PkoSession.HOME_URL + PkoSession.ACCOUNT_INFO_ENDPOINT)
        .method(Method.POST)
        .headers(pkoSession.getHeaders())
        .cookies(pkoSession.getCookies())
        .body(gson.toJson(new AccountsInfoRequestBody()))
        .build();
  }
}
