package com.fallgamlet.dnestrcinema.utils;

/**
 * Created by fallgamlet on 09.07.17.
 */

public class StringUtils {

    public static boolean isEqual(String val1, String val2) {
        return (val1 == null && val2 == null) ||
                (val1 != null && val1.equals(val2));
    }

    public static boolean isEmpty(String val) {
        return val == null || val.isEmpty();
    }

    public static boolean isEmpty(CharSequence val) {
        return val == null || val.length() == 0;
    }

}
