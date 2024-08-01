package com.currencyexchangeweb.repository;

import com.currencyexchangeweb.model.Currency;
import com.currencyexchangeweb.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyRepository {
    private static CurrencyRepository instance;

    private CurrencyRepository() {
    }

    public static CurrencyRepository getInstance() {
        if (instance == null) {
            instance = new CurrencyRepository();
        }
        return instance;
    }

    private Currency resultSetToCurrency(ResultSet rs) throws SQLException {
        Currency currency = new Currency();
        currency.setId(rs.getInt("id"));
        currency.setCode(rs.getString("code"));
        currency.setName(rs.getString("name"));
        currency.setSign(rs.getString("sign"));
        return currency;
    }

    public List<Currency> getAllCurrencies() throws SQLException {
        List<Currency> currencies = null;
        String query = "SELECT * FROM Currencies";
        try (Connection connection = DatabaseUtil.connect();
             PreparedStatement prst = connection.prepareStatement(query);
             ResultSet rs = prst.executeQuery();) {
            if (!rs.next()) {
                return null;
            } else {
                currencies = new ArrayList<>();
                do {
                    currencies.add(resultSetToCurrency(rs));
                } while (rs.next());
            }
            return currencies;
        }
    }

    public Currency getCurrencyByCode(String code) throws SQLException {
        String query = "SELECT * FROM Currencies WHERE code = ?";
        try (Connection connection = DatabaseUtil.connect();
             PreparedStatement prst = connection.prepareStatement(query);) {
            prst.setString(1, code);
            ResultSet rs = prst.executeQuery();
            if (!rs.next()) {
                return null;
            } else {
                return resultSetToCurrency(rs);
            }
        }
    }

    public void addCurrency(Currency currency) throws SQLException {
        String query = "INSERT INTO Currencies(code, name, sign) VALUES(?, ?, ?)";
        try (Connection connection = DatabaseUtil.connect();
             PreparedStatement prst = connection.prepareStatement(query)) {
            prst.setString(1, currency.getCode());
            prst.setString(2, currency.getName());
            prst.setString(3, currency.getSign());
            prst.executeUpdate();
        }
    }

    public Currency getCurrencyById(int id) throws SQLException {
        String query = "SELECT * FROM Currencies WHERE id = ?";
        try (Connection connection = DatabaseUtil.connect();
             PreparedStatement prst = connection.prepareStatement(query)) {
            prst.setInt(1, id);
            ResultSet rs = prst.executeQuery();
            if (!rs.next()) {
                return null;
            } else {
                return resultSetToCurrency(rs);
            }
        }
    }

    public int getCurrencyIdByCode(String code) throws SQLException {
        String query = "SELECT * FROM Currencies WHERE code = ?";
        try (Connection connection = DatabaseUtil.connect()) {
            PreparedStatement prst = connection.prepareStatement(query);
            prst.setString(1, code);
            try (ResultSet rs = prst.executeQuery()) {
                if (!rs.next()) {
                    return 0;
                } else {
                    return rs.getInt("id");
                }
            }
        }
    }

}
