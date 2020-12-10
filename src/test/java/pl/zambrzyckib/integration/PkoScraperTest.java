package pl.zambrzyckib.integration;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.zambrzyckib.integration.PkoIntegrationTestSpec.pkoTestCredentials;
import static pl.zambrzyckib.KontomatikChallengeApp.USER_INTERFACE;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.zambrzyckib.exception.InvalidCredentials;
import pl.zambrzyckib.model.Credentials;
import pl.zambrzyckib.pko.PkoScraper;

public class PkoScraperTest {

  private final PkoScraper pkoScraper = new PkoScraper();

  @BeforeAll
  @SneakyThrows
  static void loadProperties() {
    PkoIntegrationTestSpec.loadCredentialPropertiesIfNotLoaded();
  }

  @Test
  void shouldThrowInvalidCredentialsExceptionWhenLoginIsWrong() {
    Credentials invalidCredentials = Credentials.of("wrong", "anyPassword");
    assertThrows(
        InvalidCredentials.class, () -> pkoScraper.getAccountSummaries(invalidCredentials));
  }

  @Test
  void shouldThrowInvalidCredentialsExceptionWhenPasswordIsWrong() {
    Credentials invalidCredentials = Credentials.of(pkoTestCredentials.login, "wrong");
    assertThrows(
        InvalidCredentials.class, () -> pkoScraper.getAccountSummaries(invalidCredentials));
  }

  @Test
  void shouldNotThrowAnyExceptionWhenCredentialsAreCorrect() {
    assertDoesNotThrow(() -> pkoScraper.getAccountSummaries(pkoTestCredentials));
  }

  @Test
  void shouldLoginToBankAndDisplayAccountsSummary() {
    pkoScraper.getAndDisplayAccountsInfo(pkoTestCredentials);
    assertTrue(USER_INTERFACE.getOutput().contains("Successfully fetched accounts info\n"));
  }
}
