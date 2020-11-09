package pl.zambrzyckib.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class Credentials {
  String login;
  String password;
}
