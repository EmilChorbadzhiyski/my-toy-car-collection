package app.web;

import app.transaction.model.Transaction;
import app.transaction.service.TransactionService;
import app.user.model.User;
import app.web.dto.CreateTransactionRequest;
import jakarta.servlet.http.HttpSession;
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

    public TransactionsController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ModelAndView getTransactionsPage(HttpSession session){
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return new ModelAndView("redirect:/login");
        }

        List<Transaction> transactions = transactionService.getTransactionsForUser(user);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("transactions");
        modelAndView.addObject("transactions", transactions);
        modelAndView.addObject("user", user);
        modelAndView.addObject("createTransactionRequest", new CreateTransactionRequest());
        return modelAndView;
    }

    @PostMapping("/delete/{id}")
    public String deleteTransaction(@PathVariable("id") UUID transactionId, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        transactionService.deleteTransaction(transactionId);
        List<Transaction> updatedTransactions = transactionService.getTransactionsForUser(user);
        user.setTransactions(updatedTransactions);
        session.setAttribute("user", user);

        return "redirect:/transactions";

    }
}
