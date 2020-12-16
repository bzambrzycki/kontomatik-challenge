package pl.zambrzyckib.pko.response;

import com.google.gson.Gson;
import io.vavr.collection.List;
import lombok.experimental.UtilityClass;
import pl.zambrzyckib.connection.Response;
import pl.zambrzyckib.exception.InvalidCredentials;
import pl.zambrzyckib.model.AccountSummary;
import pl.zambrzyckib.pko.response.body.AccountsInfoResponseBody;
import pl.zambrzyckib.pko.response.body.LoginResponseBody;
import pl.zambrzyckib.pko.response.body.PasswordResponseBody;

@UtilityClass
public class PkoResponseParser {

  private final Gson GSON = new Gson();

  public LoginResponseBody deserializeLoginResponse(String responseBody) {
    return GSON.fromJson(responseBody, LoginResponseBody.class);
  }

  public PasswordResponseBody deserializePasswordResponse(String responseBody) {
    return GSON.fromJson(responseBody, PasswordResponseBody.class);
  }

  public void assertLoginCorrect(LoginResponseBody loginResponseBody) {
    if (isLoginWrong(loginResponseBody)) {
      throw new InvalidCredentials();
    }
  }

  private boolean isLoginWrong(LoginResponseBody loginResponseBody) {
    return loginResponseBody.response.fields.errors != null;
  }

  public void assertPasswordCorrect(PasswordResponseBody passwordResponseBody) {
    if (isPasswordWrong(passwordResponseBody)) {
      throw new InvalidCredentials();
    }
  }

  public void assertSignedIn(PasswordResponseBody passwordResponseBody) {
    if (!PkoResponseParser.isSessionSignedIn(passwordResponseBody))
      throw new RuntimeException("Session not signed in");
  }

  private boolean isPasswordWrong(PasswordResponseBody passwordResponseBody) {
    return passwordResponseBody.response.fields != null
        && passwordResponseBody.response.fields.password.errors != null;
  }

  private boolean isSessionSignedIn(PasswordResponseBody passwordResponseBody) {
    return passwordResponseBody.stateId.equals("END");
  }

  public List<AccountSummary> parseAccountSummaries(Response response) {
    AccountsInfoResponseBody accountsInfoResponseBody =
        deserializeAccountsInfoResponse(response.body);
    return accountsInfoResponseBody
        .getAccountSummaries()
        .map(
            pkoAccountSummary ->
                AccountSummary.of(
                    pkoAccountSummary.name, pkoAccountSummary.balance, pkoAccountSummary.currency));
  }

  private AccountsInfoResponseBody deserializeAccountsInfoResponse(String responseBody) {
    return GSON.fromJson(responseBody, AccountsInfoResponseBody.class);
  }
}
