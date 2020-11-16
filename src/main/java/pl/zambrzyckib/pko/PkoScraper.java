package pl.zambrzyckib.pko;

import com.google.gson.Gson;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import pl.zambrzyckib.UserInterface;
import pl.zambrzyckib.connection.Response;
import pl.zambrzyckib.model.AccountSummary;
import pl.zambrzyckib.model.Credentials;
import pl.zambrzyckib.pko.response.PkoResponseUtils;

public class PkoScraper {

  private final PkoSession pkoSession;
  public static final Gson GSON = new Gson();

  public PkoScraper() {
    this.pkoSession = new PkoSession();
  }

  public void getAndDisplayAccountsInfo(Credentials credentials) {
    final var accountsSummaries = getAccountSummaries(credentials);
    final var formattedAccountSummaries =
        PkoResponseUtils.formatAccountSummaries(accountsSummaries);
    UserInterface.displayAccountSummaries(formattedAccountSummaries);
  }

  public List<AccountSummary> getAccountSummaries(Credentials credentials) {
    return Stream.of(fetchAccountsInfo(credentials))
        .peek(ignored -> UserInterface.displaySuccessMessage())
        .map(PkoResponseUtils::getAccountSummariesFromResponse)
        .get();
  }

  private Response fetchAccountsInfo(Credentials credentials) {
    return Stream.of(pkoSession.sendLoginRequest(credentials.login))
        .peek(this::saveSessionId)
        .map(response -> pkoSession.sendPasswordRequest(response, credentials.password))
        .map(response -> pkoSession.sendAccountsInfoRequest())
        .get();
  }

  private void displayAccountSummaries(List<AccountSummary> accountSummaries) {
    PkoResponseUtils.formatAccountSummaries(accountSummaries).forEach(System.out::println);
  }

  private void saveSessionId(Response response) {
    pkoSession.setSessionId(response.headers.get("X-Session-Id"));
  }
}
