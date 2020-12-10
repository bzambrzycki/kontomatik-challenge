package pl.zambrzyckib.pko;

import com.google.gson.Gson;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import pl.zambrzyckib.UserInterface;
import pl.zambrzyckib.connection.Response;
import pl.zambrzyckib.exception.SessionIdNotReceived;
import pl.zambrzyckib.model.AccountSummary;
import pl.zambrzyckib.model.Credentials;
import pl.zambrzyckib.pko.response.PkoResponseParser;


public class PkoScraper {

  private final PkoSession pkoSession;
  private final UserInterface userInterface;
  public static final Gson GSON = new Gson();

  public PkoScraper(UserInterface userInterface) {
    this.userInterface = userInterface;
    this.pkoSession = new PkoSession();
  }

  public void getAndDisplayAccountsInfo(Credentials credentials) {
    final List<AccountSummary> accountsSummaries = getAccountSummaries(credentials);
    userInterface.displayAccountSummaries(accountsSummaries);
  }

  private List<AccountSummary> getAccountSummaries(Credentials credentials) {
    return Stream.of(fetchAccountsInfo(credentials))
        .peek(ignored -> userInterface.displaySuccessMessage())
        .map(PkoResponseParser::getAccountSummariesFromResponse)
        .get();
  }

  private Response fetchAccountsInfo(Credentials credentials) {
    return Stream.of(pkoSession.sendLoginRequest(credentials.login))
        .peek(this::saveSessionId)
        .map(response -> pkoSession.sendPasswordRequest(response, credentials.password))
        .map(response -> pkoSession.sendAccountsInfoRequest())
        .get();
  }

  private void saveSessionId(Response response) {
    final String sessionId = response.headers.get("X-Session-Id");
    if (sessionId != null) pkoSession.setSessionId(response.headers.get("X-Session-Id"));
    else throw new SessionIdNotReceived();
  }
}
