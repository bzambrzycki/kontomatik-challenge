package pl.zambrzyckib.pko;

import com.google.gson.Gson;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import pl.zambrzyckib.connection.Response;
import pl.zambrzyckib.model.AccountSummary;
import pl.zambrzyckib.model.Credentials;
import pl.zambrzyckib.pko.response.PkoResponsesHandler;

public class PkoScraper {

  private final PkoSession pkoSession;
  private final PkoResponsesHandler pkoResponsesHandler;
  public static final Gson GSON = new Gson();

  public PkoScraper() {
    this.pkoSession = new PkoSession();
    this.pkoResponsesHandler = new PkoResponsesHandler();
  }

  public List<String> getAccountsInfo(Credentials credentials) {
    login(credentials);

    return fetchAccountsInfo()
        .map(
            accountSummary ->
                "Account: "
                    + accountSummary.getName()
                    + ", balance: "
                    + accountSummary.getBalance()
                    + " "
                    + accountSummary.getCurrency());
  }

  private void login(Credentials credentials) {
    Stream.of(pkoSession.sendLoginRequest(credentials.getLogin()))
        .peek(this::saveSessionId)
        .map(
            response -> pkoSession.sendPasswordRequest(response, credentials.getPassword()));
  }

  private List<AccountSummary> fetchAccountsInfo() {
    return Stream.of(pkoSession.sendAccountsInfoRequest())
        .map(pkoResponsesHandler::getAccountSummaries)
        .peek(ignored -> System.out.println("Successfully fetched accounts data"))
        .get();
  }

  private void saveSessionId(Response response) {
    pkoSession.setSessionId(response.getHeader("X-Session-Id"));
  }
}
