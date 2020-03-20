package com.kuang.controller;

import com.alibaba.fastjson.JSON;
import com.kuang.pojo.ConsumInfo;
import com.kuang.pojo.MobileCard;
import com.kuang.pojo.Scene;
import com.kuang.pojo.enums.Status;
import com.kuang.service.ConsumInfoService;
import com.kuang.service.MobileCardService;
import com.kuang.service.SceneService;
import com.kuang.soso.abs.ServicePackage;
import com.kuang.soso.exce.ExceedAmountException;
import com.kuang.soso.exce.InsufficientException;
import com.kuang.soso.packages.NetPackage;
import com.kuang.soso.packages.TalkPackage;
import com.kuang.soso.service.CallService;
import com.kuang.soso.service.NetService;
import com.kuang.soso.service.SendService;
import com.kuang.soso.util.CardUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/soso")
public class MobileCardController {

    @Autowired
    @Qualifier("MobileCardServiceImpl")
    private MobileCardService mobileCardService;

    @Autowired
    @Qualifier("SceneServiceImpl")
    private SceneService sceneService;

    @Autowired
    @Qualifier("ConsumInfoServiceImpl")
    private ConsumInfoService consumInfoService;

    @RequestMapping("/home")
    public String home() {
        return "home";
    }

    @RequestMapping("/use/{type}/{cardNumber}")
    public String UseSoSo(@PathVariable("type") String type, @PathVariable("cardNumber") String number, Model model) {
        MobileCard card = mobileCardService.getMobileCard(number);
        ServicePackage serPackage = CardUtil.addPackage(card.getSerPackage());
        List<Scene> scenes = sceneService.selectByType(type);

        Random random = new Random();
        boolean status = true;
        boolean flag = true;
        int num = 0;
        String result = "";
        String description = "";
        String unit = "";

        if (card.getStatus() == Status.Out_of_service) {
            status = false;
            result = "用户已停机！请尽快充值恢复正常使用";
        }
        else {
            Scene scene = scenes.get(random.nextInt(scenes.size()));

            if ("通话".equals(scene.getType())) {
                if (!(serPackage instanceof CallService)) {
                    flag = false;
                } else {
                    CallService callService = (CallService) serPackage;
                    description = scene.getDescription();
                    try {
                        result = "OK";
                        num = callService.call(scene.getData(), card);
                    } catch (InsufficientException e) {
                        result = e.msg;
                        num = e.num;
                    } catch (ExceedAmountException e) {
                        result = e.msg;
                        num = e.num;
                    }
                    unit = "分钟";
                }
            } else if ("短信".equals(scene.getType())) {
                if (!(serPackage instanceof SendService)) {
                    flag = false;
                } else {
                    SendService sendService = (SendService) serPackage;
                    description = scene.getDescription();
                    try {
                        result = "OK";
                        num = sendService.send(scene.getData(), card);
                    } catch (InsufficientException e) {
                        result = e.msg;
                        num = e.num;
                    } catch (ExceedAmountException e) {
                        System.out.println(e.msg);
                        num = e.num;
                    }
                    unit = "条";
                }
            } else if ("上网".equals(scene.getType())) {
                if (!(serPackage instanceof NetService)) {
                    flag = false;
                } else {
                    NetService netService = (NetService) serPackage;
                    description = scene.getDescription();
                    try {
                        result = "OK";
                        num = netService.netPlay(scene.getData(), card);
                    } catch (InsufficientException e) {
                        result = e.msg;
                        num = e.num;
                    } catch (ExceedAmountException e) {
                        result = e.msg;
                        num = e.num;
                    }
                    unit = "MB";
                }
            }
            if (flag) {
                consumInfoService.insert(new ConsumInfo(number, type, num, 1));
            } else {
                result = "用户没有开通" + type + "功能！";
            }
        }
        CardUtil.changeRank(card);
        mobileCardService.update(card);
        model.addAttribute("status", status); // 判断是否停机
        model.addAttribute("flag", flag); // 判断是否有这项使用功能
        model.addAttribute("description", description); // 消费场景描述
        model.addAttribute("result", result); // 结果：是否欠费等
        model.addAttribute("num", num); // 消费的量
        model.addAttribute("unit", unit);
        return "use";
    }

    @RequestMapping("/showRemainDetail/{cardNumber}")
    public String showRemainDetail(@PathVariable("cardNumber") String cardNumber, Model model) {
        MobileCard card = mobileCardService.getMobileCard(cardNumber);
        ServicePackage serPackage = CardUtil.addPackage(card.getSerPackage());
        HashMap<String, String> map = new HashMap<String, String>();
        if (!(serPackage instanceof NetPackage)) {
            int talkTime = serPackage.remainTalkTime(card);
            int smsCount = serPackage.remainSMSCount(card);
            map.put("通话时长", Integer.toString(talkTime));
            map.put("短信条数", Integer.toString(smsCount));
        }
        if (!(serPackage instanceof TalkPackage)) {
            double flow = serPackage.remainFlow(card);
            double flowGB = flow / 1024;
            map.put("上网流量", String.format("%.1f", flowGB));
        }
        model.addAttribute("map", map);
        return "showMsg";
    }

    @RequestMapping("/showAmountDetail/{cardNumber}")
    public String showAmountDetail(@PathVariable("cardNumber") String cardNumber, Model model) {
        System.out.println("123123");
        MobileCard card = mobileCardService.getMobileCard(cardNumber);
        ServicePackage serPackage = CardUtil.addPackage(card.getSerPackage());
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("套餐资费/元", String.format("%.1f", serPackage.price));
        map.put("合计消费/元", String.format("%.1f", card.getConsumAmount()));
        map.put("账户余额/元", String.format("%.1f", card.getMoney()));
        model.addAttribute("map", map);
        return "showMsg";
    }

    @RequestMapping("/main")
    public String main() {
        return "main";
    }

    @RequestMapping("/{cardNumber}")
    public String UserMsg(@PathVariable("cardNumber") String cardNumber, Model model) {
        MobileCard card = mobileCardService.getMobileCard(cardNumber);
        List<String> head = card.getHead();
        List<String> content = card.getDetail();
        model.addAttribute("head", head);
        model.addAttribute("content", content);
        return "home";
    }

    @RequestMapping("/toUpdateUser/{cardNumber}")
    public String toUpdateUser(Model model, @PathVariable("cardNumber") String cardNumber) { // model用来带给前端
        MobileCard card = mobileCardService.getMobileCard(cardNumber);
        model.addAttribute("cardUser", card);
        return "update";
    }

    @RequestMapping("/updateUser/{cardNumber}")
    public String updateUser(Model model, @PathVariable("cardNumber") String cardNumber, String userName, String passWord) {
        MobileCard card = mobileCardService.getMobileCard(cardNumber);
        card.setUserName(userName);
        card.setPassWord(passWord);
        mobileCardService.update(card);
        model.addAttribute("cardUser", card);
        return "redirect:/soso/{cardNumber}";
    }

    @RequestMapping("/charge")
    public String toCharge() {
        return "charge";
    }

    @RequestMapping("/charge/{cardNumber}")
    public String charge(@PathVariable("cardNumber") String cardNumber, Double money) {
        MobileCard card = mobileCardService.getMobileCard(cardNumber);
        if (card.getStatus() != Status.Normal) {
            double addAmount = Math.min(card.getRank().amount, card.getRealAmount()+money);
            card.setMoney(card.getMoney() + money - (addAmount - card.getRealAmount()));
            card.setRealAmount(addAmount);
            if (card.getRealAmount() < card.getRank().amount) {
                card.setStatus(Status.Overdue);
            } else {
                card.setStatus(Status.Normal);
            }
        }
        mobileCardService.update(card);
        return "redirect:/soso/{cardNumber}";
    }

    @RequestMapping("/changePkg")
    public String ToChangePkg () {
        return "changepkg";
    }

    @RequestMapping("/changePkg/{cardNumber}")
    @ResponseBody
    public String ChangePkg(@PathVariable("cardNumber") String cardNumber, int Package) {
        String msg = "";
        try {
            MobileCard card = mobileCardService.getMobileCard(cardNumber);
            // System.out.println(Package);
            ServicePackage servicePackage = CardUtil.addPackage(Package);
            card.setSerPackage(Package);
            card.setMoney(card.getMoney() - servicePackage.price);
            card.setConsumAmount(card.getConsumAmount() + servicePackage.price);
            card.setRealFlow(0);
            card.setRealTalkTime(0);
            card.setRealSMSCount(0);
            mobileCardService.update(card);
            msg = "success";
        } catch (Exception e) {
            msg = "fail";
        }
        return JSON.toJSONString(msg);
    }

    @RequestMapping("/quit/{cardNumber}")
    public String quit(@PathVariable("cardNumber") String cardNumber) {
        MobileCard card = mobileCardService.getMobileCard(cardNumber);
        card.setStatus(Status.ExitSoso);
        mobileCardService.update(card);
        return "redirect:/soso/main";
    }

}
