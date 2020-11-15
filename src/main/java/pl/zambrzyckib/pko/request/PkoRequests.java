package pl.zambrzyckib.pko.request;

import com.google.gson.Gson;
import pl.zambrzyckib.connection.Request;
import pl.zambrzyckib.connection.Request.Method;
import pl.zambrzyckib.connection.Response;
import pl.zambrzyckib.pko.PkoScrapper;
import pl.zambrzyckib.pko.PkoSession;
import pl.zambrzyckib.pko.request.body.AccountsInfoRequestBody;
import pl.zambrzyckib.pko.request.body.LoginRequestBody;
import pl.zambrzyckib.pko.request.body.PasswordRequestBody;
import pl.zambrzyckib.pko.response.body.LoginResponseBody;

public class PkoRequests {

  static Request userLoginPostRequest(String login, PkoSession pkoSession) {
    return Request.builder()
        .url(PkoSession.HOME_URL + PkoSession.LOGIN_ENDPOINT)
        .method(Method.POST)
        .headers(pkoSession.getHeaders())
        .cookies(pkoSession.getCookies())
        .body(PkoScrapper.GSON.toJson(new LoginRequestBody(login)))
        .build();
  }

  static Request userPasswordPostRequest(String password, PkoSession pkoSession, Response sendLoginResponse) {
    final var loginResponseBody =
        new Gson().fromJson(sendLoginResponse.getBody(), LoginResponseBody.class);
    return Request.builder()
        .url(PkoSession.HOME_URL + PkoSession.LOGIN_ENDPOINT)
        .method(Method.POST)
        .headers(pkoSession.getHeaders())
        .cookies(pkoSession.getCookies())
        .body(PkoScrapper.GSON.toJson(new PasswordRequestBody(password, loginResponseBody)))
        .build();
  }

  static Request accountsInfoPostRequest(PkoSession pkoSession) {

    return Request.builder()
        .url(PkoSession.HOME_URL + PkoSession.ACCOUNT_INFO_ENDPOINT)
        .method(Method.POST)
        .headers(pkoSession.getHeaders())
        .cookies(pkoSession.getCookies())
        .body(PkoScrapper.GSON.toJson(new AccountsInfoRequestBody()))
        .build();
  }
}
