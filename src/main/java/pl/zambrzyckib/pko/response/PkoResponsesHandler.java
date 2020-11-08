package pl.zambrzyckib.pko.response;

import io.vavr.collection.List;

import org.json.JSONObject;
import pl.zambrzyckib.dto.AccountInfoDTO;
import pl.zambrzyckib.exception.InvalidCredentialsException;

public class PkoResponsesHandler {

  /**
   * @param responseBody - response from POST request with user login, or password
   * @param type - type of the requests from which the response comes from - POST with login, or
   *                POST with password
   *     This function checks whether the response contains fields that inform about
   *     providing wrong credentials. When invalid credentials are provided, the server responds
   *     with code 200 OK, but the response body contains nonempty field called errors, related to
   *     specific object (fields.login or fields.password) depending on what request was sent
   *     beforehand
   */
  public void verifyCredentialsResponse(final String responseBody, final String type) {
    final var responseJson = new JSONObject(responseBody).getJSONObject("response");
    if (responseJson.has("fields")) {
      if (responseJson.getJSONObject("fields").has(type)) {
        if (!responseJson
            .getJSONObject("fields")
            .getJSONObject(type)
            .getJSONObject("errors")
            .isEmpty()) {
          throw new InvalidCredentialsException();
        }
      }
    }
  }

  public List<AccountInfoDTO> mapAccountsInfoResponse(final String responseBody) {
    return List.of(new JSONObject(responseBody).getJSONObject("response").getJSONObject("data"))
        .flatMap(
            responseDataJson ->
                List.ofAll(responseDataJson.getJSONArray("account_ids"))
                    .map(
                        accountId ->
                            AccountInfoDTO.of(
                                responseDataJson
                                    .getJSONObject("accounts")
                                    .getJSONObject(accountId.toString())
                                    .get("name")
                                    .toString(),
                                responseDataJson
                                    .getJSONObject("accounts")
                                    .getJSONObject(accountId.toString())
                                    .get("balance")
                                    .toString())));
  }
}
