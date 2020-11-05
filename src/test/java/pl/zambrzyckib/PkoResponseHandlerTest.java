package pl.zambrzyckib;

import io.vavr.collection.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PkoResponseHandlerTest {

  @Test
  public void shouldReturnNameBalanceMapFromJson() {
    final var accountsInfoResponseBody =
        new JSONObject()
            .put("response", new JSONObject().put("data", new JSONObject()
                .put("account_ids", new JSONArray().put("abc").put("def"))
                .put("accounts", new JSONObject()
                    .put("abc", new JSONObject()
                        .put("name", "accountOne")
                        .put("balance", "100")
                    ).put("def", new JSONObject()
                        .put("name", "accountTwo")
                        .put("balance", "200")
                    ))
            )).toString();
    final var expectedList = List.of(
        AccountInfoDTO.of("accountOne", "100"),
        AccountInfoDTO.of("accountTwo", "200"));
    assert PkoResponseHandler.mapAccountsInfoResponse(accountsInfoResponseBody)
        .equals(expectedList);
  }

  @Test
  public void shouldThrowExceptionWhenCredentialsAreIncorrect() {
    final var wrongLoginResponseBody =
        new JSONObject()
            .put("response", new JSONObject()
                .put("fields", new JSONObject()
                    .put("login", new JSONObject()
                        .put("value", "null")
                        .put("errors", new JSONObject()
                            .put("hint", "Wpisz poprawny numer klienta lub login"))))).toString();
    final var wrongPasswordResponseBody =
        new JSONObject()
            .put("response", new JSONObject()
                .put("fields", new JSONObject()
                    .put("password", new JSONObject()
                        .put("value", "null")
                        .put("errors", new JSONObject()
                            .put("hint", "\"Wpisz poprawne has\\u0142o\""))))).toString();
    Assertions.assertThrows(InvalidCredentialsException.class,
        () -> PkoResponseHandler
            .verifyCredentialsResponse(wrongLoginResponseBody, "login"));
    Assertions.assertThrows(InvalidCredentialsException.class,
        () -> PkoResponseHandler
            .verifyCredentialsResponse(wrongPasswordResponseBody, "password"));
  }

}
