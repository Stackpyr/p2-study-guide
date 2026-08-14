/**
 * LinkedIn OAuth provider - implementation of OAuthProvider
 *
 * @author: Jason Hamilton
 * @created: 8/12/2026
 * @since: 0.1.0
 */

package Service;

import com.github.scribejava.apis.LinkedInApi20;
import com.github.scribejava.core.builder.api.DefaultApi20;

public class LinkedInOAuthProvider extends AbstractOAuth2Provider {

  @Override
  protected String getProviderKey() {
    return "linkedin";
  }

  @Override
  protected DefaultApi20 getApi() {
    return LinkedInApi20.instance();
  }

  @Override
  protected String getScope() {
    return "openid profile email";
  }

  @Override
  protected String getUserInfoUrl() {
    return "https://api.linkedin.com/v2/userinfo";
  }

  @Override
  protected String getSubjectField() {
    return "sub";
  }

  @Override
  protected String getEmailField() {
    return "email";
  }

  @Override
  protected String getDisplayNameField() {
    return "name";
  }
}
