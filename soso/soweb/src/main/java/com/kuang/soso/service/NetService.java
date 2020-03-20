package com.kuang.soso.service;

import com.kuang.pojo.MobileCard;
import com.kuang.soso.exce.ExceedAmountException;
import com.kuang.soso.exce.InsufficientException;

public interface NetService {
    double net_fund = 0.1;
    int netPlay(int flow, MobileCard card) throws InsufficientException, ExceedAmountException;
}
