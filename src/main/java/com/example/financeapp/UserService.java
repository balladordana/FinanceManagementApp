package com.example.financeapp;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Service
public class UserService {
    private final Map<String, String> users = new ConcurrentHashMap<>();

    public UserService() {
    }

    public void authorise(String username, String password){
        users.put(username, password);

    }

    public boolean authenticate(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }
}
