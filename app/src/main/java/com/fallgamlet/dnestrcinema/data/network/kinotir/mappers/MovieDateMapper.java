package com.fallgamlet.dnestrcinema.data.network.kinotir.mappers;

import com.fallgamlet.dnestrcinema.data.network.Mapper;
import com.fallgamlet.dnestrcinema.utils.LogUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MovieDateMapper implements Mapper<String, Date> {
    @Override
    public Date map(String src) {
        try {
            if (src == null || src.isBlank()) {
                return null;
            }

            String format = "'с' d MMMM, yyyy"; // с 16 марта, 2017
            Locale locale = new Locale("ru","RU");
            SimpleDateFormat formater = new SimpleDateFormat(format, locale);
            return formater.parse(src.trim());
        } catch (ParseException e) {
            LogUtils.INSTANCE.log(getClass().getSimpleName(), "mapping error", e);
        }
        return new Date(0);
    }
}
