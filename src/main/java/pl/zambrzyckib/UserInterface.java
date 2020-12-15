package pl.zambrzyckib;

import io.vavr.collection.List;
import lombok.Getter;
import pl.zambrzyckib.model.AccountSummary;

import java.util.function.Consumer;

public class UserInterface {

  private final Consumer<String> stringPrinter;

  @Getter private List<String> output;

  public UserInterface(Consumer<String> stringPrinter) {
    this.output = List.empty();
    this.stringPrinter = stringPrinter;
  }

  public void displayWrongArgsMessage() {
    var message =
        "Wrong arguments provided. Run the app using command like the one below\n"
            + "java -jar BUILT_JAR_NAME.jar \"YOUR_LOGIN\" \"YOUR_PASSWORD\"";
    output = output.push(message);
    stringPrinter.accept(message);
  }

  public void displaySuccessMessage() {
    var message = "Successfully fetched accounts info";
    output = output.push(message);
    stringPrinter.accept(message);
  }

  public void displayAccountSummaries(List<AccountSummary> accountSummaries) {
    formatAccountSummaries(accountSummaries)
        .forEach(
            accountSummary -> {
              output = output.push(accountSummary);
              stringPrinter.accept(accountSummary);
            });
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
