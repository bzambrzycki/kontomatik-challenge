package pl.zambrzyckib.pko.response.body;

import com.google.gson.annotations.SerializedName;
import io.vavr.control.Try;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SendLoginResponseBody {
  String token;

  @SerializedName("flow_id")
  String flowId;

  Response response;

  public Boolean hasErrors() {
    return Try.of(() -> !getResponse().getFields().getLogin().getErrors().getHint().isEmpty())
        .getOrElse(false);
  }

  @Getter
  private static class Response {
    Fields fields;

    @Getter
    private static class Fields {
      Login login;

      @Getter
      private static class Login {
        Errors errors;

        @Getter
        private static class Errors {
          String hint;
        }
      }
    }
  }
}
