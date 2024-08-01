package com.currencyexchangeweb.service;

import com.currencyexchangeweb.dto.ExchangeDTO;
import com.currencyexchangeweb.model.Currency;
import com.currencyexchangeweb.repository.CurrencyRepository;
import com.currencyexchangeweb.repository.ExchangeRateRepository;

import java.sql.SQLException;

public class ExchangeService {
    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyRepository currencyRepository;
    private static ExchangeService instance;

    private ExchangeService() {
        this.exchangeRateRepository = ExchangeRateRepository.getInstance();
        this.currencyRepository = CurrencyRepository.getInstance();
    }

    public static ExchangeService getInstance() {
        if (instance == null) {
            instance = new ExchangeService();
        }
        return instance;
    }

    public ExchangeDTO doExchange(String from, String to, Double amount) throws SQLException {
        Currency baseCurrency = currencyRepository.getCurrencyByCode(from);
        Currency targetCurrency = currencyRepository.getCurrencyByCode(to);
        if (baseCurrency == null || targetCurrency == null) {
            throw new NullPointerException("Одной из валют нет");
        } else {
            int fromId = baseCurrency.getId();
            int toId = targetCurrency.getId();
            Double rate = null;
            try {
                rate = exchangeRateRepository.getRateByPairIds(fromId, toId).getRate();
            } catch (NullPointerException e) {
                try {
                    rate = 1 / exchangeRateRepository.getRateByPairIds(toId, fromId).getRate();
                } catch (NullPointerException e2) {
                    throw new NullPointerException("Нет такой валютной пары");
                }
            }
            ExchangeDTO dto = new ExchangeDTO();
            dto.setBaseCurrency(baseCurrency);
            dto.setTargetCurrency(targetCurrency);
            dto.setAmount(amount);
            dto.setRate(rate);
            dto.setConvertedAmount(rate * amount);
            System.out.println(dto);
            return dto;
        }
    }
}
