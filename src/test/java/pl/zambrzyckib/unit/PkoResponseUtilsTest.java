package pl.zambrzyckib.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.vavr.collection.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import pl.zambrzyckib.connection.Response;
import pl.zambrzyckib.exception.InvalidCredentialsException;
import pl.zambrzyckib.model.AccountSummary;
import pl.zambrzyckib.pko.response.PkoResponseUtils;

public class PkoResponseUtilsTest {

  @Test
  @SneakyThrows
  public void shouldReturnAccountSummaryListFromJson() {
    final var accountsInfoResponseBody =
        Files.readString(Path.of("src/test/resources/accountsInfoResponseBody.json"));
    final var expectedList =
        List.of(AccountSummary.of("accountOne", "100", "PLN"), AccountSummary.of("accountTwo", "200", "PLN"));
    assertEquals(expectedList, PkoResponseUtils
            .getAccountSummariesFromResponse(Response.of(accountsInfoResponseBody, 200, Map.of(), Map.of())));
  }

  @Test
  @SneakyThrows
  public void shouldThrowExceptionWhenCredentialsAreIncorrect() {
    final var wrongLoginResponseBody =
        Files.readString(Path.of("src/test/resources/wrongLoginResponseBody.json"));
    assertThrows(
        InvalidCredentialsException.class,
        () ->
                PkoResponseUtils.verifyLoginResponse(
                Response.of(wrongLoginResponseBody, 200, Map.of(), Map.of())));
    final var wrongPasswordResponseBody =
        Files.readString(Path.of("src/test/resources/wrongPasswordResponseBody.json"));
    assertThrows(
        InvalidCredentialsException.class,
        () ->
                PkoResponseUtils.verifyPasswordResponse(
                Response.of(wrongPasswordResponseBody, 200, Map.of(), Map.of())));
  }
}
