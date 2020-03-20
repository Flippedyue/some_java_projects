package com.kuang.soso.abs;


import com.kuang.pojo.MobileCard;
import com.kuang.soso.packages.NetPackage;
import com.kuang.soso.packages.SuperPackage;
import com.kuang.soso.packages.TalkPackage;
import com.kuang.soso.util.CardUtil;

public abstract class ServicePackage {
    public double price;
    public void showInfo(){};

    public int remainTalkTime(MobileCard card) {
        ServicePackage serPackage = CardUtil.addPackage(card.getSerPackage());
        int talktime = card.getRealTalkTime();
        if (serPackage instanceof TalkPackage) {
            return Math.max(0, TalkPackage.TOTAL_TALKTIME - talktime);
        }
        else if (serPackage instanceof SuperPackage) {
            return Math.max(0, SuperPackage.TOTAL_TALKTIME - talktime);
        }
        return Integer.parseInt(null);
    }

    public int remainSMSCount(MobileCard card) {
        ServicePackage serPackage = CardUtil.addPackage(card.getSerPackage());
        int smscount = card.getRealSMSCount();
        if (serPackage instanceof TalkPackage) {
            return Math.max(0, TalkPackage.TOTAL_SMSCOUNT - smscount);
        }
        else if (serPackage instanceof SuperPackage) {
            return Math.max(0, SuperPackage.TOTAL_SMSCOUNT - smscount);
        }
        else {
            return Integer.parseInt(null);
        }
    }

    public int remainFlow(MobileCard card) {
        ServicePackage serPackage = CardUtil.addPackage(card.getSerPackage());
        int flow = card.getRealFlow();
        if (serPackage instanceof NetPackage) {
            return Math.max(0, CardUtil.GBtoMB(NetPackage.TOTAL_FLOW) - flow);
        }
        else if (serPackage instanceof SuperPackage) {
            return Math.max(0, CardUtil.GBtoMB(SuperPackage.TOTAL_FLOW) - flow);
        }
        else {
            return Integer.parseInt(null);
        }
    }

}
