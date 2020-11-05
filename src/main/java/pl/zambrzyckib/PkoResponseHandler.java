package pl.zambrzyckib;

import io.vavr.collection.List;

import lombok.experimental.UtilityClass;
import org.json.JSONObject;

@UtilityClass
public class PkoResponseHandler {

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

  public void verifyCredentialsResponse(final String responseBody, final String type) {
    final var responseJson = new JSONObject(responseBody).getJSONObject("response");
    if (responseJson.has("fields")) {
      if (responseJson.getJSONObject("fields").has(type)) {
        if (!responseJson.getJSONObject("fields").getJSONObject(type)
            .getJSONObject("errors").isEmpty()) {
          throw new InvalidCredentialsException();
        }
      }
    }
  }

}
