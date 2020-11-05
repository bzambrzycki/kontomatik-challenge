package pl.zambrzyckib;

import io.vavr.collection.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

public class PkoScrapperTest {

  @Test
  public void shouldReturnNameBalanceMapFromJson() {
    final var pkoScrapper = new PkoScrapper();
    final var testResponseDataJson =
        new JSONObject()
            .put("response", new JSONObject().put("data", new JSONObject()
                .put("account_ids", new JSONArray().put("abc"))
                .put("accounts", new JSONObject()
                    .put("abc", new JSONObject()
                        .put("name", "Konto")
                        .put("balance", "100")

                    ))
            ));
    final var expectedMap = HashMap.of("Konto", "100");
    assert pkoScrapper.parseAccountsInfoJson(testResponseDataJson)
        .getOrElse(HashMap.empty())
        .equals(expectedMap);
  }

}
