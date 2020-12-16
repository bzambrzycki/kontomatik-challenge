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
import pl.zambrzyckib.pko.response.body.LoginResponseBody;
import pl.zambrzyckib.pko.response.body.PasswordResponseBody;

public class PkoResponseParserTest {

  @Test
  public void shouldReturnAccountSummaryListFromJson() {
    List<AccountSummary> expectedList =
        List.of(
            AccountSummary.of("accountOne", "100", "PLN"),
            AccountSummary.of("accountTwo", "200", "PLN"));
    Response expectedListResponse = loadResponseFromFile("accountsInfoResponseBody.json");
    assertEquals(expectedList, PkoResponseParser.parseAccountSummaries(expectedListResponse));
  }

  @Test
  public void shouldThrowExceptionWhenLoginIsIncorrect() {
    LoginResponseBody wrongLoginResponseBody =
        PkoResponseParser.deserializeLoginResponse(readStringFromFile("wrongLoginResponseBody.json"));
    assertThrows(
        InvalidCredentials.class,
        () -> PkoResponseParser.assertLoginCorrect(wrongLoginResponseBody));
  }

  @Test
  public void shouldThrowExceptionWhenPasswordIsIncorrect() {
    PasswordResponseBody wrongPasswordResponseBody =
        PkoResponseParser.deserializePasswordResponse(readStringFromFile("wrongPasswordResponseBody.json"));
    assertThrows(
        InvalidCredentials.class,
        () -> PkoResponseParser.assertPasswordCorrect(wrongPasswordResponseBody));
  }

  @SneakyThrows
  private static String readStringFromFile(String fileName) {
    URL resourceFileUrl = PkoResponseParser.class.getResource(File.separator + fileName);
    return Files.readString(Path.of(resourceFileUrl.toURI()));
  }

  private static Response loadResponseFromFile(String fileName) {
    String responseJson = readStringFromFile(fileName);
    return Response.builder().statusCode(200).body(responseJson).cookies(Map.of()).headers(Map.of()).build();
  }

}
