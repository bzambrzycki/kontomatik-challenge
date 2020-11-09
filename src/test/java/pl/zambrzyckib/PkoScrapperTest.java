package pl.zambrzyckib;

import java.io.FileInputStream;
import java.util.Properties;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.zambrzyckib.exception.InvalidCredentialsException;
import pl.zambrzyckib.model.Credentials;
import pl.zambrzyckib.pko.PkoScrapper;

public class PkoScrapperTest {

  private final PkoScrapper pkoScrapper = new PkoScrapper();
  private static final Properties properties = new Properties();

  @BeforeAll
  @SneakyThrows
  static void loadProperties() {
    properties.load(new FileInputStream("src/test/resources/credentials.properties"));
  }

  @Test
  void shouldNotThrowAnyExceptionWhenCredentialsAreCorrect() {
    final String login = properties.getProperty("login") + "\n";
    final String password = properties.getProperty("password");
    Assertions.assertDoesNotThrow(
        () -> pkoScrapper.getAccountsInfo(Credentials.of(login, password)));
  }

  @Test
  void shouldThrowInvalidCredentialsExceptionWhenLoginIsWrong() {
    final String wrongLogin = "1" + "\n";
    final String password = properties.getProperty("password");
    Assertions.assertThrows(
        InvalidCredentialsException.class,
        () -> pkoScrapper.getAccountsInfo(Credentials.of(wrongLogin, password)));
  }

  @Test
  void shouldThrowInvalidCredentialsExceptionWhenPasswordIsWrong() {
    final String login = properties.getProperty("login") + "\n";
    final String wrongPassword = "test";
    Assertions.assertThrows(
        InvalidCredentialsException.class,
        () -> pkoScrapper.getAccountsInfo(Credentials.of(login, wrongPassword)));
  }
}
