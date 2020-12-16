package pl.zambrzyckib.integration;

import lombok.SneakyThrows;
import pl.zambrzyckib.model.Credentials;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

public class PkoIntegrationTestSpec {

  @SneakyThrows
  static Credentials loadCredentials() {
    Properties credentialProperties = new Properties();
    InputStream credentialsFileInputStream = readCredentialsFileInputStream();
    credentialProperties.load(credentialsFileInputStream);
    verifyCredentialProperties(credentialProperties);
    return Credentials.of(
        credentialProperties.getProperty("login"), credentialProperties.getProperty("password"));
  }

  private static InputStream readCredentialsFileInputStream() {
    InputStream credentialsFileInputStream =
        PkoIntegrationTestSpec.class.getResourceAsStream(File.separator + "credentials.properties");
    if (credentialsFileInputStream == null)
      throw new RuntimeException("credentials.properties file not found");
    else return credentialsFileInputStream;
  }

  private static void verifyCredentialProperties(Properties properties) {
    if (!properties.containsKey("login"))
      throw new RuntimeException("Login not specified in credentials file");
    if (!properties.containsKey("password"))
      throw new RuntimeException("Password not specified in credentials file");
  }
}
