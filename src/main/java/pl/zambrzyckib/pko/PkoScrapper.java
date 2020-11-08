package pl.zambrzyckib.pko;

import io.vavr.collection.List;
import io.vavr.control.Option;
import pl.zambrzyckib.dto.AccountInfoDTO;

public class PkoScrapper {

  private final PkoSessionHandler pkoSessionHandler = new PkoSessionHandler();

  public Option<List<AccountInfoDTO>> getAccountsInfo() {
    return pkoSessionHandler
        .getAccountsInfo()
        .map(responseDTO -> PkoResponseUtils.mapAccountsInfoResponse(responseDTO.getBody()));
  }
}
