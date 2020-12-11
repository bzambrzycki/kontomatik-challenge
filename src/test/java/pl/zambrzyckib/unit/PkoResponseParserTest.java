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
import pl.zambrzyckib.exception.InvalidCredentials;
import pl.zambrzyckib.model.AccountSummary;
import pl.zambrzyckib.pko.response.PkoResponseParser;

public class PkoResponseParserTest {

  private final Response.ResponseBuilder basicResponseBuilder =
      Response.builder().statusCode(200).cookies(Map.of()).headers(Map.of());

  @Test
  public void shouldReturnAccountSummaryListFromJson() {
    String accountsInfoResponseBody =
        readStringFromFile("src/test/resources/accountsInfoResponseBody.json");
    List<AccountSummary> expectedList =
        List.of(
            AccountSummary.of("accountOne", "100", "PLN"),
            AccountSummary.of("accountTwo", "200", "PLN"));
    assertEquals(
        expectedList,
        PkoResponseParser.parseAccountSummaries(
            basicResponseBuilder.body(accountsInfoResponseBody).build()));
  }

  @Test
  public void shouldThrowExceptionWhenLoginIsIncorrect() {
    String wrongLoginResponseBody =
        readStringFromFile("src/test/resources/wrongLoginResponseBody.json");
    Response wrongLoginResponse = basicResponseBuilder.body(wrongLoginResponseBody).build();
    assertThrows(
        InvalidCredentials.class, () -> PkoResponseParser.assertLoginCorrect(wrongLoginResponse));
  }

  @Test
  public void shouldThrowExceptionWhenPasswordIsIncorrect() {
    String wrongPasswordResponseBody =
        readStringFromFile("src/test/resources/wrongPasswordResponseBody.json");
    Response wrongPasswordResponse = basicResponseBuilder.body(wrongPasswordResponseBody).build();
    assertThrows(
        InvalidCredentials.class,
        () -> PkoResponseParser.assertPasswordCorrectAndCheckLoginStatus(wrongPasswordResponse));
  }

  @SneakyThrows
  private String readStringFromFile(String uri) {
    return Files.readString(Path.of(uri));
  }
}
