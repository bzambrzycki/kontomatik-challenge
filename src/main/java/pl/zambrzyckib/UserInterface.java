package pl.zambrzyckib;

import io.vavr.collection.List;
import lombok.Getter;
import pl.zambrzyckib.model.AccountSummary;

import java.io.PrintStream;

public class UserInterface {

  private final PrintStream printStream;

  @Getter private List<String> output;

  public UserInterface(PrintStream printStream) {
    this.output = List.empty();
    this.printStream = printStream;
  }

  public void displayWrongArgsMessage() {
    final var message =
        "Wrong arguments provided. Run the app using command like the one below\n"
            + "java -jar BUILT_JAR_NAME.jar \"YOUR_LOGIN\" \"YOUR_PASSWORD\"\n";
    output = output.push(message);
    printStream.print(message);
  }

  public void displaySuccessMessage() {
    final var message = "Successfully fetched accounts info\n";
    output = output.push(message);
    printStream.print(message);
  }

  public void displayAccountSummaries(List<AccountSummary> accountSummaries) {
    formatAccountSummaries(accountSummaries)
        .forEach(
            accountSummary -> {
              output = output.push(accountSummary + "\n");
              printStream.print(accountSummary + "\n");
            });
  }

  public void logThrowable(Throwable t) {
    final var message = "[LOG/ERR] " + t.getMessage() + "\n";
    output = output.push(message);
    printStream.print(message);
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
