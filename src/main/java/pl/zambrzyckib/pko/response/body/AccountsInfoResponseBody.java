package pl.zambrzyckib.pko.response.body;

import io.vavr.collection.List;
import java.util.Map;
import pl.zambrzyckib.model.AccountSummary;

public class AccountsInfoResponseBody {
  Response response;

  private static class Response {
    Data data;

    private static class Data {
      Map<String, AccountSummary> accounts;
    }
  }

  public List<AccountSummary> getAccountSummaries() {
    return List.ofAll(response.data.accounts.values());
  }
}
