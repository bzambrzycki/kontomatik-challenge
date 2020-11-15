package pl.zambrzyckib.pko;

import com.google.gson.Gson;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import pl.zambrzyckib.connection.Response;
import pl.zambrzyckib.model.AccountSummary;
import pl.zambrzyckib.model.Credentials;
import pl.zambrzyckib.pko.response.PkoResponseUtils;

public class PkoScraper {

    private final PkoSession pkoSession;
    public static final Gson GSON = new Gson();

    public PkoScraper() {
        this.pkoSession = new PkoSession();
    }

    public List<String> getAccountsInfo(Credentials credentials) {
        login(credentials);
        return PkoResponseUtils
                .formatAccountSummaries(fetchAccountsInfo());
    }

    private void login(Credentials credentials) {
        Stream.of(pkoSession.sendLoginRequest(credentials.getLogin()))
                .peek(this::saveSessionId)
                .map(
                        response -> pkoSession.sendPasswordRequest(response, credentials.getPassword()));
    }

    private List<AccountSummary> fetchAccountsInfo() {
        return Stream.of(pkoSession.sendAccountsInfoRequest())
                .map(PkoResponseUtils::getAccountSummariesFromResponse)
                .peek(ignored -> System.out.println("Successfully fetched accounts data"))
                .get();
    }

    private void saveSessionId(Response response) {
        pkoSession.setSessionId(response.getHeader("X-Session-Id"));
    }
}
