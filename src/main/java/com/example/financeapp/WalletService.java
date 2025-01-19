package com.example.financeapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WalletService {
    private static final String WALLET_DATA_FILE = "src/main/resources/data/wallet_data.json";
    private final Map<String, Wallet> wallets = new ConcurrentHashMap<>();

    public WalletService() {
    }

    public Wallet getWallet(String user) {
        return wallets.get(user);
    }

    public void setWallet(String user){
        wallets.put(user, new Wallet(user));
    }

    public boolean addTransaction(String user, String type, String category, double amount) {
        Wallet wallet = wallets.get(user);
        if (wallet == null) return false;

        switch (type.toLowerCase()) {
            case "income":
                wallet.addIncome(category, amount);
                break;
            case "expense":
                wallet.addExpense(category, amount);
                break;
            case "budget":
                wallet.addBudget(category, amount);
                break;
            default:
                return false;
        }
        return true;
    }

    public void saveWalletData(String username, Wallet wallet) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Создаем или перезаписываем файл с данными кошелька
            objectMapper.writeValue(new File(WALLET_DATA_FILE), wallet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Wallet loadWalletData(String username) {
        ObjectMapper objectMapper = new ObjectMapper();

        File walletFile = new File(WALLET_DATA_FILE);
        if (!walletFile.exists() || walletFile.length() == 0) {
            return null;
        }
        try {
            return objectMapper.readValue(walletFile, Wallet.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean transferFunds(String sender, String receiver, double amount, String category) {
        Wallet senderWallet = wallets.get(sender);
        Wallet receiverWallet = wallets.get(receiver);

        if (senderWallet == null || receiverWallet == null) {
            return false;
        }

        senderWallet.addExpense(category + receiver, amount);

        receiverWallet.addIncome(category + sender, amount);

        return true;
    }

}
