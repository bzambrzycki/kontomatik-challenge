package pl.zambrzyckib.pko;

import io.vavr.collection.List;
import io.vavr.collection.Stream;
import pl.zambrzyckib.dto.AccountInfoDTO;
import pl.zambrzyckib.connection.Response;
import pl.zambrzyckib.pko.request.PkoRequestsHandler;
import pl.zambrzyckib.pko.response.PkoResponsesHandler;

public class PkoScrapper {

  private final PkoSession pkoSession;
  private final PkoRequestsHandler pkoRequestsHandler;
  private final PkoResponsesHandler pkoResponsesHandler;

  public PkoScrapper() {
    this.pkoSession = new PkoSession();
    this.pkoRequestsHandler = new PkoRequestsHandler(pkoSession);
    this.pkoResponsesHandler = new PkoResponsesHandler();
  }

  public List<AccountInfoDTO> getAccountsInfo() {
    login();
    return fetchAccountsInfo();
  }

  private void login() {
    Stream.of(pkoRequestsHandler.sendLoginRequest())
        .peek(ignored -> System.out.println("Wysłano login"))
        .peek(
            responseDTO ->
                pkoResponsesHandler.verifyCredentialsResponse(responseDTO.getBody(), "login"))
        .peek(this::saveSessionId)
        .map(pkoRequestsHandler::sendPasswordRequest)
        .peek(ignored -> System.out.println("Wysłano hasło"))
        .peek(
            responseDTO ->
                pkoResponsesHandler.verifyCredentialsResponse(responseDTO.getBody(), "password"))
        .peek(ignored -> System.out.println("Pomyślnie zalogowano"));
  }

  private List<AccountInfoDTO> fetchAccountsInfo() {
    return Stream.of(pkoRequestsHandler.sendAccountsInfoRequest())
        .map(responseDTO -> pkoResponsesHandler.mapAccountsInfoResponse(responseDTO.getBody()))
        .peek(ignored -> System.out.println("Pobrano dane o kontach"))
        .get();
  }

  private void saveSessionId(final Response response) {
    pkoSession.addHeader("x-session-id", response.getHeader("X-Session-Id"));
  }
}
