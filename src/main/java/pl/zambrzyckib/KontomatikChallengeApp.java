package pl.zambrzyckib;

import pl.zambrzyckib.model.Credentials;
import pl.zambrzyckib.pko.PkoScrapper;

public class KontomatikChallengeApp {

  public static void main(String[] args) {
    PkoScrapper pkoScrapper = new PkoScrapper();
    final var formattedPkoAccountSummary = pkoScrapper.getAccountsInfo(Credentials.of(args[0], args[1]));
    formattedPkoAccountSummary.forEach(System.out::println);
  }
}
