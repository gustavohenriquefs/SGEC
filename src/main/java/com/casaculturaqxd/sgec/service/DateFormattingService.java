package com.casaculturaqxd.sgec.service;

import java.text.SimpleDateFormat;
import java.sql.Date;

public class DateFormattingService {

    private static final String BRAZILIAN_DATE_FORMAT = "dd/MM/yyyy";
    private final SimpleDateFormat brazilianDateFormat = new SimpleDateFormat(BRAZILIAN_DATE_FORMAT);

    public String formatToBrazilian(Date date) {
        return brazilianDateFormat.format(date);
    }
}