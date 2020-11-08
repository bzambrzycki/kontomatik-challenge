package pl.zambrzyckib.pko.response.body;

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

  public Map<String, AccountSummary> getAccounts() {
    return getResponse().getData().getAccounts();
  }
}
