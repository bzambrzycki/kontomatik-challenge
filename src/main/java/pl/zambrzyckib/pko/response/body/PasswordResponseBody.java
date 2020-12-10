package pl.zambrzyckib.pko.response.body;

import lombok.Getter;

@Getter
public class PasswordResponseBody {

  Response response;
  String state_id;

  @Getter
  public class Response {
    Fields fields;

    @Getter
    public class Fields {
      Password password;

      @Getter
      public class Password {
        Errors errors;

        @Getter
        public class Errors {
          String hint;
        }
      }
    }
  }
}
