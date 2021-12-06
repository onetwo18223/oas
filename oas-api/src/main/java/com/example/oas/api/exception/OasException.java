package com.example.oas.api.exception;

import lombok.Data;

@Data
public class OasException extends RuntimeException {
    private String msg;
    private int code = 500;

    public OasException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public OasException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public OasException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public OasException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }
}
