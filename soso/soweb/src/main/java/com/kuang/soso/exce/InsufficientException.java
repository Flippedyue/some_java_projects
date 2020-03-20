package com.kuang.soso.exce;

public class InsufficientException extends Exception {
    public String msg;
    public int num;

    public InsufficientException(String msg, int num) {
        this.msg = msg;
        this.num = num;
    }
}
