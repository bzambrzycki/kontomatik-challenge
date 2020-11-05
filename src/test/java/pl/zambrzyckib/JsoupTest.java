package pl.zambrzyckib;

import io.vavr.control.Try;
import org.jsoup.Jsoup;
import org.junit.Test;

//Test class used to familiarize with JSoup library
public class JsoupTest {

  @Test
  public void shouldReturnEmptyOptionOnError() {
    final var correctUrl = "https://en.wikipedia.org";
    final var wrongUrl = "https://www.some.wrong.url.address.org/";
    assert Try.of(() -> Jsoup.connect(correctUrl)
        .get()).toOption().isDefined();
    assert Try.of(() -> Jsoup.connect(wrongUrl)
        .get()).toOption().isEmpty();
  }

  @Test
  public void shouldReturnSelectedFieldAttribute() {
    final var wikipediaUrl = "https://en.wikipedia.org/";
    assert Try.of(
        () -> Jsoup.connect(wikipediaUrl).get())
        .toOption()
        .map(response -> Jsoup.parse(response.html()))
        .map(parsedResponse -> parsedResponse
            .select("input[id$=searchInput]").attr("placeholder"))
        .map(placeholder -> placeholder.equals("Search Wikipedia")).getOrElse(false);
  }
}
