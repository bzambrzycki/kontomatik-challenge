package pl.zambrzyckib.pko.response.body;

import io.vavr.collection.List;
import java.util.Map;

import lombok.AllArgsConstructor;

public class AccountsInfoResponseBody {
  Response response;

  private static class Response {
    Data data;

    private static class Data {
      Map<String, PkoAccountSummary> accounts;
    }
  }

  public List<PkoAccountSummary> getAccountSummaries() {
    return List.ofAll(response.data.accounts.values());
  }

  @AllArgsConstructor
  public static class PkoAccountSummary {
    public final String name;
    public final String balance;
    public final String currency;
  }
}
