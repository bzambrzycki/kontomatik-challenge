package pl.zambrzyckib;

import io.vavr.collection.List;
import pl.zambrzyckib.model.AccountSummary;

import java.util.function.Consumer;

public class UserInterface {

  private final Consumer<String> stringPrinter;

  public UserInterface(Consumer<String> stringPrinter) {
    this.stringPrinter = stringPrinter;
  }

  public void displayWrongArgsMessage() {
    var message =
        "Wrong arguments provided. Run the app using command like the one below\n"
            + "java -jar BUILT_JAR_NAME.jar \"YOUR_LOGIN\" \"YOUR_PASSWORD\"";
    stringPrinter.accept(message);
  }

  public void displaySuccessMessage() {
    var message = "Successfully fetched accounts info";
    stringPrinter.accept(message);
  }

  public void displayAccountSummaries(List<AccountSummary> accountSummaries) {
    formatAccountSummaries(accountSummaries).forEach(stringPrinter::accept);
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
