package com.orderservice.impl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 蚂蚁课堂创始人-余胜军QQ644064779
 * @title: OrderTempService
 * @description: 每特教育独创第五期互联网架构课程
 * @date 2020/2/419:07
 */
@RestController
public class OrderTempService {


    /**
     * orderToUserMemer
     *
     * @return
     */
    @RequestMapping("/orderToMemer")
    public String orderToUserMemer() {
        try {
            // 模拟调用其他接口
            Thread.sleep(500);
        } catch (Exception e) {

        }
        System.out.println(">>>" + Thread.currentThread().getName() + "<<<");
        return "订单调用会员成功";
    }

    /**
     * smsOrder
     *
     * @return
     */
    @RequestMapping("/smsOrder")
    public String smsOrder() {
        System.out.println(">>>" + Thread.currentThread().getName() + "<<<");
        return "订单发送短信消息";
    }
}
