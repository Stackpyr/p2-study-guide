/**
 * [Explanation]
 *
 * @author: Jason Hamilton
 * @created: 7/30/2026
 * @since: 0.1.0
 */

package Utilities;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Pdkdf2PasswordHasher implements PasswordHasher {

  @Override
  public String generateSalt() {
    byte[] saltBytes = new byte[16];
    new java.security.SecureRandom().nextBytes(saltBytes);
    // Base64-encode the raw bytes; converting binary data with `new String(bytes)`
    // uses the platform default charset and can corrupt the value (it is not
    // guaranteed to be valid text), which would make later verifyPassword() calls
    // fail unpredictably across environments.
    return Base64.getEncoder().encodeToString(saltBytes);
  }

  @Override
  public String hashPassword(String password, String salt) {
    try {
      KeySpec spec = new PBEKeySpec(password.toCharArray(), Base64.getDecoder().decode(salt), 65536, 128);
      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      byte[] hash = factory.generateSecret(spec).getEncoded();
      // Same issue as generateSalt(): encode raw bytes as Base64 instead of
      // lossily reinterpreting them as platform-default-charset text.
      return Base64.getEncoder().encodeToString(hash);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      System.out.println("Error hashing password: " + e.getMessage());
    }
    return null;
  }

  @Override
  public boolean verifyPassword(String password, String hash, String salt) {
    String hashedPassword = hashPassword(password, salt);
    return hashedPassword != null && hashedPassword.equals(hash);
  }
}
