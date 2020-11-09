package pl.zambrzyckib.pko;

import com.google.gson.Gson;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import pl.zambrzyckib.connection.Response;
import pl.zambrzyckib.model.AccountSummary;
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

  public List<String> getAccountsInfo() {
    login();
    return fetchAccountsInfo()
        .map(
            accountSummary ->
                "Konto: " + accountSummary.getName() + ", stan: " + accountSummary.getBalance());
  }

  private void login() {
    Stream.of(pkoRequestsHandler.sendLoginRequest())
        .peek(ignored -> System.out.println("Wysłano login"))
        .peek(pkoResponsesHandler::verifyLoginResponse)
        .peek(this::saveSessionId)
        .map(pkoRequestsHandler::sendPasswordRequest)
        .peek(ignored -> System.out.println("Wysłano hasło"))
        .peek(pkoResponsesHandler::verifyPasswordResponse)
        .peek(ignored -> System.out.println("Pomyślnie zalogowano"));
  }

  private List<AccountSummary> fetchAccountsInfo() {
    return Stream.of(pkoRequestsHandler.sendAccountsInfoRequest())
        .map(pkoResponsesHandler::mapAccountsInfoResponse)
        .peek(ignored -> System.out.println("Pobrano dane o kontach"))
        .get();
  }

  private void saveSessionId(final Response response) {
    pkoSession.addHeader("x-session-id", response.getHeader("X-Session-Id"));
  }
}
