/**
 * Abstract provider so that the OAuth flow can be implemented generically for any provider that
 * uses OAuth 2.0
 *
 * @author: Jason Hamilton
 * @created: 8/12/2026
 * @since: 0.1.0
 */

package Service;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.sun.net.httpserver.HttpServer;
import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractOAuth2Provider implements OAuthProvider {

  private static final String CONFIG_RESOURCE = "/oauth.properties";
  private static final int CALLBACK_TIMEOUT_MINUTES = 2;

  /**
   * This provider's key, which is used to look up its client id/secret in oauth.properties and to
   * identify it in the Profile object.
   *
   * @return the provider's key
   */
  protected abstract String getProviderKey();

  /**
   * The API details for this provider (endpoint URLs, request signing, etc).
   *
   * @return the ScribeJava API instance for this provider
   */
  protected abstract DefaultApi20 getApi();

  /**
   * The OAuth scopes to request
   *
   * @return the scope string
   */
  protected abstract String getScope();

  /**
   * The URL of this provider's userinfo/profile endpoint
   *
   * @return the userinfo endpoint URL
   */
  protected abstract String getUserInfoUrl();

  /**
   * The userinfo JSON field name with the provider's user
   *
   * @return the subject field name, or null if this provider's userinfo response doesn't include
   * one
   */
  protected abstract String getSubjectField();

  /**
   * The userinfo JSON field name holding the user's email address.
   *
   * @return the email field name
   */
  protected abstract String getEmailField();

  /**
   * The userinfo JSON field name holding the user's display name.
   *
   * @return the display name field name
   */
  protected abstract String getDisplayNameField();

  @Override
  public String getProviderName() {
    return getProviderKey();
  }

  @Override
  public Profile authenticate() throws Exception {
    Properties config = loadConfig();

    String prefix = getProviderKey();
    String clientId = require(config, prefix + ".client.id");
    String clientSecret = require(config, prefix + ".client.secret");
    String redirectUri = require(config, prefix + ".redirect.uri");

    int port = URI.create(redirectUri).getPort();

    OAuth20Service service = new ServiceBuilder(clientId)
        .apiSecret(clientSecret)
        .defaultScope(getScope())
        .callback(redirectUri)
        .build(getApi());

    String state = UUID.randomUUID().toString();
    CompletableFuture<String> authorizationCode = new CompletableFuture<>();

    HttpServer callbackServer = startCallbackServer(port, state, authorizationCode);

    try {

      String authorizationUrl = service.createAuthorizationUrlBuilder().state(state).build();
      Desktop.getDesktop().browse(URI.create(authorizationUrl));

      String code = authorizationCode.get(CALLBACK_TIMEOUT_MINUTES, TimeUnit.MINUTES);
      OAuth2AccessToken accessToken = service.getAccessToken(code);
      return fetchProfile(service, accessToken);

    } finally {

      callbackServer.stop(0);
    }
  }

  /**
   * Starts local HTTP server only to get the authorization data back from the oauth provider
   *
   * @param port              the local port to listen on
   * @param expectedState     the state value to check the redirect against
   * @param authorizationCode authorization code from the redirect
   * @return the running server
   */
  private HttpServer startCallbackServer(int port, String expectedState,
      CompletableFuture<String> authorizationCode) throws IOException {

    HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
    server.createContext("/callback", exchange -> {
      String query = exchange.getRequestURI().getQuery();
      String code = queryParam(query, "code");
      String state = queryParam(query, "state");
      boolean ok = code != null && expectedState.equals(state);

      String responseBody = ok
          ? "<html><body>Signed in. Close this tab and go back to Otter Do Better.</body></html>"
          : "<html><body>Something went wrong. Go back to Otter Do Better and try again.</body></html>";
      byte[] bytes = responseBody.getBytes(StandardCharsets.UTF_8);
      exchange.sendResponseHeaders(200, bytes.length);
      try (OutputStream os = exchange.getResponseBody()) {
        os.write(bytes);
      }

      if (ok) {
        authorizationCode.complete(code);
      } else {
        authorizationCode.completeExceptionally(
            new IllegalStateException(getProviderKey()
                + " callback was missing a code or its state didn't match what we sent"));
      }
    });
    server.start();
    return server;

  }

  /**
   * Calls this provider's userinfo endpoint and pulls out the fields this app cares about.
   *
   * @param service     the OAuth service used to sign the request
   * @param accessToken the token received from the code exchange
   * @return the user's identity
   */
  private Profile fetchProfile(OAuth20Service service, OAuth2AccessToken accessToken)
      throws Exception {
    OAuthRequest request = new OAuthRequest(Verb.GET, getUserInfoUrl());
    service.signRequest(accessToken, request);
    try (Response response = service.execute(request)) {
      String body = response.getBody();
      String subject = jsonField(body, getSubjectField());
      String email = jsonField(body, getEmailField());
      String displayName = jsonField(body, getDisplayNameField());
      if (email == null || email.isBlank()) {
        throw new IllegalStateException(getProviderKey() + " didn't return an email address");
      }
      return new Profile(getProviderKey(), subject, email, displayName);
    }
  }

  /**
   * Loads properties from the classpath
   *
   * @return the loaded config
   */
  private Properties loadConfig() throws IOException {
    Properties config = new Properties();

    try (InputStream stream = AbstractOAuth2Provider.class.getResourceAsStream(CONFIG_RESOURCE)) {

      if (stream == null) {
        throw new IOException("Missing " + CONFIG_RESOURCE + " in classpath");
      }

      config.load(stream);
    }
    return config;
  }

  /**
   * Reads a required, non-blank config value
   *
   * @param config the loaded properties
   * @param key    the property key to read
   * @return the value
   */
  private String require(Properties config, String key) throws IOException {
    String value = config.getProperty(key);
    if (value == null || value.isBlank()) {
      throw new IOException("Missing " + key + " in oauth.properties");
    }
    return value;
  }

  /**
   * Pulls a single value out of a URL query string
   *
   * @param query the query string
   * @param key   the parameter name to look up
   * @return the decoded value
   */
  private String queryParam(String query, String key) {
    if (query == null) {
      return null;
    }

    for (String pair : query.split("&")) {

      String[] parts = pair.split("=", 2);

      if (parts.length == 2 && parts[0].equals(key)) {
        return URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
      }

    }

    return null;
  }

  /**
   * Reads a JSON field from a raw response body.
   *
   * @param json the raw JSON response body
   * @param key  the top-level field name to extract
   * @return the field's value
   */
  private String jsonField(String json, String key) {
    if (key == null) {
      return null;
    }
    // Use a regex to find the field - then extract the value
    Matcher matcher = Pattern.compile("\"" + key + "\"\\s*:\\s*\"([^\"]*)\"").matcher(json);
    return matcher.find() ? matcher.group(1) : null;
  }
}
