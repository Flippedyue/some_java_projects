package com.kuang.soso.packages;

import com.kuang.pojo.MobileCard;
import com.kuang.pojo.enums.Status;
import com.kuang.soso.abs.ServicePackage;
import com.kuang.soso.exce.ExceedAmountException;
import com.kuang.soso.exce.InsufficientException;
import com.kuang.soso.service.CallService;
import com.kuang.soso.service.SendService;


public class TalkPackage extends ServicePackage implements CallService, SendService {
    public static final int TOTAL_TALKTIME = 500;
    public static final int TOTAL_SMSCOUNT = 30;

    public TalkPackage() {
        price = 58;
    }

    @Override
    public void showInfo() {
        System.out.println("话痨套餐：通话时长500分钟/月，短信条数30条/月");
    }

    @Override
    public int call(int minCount, MobileCard card) throws InsufficientException, ExceedAmountException {
        int talktime = card.getRealTalkTime();
        double money = card.getMoney();
        double consumamount = card.getConsumAmount();
        double realamount = card.getRealAmount();
        int remainingTalkTime = remainTalkTime(card);

        if (minCount <= remainingTalkTime) {//套餐剩余量足够
            card.setRealTalkTime(talktime + minCount);
        }
        else {
            double amount = minCount * call_fund;//总费用
            if (money >= amount) {//余额大于费用
                card.setMoney(money - amount);
                card.setRealTalkTime(talktime + minCount);
                card.setConsumAmount(consumamount + amount);
            }
            else if ((money + realamount) >= amount) {
                card.setMoney(0);
                card.setRealAmount(realamount - (amount - money));
                card.setRealTalkTime(talktime + minCount);
                card.setStatus(Status.Overdue);
                card.setConsumAmount(consumamount + amount);
                throw new InsufficientException(
                        "本次已通话" + minCount +"分钟，额度剩余" + card.getRealAmount() + "元，请尽快充值!",
                        minCount
                );
            }
            else {
                double total_expense = money + realamount;
                int time = (int) ((total_expense)/call_fund) ;
                minCount = time + remainingTalkTime;
                card.setMoney(0);
                card.setRealAmount(total_expense % call_fund);
                card.setRealTalkTime(talktime + remainingTalkTime);
                card.setStatus(Status.Out_of_service);
                card.setConsumAmount(consumamount + total_expense);
                throw new ExceedAmountException("本次已通话" + minCount + "分钟，已超出额度，请充值！", minCount);
            }
        }
        return minCount;
    }

    @Override
    public int send(int count, MobileCard card) throws InsufficientException, ExceedAmountException{
        int smscount = card.getRealSMSCount();
        double money = card.getMoney();
        double consumamount = card.getConsumAmount();
        double realamount = card.getRealAmount();
        int remainingSMSCount = remainSMSCount(card);

        if (count <= remainingSMSCount) {//套餐剩余量足够
            card.setRealSMSCount(smscount + count);
        }
        else {
            double amount = count * send_fund;//总费用
            if (money >= amount) {//余额大于费用
                card.setMoney(money - amount);
                card.setRealSMSCount(smscount + count);
                card.setConsumAmount(consumamount + amount);
            }
            else if ((money + realamount) >= amount) {
                card.setMoney(0);
                card.setRealAmount(realamount - (amount - money));
                card.setRealSMSCount(smscount + count);
                card.setStatus(Status.Overdue);
                card.setConsumAmount(consumamount + amount);
                throw new InsufficientException(
                        "本次已发送短信" + count +"条，额度剩余" + card.getRealAmount() + "元，请尽快充值!",
                        count
                );
            }
            else {
                double total_expense = money + realamount;
                int realCount = (int) ((total_expense)/send_fund) ;
                count = realCount + remainingSMSCount;
                card.setMoney(0);
                card.setRealAmount(total_expense % send_fund);
                card.setRealSMSCount(count + remainingSMSCount);
                card.setStatus(Status.Out_of_service);
                card.setConsumAmount(consumamount + total_expense);
                throw new ExceedAmountException("本次已发送短信" + count + "条，已超出额度，请充值！", count);
            }
        }
        return count;
    }

}


