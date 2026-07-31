package Utilities;

/**
 * [Explanation]
 *
 * @author: Jason Hamilton
 * @created: 7/30/2026
 * @since: 0.1.0
 */

public interface PasswordHasher {
  String generateSalt();
  String hashPassword(String password, String salt);
  boolean verifyPassword(String password, String hash);
}

