package pl.zambrzyckib.pko.response.body;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PasswordResponseBody {

  public final Response response;

  @SerializedName("state_id")
  public final String stateId;

  @AllArgsConstructor
  public static class Response {
    public final Fields fields;

    @AllArgsConstructor
    public static class Fields {
      public final Password password;

      @AllArgsConstructor
      public static class Password {
        public final Object errors;
      }
    }
  }
}
