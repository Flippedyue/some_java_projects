package com.kuang.controller;

import com.alibaba.fastjson.JSON;
import com.kuang.service.SceneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/scene")
public class SceneController {
    @Autowired
    @Qualifier("SceneServiceImpl")
    private SceneService sceneService;

    @RequestMapping(value = "/{type}", method = RequestMethod.GET)
    @ResponseBody
    public String getScenes(@PathVariable("type") String type) {
        return JSON.toJSONString(sceneService.selectByType(type));
    }
}
