package pl.zambrzyckib.pko;

import io.vavr.collection.List;
import io.vavr.control.Option;
import pl.zambrzyckib.dto.AccountInfoDTO;
import pl.zambrzyckib.BankScrapper;

public class PkoScrapper implements BankScrapper {

  private final PkoSession pkoSession = new PkoSession();

  @Override
  public Option<List<AccountInfoDTO>> getAccountsInfo() {
    return pkoSession
        .getAccountsInfo()
        .map(responseDTO -> PkoResponseUtils.mapAccountsInfoResponse(responseDTO.getBody()));
  }
}
