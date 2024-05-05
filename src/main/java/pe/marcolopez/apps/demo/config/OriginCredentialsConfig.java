package pe.marcolopez.apps.demo.config;

import static pe.marcolopez.apps.demo.util.Constants.*;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Reference:
 * - <a href="https://jiasli.github.io/azure-notes/aad/Service-Principal-portal.html">Create Service Principal on Azure</a>
 */
@Slf4j
@ApplicationScoped
public class OriginCredentialsConfig {

  Map<String, String> credentials = new HashMap<>();

  @ConfigProperty(name = "app.config.origin", defaultValue = "vars")
  String origin;

  @ConfigProperty(name = "app.config.username-text", defaultValue = "username")
  String usernameText;

  @ConfigProperty(name = "app.config.password-text", defaultValue = "password")
  String passwordText;

  @ConfigProperty(name = "app.config.azure.key-vault-name", defaultValue = "keyVaultName")
  String keyVaultName;

  @ConfigProperty(name = "app.config.azure.client-id", defaultValue = "clientId")
  String clientId;

  @ConfigProperty(name = "app.config.azure.client-secret", defaultValue = "clientSecret")
  String clientSecret;

  @ConfigProperty(name = "app.config.azure.tenant-id", defaultValue = "tenantId")
  String tenantId;

  @ConfigProperty(name = "app.config.azure.secret-user", defaultValue = "secretUser")
  String secretUser;

  @ConfigProperty(name = "app.config.azure.secret-pass", defaultValue = "secretPass")
  String secretPass;

  public Map<String, String> getCredentials() {
    log.info("### Get Credentials Origin...");

    return switch (origin) {
      case ORIGIN_AZURE -> {
        String keyVaultUri = "https://" + keyVaultName + ".vault.azure.net";

        ClientSecretCredential build = new ClientSecretCredentialBuilder()
            .clientId(clientId)
            .clientSecret(clientSecret)
            .tenantId(tenantId)
            .retryTimeout((time) -> Duration.ofSeconds(100))
            .executorService(Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()))
            .build();

        SecretClient secretClient = new SecretClientBuilder()
            .vaultUrl(keyVaultUri)
            .credential(build)
            .buildClient();

        KeyVaultSecret retrievedSecretUser = secretClient.getSecret(secretUser);
        KeyVaultSecret retrievedSecretPass = secretClient.getSecret(secretPass);

        credentials.put(USER_KEY_MAP, retrievedSecretUser.getValue());
        credentials.put(PASS_KEY_MAP, retrievedSecretPass.getValue());

        yield credentials;
      }
      case ORIGIN_VARS -> {
        credentials.put(USER_KEY_MAP, usernameText);
        credentials.put(PASS_KEY_MAP, passwordText);
        yield credentials;
      }
      default -> throw new IllegalStateException("Unexpected origin: " + origin);
    };
  }

  public boolean credentialsComplete() {
    return credentials.size() == 2;
  }
}
