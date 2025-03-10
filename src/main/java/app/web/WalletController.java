package app.web;

import app.user.model.User;
import app.wallet.model.Wallet;
import app.wallet.service.WalletService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.Optional;

@Controller
@RequestMapping("/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping
    public ModelAndView getWalletsPage(HttpSession session) {
        User user = (User) session.getAttribute("user");
        Optional<Wallet> wallet = walletService.getWalletByUser(user);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("wallet");
        modelAndView.addObject("user", user);
        modelAndView.addObject("wallet", wallet);

        return modelAndView;
    }

    @PostMapping("/add-money")
    public String addMoneyToWallet(HttpSession session, @RequestParam("amount") BigDecimal amount) {
        User user = (User) session.getAttribute("user");
        Wallet wallet = walletService.getWalletByUser(user).orElseThrow(() -> new RuntimeException("Wallet not found"));
        walletService.addMoney(wallet, amount);

        return "redirect:/wallet";
    }
}
