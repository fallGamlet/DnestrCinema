package com.fallgamlet.dnestrcinema.utils;

/**
 * Created by fallgamlet on 09.07.17.
 */

public class StringUtils {

    public static boolean isEmpty(String val) {
        return val == null || val.isEmpty();
    }

    public static boolean isEmpty(CharSequence val) {
        return val == null || val.length() == 0;
    }

}
