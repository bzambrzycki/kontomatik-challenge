package pl.zambrzyckib.pko;

import com.google.gson.Gson;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
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
    displayAccountSummaries(accountsSummaries);
  }

  public List<AccountSummary> getAccountSummaries(Credentials credentials) {
    return Stream.of(fetchAccountsInfo(credentials))
        .peek(ignored -> System.out.println("Successfully fetched accounts info"))
        .map(PkoResponseUtils::getAccountSummariesFromResponse)
        .get();
  }

  private Response fetchAccountsInfo(Credentials credentials) {
    return Stream.of(pkoSession.sendLoginRequest(credentials.getLogin()))
        .peek(this::saveSessionId)
        .map(response -> pkoSession.sendPasswordRequest(response, credentials.getPassword()))
        .map(response -> pkoSession.sendAccountsInfoRequest())
        .get();
  }

  private void displayAccountSummaries(List<AccountSummary> accountSummaries) {
    PkoResponseUtils.formatAccountSummaries(accountSummaries).forEach(System.out::println);
  }

  private void saveSessionId(Response response) {
    pkoSession.setSessionId(response.getHeader("X-Session-Id"));
  }
}
