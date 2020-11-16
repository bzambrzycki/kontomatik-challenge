package pl.zambrzyckib;

import io.vavr.collection.List;
import lombok.experimental.UtilityClass;
import pl.zambrzyckib.model.AccountSummary;

@UtilityClass
public class UserInterface {

  public void displayNotEnoughArgsMessage() {
    System.out.println(
        "Not enough arguments provided. Run the app using command like the one below\n"
            + "java -jar BUILT_JAR_NAME.jar \"YOUR_LOGIN\" \"YOUR_PASSWORD\"");
  }

  public void displaySuccessMessage() {
    System.out.println("Successfully fetched accounts info");
  }

  public void displayAccountSummaries(List<AccountSummary> accountSummaries) {
    formatAccountSummaries(accountSummaries).forEach(System.out::println);
  }

  public void logThrowable(Throwable t) {
    System.out.println("[LOG/ERR] " + t.getMessage());
  }

  private List<String> formatAccountSummaries(List<AccountSummary> accountSummaries) {
    return List.ofAll(accountSummaries)
        .map(
            accountSummary ->
                "Account: "
                    + accountSummary.name
                    + ", balance: "
                    + accountSummary.balance
                    + " "
                    + accountSummary.currency);
  }
}
