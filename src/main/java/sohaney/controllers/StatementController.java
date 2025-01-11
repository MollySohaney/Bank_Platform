package sohaney.controllers;

import java.util.List;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import sohaney.services.UserService;
import sohaney.services.AccountService;
import sohaney.services.StatementService;
import sohaney.services.TransactionService;
import sohaney.models.BankStatement;
import sohaney.models.Account;

@Controller
@RequestMapping("/statements")
public class StatementController {

    private final AccountService accountService;
    private final UserService userService;
    private final StatementService stmtService;
	private final TransactionService transactionService;

    @Autowired
    public StatementController(AccountService accountService, UserService userService, StatementService stmtService, TransactionService transactionService) {
        this.accountService = accountService;
        this.userService = userService;
        this.stmtService = stmtService;
		this.transactionService = transactionService;
    }    

    @GetMapping
    public ModelAndView webpage() {
        ModelAndView mv = new ModelAndView("statements_page");
        // statement = stmtService.createStatement("2024-1-29", "2024-1-31", 1);
        mv.addObject("user", userService.getLoggedInUser());
		List<BankStatement> statement = stmtService.getStatementsUser(userService.getLoggedInUser().getUserId());
        mv.addObject("statement", statement);
		return mv;
    }

    @GetMapping("/create")
    public ModelAndView createStatementPage() {
        int userId = userService.getLoggedInUser().getUserId();
        System.out.println("The user is attempting to create a new statement.");
		ModelAndView mv = new ModelAndView("statement_creation");
        try {
            List<Account> accounts = accountService.getAccounts(userId);
            String owner_name = userService.getLoggedInUser().getFirstName() +  " " + userService.getLoggedInUser().getLastName();
            mv.addObject("owner_name", owner_name);
            mv.addObject("accounts", accounts);
            if(accounts.isEmpty()) {
                mv.addObject("isNoContent", true);
            }
        } catch(SQLException sqle) {
            mv.addObject("accounts", new ArrayList<Account>());
            mv.addObject("isNoContent", true);
        }
		return mv;
    }

    @PostMapping("/create")
    public String createStatement(@RequestParam("my_account")int acc_id,
                                    @RequestParam("from_date")String from_date,
                                    @RequestParam("to_date")String to_date) {
        System.out.println("Attempt to make statement for " + acc_id + " " + from_date + " " + to_date);
        boolean success = stmtService.createStatement(from_date, to_date, acc_id);
        if (success == false) {
            String message = URLEncoder.encode("Failed to create a statement. Please try again.",
                    StandardCharsets.UTF_8);
            return "redirect:/statements/create?error=" + message;
        }
        return "redirect:/statements";
    }

	@GetMapping("{st_id}")
    public ModelAndView createStatementPage(@PathVariable("st_id")int st_id) {
        int userId = userService.getLoggedInUser().getUserId();
        System.out.println("The user is attempting to view a particular statement.");
		ModelAndView mv = new ModelAndView("single_statement_page");
		BankStatement statement = stmtService.getStatement(st_id);
		int acc_id = statement.getAccountId();
		String start = statement.getStartDate();
		String end = statement.getEndDate();
		mv.addObject("statement", statement);
		mv.addObject("acc_id", acc_id);
		mv.addObject("start_date", start);
		mv.addObject("end_date", end);
        mv.addObject("transactions", transactionService.getTransactionsBetween(acc_id, start, end));
		return mv;
    }
}
