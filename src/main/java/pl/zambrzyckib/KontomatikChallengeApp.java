package pl.zambrzyckib;

import java.util.Scanner;
import pl.zambrzyckib.pko.PkoScrapper;

public class KontomatikChallengeApp {

  public static final Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {
    PkoScrapper pkoScrapper = new PkoScrapper();
    final var pkoResult = pkoScrapper.getAccountsInfo();
    if (pkoResult.isDefined()) {
      System.out.println("Udało się pobrać informacje o kontach");
      // TODO better presentation
      System.out.println(pkoResult.get());
    } else if (pkoResult.isEmpty()) {
      System.out.println("Nie udało się pobrać informacji o kontach");
    }
  }
}
