package pl.zambrzyckib.integration;

import io.vavr.control.Try;
import lombok.SneakyThrows;
import pl.zambrzyckib.model.Credentials;

import java.io.FileInputStream;
import java.util.Properties;

public class PkoIntegrationTestSpec {

  private static final Properties PROPERTIES = new Properties();

  private static Credentials pkoTestCredentials;

  static Credentials loadCredentials() {
    if (PROPERTIES.isEmpty()) {
      loadCredentialsProperties();
      pkoTestCredentials =
          Credentials.of(PROPERTIES.getProperty("login"), PROPERTIES.getProperty("password"));
    }
    return pkoTestCredentials;
  }

  @SneakyThrows
  private static void loadCredentialsProperties() {
    var credentialsFileInputStream =
        Try.of(() -> new FileInputStream("src/test/resources/credentials.properties"))
            .getOrElseThrow(t -> new RuntimeException("Credentials file not found"));
    PROPERTIES.load(credentialsFileInputStream);
    verifyCredentialProperties();
  }

  private static void verifyCredentialProperties() {
    if (!PROPERTIES.containsKey("login"))
      throw new RuntimeException("Login not specified in credentials file");
    if (!PROPERTIES.containsKey("password"))
      throw new RuntimeException("Password not specified in credentials file");
  }
}
