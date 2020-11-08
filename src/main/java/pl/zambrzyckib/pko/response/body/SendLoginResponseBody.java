package pl.zambrzyckib.pko.response.body;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class SendLoginResponseBody {
  String token;

  @SerializedName("flow_id")
  String flowId;

  Response response;

  private static class Response {
    Fields fields;

    private static class Fields {
      Password password;

      private static class Password {
        Errors errors;

        private static class Errors {}
      }
    }
  }
}
