package pl.zambrzyckib.pko.response;

import io.vavr.collection.List;

import lombok.experimental.UtilityClass;
import pl.zambrzyckib.connection.Response;
import pl.zambrzyckib.exception.InvalidCredentialsException;
import pl.zambrzyckib.model.AccountSummary;
import pl.zambrzyckib.pko.PkoScraper;
import pl.zambrzyckib.pko.response.body.AccountsInfoResponseBody;
import pl.zambrzyckib.pko.response.body.LoginResponseBody;
import pl.zambrzyckib.pko.response.body.PasswordResponseBody;

@UtilityClass
public class PkoResponseUtils {

    public void verifyLoginResponse(Response response) {
        final var loginResponseBody =
                PkoScraper.GSON.fromJson(response.getBody(), LoginResponseBody.class);
        if (loginResponseBody.hasErrors()) {
            throw new InvalidCredentialsException();
        }
    }

    public void verifyPasswordResponse(Response response) {
        final var passwordResponseBody =
                PkoScraper.GSON.fromJson(response.getBody(), PasswordResponseBody.class);
        if (passwordResponseBody.hasErrors()) {
            throw new InvalidCredentialsException();
        }
    }

    public List<AccountSummary> getAccountSummariesFromResponse(Response response) {
        return PkoScraper.GSON
                .fromJson(response.getBody(), AccountsInfoResponseBody.class)
                .getAccountSummaries();
    }

    public List<String> formatAccountSummaries(List<AccountSummary> accountSummaries) {
        return List.ofAll(accountSummaries).map(accountSummary ->
                "Account: "
                        + accountSummary.getName()
                        + ", balance: "
                        + accountSummary.getBalance()
                        + " "
                        + accountSummary.getCurrency());
    }
}
