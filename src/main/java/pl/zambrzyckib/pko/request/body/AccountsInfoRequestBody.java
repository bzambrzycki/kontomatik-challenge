package pl.zambrzyckib.pko.request.body;

import com.google.gson.annotations.SerializedName;

public class AccountsInfoRequestBody {
  Data data;

  public AccountsInfoRequestBody() {
    this.data = new Data();
  }

  private static class Data {
    Object accounts;

    @SerializedName("account_ids")
    Object accountIds;

    public Data() {
      this.accounts = new Object();
      this.accountIds = new Object();
    }
  }
}
