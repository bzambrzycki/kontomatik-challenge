package pl.zambrzyckib;

import io.vavr.collection.List;

import lombok.experimental.UtilityClass;
import org.json.JSONObject;

@UtilityClass
public class PkoResponseMapper {

  public List<AccountInfoDTO> mapAccountsInfoResponse(final String responseBody) {
    return List.of(new JSONObject(responseBody)
        .getJSONObject("response")
        .getJSONObject("data"))
        .flatMap(responseDataJson -> List.ofAll(responseDataJson.getJSONArray("account_ids"))
            .map(accountId -> AccountInfoDTO.of(
                responseDataJson.getJSONObject("accounts").getJSONObject(accountId.toString())
                    .get("name").toString(),
                responseDataJson.getJSONObject("accounts").getJSONObject(accountId.toString())
                    .get("balance").toString())));
  }

}
