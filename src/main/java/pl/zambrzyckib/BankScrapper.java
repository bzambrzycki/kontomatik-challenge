package pl.zambrzyckib;

import io.vavr.collection.HashMap;
import io.vavr.control.Option;

public interface BankScrapper {
  Option<HashMap<String, String>> getAccountsInfo();
}
