package com.currencyexchangeweb.util;

import com.currencyexchangeweb.dto.CurrencyDTO;
import com.currencyexchangeweb.dto.ExchangeRateDTO;
import com.currencyexchangeweb.model.Currency;
import com.currencyexchangeweb.model.ExchangeRate;

public class RateDtoMapper {
    public static ExchangeRateDTO ModelToDto(ExchangeRate rate) {
        ExchangeRateDTO dto = new ExchangeRateDTO(rate.getId(), rate.getBaseCurrency(), rate.getTargetCurrency(), rate.getRate());
        return dto;
    }

    public static ExchangeRate DtoToModel(ExchangeRateDTO dto) {
        ExchangeRate model = new ExchangeRate(dto.getId(), dto.getBaseCurrency(), dto.getTargetCurrency(), dto.getRate());
        return model;
    }
}
