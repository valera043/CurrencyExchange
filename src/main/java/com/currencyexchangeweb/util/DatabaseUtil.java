package com.currencyexchangeweb.util;

import com.currencyexchangeweb.model.Currency;
import com.currencyexchangeweb.model.ExchangeRate;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUtil {

    private static DatabaseUtil instance;

    private static final String URL = "jdbc:sqlite:C:\\Users\\dominik\\Documents\\IdeaProjects\\CurrencyExchangeWeb\\bin";

    private DatabaseUtil() {}

    public static DatabaseUtil getInstance() {
        if (instance == null) {
            instance = new DatabaseUtil();
        }
        return instance;
    }

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Ошибка при подключении");
            throw new RuntimeException(e);
        }
    }

}
