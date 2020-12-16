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
        String wrongLoginResponseJson = readStringFromFile("wrongLoginResponseBody.json");
        Response wrongLoginResponse = baseResponseBuilder().body(wrongLoginResponseJson).build();
        LoginResponseBody wrongLoginResponseBody = PkoResponseParser.deserializeLoginResponse(wrongLoginResponse.body);
        assertThrows(
                InvalidCredentials.class, () -> PkoResponseParser.assertLoginCorrect(wrongLoginResponseBody));
    }

    @Test
    public void shouldThrowExceptionWhenPasswordIsIncorrect() {
        String wrongPasswordResponseJson = readStringFromFile("wrongPasswordResponseBody.json");
        Response wrongPasswordResponse = baseResponseBuilder().body(wrongPasswordResponseJson).build();
        PasswordResponseBody wrongPasswordResponseBody = PkoResponseParser.deserializePasswordResponse(wrongPasswordResponse.body);
        assertThrows(
                InvalidCredentials.class,
                () -> PkoResponseParser.assertPasswordCorrect(wrongPasswordResponseBody));
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
