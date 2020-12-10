package pl.zambrzyckib;

import io.vavr.collection.List;
import lombok.Getter;
import pl.zambrzyckib.model.AccountSummary;

import java.util.function.Consumer;

public class UserInterface {

  private final Consumer<String> stringConsumer;

  @Getter private List<String> output;

  public UserInterface(Consumer<String> stringConsumer) {
    this.output = List.empty();
    this.stringConsumer = stringConsumer;
  }

  public void displayWrongArgsMessage() {
    final var message =
        "Wrong arguments provided. Run the app using command like the one below\n"
            + "java -jar BUILT_JAR_NAME.jar \"YOUR_LOGIN\" \"YOUR_PASSWORD\"";
    output = output.push(message);
    stringConsumer.accept(message);
  }

  public void displaySuccessMessage() {
    final var message = "Successfully fetched accounts info";
    output = output.push(message);
    stringConsumer.accept(message);
  }

  public void displayAccountSummaries(List<AccountSummary> accountSummaries) {
    formatAccountSummaries(accountSummaries)
        .forEach(
            accountSummary -> {
              output = output.push(accountSummary);
              stringConsumer.accept(accountSummary);
            });
  }

  public void logThrowable(Throwable t) {
    final var message = "[LOG/ERR] " + t.getMessage();
    output = output.push(message);
    stringConsumer.accept(message);
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
