package pl.zambrzyckib.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.vavr.collection.List;

import java.io.File;
import java.net.URL;
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

  @Test
  public void shouldReturnAccountSummaryListFromJson() {
    String accountsInfoResponseBody = readStringFromFile("accountsInfoResponseBody.json");
    List<AccountSummary> expectedList =
        List.of(
            AccountSummary.of("accountOne", "100", "PLN"),
            AccountSummary.of("accountTwo", "200", "PLN"));
    Response expectedListResponse = baseResponseBuilder().body(accountsInfoResponseBody).build();
    assertEquals(expectedList, PkoResponseParser.parseAccountSummaries(expectedListResponse));
  }

  @Test
  public void shouldThrowExceptionWhenLoginIsIncorrect() {
    String wrongLoginResponseBody = readStringFromFile("wrongLoginResponseBody.json");
    Response wrongLoginResponse = baseResponseBuilder().body(wrongLoginResponseBody).build();
    assertThrows(
        InvalidCredentials.class, () -> PkoResponseParser.assertLoginCorrect(wrongLoginResponse));
  }

  @Test
  public void shouldThrowExceptionWhenPasswordIsIncorrect() {
    String wrongPasswordResponseBody = readStringFromFile("wrongPasswordResponseBody.json");
    Response wrongPasswordResponse = baseResponseBuilder().body(wrongPasswordResponseBody).build();
    assertThrows(
        InvalidCredentials.class,
        () -> PkoResponseParser.assertPasswordCorrectAndCheckLoginStatus(wrongPasswordResponse));
  }

  @SneakyThrows
  private static String readStringFromFile(String fileName) {
    URL resourceFileUrl = PkoResponseParser.class.getResource(File.separator + fileName);
    return Files.readString(Path.of(resourceFileUrl.toURI()));
  }

  private Response.ResponseBuilder baseResponseBuilder() {
    return Response.builder().statusCode(200).cookies(Map.of()).headers(Map.of());
  }
}
