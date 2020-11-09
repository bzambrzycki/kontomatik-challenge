package pl.zambrzyckib;

import com.google.gson.Gson;
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
    final String wrongLogin = "test" + "\n";
    final String correctLogin = properties.getProperty("login") + "\n";
    final var wrongLoginResponse = pkoRequestsHandler.sendLoginRequest(wrongLogin);
    final var correctLoginResponse = pkoRequestsHandler.sendLoginRequest(correctLogin);
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
    final String login = properties.getProperty("login") + "\n";
    final String wrongPassword = "test";
    final var loginResponse = pkoRequestsHandler.sendLoginRequest(login);
    pkoSession.addHeader("x-session-id", loginResponse.getHeader("X-Session-Id"));
    Assertions.assertTrue(
        GSON.fromJson(
                pkoRequestsHandler.sendPasswordRequest(loginResponse, wrongPassword).getBody(),
                PasswordResponseBody.class)
            .hasErrors());
  }

  @Test
  void shouldReceiveResponseWithoutErrorsWhenPasswordIsCorrect() {
    final String login = properties.getProperty("login") + "\n";
    final String password = properties.getProperty("password");
    final var loginResponse = pkoRequestsHandler.sendLoginRequest(login);
    pkoSession.addHeader("x-session-id", loginResponse.getHeader("X-Session-Id"));
    Assertions.assertFalse(
        GSON.fromJson(
                pkoRequestsHandler.sendPasswordRequest(loginResponse, password).getBody(),
                PasswordResponseBody.class)
            .hasErrors());
  }
}
