package pl.zambrzyckib.integration;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.zambrzyckib.integration.PkoIntegrationTestSpec.properties;

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
        final var login = properties.getProperty("login");
        final var password = properties.getProperty("password");
        assertDoesNotThrow(
                () -> pkoScraper.getAccountSummaries(Credentials.of(login, password)));
    }

    @Test
    void shouldThrowInvalidCredentialsExceptionWhenLoginIsWrong() {
        final var wrongLogin = "1";
        final var password = properties.getProperty("password");
        assertThrows(
                InvalidCredentialsException.class,
                () -> pkoScraper.getAccountSummaries(Credentials.of(wrongLogin, password)));
    }

    @Test
    void shouldThrowInvalidCredentialsExceptionWhenPasswordIsWrong() {
        final var login = properties.getProperty("login");
        final var wrongPassword = "test";
        assertThrows(
                InvalidCredentialsException.class,
                () -> pkoScraper.getAccountSummaries(Credentials.of(login, wrongPassword)));
    }

    @Test
    void shouldLoginToBankAndDisplayAccountsSummary(){
        final var standardOut = System.out;
        final var byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));
        final var login = properties.getProperty("login");
        final var password = properties.getProperty("password");
        pkoScraper.getAndDisplayAccountsInfo(Credentials.of(login, password));
        assertTrue(byteArrayOutputStream.toString().contains("Successfully fetched accounts info"));
        System.setOut(standardOut);
    }
}
