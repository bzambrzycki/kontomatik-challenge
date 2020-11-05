package pl.zambrzyckib;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class AccountInfoDTO {
  private String name;
  private String balance;
}
