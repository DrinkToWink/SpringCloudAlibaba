package com.orderservice.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 蚂蚁课堂创始人-余胜军QQ644064779
 * @title: MemberServiceFeign
 * @description: 每特教育独创第五期互联网架构课程
 * @date 2020/1/921:43
 */

@FeignClient("meitemayikt-member")
public interface MemberServiceFeign {

    /**
     * 提供我们发布的接口
     * @param userId
     * @return
     */
    @GetMapping("/getUser")
    String getUser(@RequestParam("userId") Integer userId);
}
