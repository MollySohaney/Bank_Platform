package sohaney.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import sohaney.models.Account;
import sohaney.models.ExtAccount;

/**
 * This service contains people related functions.
 */
@Service
public class AccountService {
    // dataSource enables talking to the database.
    private final DataSource dataSource;

    /**
     * See AuthInterceptor notes regarding dependency injection and
     * inversion of control.
     */
    public AccountService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

	public Account createAccount(String name, double init_balance, int user_id) {
		System.out.println("here");
		final String sql = "insert into Account (name, balance, user_id, acc_id) values (?, ?, ?, ?);";
		final String accIdSql = "select max(acc_id) from Account;";
		Account account = new Account(0, "default", 0);
		try (Connection conn = dataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			PreparedStatement accIdPstmt = conn.prepareStatement(accIdSql)) {
			int rowsAffected = 0;
			int accId = 0;
			ResultSet rs = accIdPstmt.executeQuery();
			rs.next();
			accId = rs.getInt("max(acc_id)");
			while (rowsAffected == 0) {
				accId++;
				pstmt.setString(1, name);
				pstmt.setDouble(2, init_balance);
				pstmt.setInt(3, user_id);
				pstmt.setInt(4, accId);
				rowsAffected = pstmt.executeUpdate();
			}
			account = new Account(accId, name, init_balance);
		} catch (SQLException e) {
					System.out.println("here 2");
			e.printStackTrace();
			return account;
		}
		return account;
	}

    /**
     * This function should query and return all accounts
	 * owned by a given user, specified by user ID.
     */
    public List<Account> getAccounts(int userId) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        final String sql = "select * from Account where user_id = ?;";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int acc_id = rs.getInt("acc_id");
                    String name = rs.getString("name");
                    double balance = rs.getDouble("balance");
                    accounts.add(new Account(acc_id, name, balance));
                }
            }
        }
        return accounts;
    }
	
	/**
     * This function should query and return a given account
	 * owned by a given user, specified by user ID.
     */
    public Account getOneAccount(int userId, int acc_id) throws SQLException {
        final String sql = "select * from Account where user_id = ? and acc_id = ?;";
		Account account = null;
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
			pstmt.setInt(2, acc_id);
            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                String name = rs.getString("name");
                double balance = rs.getDouble("balance");
                account = new Account(acc_id, name, balance);
            }
        }
        return account;
    }

	public List<ExtAccount> getAllAccounts(int accId) throws SQLException {
        List<ExtAccount> accounts = new ArrayList<>();
        final String sql = "select a.acc_id, a.name, a.balance, u.fname, u.lname from Account a, User u where a.user_id=u.user_id and not a.acc_id = ?;";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, accId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int acc_id = rs.getInt("acc_id");
                    String name = rs.getString("name");
                    double balance = rs.getDouble("balance");
					String owner_name = rs.getString("fname") + " " + rs.getString("lname");
                    accounts.add(new ExtAccount(acc_id, name, balance, owner_name));
                }
            }
        }
        return accounts;
    }

}
