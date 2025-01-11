package sohaney.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sohaney.models.SingleTransaction;
import sohaney.models.DoubleTransaction;
import sohaney.models.Account;

/**
 * This service contains transaction related functions.
 */
@Service
public class TransactionService {

    private final DataSource dataSource;
    private final UserService userService;

    @Autowired
    public TransactionService(DataSource dataSource, UserService userService) {
        this.dataSource = dataSource;
        this.userService = userService;
    }

	/**
	 * This function handles the addition of money to an account's balance.
	 */
	public boolean deposit(int acc_id, int userId, double amount) {
		final String owns = "select * from Account where user_id = ? and acc_id = ? and (balance+?)<=1000000000000;";
		final String sql1 = "insert into Transaction (amount, date, time, acc_id) values (?, ?, ?, ?);";
		final String sql2 = "update Account set balance = balance+? where acc_id = ?;";
		try (Connection conn = dataSource.getConnection();
			PreparedStatement ownsStmt = conn.prepareStatement(owns);
			PreparedStatement pstmt1 = conn.prepareStatement(sql1);
			PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {
			ownsStmt.setInt(1, userId);
			ownsStmt.setInt(2, acc_id);
			ownsStmt.setDouble(3, amount);
			ResultSet rs = ownsStmt.executeQuery();
			if (!rs.next()) {
				conn.rollback();
				return false; // false if the user doesn't own the account or if this would set the balance over $1 trillion
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            String date = dateFormat.format(new Date());
			String time = timeFormat.format(new Date());
			conn.setAutoCommit(false);
			pstmt1.setDouble(1, amount);
			pstmt1.setString(2, date);
			pstmt1.setString(3, time);
			pstmt1.setInt(4, acc_id);
			pstmt2.setDouble(1, amount);
			pstmt2.setInt(2, acc_id);
			pstmt2.executeUpdate();
			pstmt1.executeUpdate();
			System.out.println("WORKED");
			conn.commit();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This function handles the withdrawal of money from an account.
	 */
	public boolean withdraw(int acc_id, int userId, double amount) {
		amount = (0-amount);
		final String owns = "select * from Account where user_id = ? and acc_id = ? and balance>=?;";
		final String sql1 = "insert into Transaction (amount, date, time, acc_id) values (?, ?, ?, ?);";
		final String sql2 = "update Account set balance = balance+? where acc_id = ?;";
		try (Connection conn = dataSource.getConnection();
			PreparedStatement ownsStmt = conn.prepareStatement(owns);
			PreparedStatement pstmt1 = conn.prepareStatement(sql1);
			PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {
			ownsStmt.setInt(1, userId);
			ownsStmt.setInt(2, acc_id);
			ownsStmt.setDouble(3, amount);
			ResultSet rs = ownsStmt.executeQuery();
			if (!rs.next()) {
				conn.rollback();
				return false; // false if the user doesn't own the account or the balance is insufficient
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            String date = dateFormat.format(new Date());
			String time = timeFormat.format(new Date());
			conn.setAutoCommit(false);
			pstmt1.setDouble(1, amount);
			pstmt1.setString(2, date);
			pstmt1.setString(3, time);
			pstmt1.setInt(4, acc_id);
			pstmt2.setDouble(1, amount);
			pstmt2.setInt(2, acc_id);
			pstmt2.executeUpdate();
			pstmt1.executeUpdate();
			conn.commit();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This function handles the transfer of money from one account to another.
	 */
	public boolean transfer(int acc_id, int userId, double amount, int toId) {
		final String owns = "select * from Account where user_id = ? and acc_id = ? and balance>=?;";
		final String exists = "select * from Account where acc_id = ? and (balance+?)<=1000000000000;";
		final String trId = "select max(transaction_id) from Transaction;";
		final String sql1 = "insert into Transaction (amount, date, time, acc_id, transaction_id) values (?, ?, ?, ?, ?);";
		final String sql2 = "insert into Transfer (from_acc_id, to_acc_id, transaction_id) values (?, ?, ?);";
		final String sql3 = "update Account set balance = balance-? where acc_id = ?;";
		final String sql4 = "update Account set balance = balance+? where acc_id = ?;";
		try (Connection conn = dataSource.getConnection();
			PreparedStatement ownsStmt = conn.prepareStatement(owns);
			PreparedStatement existsStmt = conn.prepareStatement(exists);
			PreparedStatement trIdStmt = conn.prepareStatement(trId);
			PreparedStatement pstmt1 = conn.prepareStatement(sql1);
			PreparedStatement pstmt2 = conn.prepareStatement(sql2);
			PreparedStatement pstmt3 = conn.prepareStatement(sql3);
			PreparedStatement pstmt4 = conn.prepareStatement(sql4)) {
			ownsStmt.setInt(1, userId);
			ownsStmt.setInt(2, acc_id);
			ownsStmt.setDouble(3, amount);
			ResultSet rs = ownsStmt.executeQuery();
			if (!rs.next()) {
				conn.rollback();
				return false; // false if the user doesn't own the account or the balance is insufficient
			}
			existsStmt.setInt(1, toId);
			existsStmt.setDouble(2, amount);
			rs = existsStmt.executeQuery();
			if (!rs.next()) {
				conn.rollback();
				return false; // false if the destination account does not exist or this would set the balance over $1 trillion
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            String date = dateFormat.format(new Date());
			String time = timeFormat.format(new Date());
			conn.setAutoCommit(false);
			rs = trIdStmt.executeQuery();
			int transfer_id;
			if (!rs.next()) {
				transfer_id = 0;
			} else {
				transfer_id = rs.getInt("max(transaction_id)");
				transfer_id++;
			}
			pstmt1.setDouble(1, amount);
			pstmt1.setString(2, date);
			pstmt1.setString(3, time);
			pstmt1.setInt(4, acc_id);
			pstmt1.setInt(5, transfer_id);
			pstmt2.setInt(1, acc_id);
			pstmt2.setInt(2, toId);
			pstmt2.setInt(3, transfer_id);
			pstmt3.setDouble(1, amount);
			pstmt3.setInt(2, acc_id);
			pstmt4.setDouble(1, amount);
			pstmt4.setInt(2, toId);
			int rowsAffected = pstmt3.executeUpdate();
			if (rowsAffected == 0) {
				conn.rollback();
				return false; // returns false if the sending account was unaffected
			}
			rowsAffected = pstmt4.executeUpdate();
			if (rowsAffected == 0) {
				conn.rollback();
				return false; // returns false if this would set the receiving account would be set to over $1000000000
			}
			pstmt1.executeUpdate();
			pstmt2.executeUpdate();
			conn.commit();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

    /**
     * This function should query and return all accounts
	 * owned by a given user, specified by user ID.
     */
    public List<Account> getAccounts(int userId) {
        List<Account> accounts = new ArrayList<>();
        // Write an SQL query to find the users that are not the current user.
        final String sql = "select * from account where userId = ?";
        // Run the query with a datasource.
        // See UserService.java to see how to inject DataSource instance and
        // use it to run a query.
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
        // Use the query result to create a list of followable users.
        // See UserService.java to see how to access rows and their attributes
        // from the query result.
        // Check the following createSampleFollowableUserList function to see 
        // how to create a list of FollowableUsers.
                while (rs.next()) {
                    int acc_id = rs.getInt("acc_id");
                    String name = rs.getString("name");
                    double balance = rs.getDouble("balance");
                    accounts.add(new Account(acc_id, name, balance));
                }
        
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Replace the following line and return the list you created.
        return accounts;
    }
	
	/**
	 * Retrieves a list of all deposits and withdrawals involving an account,
	 * if the current user owns the account.
	 */
	public List<SingleTransaction> getSingleTransactions(int acc_id) throws SQLException {
        int userId = userService.getLoggedInUser().getUserId();
		List<SingleTransaction> transactions = new ArrayList<>();
		final String sql = "select * from Transaction t, Account a where t.acc_id = ? and t.acc_id = a.acc_id and a.user_id = ? and t.transaction_id not in(select transaction_id from Transfer) sort by date desc;";
		try (Connection conn = dataSource.getConnection();
		PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, acc_id);
			pstmt.setInt(2, userId);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					int tr_id = rs.getInt("transaction_id");
					double amount = rs.getDouble("amount");
					String date = rs.getString("date");
					String time = rs.getString("time");
					boolean isDeposit = true;
					if (amount < 0) {
						isDeposit = false;
					}
					transactions.add(new SingleTransaction(tr_id, amount, date, time, acc_id, userId, isDeposit));
				}
			}
		}
		return transactions;
	}

	/**
	 * Retrieves a list of all deposits, withdrawals, and transfers
	 * between accounts involving a certain account.
	 * Does NOT show the other account in transactions between accounts.
	 */
	public List<SingleTransaction> getAllTransactions(int acc_id) throws SQLException {
		int userId = userService.getLoggedInUser().getUserId();
		List<SingleTransaction> transactions = new ArrayList<>();
		final String sql = "select * from Transaction t, Account a where t.acc_id = ? and t.acc_id = a.acc_id and a.user_id = ? sort by date desc;";
		try (Connection conn = dataSource.getConnection();
		PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, acc_id);
			pstmt.setInt(2, userId);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					int tr_id = rs.getInt("transaction_id");
					double amount = rs.getDouble("amount");
					String date = rs.getString("date");
					String time = rs.getString("time");
					boolean isDeposit = true;
					if (amount < 0) {
						isDeposit = false;
					}
					transactions.add(new SingleTransaction(tr_id, amount, date, time, acc_id, userId, isDeposit));
				}
			}
		}
		return transactions;
	}

	/**
	 * Retrieves a list of all transfers between accounts
	 * involving a certain account.
	 */
	public List<DoubleTransaction> getDoubleTransactions(int acc_id) throws SQLException {
		int userId = userService.getLoggedInUser().getUserId();
		List<DoubleTransaction> transactions = new ArrayList<>();
		final String sql = "select * from Transaction t, Account a, Transfer x where (t.acc_id = ? or x.to_acc_id = ?) and a.acc_id = ? and t.transaction_id = x.transaction_id and a.user_id = ? sort by date desc;";
		try (Connection conn = dataSource.getConnection();
		PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, acc_id);
			pstmt.setInt(2, acc_id);
			pstmt.setInt(3, acc_id);
			pstmt.setInt(4, userId);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					int tr_id = rs.getInt("transaction_id");
					double amount = rs.getDouble("amount");
					String date = rs.getString("date");
					String time = rs.getString("time");
					int dest_id = rs.getInt("to_acc_id");
					transactions.add(new DoubleTransaction(tr_id, amount, date, time, acc_id, dest_id, userId, "Transfer"));
				}
			}
		}
		return transactions;
	}
	
	/**
	 * Retrieves a list of all deposits, withdrawals, and transfers
	 * between accounts involving a certain account.
	 * DOES show the other account in transactions between accounts.
	 * To show ONLY information pertaining to the current account,
	 * see getAllTransactionsInfo().
	 */
	public List<DoubleTransaction> getAllTransactionsInfo(int acc_id) {
		int userId = userService.getLoggedInUser().getUserId();
		List<DoubleTransaction> transactions = new ArrayList<>();
		final String sql = "select *, null as from_acc_id, null as to_acc_id, null as transaction_id from Transaction t where t.acc_id=? and (t.transaction_id not in(select transaction_id from Transfer)) union select * from Transaction t, Transfer r where t.acc_id=? and t.transaction_id=r.transaction_id order by date desc, time desc;";
		try (Connection conn = dataSource.getConnection()) {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, acc_id);
			pstmt.setInt(2, acc_id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int tr_id = rs.getInt("transaction_id");
				double amount = rs.getDouble("amount");
				String date = rs.getString("date");
				String time = rs.getString("time");
				int dest_id = rs.getInt("to_acc_id");
				String type;
				String destIdStr = rs.getString("to_acc_id");
				if (destIdStr != null) {
					type = "Transfer";
				} else {
					if (amount > 0) {
						type = "Deposit";
					} else {
						if (amount < 0) {
							type = "Withdrawal";
						} else {
							type = "NONE";
						}
					}
				}
				transactions.add(new DoubleTransaction(tr_id, amount, date, time, acc_id, dest_id, userId, type));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return transactions;
	}
	
	public double changeSince(String date, int acc_id) {
		double change = 0;
		final String sql = "select amount from Transaction where date>=? and acc_id=?;";
		try (Connection conn = dataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, date);
			pstmt.setInt(2, acc_id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				change += rs.getDouble("amount");
			}
		} catch (SQLException e) {
			return 0;
		}
		return change;
	}

	public double changeBetween(String date1, String date2, int acc_id) {
		double change = 0;
		final String sql = "select sum(amount) from Transaction where acc_id = ? and date between ? and ?;";
		try (Connection conn = dataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, acc_id);
			pstmt.setString(2, date1);
			pstmt.setString(3, date2);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			change = rs.getDouble("sum(amount)");
		} catch (SQLException e) {
			return 0;
		}
		return change;
	}

	public List<DoubleTransaction> getTransactionsBetween(int acc_id, String date1, String date2) {
		int userId = userService.getLoggedInUser().getUserId();
		List<DoubleTransaction> transactions = new ArrayList<>();
		final String sql = "select *, null as from_acc_id, null as to_acc_id, null as transaction_id from Transaction t where t.acc_id=? and (t.transaction_id not in(select transaction_id from Transfer)) and t.date between ? and ? union select * from Transaction t, Transfer r where t.acc_id=? and t.transaction_id=r.transaction_id and t.date between ? and ? order by date desc, time desc;";
		try (Connection conn = dataSource.getConnection()) {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, acc_id);
			pstmt.setString(2, date1);
			pstmt.setString(3, date2);
			pstmt.setInt(4, acc_id);
			pstmt.setString(5, date1);
			pstmt.setString(6, date2);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int tr_id = rs.getInt("transaction_id");
				double amount = rs.getDouble("amount");
				String date = rs.getString("date");
				String time = rs.getString("time");
				int dest_id = rs.getInt("to_acc_id");
				String type;
				String destIdStr = rs.getString("to_acc_id");
				if (destIdStr != null) {
					type = "Transfer";
				} else {
					if (amount > 0) {
						type = "Deposit";
					} else {
						if (amount < 0) {
							type = "Withdrawal";
						} else {
							type = "NONE";
						}
					}
				}
				transactions.add(new DoubleTransaction(tr_id, amount, date, time, acc_id, dest_id, userId, type));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return transactions;
	}

}