package pl.zambrzyckib.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@AllArgsConstructor(staticName = "of")
public class AccountSummary {
  public final String name;
  public final String balance;
  public final String currency;
}
