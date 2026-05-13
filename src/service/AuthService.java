package service;

import dao.UserDAO;
import model.User;

public class AuthService {

    private UserDAO userDAO;

    public AuthService() {

        userDAO = new UserDAO();
    }

    // LOGIN
    public User login(String username, String password) {

        // VALIDATE

        if (username == null || username.isEmpty()) {

            return null;
        }

        if (password == null || password.isEmpty()) {

            return null;
        }

        // DAO

        return userDAO.login(username, password);
    }
}