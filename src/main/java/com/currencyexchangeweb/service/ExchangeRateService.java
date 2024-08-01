package com.currencyexchangeweb.service;

import com.currencyexchangeweb.dto.ExchangeRateDTO;
import com.currencyexchangeweb.model.Currency;
import com.currencyexchangeweb.model.ExchangeRate;
import com.currencyexchangeweb.repository.CurrencyRepository;
import com.currencyexchangeweb.repository.ExchangeRateRepository;
import com.currencyexchangeweb.util.RateDtoMapper;

import javax.naming.NameNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateService {
    private final CurrencyRepository currencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    private static ExchangeRateService instance;

    //Реализация SINGLETON
    private ExchangeRateService() {
        this.exchangeRateRepository = ExchangeRateRepository.getInstance();
        this.currencyRepository = CurrencyRepository.getInstance();
    }

    public static ExchangeRateService getInstance() {
        if (instance == null) {
            instance = new ExchangeRateService();
        }
        return instance;
    }
    // Конец SINGLETON

    public List<ExchangeRateDTO> getAllRates() {
        List<ExchangeRate> rates = null;
        try {
            rates = exchangeRateRepository.getAllRates();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        List<ExchangeRateDTO> dtos = new ArrayList<ExchangeRateDTO>();
        for (ExchangeRate rate : rates) {
            dtos.add(RateDtoMapper.ModelToDto(rate));
        }
        return dtos;
    }

    public ExchangeRateDTO getRateByPair(String pair) throws NameNotFoundException {
        try {
            pair = pair.substring(1);
            String firstCurrencyCode = pair.substring(0, 3);
            String targetCurrencyCode = pair.substring(3, 6);
            int baseCurrencyId = currencyRepository.getCurrencyIdByCode(firstCurrencyCode);
            int targetCurrencyId = currencyRepository.getCurrencyIdByCode(targetCurrencyCode);
            ExchangeRate rate = exchangeRateRepository.getRateByPairIds(baseCurrencyId, targetCurrencyId);
            return RateDtoMapper.ModelToDto(rate);
        } catch (Exception e) {
            throw new NameNotFoundException("Обменный курс для пары не найден");
        }
    }

    public ExchangeRateDTO addCurrency(String baseCurrencyCode, String targetCurrencyCode, Double rate) throws SQLException, NameNotFoundException {
        if (baseCurrencyCode == null || targetCurrencyCode == null || rate == 0) {
            throw new IllegalArgumentException("Отсутствует одно или несколько полей формы");
        }
        Currency baseCurrency = currencyRepository.getCurrencyByCode(baseCurrencyCode);
        Currency targetCurrency = currencyRepository.getCurrencyByCode(targetCurrencyCode);
        if (baseCurrency == null || targetCurrency == null) {
            throw new NameNotFoundException("Одна (или обе) валюта из валютной пары не существует в БД");
        }
        ExchangeRateDTO dto = new ExchangeRateDTO(baseCurrency, targetCurrency, rate);
        System.out.println("Добавляем дто: " + dto);
        exchangeRateRepository.addCurrency(RateDtoMapper.DtoToModel(dto));
        ExchangeRate createdRate = exchangeRateRepository.getRateByPairIds(baseCurrency.getId(), targetCurrency.getId());
        return RateDtoMapper.ModelToDto(createdRate);
    }

    public ExchangeRateDTO updateRatePair(String pair, Double rate) throws NameNotFoundException, SQLException {
        ExchangeRateDTO exchangeRate = getRateByPair(pair);
        exchangeRateRepository.updateRatePair(RateDtoMapper.DtoToModel(exchangeRate), rate);
        return getRateByPair(pair);
    }
}
