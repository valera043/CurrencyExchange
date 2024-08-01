package com.currencyexchangeweb.util;

import com.currencyexchangeweb.dto.CurrencyDTO;
import com.currencyexchangeweb.model.Currency;

public class DtoMapper {
    public static CurrencyDTO ModelToDto(Currency currency) {
        if (currency == null) {
            return null;
        }
        CurrencyDTO dto = new CurrencyDTO(currency.getId(), currency.getCode(), currency.getName(), currency.getSign());
        return dto;
    }

    public static Currency DtoToModel(CurrencyDTO dto) {
        if (dto == null) {
            return null;
        }
        Currency model = new Currency(dto.getId(), dto.getCode(), dto.getName(), dto.getSign());
        return model;
    }
}
