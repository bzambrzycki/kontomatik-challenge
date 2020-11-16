package pl.zambrzyckib;

import pl.zambrzyckib.model.Credentials;
import pl.zambrzyckib.pko.PkoScraper;

public class KontomatikChallengeApp {

  public static void main(String[] args) {
    if (args.length == 2){
      PkoScraper pkoScraper = new PkoScraper();
      pkoScraper.getAndDisplayAccountsInfo(Credentials.of(args[0], args[1]));
    }
    else {
      UserInterface.displayWrongArgsMessage();
    }
  }
}
