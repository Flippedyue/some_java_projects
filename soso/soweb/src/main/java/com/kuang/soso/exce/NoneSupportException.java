package com.kuang.soso.exce;

public class NoneSupportException extends Exception {
    public String msg;

    public NoneSupportException(String msg) {
        this.msg = msg;
    }
}
