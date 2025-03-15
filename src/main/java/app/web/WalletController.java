package app.web;

import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.service.UserService;
import app.wallet.model.Wallet;
import app.wallet.service.WalletService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final UserService userService;

    public WalletController(WalletService walletService, UserService userService) {
        this.walletService = walletService;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getWalletsPage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        User user = userService.getById(authenticationMetadata.getUserId());
        Optional<Wallet> wallet = walletService.getWalletByUser(user);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("wallet");
        modelAndView.addObject("user", user);
        modelAndView.addObject("wallet", wallet);

        return modelAndView;
    }

    @PostMapping("/add-money")
    public String addMoneyToWallet(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata, @RequestParam("amount") BigDecimal amount) {
        User user = userService.getById(authenticationMetadata.getUserId());
        Wallet wallet = walletService.getWalletByUser(user).orElseThrow(() -> new RuntimeException("Wallet not found"));
        walletService.addMoney(wallet, amount);

        return "redirect:/wallet";
    }
}
