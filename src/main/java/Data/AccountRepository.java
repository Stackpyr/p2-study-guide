/**
 *  Responsible for CRUD operations on the account table and hydrating the AccountDao object
 *
 * @author: Jason Hamilton
 * @created: 7/30/2026
 * @since: 0.1.0
 */

package Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AccountRepository {

  private final Connection conn;
  private final String GET_ALL_ACCOUNTS_CMD = "SELECT * FROM account ORDER BY account_id";
  private final String GET_BY_ID_CMD = "SELECT * FROM account WHERE account_id = ?";
  private final String GET_BY_USERNAME_CMD = "SELECT * FROM account WHERE username = ?";
  private final String GET_BY_OAUTH_IDENTITY_CMD = "SELECT * FROM account WHERE oauth_provider = ? AND oauth_subject = ?";
  private final String INSERT_CMD = "INSERT INTO account (username, email, password_hash, password_salt, display_name, is_active, is_admin, oauth_provider, oauth_subject) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
  private final String UPDATE_PWD_CMD = "UPDATE account SET password_hash = ?, password_salt = ?, updated_at = CURRENT_TIMESTAMP WHERE account_id = ?";
  private final String UPDATE_ACTIVE_CMD = "UPDATE account SET is_active = ?, updated_at = CURRENT_TIMESTAMP WHERE account_id = ?";
  private final String UPDATE_ADMIN_CMD = "UPDATE account SET is_admin = ?, updated_at = CURRENT_TIMESTAMP WHERE account_id = ?";
  private final String UPDATE_OAUTH_IDENTITY_CMD = "UPDATE account SET oauth_provider = ?, oauth_subject = ?, updated_at = CURRENT_TIMESTAMP WHERE account_id = ?";
  private final String DELETE_ACCOUNT_CMD = "DELETE FROM account WHERE account_id = ?";

  /**
   * Constructor for AccountRepository
   */
  public AccountRepository() {
    conn = DatabaseManager.getConnection();
  }

  /**
   * Adds a new account to the database
   * @param account AccountDao object to be added
   * @return AccountDao object with the newly added account's information
   */
  public Account addAccount(Account account) {
    try (PreparedStatement addStmt = conn.prepareStatement(INSERT_CMD,
        Statement.RETURN_GENERATED_KEYS)) {
      addStmt.setString(1, account.getUsername());
      addStmt.setString(2, account.getEmailAddress());
      addStmt.setString(3, account.getPasswordHash());
      addStmt.setString(4, account.getPasswordSalt());
      addStmt.setString(5, account.getDisplayName());
      addStmt.setBoolean(6, account.getIsActive());
      addStmt.setBoolean(7, account.getIsAdmin());
      addStmt.setString(8, account.getOauthProvider());
      addStmt.setString(9, account.getOauthSubject());
      addStmt.executeUpdate();

      try (ResultSet rs = addStmt.getGeneratedKeys()) {
        if (!rs.next()) {
          System.out.println("Error: No keys generated when inserting account");
        } else {
          return getById(rs.getInt(1)); // return the newly added account
        }
      }
    } catch (SQLException e) {
      System.out.println("Error: " + e.getMessage());
    }
    return null;
  }

  /**
   * Retrieves an account by its ID
   * @param accountId ID of the account to retrieve
   * @return AccountDao object with the account's information
   */
  public Account getById(int accountId) {
    try (PreparedStatement selectStmt = conn.prepareStatement(GET_BY_ID_CMD)) {
      selectStmt.setInt(1, accountId);

      try (ResultSet rs = selectStmt.executeQuery()) {
        if (rs.next()) { // advance to the first row
          return mapRow(rs); // map the row to an AccountDao object
        }
        return null;
      }
    } catch (SQLException e) {
      System.out.println("Error: " + e.getMessage());
    }
    return null;
  }

  /**
   * Retrieves all accounts from the database
   * @return List of AccountDao objects with the accounts' information
   */
  public List<Account> getAllAccounts() {
    List<Account> accounts = new ArrayList<>();
    try (PreparedStatement selectStmt = conn.prepareStatement(GET_ALL_ACCOUNTS_CMD)) {
      try (ResultSet rs = selectStmt.executeQuery()) {
        while (rs.next()) {
          accounts.add(mapRow(rs));
        }
      }
    } catch (SQLException e) {
      System.out.println("Error: " + e.getMessage());
    }
    return accounts;
  }

  /**
   * Retrieves an account by its username
   * @param username Username of the account to retrieve
   * @return AccountDao object with the account's information
   */
  public Account getByUsername(String username) {
    try (PreparedStatement selectStmt = conn.prepareStatement(GET_BY_USERNAME_CMD)) {
      selectStmt.setString(1, username);

      try (ResultSet rs = selectStmt.executeQuery()) {
        if (rs.next()) { // advance to the first row
          return mapRow(rs); // map the row to an AccountDao object
        }
        return null;
      }
    } catch (SQLException e) {
      System.out.println("Error: " + e.getMessage());
    }
    return null;
  }

  /**
   * Retrieves an account by its social identity.
   * @param provider the provider key
   * @param subject the provider's user id
   * @return AccountDao object with the account's information
   */
  public Account getByOAuthIdentity(String provider, String subject) {
    try (PreparedStatement selectStmt = conn.prepareStatement(GET_BY_OAUTH_IDENTITY_CMD)) {
      selectStmt.setString(1, provider);
      selectStmt.setString(2, subject);

      try (ResultSet rs = selectStmt.executeQuery()) {
        if (rs.next()) {
          return mapRow(rs);
        }
        return null;
      }
    } catch (SQLException e) {
      System.out.println("Error: " + e.getMessage());
    }
    return null;
  }

  /**
   * Links an existing account to a social identity
   * @param accountId ID of the account to link
   * @param provider the provider key
   * @param subject the provider's user id
   */
  public void linkOAuthIdentity(int accountId, String provider, String subject) {
    try (PreparedStatement stmt = conn.prepareStatement(UPDATE_OAUTH_IDENTITY_CMD)) {
      stmt.setString(1, provider);
      stmt.setString(2, subject);
      stmt.setInt(3, accountId);
      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  /**
   * Updates the password of an account
   * @param accountId ID of the account to update
   * @param hash New password hash
   * @param salt New password salt
   */
  public void updatePassword(int accountId, String hash, String salt) {
    try (PreparedStatement stmt = conn.prepareStatement(UPDATE_PWD_CMD)) {
      stmt.setString(1, hash);
      stmt.setString(2, salt);
      stmt.setInt(3, accountId);
      // update the password hash and salt
      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  /**
   * Updates the admin status of an account
   * @param accountId ID of the account to update
   * @param isAdmin New admin status
   */
  public void updateAdmin(int accountId, boolean isAdmin) {
    try (PreparedStatement stmt = conn.prepareStatement(UPDATE_ADMIN_CMD)) {
      stmt.setBoolean(1, isAdmin);
      stmt.setInt(2, accountId);
      // set the admin status to either true or false
      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  /**
   * Updates the active status of an account
   * @param accountId ID of the account to update
   * @param isActive New active status
   */
  public void updateStatus(int accountId, boolean isActive) {
    try (PreparedStatement stmt = conn.prepareStatement(UPDATE_ACTIVE_CMD)) {
      stmt.setBoolean(1, isActive);
      stmt.setInt(2, accountId);
      // set the status to either active or inactive
      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  /**
   * Deletes an account. If the account cannot be deleted due to a referential integrity constraint,
   * the account will instead be marked as inactive.
   * @param accountId ID of the account to delete
   */
  public void deleteAccount(int accountId) {
    try (PreparedStatement stmt = conn.prepareStatement(DELETE_ACCOUNT_CMD)) {
      stmt.setInt(1, accountId);
      // delete the account
      stmt.executeUpdate();
    } catch (SQLException e) {
      if (e.getMessage().contains("FOREIGN KEY constraint failed")) {
        // Mark the account as inactive instead of deleting it
        updateStatus(accountId, false);
      } else {
        System.out.println("Error: " + e.getMessage());
      }
    }
  }

  /**
   * Maps a ResultSet row to an AccountDao object
   * @param rs ResultSet row to map
   * @return AccountDao object with the mapped data
   */
  private Account mapRow(ResultSet rs) {
    try {
      // map each column to a field in the AccountDao object
      return new Account(
          rs.getInt("account_id"),
          rs.getString("username"),
          rs.getString("email"),
          rs.getString("display_name"),
          rs.getBoolean("is_active"),
          rs.getBoolean("is_admin"),
          rs.getString("password_hash"),
          rs.getString("password_salt"),
          rs.getString("oauth_provider"),
          rs.getString("oauth_subject")
      );
    } catch (SQLException ex) {
      System.out.println("Error: " + ex.getMessage());
    }
    return null;
  }
}
