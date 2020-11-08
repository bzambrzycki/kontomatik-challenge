package pl.zambrzyckib.pko.request.body;

import com.google.gson.annotations.SerializedName;

public class SendLoginRequestBody {

  String action;
  Data data;
  @SerializedName("state_id")
  String stateId;

  public SendLoginRequestBody(final String login) {
    this.action = "submit";
    this.data = new Data(login);
    this.stateId = "login";
  }

  private static class Data {
    String login;

    public Data(String login) {
      this.login = login;
    }
  }
}
