package pl.zambrzyckib.pko.response.body;

import com.google.gson.annotations.SerializedName;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoginResponseBody {
  public final String token;

  @SerializedName("flow_id")
  public final String flowId;

  Response response;

  public Boolean hasErrors() {
    return Try.of(() -> !response.fields.login.errors.hint.isEmpty()).getOrElse(false);
  }

  private static class Response {
    Fields fields;

    private static class Fields {
      Login login;

      private static class Login {
        Errors errors;

        private static class Errors {
          String hint;
        }
      }
    }
  }
}
