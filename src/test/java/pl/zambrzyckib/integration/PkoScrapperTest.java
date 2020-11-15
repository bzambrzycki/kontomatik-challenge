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
    final var login = properties.getProperty("login");
    final var password = properties.getProperty("password");
    assertDoesNotThrow(
        () -> pkoScrapper.getAccountsInfo(Credentials.of(login, password)));
  }

  @Test
  void shouldThrowInvalidCredentialsExceptionWhenLoginIsWrong() {
    final var wrongLogin = "1" + "\n";
    final var password = properties.getProperty("password");
    assertThrows(
        InvalidCredentialsException.class,
        () -> pkoScrapper.getAccountsInfo(Credentials.of(wrongLogin, password)));
  }

  @Test
  void shouldThrowInvalidCredentialsExceptionWhenPasswordIsWrong() {
    final var login = properties.getProperty("login");
    final var wrongPassword = "test";
    assertThrows(
        InvalidCredentialsException.class,
        () -> pkoScrapper.getAccountsInfo(Credentials.of(login, wrongPassword)));
  }
}