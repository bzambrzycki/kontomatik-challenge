package pl.zambrzyckib.pko.response.body;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoginResponseBody {
  public final String token;

  @SerializedName("flow_id")
  public final String flowId;

  public final Response response;

  @AllArgsConstructor
  public static class Response {
    public final Fields fields;

    @AllArgsConstructor
    public static class Fields {
      public final Object errors;
    }
  }
}
