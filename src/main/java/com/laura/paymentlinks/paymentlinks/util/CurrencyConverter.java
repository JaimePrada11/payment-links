package com.laura.paymentlinks.paymentlinks.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Currency;

@Converter(autoApply = true)
public class CurrencyConverter implements AttributeConverter<Currency, String> {

    @Override
    public String convertToDatabaseColumn(Currency currency) {
        return currency != null ? currency.getCurrencyCode() : null;
    }

    @Override
    public Currency convertToEntityAttribute(String dbData) {
        return dbData != null ? Currency.getInstance(dbData) : null;
    }
}
