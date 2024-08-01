package com.currencyexchangeweb.service;

import com.currencyexchangeweb.dto.CurrencyDTO;
import com.currencyexchangeweb.model.Currency;
import com.currencyexchangeweb.repository.CurrencyRepository;
import com.currencyexchangeweb.util.DtoMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CurrencyService {

    private static CurrencyService instance;
    private final CurrencyRepository currencyRepository;

    private CurrencyService() {
        this.currencyRepository = CurrencyRepository.getInstance();
    }

    public static CurrencyService getInstance() {
        if (instance == null) {
            instance = new CurrencyService();
        }
        return instance;
    }

    public CurrencyDTO getCurrencyByCode(String code) {
        Currency currency = null;
        try {
            currency = currencyRepository.getCurrencyByCode(code);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return DtoMapper.ModelToDto(currency);
    }

    public List<CurrencyDTO> getAllCurrencies() {
        List<Currency> currencies;
        List<CurrencyDTO> dtos = new ArrayList<>();
        try {
            currencies = currencyRepository.getAllCurrencies();
            for (Currency currency : currencies) {
                dtos.add(DtoMapper.ModelToDto(currency));
            }
            return dtos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CurrencyDTO addCurrency(String name, String code, String sign) throws SQLException {
        if (name == null || code == null || sign == null) {
            throw new IllegalArgumentException("Отсутствует одно или несколько полей формы");
        }
        CurrencyDTO dto = new CurrencyDTO(code, name, sign);
        currencyRepository.addCurrency(DtoMapper.DtoToModel(dto));
        Currency createdCurrency = currencyRepository.getCurrencyByCode(code);
        return DtoMapper.ModelToDto(createdCurrency);
    }
}
