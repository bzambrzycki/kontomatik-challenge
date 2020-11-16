package pl.zambrzyckib;

import io.vavr.collection.List;
import lombok.experimental.UtilityClass;

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

  public void displayAccountSummaries(List<String> accountSummaries) {
    accountSummaries.forEach(System.out::println);
  }

  public void logThrowable(Throwable t) {
    System.out.println("[LOG/ERR] " + t.getMessage());
  }
}
