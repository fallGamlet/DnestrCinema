package com.fallgamlet.dnestrcinema.network;

/**
 * Created by fallgamlet on 04.07.16.
 */
public class MyException extends Exception {
    //region Static public fields
    public static int ERROR_CODE_PARAMS = 1001;
    public static int ERROR_CODE_PARSE = 1002;
    public static int ERROR_CODE_SOAP = 1003;
    public static int ERROR_CODE_1C = 2001;

    public static int ERROR_CODE_LIFETIME_END = -32501;
    public static int ERROR_CODE_KEY_MISSMATCH = -32502;
    public static int ERROR_CODE_ROLE_MISSMATCH = -32503;
    public static int ERROR_CODE_TOKEN_COLLISION = -32504;
    public static int ERROR_CODE_USER_NOT_FOUND = -32505;
    public static int ERROR_CODE_TRAINER_NOT_FOUND = -32506;
    public static int ERROR_CODE_CLIENT_NOT_FOUND = -32507;

    public static int ERROR_NETWORK_UNAUTHORIZED  = 401;
    public static int ERROR_NETWORK_BADREQUEST = 400;
    public static int ERROR_NETWORK_FORBIDDEN = 403;
    public static int ERROR_NETWORK_NOTFOUND = 404;
    public static int ERROR_NETWORK_REQUESTTIMEOUT = 408;
    //endregion


    //region Fields
    protected int mCode;
    protected String mMessage;
    //endregion

    public MyException(int code, String message) {
        mCode = code;
        mMessage = message;
    }

    public int getCode() {
        return mCode;
    }

    @Override
    public String getLocalizedMessage() {
        return getMessage();
    }

    @Override
    public String getMessage() {
        return mMessage;
    }
}
