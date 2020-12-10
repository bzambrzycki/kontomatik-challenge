package pl.zambrzyckib.pko.response;

import io.vavr.collection.List;
import io.vavr.control.Try;
import lombok.experimental.UtilityClass;
import pl.zambrzyckib.connection.Response;
import pl.zambrzyckib.exception.InvalidCredentials;
import pl.zambrzyckib.model.AccountSummary;
import pl.zambrzyckib.pko.PkoScraper;
import pl.zambrzyckib.pko.response.body.AccountsInfoResponseBody;
import pl.zambrzyckib.pko.response.body.LoginResponseBody;
import pl.zambrzyckib.pko.response.body.PasswordResponseBody;

@UtilityClass
public class PkoResponseParser {

  public void assertLoginCorrect(Response response) {
    final LoginResponseBody loginResponseBody = deserializeLoginResponse(response.body);
    if (checkIsLoginWrong(loginResponseBody)) {
      throw new InvalidCredentials();
    }
  }

  public boolean assertPasswordCorrectAndCheckLoginStatus(Response response) {
    final PasswordResponseBody passwordResponseBody = deserializePasswordResponse(response.body);
    if (checkIsPasswordWrong(passwordResponseBody)) {
      throw new InvalidCredentials();
    }
    return passwordResponseBody.getState_id().equals("END");
  }

  public List<AccountSummary> getAccountSummariesFromResponse(Response response) {
    final AccountsInfoResponseBody accountsInfoResponseBody =
        deserializeAccountsInfoResponse(response.body);
    return accountsInfoResponseBody
        .getAccountSummaries()
        .map(
            pkoAccountSummary ->
                AccountSummary.of(
                    pkoAccountSummary.name, pkoAccountSummary.balance, pkoAccountSummary.currency));
  }

  private boolean checkIsLoginWrong(LoginResponseBody loginResponseBody) {
    return (Try.of(
            () ->
                !loginResponseBody
                    .getResponse()
                    .getFields()
                    .getLogin()
                    .getErrors()
                    .getHint()
                    .isEmpty())
        .getOrElse(false));
  }

  private boolean checkIsPasswordWrong(PasswordResponseBody passwordResponseBody) {
    return (Try.of(
            () ->
                !passwordResponseBody
                    .getResponse()
                    .getFields()
                    .getPassword()
                    .getErrors()
                    .getHint()
                    .isEmpty())
        .getOrElse(false));
  }

  private LoginResponseBody deserializeLoginResponse(String responseBody) {
    return PkoScraper.GSON.fromJson(responseBody, LoginResponseBody.class);
  }

  private PasswordResponseBody deserializePasswordResponse(String responseBody) {
    return PkoScraper.GSON.fromJson(responseBody, PasswordResponseBody.class);
  }

  private AccountsInfoResponseBody deserializeAccountsInfoResponse(String responseBody) {
    return PkoScraper.GSON.fromJson(responseBody, AccountsInfoResponseBody.class);
  }
}
