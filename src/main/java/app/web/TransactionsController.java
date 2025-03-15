package app.web;

import app.security.AuthenticationMetadata;
import app.transaction.model.Transaction;
import app.transaction.service.TransactionService;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.CreateTransactionRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/transactions")
public class TransactionsController {

    private final TransactionService transactionService;
    private final UserService userService;

    public TransactionsController(TransactionService transactionService, UserService userService) {
        this.transactionService = transactionService;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getTransactionsPage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        User user = userService.getById(authenticationMetadata.getUserId());

        if (user == null) {
            return new ModelAndView("redirect:/login");
        }

        List<Transaction> transactions = transactionService.getTransactionsForUser(user);

        ModelAndView modelAndView = new ModelAndView("transactions");
        modelAndView.addObject("transactions", transactions);
        modelAndView.addObject("user", user);
        modelAndView.addObject("createTransactionRequest", new CreateTransactionRequest());

        return modelAndView;
    }

    @PostMapping("/delete/{id}")
    public String deleteTransaction(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata,
                                    @PathVariable("id") UUID transactionId) {
        User user = userService.getById(authenticationMetadata.getUserId());
        if (user == null) {
            return "redirect:/login";
        }
        transactionService.deleteTransaction(transactionId);
        return "redirect:/transactions";
    }
}
