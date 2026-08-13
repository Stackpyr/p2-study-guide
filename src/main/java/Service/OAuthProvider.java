/**
 * A generic OAuth provider interface used to implement OAuth providers for different providers
 *
 * @author: Jason Hamilton
 * @created: 8/12/2026
 * @since: 0.1.0
 */

package Service;

public interface OAuthProvider {

  /**
   * The name of the provider (e.g., LinkedIn, Google, etc.)
   *
   * @return the provider's name
   */
  String getProviderName();

  /**
   * Runs this provider's full auth flow through redirection to the provider's website.
   *
   * @return the signed-in user's identity
   * @throws Exception if the flow fails for any reason (missing config, timeout, provider
   *     error, etc.)
   */
  Profile authenticate() throws Exception;

  /**
   * A record representing a user's profile information returned by the OAuth provider.
   *
   * @param provider the provider's key
   * @param subject the provider's user id
   * @param email the user's email address
   * @param displayName the user's display name
   */
  record Profile(String provider, String subject, String email, String displayName) {
  }
}
