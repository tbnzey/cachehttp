package com.example.maze.cachehttp.Data.http.exception;

/**
 * 异常处理类，将异常包装成一个 FaultException ,抛给上层统一处理
 * Created by zhouwei on 16/11/17.
 */

public class FaultException extends RuntimeException {
    private int errorCode;

    public FaultException(int errorCode, String message){
        super(message);
        errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
