package pl.zambrzyckib.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static pl.zambrzyckib.integration.PkoIntegrationTestSpec.properties;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.zambrzyckib.pko.PkoSession;
import pl.zambrzyckib.pko.response.body.LoginResponseBody;
import pl.zambrzyckib.pko.response.body.PasswordResponseBody;

public class PkoSessionTest {

  private final PkoSession pkoSession = new PkoSession();
  private static final Gson GSON = new Gson();

  @BeforeAll
  @SneakyThrows
  static void loadProperties() {
    PkoIntegrationTestSpec.loadCredentialPropertiesIfNotLoaded();
  }

  @Test
  void shoudlReturnCodeOkAndBodyWithErrorsWhenLoginIsWrong(){
    final var wrongLogin = "test";
    final var wrongLoginResponse = pkoSession.sendLoginRequest(wrongLogin);
    assertEquals(
            200,
            wrongLoginResponse
                    .getStatusCode()); // Server returns 200 even when provided credentials are incorrect
    assertTrue(
            GSON.fromJson(wrongLoginResponse.getBody(), LoginResponseBody.class).hasErrors());
  }

  @Test
  void shoudlReturnBodyWithoutErrorsWhenLoginIsCorrect(){
    final var correctLogin = properties.getProperty("login");
    final var correctLoginResponse = pkoSession.sendLoginRequest(correctLogin);
    assertFalse(
            GSON.fromJson(correctLoginResponse.getBody(), LoginResponseBody.class).hasErrors());
  }

  @Test
  void shouldReceiveResponseWithErrorsWhenPasswordIsWrong() {
    final var login = properties.getProperty("login");
    final var wrongPassword = "test";
    final var loginResponse = pkoSession.sendLoginRequest(login);
    pkoSession.addHeader("x-session-id", loginResponse.getHeader("X-Session-Id"));
    assertTrue(
        GSON.fromJson(
                pkoSession.sendPasswordRequest(loginResponse, wrongPassword).getBody(),
                PasswordResponseBody.class)
            .hasErrors());
  }

  @Test
  void shouldReceiveResponseWithoutErrorsWhenPasswordIsCorrect() {
    final var login = properties.getProperty("login");
    final var password = properties.getProperty("password");
    final var loginResponse = pkoSession.sendLoginRequest(login);
    pkoSession.addHeader("x-session-id", loginResponse.getHeader("X-Session-Id"));
    assertFalse(
        GSON.fromJson(
                pkoSession.sendPasswordRequest(loginResponse, password).getBody(),
                PasswordResponseBody.class)
            .hasErrors());
  }
}
