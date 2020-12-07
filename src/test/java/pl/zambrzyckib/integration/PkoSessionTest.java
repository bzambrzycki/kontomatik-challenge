package pl.zambrzyckib.integration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static pl.zambrzyckib.integration.PkoIntegrationTestSpec.pkoTestCredentials;
import static pl.zambrzyckib.pko.PkoScraper.GSON;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.zambrzyckib.exception.InvalidCredentialsException;
import pl.zambrzyckib.pko.PkoSession;
import pl.zambrzyckib.pko.response.body.LoginResponseBody;
import pl.zambrzyckib.pko.response.body.PasswordResponseBody;

public class PkoSessionTest {

  private final PkoSession pkoSession = new PkoSession();

  @BeforeAll
  @SneakyThrows
  static void loadProperties() {
    PkoIntegrationTestSpec.loadCredentialPropertiesIfNotLoaded();
  }

  @Test
  void whenLoginIsCorrectResponseBodyShouldNotContainErrorInfo() {
    var loginResponse = pkoSession.sendLoginRequest(pkoTestCredentials.login);
    assertFalse(GSON.fromJson(loginResponse.body, LoginResponseBody.class).hasErrors());
  }

  @Test
  void whenPasswordIsCorrectResponseBodyShouldNotContainErrorInfo() {
    var loginResponse = pkoSession.sendLoginRequest(pkoTestCredentials.login);
    pkoSession.setSessionId(loginResponse.headers.get("X-Session-Id"));
    var passwordResponse =
        pkoSession.sendPasswordRequest(loginResponse, pkoTestCredentials.password);
    assertFalse(GSON.fromJson(passwordResponse.body, PasswordResponseBody.class).hasErrors());
  }

  @Test
  void whenPasswordIsWrongExceptionShouldBeThrown() {
    String wrongPassword = "test";
    var loginResponse = pkoSession.sendLoginRequest(pkoTestCredentials.login);
    pkoSession.setSessionId(loginResponse.headers.get("X-Session-Id"));
    assertThrows(
        InvalidCredentialsException.class,
        () -> pkoSession.sendPasswordRequest(loginResponse, wrongPassword));
  }

  @Test
  void whenLoginIsWrongExceptionShouldBeThrown() {
    String wrongLogin = "test";
    assertThrows(InvalidCredentialsException.class, () -> pkoSession.sendLoginRequest(wrongLogin));
  }
}
