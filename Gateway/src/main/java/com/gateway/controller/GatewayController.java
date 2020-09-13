package com.gateway.controller;

import com.gateway.service.GatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 蚂蚁课堂创始人-余胜军QQ644064779
 * @title: GatewayController
 * @description: 每特教育独创第五期互联网架构课程
 * @date 2020/1/1821:05
 */
@RestController
public class GatewayController {
    @Autowired
    private GatewayService gatewayService;

    /**
     *@description: 从数据库中加载配置信息
     *@param:
     *@return:
     */
    @RequestMapping("/synGatewayConfig")
    public String synGatewayConfig() {
        return gatewayService.loadAllLoadRoute();
    }
}
