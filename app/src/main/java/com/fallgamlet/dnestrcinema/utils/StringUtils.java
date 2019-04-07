package com.fallgamlet.dnestrcinema.utils;


public class StringUtils {

    public static boolean isNotEquals(String val1, String val2) {
        return !isEqual(val1, val2);
    }

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

    public static String trim(String value) {
        if (value == null) {
            return "";
        } else {
            return value.trim();
        }
    }

    public static String sliceWithPostfix(String value, int end, String postfix) {
        return isEmpty(value) || value.length() < end ?
                value:
                slice(value, end) + postfix;
    }

    public static String slice(String value, int end) {
        return slice(value, 0, end);
    }

    public static String slice(String value, int start, int end) {
        if (isEmpty(value)) {
            return "";
        }

        if (value.length() > end) {
            value = value.substring(start, end);
        }

        return value;
    }

}
