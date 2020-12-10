package pl.zambrzyckib;

import pl.zambrzyckib.model.Credentials;
import pl.zambrzyckib.pko.PkoScraper;

public class KontomatikChallengeApp {

  public static void main(String[] args) {
    final UserInterface userInterface = new UserInterface(System.out::println);
    if (args.length == 2) {
      PkoScraper pkoScraper = new PkoScraper(userInterface);
      pkoScraper.getAndDisplayAccountsInfo(Credentials.of(args[0], args[1]));
    } else {
      userInterface.displayWrongArgsMessage();
    }
  }
}
