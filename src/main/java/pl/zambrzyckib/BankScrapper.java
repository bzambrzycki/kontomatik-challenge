package pl.zambrzyckib;

import io.vavr.collection.List;
import io.vavr.control.Option;

public interface BankScrapper {

  Option<List<AccountInfoDTO>> getAccountsInfo();
}
