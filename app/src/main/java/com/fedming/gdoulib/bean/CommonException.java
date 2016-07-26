package com.fedming.gdoulib.bean;

public class CommonException extends Exception {

    /**
     * 通用异常处理
     */
    private static final long serialVersionUID = 1L;

    public CommonException() {
        super();
    }

    public CommonException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonException(String message) {
        super(message);
    }

    public CommonException(Throwable cause) {
        super(cause);
    }

}
