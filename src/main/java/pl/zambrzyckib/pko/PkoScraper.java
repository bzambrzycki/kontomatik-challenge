package pl.zambrzyckib.pko;

import io.vavr.collection.List;
import pl.zambrzyckib.UserInterface;
import pl.zambrzyckib.connection.Response;
import pl.zambrzyckib.model.AccountSummary;
import pl.zambrzyckib.model.Credentials;
import pl.zambrzyckib.pko.response.PkoResponseParser;

public class PkoScraper {

  private final PkoSession pkoSession;
  private final UserInterface userInterface;

  public PkoScraper(UserInterface userInterface) {
    this.userInterface = userInterface;
    this.pkoSession = new PkoSession();
  }

  public void getAndDisplayAccountsInfo(Credentials credentials) {
    final List<AccountSummary> accountsSummaries = getAccountSummaries(credentials);
    userInterface.displayAccountSummaries(accountsSummaries);
  }

  private List<AccountSummary> getAccountSummaries(Credentials credentials) {
    final Response accountsResponse = fetchAccountsInfo(credentials);
    userInterface.displaySuccessMessage();
    return PkoResponseParser.getAccountSummariesFromResponse(accountsResponse);
  }

  private Response fetchAccountsInfo(Credentials credentials) {
    login(credentials);
    return pkoSession.fetchAccounts();
  }

  private void login(Credentials credentials) {
    final Response loginResponse = pkoSession.sendLoginRequest(credentials.login);
    pkoSession.sendPasswordRequest(loginResponse, credentials.password);
  }
}
