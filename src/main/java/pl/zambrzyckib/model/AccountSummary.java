package pl.zambrzyckib.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(staticName = "of")
public class AccountSummary {
  String name;
  String balance;
  String currency;
}
