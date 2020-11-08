package pl.zambrzyckib.pko.request.body;

import com.google.gson.annotations.SerializedName;
import pl.zambrzyckib.pko.response.body.SendLoginResponseBody;

public class SendPasswordRequestBody {

  String action;
  Data data;

  @SerializedName("state_id")
  String stateId;

  @SerializedName("flow_id")
  String flowId;

  String token;

  public SendPasswordRequestBody(
      final String password, final SendLoginResponseBody sendLoginResponseBody) {
    this.action = "submit";
    this.data = new Data(password);
    this.stateId = "password";
    this.flowId = sendLoginResponseBody.getFlowId();
    this.token = sendLoginResponseBody.getToken();
  }

  private static class Data {
    String password;

    public Data(String password) {
      this.password = password;
    }
  }
}
