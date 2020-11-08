package pl.zambrzyckib.pko;

import io.vavr.collection.List;
import pl.zambrzyckib.dto.AccountInfoDTO;

public class PkoScrapper {

  private final PkoSessionHandler pkoSessionHandler;

  public PkoScrapper() {
    this.pkoSessionHandler = new PkoSessionHandler();
  }

  public List<AccountInfoDTO> getAccountsInfo() {
    return pkoSessionHandler.getAccountsInfo();
  }
}
