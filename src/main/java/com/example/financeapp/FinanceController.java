package com.example.financeapp;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.Objects;

@Controller
@RequestMapping("/app")
public class FinanceController {
    private final UserService userService;
    private final WalletService walletService;

    public FinanceController(UserService userService, WalletService walletService) {
        this.userService = userService;
        this.walletService = walletService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    @GetMapping("/authorise")
    public String authorisePage() {
        return "authorise";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        if (userService.authenticate(username, password)) {
            session.setAttribute("username", username);
            session.setAttribute("loaded", "0");
            return "redirect:/app/dashboard?user=" + username;
        }
        model.addAttribute("error", "Invalid login credentials");
        return "login";
    }

    @PostMapping("/authorise")
    public String authorise(@RequestParam String username, @RequestParam String password, HttpSession session) {
        userService.authorise(username, password);
        walletService.setWallet(username);
        session.setAttribute("username", username);
        session.setAttribute("loaded", "0");
        return "redirect:/app/dashboard?user=" + username;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Wallet wallet;
        String selectedType = (String) session.getAttribute("selectedType");
        String user = (String) session.getAttribute("username");
        String loaded = (String) session.getAttribute("loaded");
        model.addAttribute("username", user);
        model.addAttribute("selectedType", selectedType);
        if (Objects.equals(loaded, "0") && walletService.loadWalletData(user) != null){
            wallet = walletService.loadWalletData(user);
        } else {
            wallet = walletService.getWallet(user);
        }
        session.setAttribute("loaded", "1");
        model.addAttribute("wallet", wallet);
        model.addAttribute("categories", wallet.getCategories());
        return "dashboard";
    }

    @PostMapping("/addTransaction")
    public String addTransaction(@RequestParam String type, @RequestParam String category, @RequestParam double amount, HttpSession session) {
        String user = (String) session.getAttribute("username");
        if (!walletService.addTransaction(user, type, category, amount)) {
            session.setAttribute("error", "Invalid transaction details");
            return "error";
        }
        session.setAttribute("selectedType", type);
        return "redirect:/app/dashboard?user=" + user;
    }

    @PostMapping("/transfer")
    public String transferFunds(@RequestParam String receiver, @RequestParam double amount, HttpSession session, Model model) {
        String sender = (String) session.getAttribute("username");
        if (walletService.transferFunds(sender, receiver, amount, "Перевод ")) {
            model.addAttribute("success", "Перевод успешно выполнен");
        } else {
            model.addAttribute("error", "Ошибка при переводе средств. Проверьте данные.");
        }

        return "redirect:/app/dashboard?user=" + sender;
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        String user = (String) session.getAttribute("username");
        // Получаем данные кошелька
        Wallet wallet = walletService.getWallet(user);

        // Сохраняем данные кошелька
        walletService.saveWalletData(user, wallet);

        // Выход пользователя
        session.invalidate();

        return "redirect:/app/login";
    }

}
