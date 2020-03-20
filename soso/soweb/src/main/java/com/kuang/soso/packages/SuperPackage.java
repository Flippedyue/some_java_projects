package com.kuang.soso.packages;

import com.kuang.pojo.MobileCard;
import com.kuang.soso.abs.ServicePackage;
import com.kuang.soso.exce.ExceedAmountException;
import com.kuang.soso.exce.InsufficientException;
import com.kuang.soso.service.CallService;
import com.kuang.soso.service.NetService;
import com.kuang.soso.service.SendService;

public class SuperPackage extends ServicePackage implements CallService, SendService, NetService {
    public static final int TOTAL_TALKTIME = 200;
    public static final int TOTAL_SMSCOUNT = 50;
    public static final int TOTAL_FLOW = 1;

    public SuperPackage() {
        price = 78;
    }

    @Override
    public void showInfo() {
        System.out.println("超人套餐：通话时长为200分钟/月，短信条数为50条/月，上网流量为1GB/月");
    }

    @Override
    public int netPlay(int flow, MobileCard card) throws InsufficientException, ExceedAmountException {
        NetPackage netPackage = new NetPackage();
        return netPackage.netPlay(flow, card);
    }

    @Override
    public int call(int minCount, MobileCard card) throws InsufficientException, ExceedAmountException {
        TalkPackage talkPackage = new TalkPackage();
        return talkPackage.call(minCount, card);
    }

    @Override
    public int send(int count, MobileCard card) throws InsufficientException, ExceedAmountException {
        TalkPackage talkPackage = new TalkPackage();
        return talkPackage.send(count, card);
    }
    // 上面三个函数是不是可以去掉？
}
