package sohaney.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sohaney.models.SingleTransaction;
import sohaney.models.DoubleTransaction;
import sohaney.models.BankStatement;
import sohaney.services.TransactionService;

/**
 * This service contains statement related functions.
 */
@Service
public class StatementService {
    private final DataSource dataSource;
    private final UserService userService;
    private final TransactionService transactionService;

    @Autowired
    public StatementService(DataSource dataSource, UserService userService, TransactionService transactionService) {
        this.dataSource = dataSource;
        this.userService = userService;
        this.transactionService = transactionService;
    }

	public boolean createStatement(String start, String end, int acc_id) {
		final String sql = "insert into BankStatement (statement_id, balance_change, start_date, end_date, acc_id) values (?, ?, ?, ?, ?);";
		final String stIdSql = "select max(statement_id) from BankStatement;";
		try (Connection conn = dataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			PreparedStatement stIdPstmt = conn.prepareStatement(stIdSql)) {
			double change = transactionService.changeBetween(start, end, acc_id);
			int rowsAffected = 0;
			int st_id = 0;
			ResultSet rs = stIdPstmt.executeQuery();
			rs.next();
			st_id = rs.getInt("max(statement_id)");
			while (rowsAffected == 0) {
				st_id++;
				pstmt.setInt(1, st_id);
				pstmt.setDouble(2, change);
				pstmt.setString(3, start);
				pstmt.setString(4, end);
				pstmt.setInt(5, acc_id);
				rowsAffected = pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public List<BankStatement> getStatementsUser(int user_id) {
		final String sql = "select b.statement_id, b.acc_id, b.balance_change, b.start_date, b.end_date, a.name from BankStatement b, Account a where b.acc_id=a.acc_id and a.user_id=? order by b.statement_id desc;";
		List<BankStatement> statements = new ArrayList<>();
		try (Connection conn = dataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, user_id);
			ResultSet rs = pstmt.executeQuery();
			int st_id;
			int acc_id;
			double change;
			String start;
			String end;
			String name;
			while (rs.next()) {
				st_id = rs.getInt("statement_id");
				acc_id = rs.getInt("acc_id");
				change = rs.getDouble("balance_change");
				start = rs.getString("start_date");
				end = rs.getString("end_date");
				name = rs.getString("name");
				statements.add(new BankStatement(st_id, acc_id, name, change, start, end));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return statements;
		}
		return statements;
	}

	public List<BankStatement> getStatementsAccount(int acc_id) {
		final String sql = "select b.statement_id, b.balance_change, b.start_date, b.end_date, a.name from BankStatement b, Account a where b.acc_id=? and a.acc_id=b.acc_id order by statement_id desc;";
		List<BankStatement> statements = new ArrayList<>();
		try (Connection conn = dataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, acc_id);
			ResultSet rs = pstmt.executeQuery();
			int st_id;
			double change;
			String start;
			String end;
			String name;
			while (rs.next()) {
				st_id = rs.getInt("statement_id");
				change = rs.getDouble("balance_change");
				start = rs.getString("start_date");
				end = rs.getString("end_date");
				name = rs.getString("name");
				statements.add(new BankStatement(st_id, acc_id, name, change, start, end));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return statements;
		}
		return statements;
	}

	public BankStatement getStatement(int st_id) {
		final String sql = "select b.balance_change, b.start_date, b.end_date, b.acc_id, a.name from BankStatement b, Account a where b.statement_id=? and b.acc_id=a.acc_id;";
		BankStatement statement = new BankStatement(0, 0, "default", 0, "default", "default");
		try (Connection conn = dataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, st_id);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			double change = rs.getDouble("balance_change");
			String start = rs.getString("start_date");
			String end = rs.getString("end_date");
			int acc_id = rs.getInt("acc_id");
			String name = rs.getString("name");
			statement = new BankStatement(st_id, acc_id, name, change, start, end);
		} catch (SQLException e) {
			e.printStackTrace();
			return statement;
		}
		return statement;
	}

}
