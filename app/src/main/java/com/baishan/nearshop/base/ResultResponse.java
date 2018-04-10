package com.baishan.nearshop.base;

/**
 * Created by Administrator on 2016/4/14.
 */
public class ResultResponse<T> {

    public int Code;
    public String ServerTime;
    public String Message;
    public T Result;

    public ResultResponse(int code, String serverTime, String message, T result) {
        Code = code;
        ServerTime = serverTime;
        Message = message;
        Result = result;
    }
}
