package pl.zambrzyckib.pko;

import io.vavr.collection.List;
import io.vavr.collection.Stream;
import pl.zambrzyckib.dto.AccountInfoDTO;
import pl.zambrzyckib.dto.ResponseDTO;

public class PkoSessionHandler {

  private final PkoSession pkoSession = new PkoSession();
  private final PkoRequestHandler pkoRequestHandler = new PkoRequestHandler(pkoSession);
  private final PkoResponseHandler pkoResponseHandler = new PkoResponseHandler();

  public List<AccountInfoDTO> getAccountsInfo() {
    login();
    return fetchAccountsInfo();
  }

  public void login() {
    Stream.of(pkoRequestHandler.sendUserLoginRequest())
        .peek(ignored -> System.out.println("Wysłano login"))
        .peek(
            responseDTO ->
                pkoResponseHandler.verifyCredentialsResponse(responseDTO.getBody(), "login"))
        .peek(this::saveSessionId)
        .map(pkoRequestHandler::sendUserPasswordRequest)
        .peek(ignored -> System.out.println("Wysłano hasło"))
        .peek(
            responseDTO ->
                pkoResponseHandler.verifyCredentialsResponse(responseDTO.getBody(), "password"))
        .peek(ignored -> System.out.println("Pomyślnie zalogowano"));
  }

  private List<AccountInfoDTO> fetchAccountsInfo() {
    return Stream.of(pkoRequestHandler.sendAccountsInfoRequest())
        .map(responseDTO -> pkoResponseHandler.mapAccountsInfoResponse(responseDTO.getBody()))
        .peek(ignored -> System.out.println("Pobrano dane o kontach"))
        .get();
  }

  private void saveSessionId(final ResponseDTO responseDTO) {
    pkoSession.addHeader("x-session-id", responseDTO.getHeader("X-Session-Id"));
  }
}
