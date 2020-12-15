package pl.zambrzyckib.integration;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.zambrzyckib.integration.PkoIntegrationTestSpec.loadCredentials;

import io.vavr.collection.List;
import org.junit.jupiter.api.Test;
import pl.zambrzyckib.UserInterface;
import pl.zambrzyckib.exception.InvalidCredentials;
import pl.zambrzyckib.model.Credentials;
import pl.zambrzyckib.pko.PkoScraper;

public class PkoScraperTest {

  private List<String> output = List.empty();
  private final UserInterface userInterface =
      new UserInterface(string -> output = output.push(string));
  private final PkoScraper pkoScraper = new PkoScraper(userInterface);
  private final Credentials pkoTestCredentials = loadCredentials();

  @Test
  void shouldThrowInvalidCredentialsExceptionWhenLoginIsWrong() {
    Credentials invalidCredentials = Credentials.of("wrong", "anyPassword");
    assertThrows(
        InvalidCredentials.class, () -> pkoScraper.getAndDisplayAccountsInfo(invalidCredentials));
  }

  @Test
  void shouldThrowInvalidCredentialsExceptionWhenPasswordIsWrong() {
    Credentials invalidCredentials = Credentials.of(pkoTestCredentials.login, "wrong");
    assertThrows(
        InvalidCredentials.class, () -> pkoScraper.getAndDisplayAccountsInfo(invalidCredentials));
  }

  @Test
  void shouldLoginToBankAndDisplayAccountsSummary() {
    pkoScraper.getAndDisplayAccountsInfo(pkoTestCredentials);
    assertTrue(output.length() > 0);
  }
}
