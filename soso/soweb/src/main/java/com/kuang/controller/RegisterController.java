package com.kuang.controller;

import com.alibaba.fastjson.JSON;
import com.kuang.pojo.MobileCard;
import com.kuang.service.MobileCardService;
import com.kuang.soso.abs.ServicePackage;
import com.kuang.soso.util.CardUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    @Qualifier("MobileCardServiceImpl")
    private MobileCardService mobileCardService;
    private static final int LENGTH = 8;
    private static final int COUNT = 9;
    private static final String PHONE_PREFIX = "139";

    public static String createNumber() {
        Random random = new Random();
        String NewNumber;
        NewNumber = PHONE_PREFIX;
        for (int j = 0; j < LENGTH; j++) {
            NewNumber += random.nextInt(10);
        }
        return NewNumber;
    }

    @RequestMapping("/phoneNum")
    public String phoneNum() {
        List<String> NewNumbers = new ArrayList<>();
        int dup = 0;
        do {
            for (int i = 0; i < COUNT; i++) {
                NewNumbers.add(createNumber());
            }
            dup = mobileCardService.dupCount(NewNumbers);
        } while (dup > 0);
        return JSON.toJSONString(NewNumbers);
    }

    @RequestMapping("/money")
    public String checkMoney(String Money, String Package) {
        String msg = "";
        double money = Double.parseDouble(Money);
        int Choose = Integer.parseInt(Package);
        ServicePackage servicePackage = CardUtil.addPackage(Choose);
        if (money < servicePackage.price) {
            msg = "充值金额小于套餐余额" + servicePackage.price + "元!";
        } else {
            msg = "OK";
        }
        return msg;
    }

    @RequestMapping("/commit")
    public String Register(String cardNumber, String userName, String passWord, int serPackage, double money, HttpSession session){
        MobileCard card = new MobileCard(cardNumber, userName, passWord, serPackage, money);
        System.out.println(cardNumber);
        try {
            mobileCardService.insertCard(card);
            session.setAttribute("cardUser", card);
            System.out.println("success");
            return "success";
        } catch (Exception e) {
            return "fail";
        }
    }
}
