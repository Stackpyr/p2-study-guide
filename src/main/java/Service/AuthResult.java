/**
 * This enum represents the result of an authentication attempt.
 *
 * @author: Jason Hamilton
 * @created: 7/31/2026
 * @since: 0.1.0
 */


package Service;


public enum AuthResult {
  // Success
  SUCCESS(0, "Authenticated successfully"),

  // Errors
  USERNAME_NOT_FOUND(-1, "Username was not found"),
  INCORRECT_PASSWORD(-2, "Incorrect password"),
  INCORRECT_USER_PASSWORD(-3, "Incorrect username or password"),
  ACCOUNT_NOT_ACTIVE(-4, "Account is inactive. Contact your admin."),

  // VALIDATION
  USERNAME_BLANK(-10, "Username is required"),
  PASSWORD_BLANK(-11, "Password is required");

  private final int code;
  private final String message;

  AuthResult(int code, String message) {
    this.code = code;
    this.message = message;
  }

  /**
   * Gets the numeric code value.
   *
   * @return The code number
   */
  public int getCode() {
    return code;
  }

  /**
   * Gets the human-readable message.
   *
   * @return Description of this code
   */
  public String getMessage() {
    return message;
  }

  /**
   * Checks if this code represents success.
   *
   * @return True if this is SUCCESS
   */
  public boolean isSuccess() {
    return this == SUCCESS;
  }

  /**
   * Checks if this code represents an error.
   *
   * @return True if this is not SUCCESS
   */
  public boolean isError() {
    return this != SUCCESS;
  }

  @Override
  public String toString() {
    return String.format("%s: %n", name(), code, message);
  }
}
