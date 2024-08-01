package com.currencyexchangeweb.repository;

import com.currencyexchangeweb.model.ExchangeRate;
import com.currencyexchangeweb.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateRepository {
    private final CurrencyRepository currencyRepository;
    private static ExchangeRateRepository instance;

    private ExchangeRateRepository() {
        this.currencyRepository = CurrencyRepository.getInstance();
    }

    public static ExchangeRateRepository getInstance() {
        if (instance == null) {
            instance = new ExchangeRateRepository();
        }
        return instance;
    }

    private ExchangeRate resultSetToExchangeRate(ResultSet rs) throws SQLException {
        ExchangeRate rate = new ExchangeRate();
        rate.setId(rs.getInt("id"));
        rate.setBaseCurrency(currencyRepository.getCurrencyById(rs.getInt("baseCurrencyId")));
        rate.setTargetCurrency(currencyRepository.getCurrencyById(rs.getInt("targetCurrencyId")));
        rate.setRate(rs.getDouble("rate"));
        return rate;
    }

    public List<ExchangeRate> getAllRates() throws SQLException {
        List<ExchangeRate> rates = new ArrayList<ExchangeRate>();
        String query = "SELECT * FROM ExchangeRates";
        try (Connection connection = DatabaseUtil.connect();
             PreparedStatement prst = connection.prepareStatement(query);) {
            ResultSet rs = prst.executeQuery();
            while (rs.next()) {
                rates.add(resultSetToExchangeRate(rs));
            }
            return rates;
        }
    }

    public ExchangeRate getRateByPairIds(int baseCurrencyId, int targetCurrencyId) throws SQLException {
        String query = "SELECT * FROM ExchangeRates WHERE baseCurrencyId = ? AND targetCurrencyId = ?";
        try (Connection connection = DatabaseUtil.connect();
             PreparedStatement prst = connection.prepareStatement(query);) {
            prst.setInt(1, baseCurrencyId);
            prst.setInt(2, targetCurrencyId);
            ResultSet rs = prst.executeQuery();
            if (!rs.next()) {
                return null;
            } else {
                return resultSetToExchangeRate(rs);
            }
        }
    }

    public void addCurrency(ExchangeRate exchangeRate) throws SQLException {
        String query = "INSERT INTO ExchangeRates(baseCurrencyId, targetCurrencyId, rate) VALUES(?, ?, ?)";
        try (Connection connection = DatabaseUtil.connect();
             PreparedStatement prst = connection.prepareStatement(query)) {
            prst.setInt(1, exchangeRate.getBaseCurrency().getId());
            prst.setInt(2, exchangeRate.getTargetCurrency().getId());
            prst.setDouble(3, exchangeRate.getRate());
            prst.executeUpdate();
        }
    }

    public void updateRatePair(ExchangeRate exchangeRate, Double rate) throws SQLException {
        String query = "UPDATE ExchangeRates SET rate = ? where baseCurrencyId = ? and targetCurrencyId = ?";
        try (Connection connection = DatabaseUtil.connect();
             PreparedStatement prst = connection.prepareStatement(query);) {
            System.out.println(rate);
            int baseCurrencyId = exchangeRate.getBaseCurrency().getId();
            int targetCurrencyId = exchangeRate.getTargetCurrency().getId();
            prst.setInt(2, baseCurrencyId);
            prst.setInt(3, targetCurrencyId);
            prst.setDouble(1, rate);
            prst.executeUpdate();
        }
    }
}
