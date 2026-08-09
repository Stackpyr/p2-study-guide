package Data;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for Account
 *
 * @author: Jason Hamilton
 * @created: 7/30/2026
 * @since: 0.1.0
 */

class AccountTest {

  /**
   * LLM GENERATED - Verifies that constructing an Account with only an email address stores
   * that email and defaults the username to the same value.
   */
  @Test
  void constructor_validEmail_setsEmailAndDefaultsUsernameToEmail() {
    Account account = new Account("jane@example.com");
    assertEquals("jane@example.com", account.getEmailAddress());
    assertEquals("jane@example.com", account.getUsername());
  }

  /**
   * LLM GENERATED - Verifies that constructing an Account with both an email address and a
   * username stores both values independently.
   */
  @Test
  void constructor_validEmailAndUsername_setsBoth() {
    Account account = new Account("jane@example.com", "jdoe");
    assertEquals("jane@example.com", account.getEmailAddress());
    assertEquals("jdoe", account.getUsername());
  }

  /**
   * LLM GENERATED - Verifies that a blank (whitespace-only) username falls back to the email
   * address, the same as a null username would.
   */
  @Test
  void constructor_blankUsername_fallsBackToEmail() {
    Account account = new Account("jane@example.com", "   ");
    assertEquals("jane@example.com", account.getUsername());
  }

  /**
   * LLM GENERATED - Verifies that an invalid email address (missing "@" or ".") fails
   * validation and leaves both email and username unset.
   */
  @Test
  void constructor_invalidEmail_leavesFieldsUnset() {
    Account account = new Account("not-an-email");
    assertNull(account.getEmailAddress());
    assertNull(account.getUsername());
  }

  /**
   * LLM GENERATED - Verifies that accountId defaults to 0 when an Account is built through the
   * public constructors, since only the database (via the package-private hydration
   * constructor) ever assigns a real id.
   */
  @Test
  void getAccountId() {
    // accountId is only populated via the package-private hydration constructor that
    // AccountRepository.mapRow() uses - exercised over there, not here. Through the
    // public constructors it just holds its default value until the DB assigns one.
    Account account = new Account("jane@example.com");
    assertEquals(0, account.getAccountId());
  }

  /**
   * LLM GENERATED - Verifies that toString() includes both the username and email address.
   */
  @Test
  void testToString_includesUsernameAndEmail() {
    Account account = new Account("jane@example.com", "jdoe");
    String result = account.toString();
    assertTrue(result.contains("jdoe"));
    assertTrue(result.contains("jane@example.com"));
  }

  /**
   * LLM GENERATED - Verifies that two Accounts with the same username are considered equal,
   * regardless of email address.
   */
  @Test
  void testEquals_sameUsername_isEqual() {
    // equals() only compares username and accountId
    Account a = new Account("jane@example.com", "jdoe");
    Account b = new Account("someone-else@example.com", "jdoe");
    assertEquals(a, b);
  }

  /**
   * LLM GENERATED - Verifies that two Accounts with different usernames are not considered
   * equal.
   */
  @Test
  void testEquals_differentUsername_isNotEqual() {
    Account a = new Account("jane@example.com", "jdoe");
    Account b = new Account("jane@example.com", "jsmith");
    assertNotEquals(a, b);
  }

  /**
   * LLM GENERATED - Verifies that verifyPassword() returns true when given the same plaintext
   * password that was just hashed and stored via setPassword().
   */
  @Test
  void setPassword_thenVerifyPassword_correctPassword_returnsTrue() {
    Account account = new Account("jane@example.com");
    account.setPassword("correct horse battery staple");
    assertTrue(account.verifyPassword("correct horse battery staple"));
  }

  /**
   * LLM GENERATED - Verifies that verifyPassword() returns false for a password that doesn't
   * match the stored hash.
   */
  @Test
  void verifyPassword_wrongPassword_returnsFalse() {
    Account account = new Account("jane@example.com");
    account.setPassword("correct horse battery staple");
    assertFalse(account.verifyPassword("wrong password"));
  }

  /**
   * LLM GENERATED - Verifies that verifyPassword() returns false (rather than throwing) when
   * no password has been set yet - relevant for a future OAuth-only account with no local
   * password at all.
   */
  @Test
  void verifyPassword_noPasswordSet_returnsFalse() {
    // relevant for a future OAuth-only account, which won't have a local password at all
    Account account = new Account("jane@example.com");
    assertFalse(account.verifyPassword("anything"));
  }

  /**
   * LLM GENERATED - Verifies that hashing the same plaintext password for two different
   * accounts produces a different salt and a different hash for each.
   */
  @Test
  void setPassword_generatesDifferentSaltAndHashPerAccount() {
    Account a = new Account("jane@example.com");
    Account b = new Account("john@example.com");
    a.setPassword("samepassword");
    b.setPassword("samepassword");
    assertNotEquals(a.getPasswordSalt(), b.getPasswordSalt());
    assertNotEquals(a.getPasswordHash(), b.getPasswordHash());
  }

  /**
   * LLM GENERATED - Verifies the display name getter/setter round-trip.
   */
  @Test
  void displayName_getterAndSetter() {
    Account account = new Account("jane@example.com");
    account.setDisplayName("Jane Doe");
    assertEquals("Jane Doe", account.getDisplayName());
  }

  /**
   * LLM GENERATED - Verifies that passwordHash and passwordSalt are null before setPassword()
   * is called, and non-null immediately afterward.
   */
  @Test
  void getPasswordHash_and_getPasswordSalt_populatedBySetPassword() {
    Account account = new Account("jane@example.com");
    assertNull(account.getPasswordHash());
    assertNull(account.getPasswordSalt());
    account.setPassword("testpassword");
    assertNotNull(account.getPasswordHash());
    assertNotNull(account.getPasswordSalt());
  }

  /**
   * LLM GENERATED - Verifies that isActive defaults to true and can be toggled via setIsActive().
   */
  @Test
  void isActive_defaultsTrue_andCanBeChanged() {
    Account account = new Account("jane@example.com");
    assertTrue(account.getIsActive());
    account.setIsActive(false);
    assertFalse(account.getIsActive());
  }

  /**
   * LLM GENERATED - Verifies that isAdmin defaults to false and can be toggled via setIsAdmin().
   */
  @Test
  void isAdmin_defaultsFalse_andCanBeChanged() {
    Account account = new Account("jane@example.com");
    assertFalse(account.getIsAdmin());
    account.setIsAdmin(true);
    assertTrue(account.getIsAdmin());
  }
}
