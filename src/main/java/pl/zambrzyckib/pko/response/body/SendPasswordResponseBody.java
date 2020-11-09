package pl.zambrzyckib.pko.response.body;

import io.vavr.control.Try;
import lombok.Getter;

@Getter
public class SendPasswordResponseBody {

  Response response;

  public Boolean hasErrors() {
    return Try.of(() -> !getResponse().getFields().getPassword().getErrors().getHint().isEmpty())
        .getOrElse(false);
  }

  @Getter
  private static class Response {
    Fields fields;

    @Getter
    private static class Fields {
      Password password;

      @Getter
      private static class Password {
        Errors errors;

        @Getter
        private static class Errors {
          String hint;
        }
      }
    }
  }
}
