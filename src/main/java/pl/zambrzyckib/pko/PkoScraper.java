package pl.zambrzyckib.pko;

import static pl.zambrzyckib.KontomatikChallengeApp.USER_INTERFACE;

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
  public static final Gson GSON = new Gson();

  public PkoScraper() {
    this.pkoSession = new PkoSession();
  }

  public void getAndDisplayAccountsInfo(Credentials credentials) {
    final List<AccountSummary> accountsSummaries = getAccountSummaries(credentials);
    USER_INTERFACE.displayAccountSummaries(accountsSummaries);
  }

  public List<AccountSummary> getAccountSummaries(Credentials credentials) {
    return Stream.of(fetchAccountsInfo(credentials))
        .peek(ignored -> USER_INTERFACE.displaySuccessMessage())
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
