package com.alan.controller;

import com.alan.config.MqConfig;
import com.alan.service.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Desc:
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2015-11-23.
 */
@RestController
public class SenderController {

    @Autowired
    private SenderService senderService;

    @RequestMapping(value = "/sender")
    public String sendMessage(@RequestParam("message")String message){
        senderService.sendInfo(message);
        return "sendRes";
    }
}
