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

  public void assertLoginCorrect(Response response) {
    LoginResponseBody loginResponseBody = deserializeLoginResponse(response.body);
    if (checkIsLoginWrong(loginResponseBody)) {
      throw new InvalidCredentials();
    }
  }

  private LoginResponseBody deserializeLoginResponse(String responseBody) {
    return GSON.fromJson(responseBody, LoginResponseBody.class);
  }

  private boolean checkIsLoginWrong(LoginResponseBody loginResponseBody) {
    return loginResponseBody.response.fields.errors != null;
  }

  public boolean assertPasswordCorrectAndCheckLoginStatus(Response response) {
    PasswordResponseBody passwordResponseBody = deserializePasswordResponse(response.body);
    if (checkIsPasswordWrong(passwordResponseBody)) {
      throw new InvalidCredentials();
    }
    return passwordResponseBody.stateId.equals("END");
  }

  private PasswordResponseBody deserializePasswordResponse(String responseBody) {
    return GSON.fromJson(responseBody, PasswordResponseBody.class);
  }

  private boolean checkIsPasswordWrong(PasswordResponseBody passwordResponseBody) {
    return passwordResponseBody.response.fields!= null
        && passwordResponseBody.response.fields.password.errors != null;
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
