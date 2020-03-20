package com.kuang.soso.service;

import com.kuang.pojo.MobileCard;
import com.kuang.soso.exce.ExceedAmountException;
import com.kuang.soso.exce.InsufficientException;

public interface SendService {
    double send_fund = 0.1;
    int send(int count, MobileCard card) throws InsufficientException, ExceedAmountException;
}
