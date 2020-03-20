package com.kuang.soso.exce;

public class ExceedAmountException extends Exception{
    public String msg;
    public int num;

    public ExceedAmountException(String msg, int num) {
        this.msg = msg;
        this.num = num;
    }
}
