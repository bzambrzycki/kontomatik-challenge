package pl.zambrzyckib.pko.request.body;

import com.google.gson.annotations.SerializedName;
import pl.zambrzyckib.pko.response.body.LoginResponseBody;

public class PasswordRequestBody {

  String action;
  Data data;

  @SerializedName("state_id")
  String stateId;

  @SerializedName("flow_id")
  String flowId;

  String token;

  public PasswordRequestBody(String password, LoginResponseBody loginResponseBody) {
    this.action = "submit";
    this.data = new Data(password);
    this.stateId = "password";
    this.flowId = loginResponseBody.flowId;
    this.token = loginResponseBody.token;
  }

  private static class Data {
    String password;

    public Data(String password) {
      this.password = password;
    }
  }
}
