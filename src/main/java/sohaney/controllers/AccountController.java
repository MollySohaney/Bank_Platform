package sohaney.controllers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import sohaney.models.Account;
import sohaney.models.User;
import sohaney.models.DoubleTransaction;
import sohaney.services.TransactionService;
import sohaney.services.AccountService;
import sohaney.services.UserService;

/**
 * Handles /account URL and its sub urls.
 */
@Controller
@RequestMapping("/account")
public class AccountController {

    private final UserService userService;
    private final TransactionService transactionService;
	private final AccountService accountService;

    public AccountController(UserService userService, TransactionService transactionService, AccountService accountService) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.accountService = accountService;
    }

	@GetMapping("/create")
	public ModelAndView accountCreation(@RequestParam(name = "error", required = false) String error) {
		System.out.println("The user is attempting to create a new account.");
		ModelAndView mv = new ModelAndView("account_creation");
		return mv;
	}

	@PostMapping("/create")
	public String create(@RequestParam("name") String name,
			@RequestParam("balance") double init_balance) {
		int user_id = userService.getLoggedInUser().getUserId();
		Account newAcc = accountService.createAccount(name, init_balance, user_id);
		return "redirect:/account";
	}

    /**
     * This function handles the /account/{accId} URL.
     * This handlers serves the web page for a specific account.
     * Note there is a path variable {accId}.
     * An example URL handled by this function looks like below:
     * http://localhost:8081/account/1
     * The above URL assigns 1 to accId.
     * 
     * See notes from HomeController.java regardig error URL parameter.
     */
    @GetMapping
    public ModelAndView webpage(@RequestParam(name = "error", required = false) String error) {
        System.out.println("The user is attempting to view accounts");
        ModelAndView mv = new ModelAndView("accounts_page");
        int userId = userService.getLoggedInUser().getUserId();
		mv.addObject("user", userService.getLoggedInUser());
        try {
            List<Account> accounts = accountService.getAccounts(userId);
            mv.addObject("accounts", accounts);
            if(accounts.isEmpty()) {
                mv.addObject("isNoContent", true);
            }
        } catch(SQLException sqle) {
			String errorMessage = error;
			mv.addObject("errorMessage", errorMessage);
            mv.addObject("accounts", new ArrayList<Account>());
            mv.addObject("isNoContent", true);
        }
        String errorMessage = error;
        mv.addObject("errorMessage", errorMessage);
        return mv;
    }

	/**
	 * This function handles the /account/{accId} URL.
	 */
	@GetMapping("/{accId}")
	public ModelAndView accountCreation(@PathVariable("accId") int accId,
			@RequestParam(name = "error", required = false) String error) {
		ModelAndView mv = new ModelAndView("single_account_page");
		User user = userService.getLoggedInUser();
		mv.addObject("user", user);
		mv.addObject("acc_id", accId);
		try {
			Account acc = accountService.getOneAccount(user.getUserId(), accId);
			mv.addObject("name", acc.getName());
			mv.addObject("balance", acc.getBalance());
			SimpleDateFormat startOfMonthFormat = new SimpleDateFormat("yyyy-MM-01");
			String startOfMonth = startOfMonthFormat.format(new Date());
			mv.addObject("startOfMonth", startOfMonth);
			mv.addObject("balanceChange", transactionService.changeSince(startOfMonth, accId));
			mv.addObject("transactions", transactionService.getAllTransactionsInfo(accId));
		} catch (SQLException e) {
			String errorMessage = URLEncoder.encode("Failed to access your account. Please try again.",
                    StandardCharsets.UTF_8);
			mv.addObject(errorMessage);
			return mv;
		}
		return mv;
	}

	@GetMapping("/{accId}/transfer")
	public ModelAndView accountTransfer(@PathVariable("accId") int accId,
			@RequestParam(name = "error", required = false) String error) {
		ModelAndView mv = new ModelAndView("transfer_page");
		User user = userService.getLoggedInUser();
		mv.addObject("user", user);
		mv.addObject("acc_id", accId);
		try {
			Account acc = accountService.getOneAccount(user.getUserId(), accId);
			mv.addObject("name", acc.getName());
			mv.addObject("balance", acc.getBalance());
			SimpleDateFormat startOfMonthFormat = new SimpleDateFormat("yyyy-MM-01");
			String startOfMonth = startOfMonthFormat.format(new Date());
			SimpleDateFormat currentDayFormat = new SimpleDateFormat("yyyy--MM-dd");
			String currentDay = currentDayFormat.format(new Date());
			mv.addObject("startOfMonth", startOfMonth);
			mv.addObject("balanceChange", transactionService.changeSince(startOfMonth, accId));
			mv.addObject("accounts", accountService.getAllAccounts(accId));
		} catch (SQLException e) {
			String errorMessage = URLEncoder.encode("Failed to access your account. Please try again.",
                    StandardCharsets.UTF_8);
			mv.addObject(errorMessage);
			return mv;
		}
		return mv;
	}

	/**
     * Handles deposits into accounts.
     */
    @PostMapping("/{accId}/deposit")
    public String handleDeposit(@PathVariable("accId") int accId,
			@RequestParam(name = "transfer_amount") double amount) {
        System.out.println("The user is attempting to perform a deposit:");
        System.out.println("\taccId: " + accId);
        System.out.println("\tamount: " + amount);

        int userId = userService.getLoggedInUser().getUserId();
        boolean success = true;
        success = transactionService.deposit(accId, userId, amount);

        if(success) {
            return "redirect:/account/" + accId;
        } else {
            String message = URLEncoder.encode("Failed to deposit. Please try again.",
                    StandardCharsets.UTF_8);
            return "redirect:/account/" + accId + "?error=" + message;
        }
    }

	/**
     * Handles withdrawals from accounts.
     */
    @PostMapping("/{accId}/withdraw")
    public String handleWithdrawal(@PathVariable("accId") int accId,
			@RequestParam(name = "transfer_amount") double amount) {
        System.out.println("The user is attempting to perform a withdrawal:");
        System.out.println("\taccId: " + accId);
        System.out.println("\tamount: " + amount);

        int userId = userService.getLoggedInUser().getUserId();
        boolean success = true;
        success = transactionService.withdraw(accId, userId, amount);

        if(success) {
            return "redirect:/account/" + accId;
        } else {
            String message = URLEncoder.encode("Failed to withdraw. Please try again.",
                    StandardCharsets.UTF_8);
            return "redirect:/account/" + accId + "?error=" + message;
        }
    }

	/**
     * Handles transfers between accounts.
     */
    @PostMapping("/{accId}/transfer")
    public String handleTransfer(@PathVariable("accId") int accId,
			@RequestParam(name = "transfer_amount") double amount,
			@RequestParam(name = "other_account") int toId) {
        System.out.println("The user is attempting to perform a withdrawal:");
        System.out.println("\taccId: " + accId);
        System.out.println("\tamount: " + amount);
		System.out.println("\tto: " + toId);
        int userId = userService.getLoggedInUser().getUserId();
        boolean success = true;
        success = transactionService.transfer(accId, userId, amount, toId);

        if(success) {
            return "redirect:/account/" + accId;
        } else {
            String message = URLEncoder.encode("Failed to transfer. Please try again.",
                    StandardCharsets.UTF_8);
            return "redirect:/account/" + accId + "?error=" + message;
        }
    }

}
