package com.kuang.pojo;

import com.kuang.pojo.enums.Rank;
import com.kuang.pojo.enums.Status;
import com.kuang.soso.util.CardUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MobileCard {
    private String cardNumber;
    private String userName;
    private String passWord;
    private int serPackage;
    private double consumAmount;
    private double money; //余额
    private int realTalkTime; //总通话时间
    private int realSMSCount; //总短信条数
    private int realFlow; //总消耗流量
    private Rank rank; //用户等级
    private double realAmount;//欠费额度
    private Status status;//号码状态

    public MobileCard(String cardNumber, String userName, String passWord, int serPackage, double money) {
        this.cardNumber = cardNumber;
        this.userName = userName;
        this.passWord = passWord;
        this.serPackage = serPackage;
        this.consumAmount = CardUtil.addPackage(serPackage).price;
        this.money = money - consumAmount;
        this.realTalkTime = 0;
        this.realSMSCount = 0;
        this.realFlow = 0;
        this.rank = Rank.common;
        this.status = Status.Normal;
        this.realAmount = 0;
    }

    public List<String> getHead() {
        List<String> head = new ArrayList<>();
        head.add("卡号");
        head.add("用户名");
        head.add("使用套餐");
        head.add("消费金额");
        head.add("余额");
        head.add("实际通话时长/分钟");
        head.add("实际短信条数/条");
        head.add("实际上网流量/MB");
        head.add("用户等级");
        head.add("用户状态");
        head.add("欠费总额度");
        return head;
    }

    public List<String> getDetail() {
        List<String> detail = new ArrayList<>();
        detail.add(cardNumber);
        detail.add(userName);
        switch (serPackage) {
            case 1:
                detail.add("话痨套餐");
                break;
            case 2:
                detail.add("网虫套餐");
                break;
            case 3:
                detail.add("超人套餐");
                break;
            default:
                break;
        }
        detail.add(Double.toString(consumAmount));
        detail.add(Double.toString(money));
        detail.add(Integer.toString(realTalkTime));
        detail.add(Integer.toString(realSMSCount));
        detail.add(Integer.toString(realFlow));
        detail.add(rank.name());
        detail.add(status.name());
        detail.add(Double.toString(realAmount));
        return detail;
    }
}
