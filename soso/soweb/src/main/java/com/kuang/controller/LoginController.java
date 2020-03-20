package com.kuang.controller;

import com.kuang.pojo.MobileCard;
import com.kuang.service.MobileCardService;
import com.kuang.soso.abs.ServicePackage;
import com.kuang.soso.util.CardUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    @Qualifier("MobileCardServiceImpl")
    private MobileCardService mobileCardService;



    @RequestMapping("/a3")
    public String a3(String cardNumber, String password, HttpSession session) {
        MobileCard card = null;
        String msg = "";
        if (cardNumber != null) {
            card = mobileCardService.getMobileCard(cardNumber);
        }

        if (card == null) {
            msg = "号码错误！";
        } else {
            msg = "OK";
        }

        if (password != null) {
            if (card == null || !card.getPassWord().equals(password)) {
                msg = "密码错误!";
            } else {
                msg = "OK";
                session.setAttribute("cardUser", card);
            }
        }
        return msg;
    }

    @RequestMapping("/check")
    public String check(String result) {

        String msg = "";
        if (result != null) {
            if (result.equals("OK") ) {
                msg = "success";
            } else {
                msg = "fail";
            }
        } else {
            msg = "fail";
        }
        return msg;
    }

    @RequestMapping(value = "/checkpkg")
    public String checkPkg (String cardNumber, int serPackage) {
        System.out.println("123123");
        String msg = "";
        MobileCard card = mobileCardService.getMobileCard(cardNumber);
        ServicePackage servicePackage = CardUtil.addPackage(serPackage);
        if (serPackage == card.getSerPackage()) {
            msg = "不能和之前的套餐一致！";
        } else if (card.getMoney() < servicePackage.price) {
            msg = "余额不足以支付新套餐！";
        } else {
            msg = "OK";
        }
        return msg;
    }
}
