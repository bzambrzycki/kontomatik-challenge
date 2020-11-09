package pl.zambrzyckib.pko.response.body;

import io.vavr.collection.List;
import java.util.Map;
import lombok.Getter;
import pl.zambrzyckib.model.AccountSummary;

@Getter
public class AccountsInfoResponseBody {
  Response response;

  @Getter
  private static class Response {
    Data data;

    @Getter
    private static class Data {
      Map<String, AccountSummary> accounts;
    }
  }

  public List<AccountSummary> getAccountSummaries() {
    return List.ofAll(getResponse().getData().getAccounts().values());
  }
}
