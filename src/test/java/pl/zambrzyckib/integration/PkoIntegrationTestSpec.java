package pl.zambrzyckib.integration;

import io.vavr.control.Try;
import lombok.SneakyThrows;
import pl.zambrzyckib.integration.exception.WrongCredentialsFileException;
import pl.zambrzyckib.model.Credentials;

import java.io.FileInputStream;
import java.util.Properties;

public class PkoIntegrationTestSpec {

  private static final Properties PROPERTIES = new Properties();
  public static Credentials pkoTestCredentials;

  @SneakyThrows
  static void loadCredentialPropertiesIfNotLoaded() {
    if (PROPERTIES.isEmpty()) {
      var credentialsFileInputStream =
          Try.of(() -> new FileInputStream("src/test/resources/credentials.properties"))
              .getOrElseThrow(t -> new WrongCredentialsFileException("Credentials file not found"));
      PROPERTIES.load(credentialsFileInputStream);
      verifyCredentialProperties();
      loadAndSetCredentials();
    }
  }

  private static void verifyCredentialProperties() {
    if (!PROPERTIES.containsKey("login"))
      throw new WrongCredentialsFileException("Login not specified in credentials file");
    if (!PROPERTIES.containsKey("password"))
      throw new WrongCredentialsFileException("Password not specified in credentials file");
  }

  private static void loadAndSetCredentials() {
    pkoTestCredentials =
        Credentials.of(PROPERTIES.getProperty("login"), PROPERTIES.getProperty("password"));
  }
}
