package com.kuang.controller;

import com.kuang.pojo.ConsumInfo;
import com.kuang.service.ConsumInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/consum")
public class ConsumInfoController {
    @Autowired
    @Qualifier("ConsumInfoServiceImpl")
    private ConsumInfoService consumInfoService;

    @RequestMapping("/info/{cardNumber}")
    public String printConsumInfo(@PathVariable("cardNumber") String cardNumber, Model model) {
        List<ConsumInfo> infos = null;
        int status = 0;
        infos = consumInfoService.selectByCardNumber(cardNumber);
        if (infos != null) {
            status = 1;
        }
        model.addAttribute("infos", infos);
        model.addAttribute("status", status);
        return "consumInfos";
    }
}
