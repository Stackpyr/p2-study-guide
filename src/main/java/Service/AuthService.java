/**
 * AuthService handles the authentication of users and session management. This is a singleton and can be used throughout the application
 * to see who is logged in and if they are an admin.
 *
 * @author: Jason Hamilton
 * @created: 7/31/2026
 * @since: 0.1.0
 */


package Service;

import Data.Account;
import Data.AccountRepository;
import Utilities.PasswordHasher;
import Utilities.Pdkdf2PasswordHasher;

public class AuthService {

  private static AuthService instance;
  private final AccountRepository accountRepository;
  private Account currentAccount;

  /**
   * Private constructor for AuthService since this is a singleton class
   */
  private AuthService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  /**
   * Gets the instance of the AuthService
   * @return instance of AuthService
   */
  public static AuthService getInstance() {
    if (instance == null) {
      instance = new AuthService(new AccountRepository());
    }
    return instance;
  }

  /**
   * Logs in a user
   * @param username Username of the user
   * @param password Password of the user
   * @return AuthResult indicating success or failure
   */
  public AuthResult login(String username, String password) {
    if (isLoggedIn()) {
      logout(); // reset the current account
    }

    if (username == null || username.isBlank()) {
      return AuthResult.USERNAME_BLANK;
    } else if (password == null || password.isBlank()) {
      return AuthResult.PASSWORD_BLANK;
    }

    // try to find the account
    boolean success = false;
    Account match = accountRepository.getByUsername(username);
    if (match != null) {
      // check password
      success = match.verifyPassword(password);
      if (success) {
        // check to see if the account is active
        if (!match.getIsActive()) {
          return AuthResult.ACCOUNT_NOT_ACTIVE;
        }
      }
    }

    if (success) {
      currentAccount = match;
      return AuthResult.SUCCESS;
    } else {
      // user wasn't found, or password didn't match - return failure
      return AuthResult.INCORRECT_USER_PASSWORD;
    }
  }

  /**
   * Logs in a user using OAuth2 authentication
   *
   * @param profile the verified identity a provider returned
   * @return AuthResult indicating success or failure
   */
  public AuthResult loginWithOAuth(OAuthProvider.Profile profile) {
    if (isLoggedIn()) {
      logout(); // reset the current account
    }

    if (profile == null || profile.email() == null || profile.email().isBlank()) {
      return AuthResult.USERNAME_BLANK;
    }

    Account account = accountRepository.getByOAuthIdentity(profile.provider(), profile.subject());

    if (account == null) {
      // not linked yet - try to find an account with the same username and link it
      account = accountRepository.getByUsername(profile.email());
      if (account != null) {
        // found an account with the same username - link the identity
        accountRepository.linkOAuthIdentity(account.getAccountId(), profile.provider(),
            profile.subject());
        account.setOauthIdentity(profile.provider(), profile.subject());
      }
    }

    if (account == null) {
      // not linked yet - create a new account
      account = new Account(profile.email()); // username defaults to email

      if (profile.displayName() != null && !profile.displayName().isBlank()) {
        account.setDisplayName(profile.displayName());
      }

      account.setOauthIdentity(profile.provider(), profile.subject());
      account = accountRepository.addAccount(account);

      if (account == null) {
        return AuthResult.INCORRECT_USER_PASSWORD; // insert failed for some other reason
      }
    }

    if (!account.getIsActive()) {
      return AuthResult.ACCOUNT_NOT_ACTIVE;
    }

    currentAccount = account;
    return AuthResult.SUCCESS;
  }

  public void logout() {
    currentAccount = null;
  }

  public boolean isLoggedIn() {
    return currentAccount != null;
  }

  public boolean isAdmin() {
    return isLoggedIn() && currentAccount.getIsAdmin();
  }

  public Account getCurrentAccount() {
    return currentAccount;
  }

  /**
   * Resets the singleton instance for testing purposes
   */
  public static void resetForTesting() {
    instance = null;
  }
}
