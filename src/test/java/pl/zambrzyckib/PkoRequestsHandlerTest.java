package pl.zambrzyckib;

import com.google.gson.Gson;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.util.Properties;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.zambrzyckib.pko.PkoSession;
import pl.zambrzyckib.pko.request.PkoRequestsHandler;
import pl.zambrzyckib.pko.response.body.LoginResponseBody;
import pl.zambrzyckib.pko.response.body.PasswordResponseBody;

public class PkoRequestsHandlerTest {

  private final PkoSession pkoSession = new PkoSession();
  private final PkoRequestsHandler pkoRequestsHandler = new PkoRequestsHandler(pkoSession);
  private static final Properties properties = new Properties();
  private static final Gson GSON = new Gson();

  @BeforeAll
  @SneakyThrows
  static void loadProperties() {
    properties.load(new FileInputStream("src/test/resources/credentials.properties"));
  }

  @Test
  void shouldReachPkoServerAndReceiveResponseBasedOnProvidedLogin() {
    final String wrongLoginInput = "test" + "\n";
    final String correctLoginInput = properties.getProperty("login") + "\n";
    System.setIn(new ByteArrayInputStream((wrongLoginInput + correctLoginInput).getBytes()));
    final var wrongLoginResponse = pkoRequestsHandler.sendLoginRequest();
    final var correctLoginResponse = pkoRequestsHandler.sendLoginRequest();
    Assertions.assertEquals(
        200,
        wrongLoginResponse
            .getStatusCode()); // Server returns 200 even when provided credentials are incorrect
    Assertions.assertTrue(
        GSON.fromJson(wrongLoginResponse.getBody(), LoginResponseBody.class).hasErrors());
    Assertions.assertFalse(
        GSON.fromJson(correctLoginResponse.getBody(), LoginResponseBody.class).hasErrors());
  }

  @Test
  void shouldReceiveResponseWithErrorsWhenPasswordIsWrong() {
    final String correctLoginInput = properties.getProperty("login") + "\n";
    final String wrongPasswordInput = "test";
    System.setIn(new ByteArrayInputStream((correctLoginInput + wrongPasswordInput).getBytes()));
    final var loginResponse = pkoRequestsHandler.sendLoginRequest();
    pkoSession.addHeader("x-session-id", loginResponse.getHeader("X-Session-Id"));
    Assertions.assertTrue(
        GSON.fromJson(
                pkoRequestsHandler.sendPasswordRequest(loginResponse).getBody(),
                PasswordResponseBody.class)
            .hasErrors());
  }

  @Test
  void shouldReceiveResponseWithoutErrorsWhenPasswordIsCorrect() {
    final String correctLoginInput = properties.getProperty("login") + "\n";
    final String correctPasswordInput = properties.getProperty("password");
    System.setIn(new ByteArrayInputStream((correctLoginInput + correctPasswordInput).getBytes()));
    final var loginResponse = pkoRequestsHandler.sendLoginRequest();
    pkoSession.addHeader("x-session-id", loginResponse.getHeader("X-Session-Id"));
    Assertions.assertFalse(
        GSON.fromJson(
                pkoRequestsHandler.sendPasswordRequest(loginResponse).getBody(),
                PasswordResponseBody.class)
            .hasErrors());
  }
}
