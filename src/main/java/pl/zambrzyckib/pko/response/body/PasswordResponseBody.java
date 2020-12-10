package pl.zambrzyckib.pko.response.body;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PasswordResponseBody {

  public final Response response;

  @SerializedName("state_id")
  public final String stateId;

  @Getter
  public static class Response {
    Fields fields;

    @Getter
    public static class Fields {
      Password password;

      @Getter
      public static class Password {
        Object errors;
      }
    }
  }
}
