package pl.zambrzyckib.pko.response;

import io.vavr.collection.List;

import pl.zambrzyckib.connection.Response;
import pl.zambrzyckib.exception.InvalidCredentialsException;
import pl.zambrzyckib.model.AccountSummary;
import pl.zambrzyckib.pko.PkoScrapper;
import pl.zambrzyckib.pko.response.body.AccountsInfoResponseBody;
import pl.zambrzyckib.pko.response.body.SendLoginResponseBody;
import pl.zambrzyckib.pko.response.body.SendPasswordResponseBody;

public class PkoResponsesHandler {

  public void verifyLoginResponse(final Response response) {
    final var sendLoginResponseBody =
        PkoScrapper.GSON.fromJson(response.getBody(), SendLoginResponseBody.class);
    if (sendLoginResponseBody.hasErrors()) {
      throw new InvalidCredentialsException();
    }
  }

  public void verifyPasswordResponse(final Response response) {
    final var sendPasswordResponseBody =
        PkoScrapper.GSON.fromJson(response.getBody(), SendPasswordResponseBody.class);
    if (sendPasswordResponseBody.hasErrors()) {
      throw new InvalidCredentialsException();
    }
  }

  public List<AccountSummary> mapAccountsInfoResponse(final Response response) {
    return PkoScrapper.GSON
        .fromJson(response.getBody(), AccountsInfoResponseBody.class)
        .getAccountSummaries();
  }
}
