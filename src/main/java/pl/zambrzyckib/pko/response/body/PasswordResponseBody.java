package pl.zambrzyckib.pko.response.body;

import io.vavr.control.Try;

public class PasswordResponseBody {

  Response response;

  public Boolean hasErrors() {
    return Try.of(() -> !response.fields.password.errors.hint.isEmpty()).getOrElse(false);
  }

  private static class Response {
    Fields fields;

    private static class Fields {
      Password password;

      private static class Password {
        Errors errors;

        private static class Errors {
          String hint;
        }
      }
    }
  }
}
