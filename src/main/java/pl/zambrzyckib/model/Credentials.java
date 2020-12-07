package pl.zambrzyckib.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor(staticName = "of")
public class Credentials {
  public final String login;
  public final String password;
}
