package pl.zambrzyckib.pko.response.body;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponseBody {
  public final String token;

  @SerializedName("flow_id")
  public final String flowId;

  public Response response;

  @Getter
  public static class Response {
    Fields fields;

    @Getter
    public static class Fields {
      Object errors;
    }
  }
}
