package pl.zambrzyckib.pko;

import com.google.gson.Gson;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import pl.zambrzyckib.connection.Response;
import pl.zambrzyckib.model.AccountSummary;
import pl.zambrzyckib.model.Credentials;
import pl.zambrzyckib.pko.request.PkoRequestsHandler;
import pl.zambrzyckib.pko.response.PkoResponsesHandler;

public class PkoScrapper {

  private final PkoSession pkoSession;
  private final PkoRequestsHandler pkoRequestsHandler;
  private final PkoResponsesHandler pkoResponsesHandler;
  public static final Gson GSON = new Gson();

  public PkoScrapper() {
    this.pkoSession = new PkoSession();
    this.pkoRequestsHandler = new PkoRequestsHandler(pkoSession);
    this.pkoResponsesHandler = new PkoResponsesHandler();
  }

  public List<String> getAccountsInfo(final Credentials credentials) {
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

  private void login(final Credentials credentials) {
    Stream.of(pkoRequestsHandler.sendLoginRequest(credentials.getLogin()))
        .peek(ignored -> System.out.println("Login sent"))
        .peek(pkoResponsesHandler::verifyLoginResponse)
        .peek(this::saveSessionId)
        .map(
            response -> pkoRequestsHandler.sendPasswordRequest(response, credentials.getPassword()))
        .peek(ignored -> System.out.println("Password sent"))
        .peek(pkoResponsesHandler::verifyPasswordResponse)
        .peek(ignored -> System.out.println("Logged in successfully"));
  }

  private List<AccountSummary> fetchAccountsInfo() {
    return Stream.of(pkoRequestsHandler.sendAccountsInfoRequest())
        .map(pkoResponsesHandler::getAccountSummaries)
        .peek(ignored -> System.out.println("Fetched accounts data"))
        .get();
  }

  private void saveSessionId(final Response response) {
    pkoSession.addHeader("x-session-id", response.getHeader("X-Session-Id"));
  }
}