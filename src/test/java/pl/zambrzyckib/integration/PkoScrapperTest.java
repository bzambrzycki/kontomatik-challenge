package pl.zambrzyckib.integration;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static pl.zambrzyckib.integration.PkoIntegrationTestSpec.properties;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.zambrzyckib.exception.InvalidCredentialsException;
import pl.zambrzyckib.model.Credentials;
import pl.zambrzyckib.pko.PkoScrapper;

public class PkoScrapperTest {

  private final PkoScrapper pkoScrapper = new PkoScrapper();

  @BeforeAll
  @SneakyThrows
  static void loadProperties() {
   PkoIntegrationTestSpec.loadCredentialPropertiesIfNotLoaded();
  }

  @Test
  void shouldNotThrowAnyExceptionWhenCredentialsAreCorrect() {
    final String login = properties.getProperty("login") + "\n";
    final String password = properties.getProperty("password");
    assertDoesNotThrow(
        () -> pkoScrapper.getAccountsInfo(Credentials.of(login, password)));
  }

  @Test
  void shouldThrowInvalidCredentialsExceptionWhenLoginIsWrong() {
    final String wrongLogin = "1" + "\n";
    final String password = properties.getProperty("password");
    assertThrows(
        InvalidCredentialsException.class,
        () -> pkoScrapper.getAccountsInfo(Credentials.of(wrongLogin, password)));
  }

  @Test
  void shouldThrowInvalidCredentialsExceptionWhenPasswordIsWrong() {
    final String login = properties.getProperty("login") + "\n";
    final String wrongPassword = "test";
    assertThrows(
        InvalidCredentialsException.class,
        () -> pkoScrapper.getAccountsInfo(Credentials.of(login, wrongPassword)));
  }
}
