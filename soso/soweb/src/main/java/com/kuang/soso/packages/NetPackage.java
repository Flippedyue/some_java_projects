package com.kuang.soso.packages;


import com.kuang.pojo.MobileCard;
import com.kuang.pojo.enums.Status;
import com.kuang.soso.abs.ServicePackage;
import com.kuang.soso.exce.ExceedAmountException;
import com.kuang.soso.exce.InsufficientException;
import com.kuang.soso.service.NetService;

public class NetPackage extends ServicePackage implements NetService {
    public static final int TOTAL_FLOW = 3;
    public NetPackage() {
        price = 68;
    }

    @Override
    public void showInfo() {
        System.out.println("网虫套餐：上网流量3GB/月");
    }

    @Override
    public int netPlay(int flow, MobileCard card) throws InsufficientException, ExceedAmountException {
        int realflow = card.getRealFlow();
        double money = card.getMoney();
        double consumamount = card.getConsumAmount();
        double realamount = card.getRealAmount();
        int remainingFlow = remainFlow(card);

        if (flow <= remainingFlow) {//套餐剩余量足够
            card.setRealFlow(realflow + flow);
        }
        else {
            double amount = flow * net_fund;//总费用
            if (money >= amount) {//余额大于费用
                card.setMoney(money - amount);
                card.setRealFlow(realflow + flow);
                card.setConsumAmount(consumamount + amount);
            }
            else if ((money + realamount) >= amount) {
                card.setMoney(0);
                card.setRealAmount(realamount - (amount - money));
                card.setRealFlow(realflow + flow);
                card.setStatus(Status.Overdue);
                card.setConsumAmount(consumamount + amount);
                throw new InsufficientException(
                        "本次已使用流量" + flow +"MB，额度剩余" + card.getRealAmount() + "元，请尽快充值!", flow
                );
            }
            else {
                double total_expense = money + realamount;
                flow = (int) (((total_expense)/net_fund) + remainingFlow);
                card.setMoney(0);
                card.setRealAmount(0);
                card.setRealFlow(realflow + remainingFlow);
                card.setStatus(Status.Out_of_service);
                card.setConsumAmount(consumamount + total_expense);
                throw new ExceedAmountException("本次已使用流量" + flow  + "MB，已超出额度，请充值！", flow);
            }
        }
        return flow;
    }
}
