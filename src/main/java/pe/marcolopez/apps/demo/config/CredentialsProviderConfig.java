package pe.marcolopez.apps.demo.config;

import io.quarkus.arc.Unremovable;
import io.quarkus.credentials.CredentialsProvider;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;
import static pe.marcolopez.apps.demo.util.Constants.*;

@ApplicationScoped
@Unremovable
@Named("credentials-provider-config")
public class CredentialsProviderConfig implements CredentialsProvider {

  @Inject
  OriginCredentialsConfig originCredentialsConfig;

  @Override
  public Map<String, String> getCredentials(String credentialsProviderName) {
    while (!originCredentialsConfig.credentialsComplete()) {
      var map = originCredentialsConfig.getCredentials();
      DB_USERNAME = map.get(USER_KEY_MAP);
      DB_PASSWORD = map.get(PASS_KEY_MAP);
    }

    return Map.of(
        USER_PROPERTY_NAME, DB_USERNAME,
        PASSWORD_PROPERTY_NAME, DB_PASSWORD
    );
  }
}

