package pl.zambrzyckib;

import io.vavr.collection.Map;

public interface BankScrapper {
  Map<String, String> getAccountsInfo();
}
