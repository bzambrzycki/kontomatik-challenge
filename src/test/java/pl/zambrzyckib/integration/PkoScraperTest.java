package pl.zambrzyckib.integration;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.zambrzyckib.integration.PkoIntegrationTestSpec.pkoTestCredentials;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.zambrzyckib.exception.InvalidCredentialsException;
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
  void shouldNotThrowAnyExceptionWhenCredentialsAreCorrect() {
    assertDoesNotThrow(() -> pkoScraper.getAccountSummaries(pkoTestCredentials));
  }

  @Test
  void shouldThrowInvalidCredentialsExceptionWhenLoginIsWrong() {
    var wrongLogin = "test";
    assertThrows(
        InvalidCredentialsException.class,
        () ->
            pkoScraper.getAccountSummaries(
                Credentials.of(wrongLogin, pkoTestCredentials.password)));
  }

  @Test
  void shouldThrowInvalidCredentialsExceptionWhenPasswordIsWrong() {
    var wrongPassword = "test";
    assertThrows(
        InvalidCredentialsException.class,
        () ->
            pkoScraper.getAccountSummaries(
                Credentials.of(pkoTestCredentials.login, wrongPassword)));
  }

  @Test
  void shouldLoginToBankAndDisplayAccountsSummary() {
    var standardOut = System.out;
    var byteArrayOutputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(byteArrayOutputStream));
    pkoScraper.getAndDisplayAccountsInfo(pkoTestCredentials);
    assertTrue(byteArrayOutputStream.toString().contains("Successfully fetched accounts info"));
    System.setOut(standardOut);
  }
}
