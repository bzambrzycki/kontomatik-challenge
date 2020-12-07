package pl.zambrzyckib.integration;

import io.vavr.control.Try;
import lombok.SneakyThrows;
import pl.zambrzyckib.integration.exception.WrongCredentialsFileException;

import java.io.FileInputStream;
import java.util.Properties;

public class PkoIntegrationTestSpec {

  public static final Properties properties = new Properties();

  @SneakyThrows
  static void loadCredentialPropertiesIfNotLoaded() {
    if (properties.isEmpty()) {
      final var credentialsFileInputStream =
          Try.of(() -> new FileInputStream("src/test/resources/credentials.properties"))
              .getOrElseThrow(t -> new WrongCredentialsFileException("Credentials file not found"));
      properties.load(credentialsFileInputStream);
      verifyCredentialProperties();
    }
  }

  private static void verifyCredentialProperties() {
    if (!properties.containsKey("login"))
      throw new WrongCredentialsFileException("Login not specified in credentials file");
    if (!properties.containsKey("password"))
      throw new WrongCredentialsFileException("Password not specified in credentials file");
  }
}
