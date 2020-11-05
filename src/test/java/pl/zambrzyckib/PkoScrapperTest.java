package pl.zambrzyckib;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;
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
                .put("account_ids", new JSONArray().put("abc").put("def"))
                .put("accounts", new JSONObject()
                    .put("abc", new JSONObject()
                        .put("name", "Konto1")
                        .put("balance", "100")
                    ).put("def", new JSONObject()
                        .put("name", "Konto2")
                        .put("balance", "200")
                    ))
            ));
    final var expectedList = List.of(
        AccountInfoDTO.of("Konto1", "100"),
        AccountInfoDTO.of("Konto2", "200"));
    assert pkoScrapper.parseAccountsInfoJson(testResponseDataJson)
        .get()
        .equals(expectedList);
  }

}
