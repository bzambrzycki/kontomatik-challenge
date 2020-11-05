package pl.zambrzyckib;

import io.vavr.collection.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

public class PkoScrapperTest {

  @Test
  public void shouldReturnNameBalanceMapFromJson() {
    final var pkoScrapper = new PkoScrapper();
    final var testResponseBody =
        new JSONObject()
            .put("response", new JSONObject().put("data", new JSONObject()
                .put("account_ids", new JSONArray().put("abc").put("def"))
                .put("accounts", new JSONObject()
                    .put("abc", new JSONObject()
                        .put("name", "accountOne")
                        .put("balance", "100")
                    ).put("def", new JSONObject()
                        .put("name", "accountTwo")
                        .put("balance", "200")
                    ))
            )).toString();
    final var expectedList = List.of(
        AccountInfoDTO.of("accountOne", "100"),
        AccountInfoDTO.of("accountTwo", "200"));
    assert pkoScrapper.parseAccountsInfoJson(testResponseBody)
        .get()
        .equals(expectedList);
  }

}
