package pl.zambrzyckib;

import java.util.Scanner;
import pl.zambrzyckib.pko.PkoScrapper;

public class KontomatikChallengeApp {

  public static final Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {
    PkoScrapper pkoScrapper = new PkoScrapper();
    final var formattedPkoAccountSummary = pkoScrapper.getAccountsInfo();
    formattedPkoAccountSummary.forEach(System.out::println);
  }
}
