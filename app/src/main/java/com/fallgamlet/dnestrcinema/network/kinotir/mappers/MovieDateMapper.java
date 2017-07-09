package com.fallgamlet.dnestrcinema.network.kinotir.mappers;

import com.fallgamlet.dnestrcinema.network.Mapper;
import com.fallgamlet.dnestrcinema.utils.LogUtils;
import com.fallgamlet.dnestrcinema.utils.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by fallgamlet on 09.07.17.
 */

public class MovieDateMapper implements Mapper<String, Date> {
    @Override
    public Date map(String src) {
        try {
            if (StringUtils.isEmpty(src)) {
                return null;
            }

            String format = "'с' d MMMM, yyyy"; // с 16 марта, 2017
            Locale locale = new Locale("ru","RU");
            SimpleDateFormat formater = new SimpleDateFormat(format, locale);
            return formater.parse(src.trim());
        } catch (ParseException e) {
            LogUtils.log(getClass().getSimpleName(), "mapping error", e);
        }
        return null;
    }
}
