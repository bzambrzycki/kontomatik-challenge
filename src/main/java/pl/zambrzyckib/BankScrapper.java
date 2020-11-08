package pl.zambrzyckib;

import io.vavr.collection.List;
import io.vavr.control.Option;
import pl.zambrzyckib.dto.AccountInfoDTO;

public interface BankScrapper {

  Option<List<AccountInfoDTO>> getAccountsInfo();
}
