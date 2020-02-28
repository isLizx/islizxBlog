package com.islizx.exception;

/**
 * @author lizx
 * @date 2020-02-20 - 21:50
 */
public class CustomException extends Exception {

    private Integer code;

    private String message;


    public CustomException() {
        super();
    }

    public CustomException(String message) {
        this.code = 500;
        this.message = message;
    }

    public CustomException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
