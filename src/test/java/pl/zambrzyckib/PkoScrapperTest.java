package pl.zambrzyckib;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.util.Properties;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.zambrzyckib.exception.InvalidCredentialsException;
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
    final String loginInput = properties.getProperty("login") + "\n";
    final String passwordInput = properties.getProperty("password");
    System.setIn(new ByteArrayInputStream((loginInput + passwordInput).getBytes()));
    Assertions.assertDoesNotThrow(pkoScrapper::getAccountsInfo);
  }

  @Test
  void shouldThrowInvalidCredentialsExceptionWhenLoginIsWrong() {
    final String loginInput = "1" + "\n";
    final String passwordInput = properties.getProperty("password");
    System.setIn(new ByteArrayInputStream((loginInput + passwordInput).getBytes()));
    Assertions.assertThrows(InvalidCredentialsException.class, pkoScrapper::getAccountsInfo);
  }

  @Test
  void shouldThrowInvalidCredentialsExceptionWhenPasswordIsWrong() {
    final String loginInput = properties.getProperty("login") + "\n";
    final String passwordInput = "test";
    System.setIn(new ByteArrayInputStream((loginInput + passwordInput).getBytes()));
    Assertions.assertThrows(InvalidCredentialsException.class, pkoScrapper::getAccountsInfo);
  }
}
