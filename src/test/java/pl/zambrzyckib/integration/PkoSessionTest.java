package pl.zambrzyckib.integration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static pl.zambrzyckib.pko.PkoScraper.GSON;
import static pl.zambrzyckib.integration.PkoIntegrationTestSpec.properties;

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
    final String login = properties.getProperty("login");
    final var loginResponse = pkoSession.sendLoginRequest(login);
    assertFalse(GSON.fromJson(loginResponse.body, LoginResponseBody.class).hasErrors());
  }

  @Test
  void whenPasswordIsCorrectResponseBodyShouldNotContainErrorInfo() {
    final String login = properties.getProperty("login");
    final String password = properties.getProperty("password");
    final var loginResponse = pkoSession.sendLoginRequest(login);
    pkoSession.setSessionId(loginResponse.headers.get("X-Session-Id"));
    final var passwordResponse = pkoSession.sendPasswordRequest(loginResponse, password);
    assertFalse(GSON.fromJson(passwordResponse.body, PasswordResponseBody.class).hasErrors());
  }

  @Test
  void whenPasswordIsWrongExceptionShouldBeThrown() {
    final String login = properties.getProperty("login");
    final String wrongPassword = "test";
    final var loginResponse = pkoSession.sendLoginRequest(login);
    pkoSession.setSessionId(loginResponse.headers.get("X-Session-Id"));
    assertThrows(
        InvalidCredentialsException.class,
        () -> pkoSession.sendPasswordRequest(loginResponse, wrongPassword));
  }

  @Test
  void whenLoginIsWrongExceptionShouldBeThrown() {
    final String wrongLogin = "test";
    assertThrows(InvalidCredentialsException.class, () -> pkoSession.sendLoginRequest(wrongLogin));
  }
}
