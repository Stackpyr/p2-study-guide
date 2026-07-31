/**
 * [Explanation]
 *
 * @author: Jason Hamilton
 * @created: 7/30/2026
 * @since: 0.1.0
 */

package Utilities;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Pdkdf2PasswordHasher implements PasswordHasher {

  @Override
  public String generateSalt() {
    byte[] saltBytes = new byte[16];
    new java.security.SecureRandom().nextBytes(saltBytes);
    return new String(saltBytes);
  }

  @Override
  public String hashPassword(String password, String salt) {
    try {
      KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 128);
      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      byte[] hash = factory.generateSecret(spec).getEncoded();
      return new String(hash);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      System.out.println("Error hashing password: " + e.getMessage());
    }
    return null;
  }

  @Override
  public boolean verifyPassword(String password, String hash) {
    String hashedPassword = hashPassword(password, hash);
    return hashedPassword != null && hashedPassword.equals(hash);
  }
}
