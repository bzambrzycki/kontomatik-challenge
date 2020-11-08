package pl.zambrzyckib.pko;

import org.json.JSONObject;
import pl.zambrzyckib.dto.RequestDTO;
import pl.zambrzyckib.dto.RequestDTO.Method;

public class PkoRequests {

  static RequestDTO userLoginPostRequest(final String login, final PkoSession pkoSession) {
    return RequestDTO.builder()
        .url(PkoSession.HOME_URL + PkoSession.LOGIN_ENDPOINT)
        .method(Method.POST)
        .headers(pkoSession.getHeaders())
        .cookies(pkoSession.getCookies())
        .body(
            new JSONObject()
                .put("action", "submit")
                .put("data", new JSONObject().put("login", login))
                .put("state_id", "login")
                .toString())
        .build();
  }

  static RequestDTO userPasswordPostRequest(
      final String password, final PkoSession pkoSession, final String flowId, final String token) {
    return RequestDTO.builder()
        .url(PkoSession.HOME_URL + PkoSession.LOGIN_ENDPOINT)
        .method(Method.POST)
        .headers(pkoSession.getHeaders())
        .cookies(pkoSession.getCookies())
        .body(
            new JSONObject()
                .put("action", "submit")
                .put("data", new JSONObject().put("password", password))
                .put("flow_id", flowId)
                .put("state_id", "password")
                .put("token", token)
                .toString())
        .build();
  }

  static RequestDTO accountsInfoPostRequest(final PkoSession pkoSession) {

    return RequestDTO.builder()
        .url(PkoSession.HOME_URL + PkoSession.ACCOUNT_INFO_ENDPOINT)
        .method(Method.POST)
        .headers(pkoSession.getHeaders())
        .cookies(pkoSession.getCookies())
        .body(
            new JSONObject()
                .put(
                    "data",
                    new JSONObject()
                        .put("account_ids", new JSONObject())
                        .put("accounts", new JSONObject()))
                .toString())
        .build();
  }
}
