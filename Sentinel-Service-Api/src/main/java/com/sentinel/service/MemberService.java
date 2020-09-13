package com.sentinel.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *@author: 一个小菜逼
 *@Date: 2020/3/25
 *@description:
 */
public interface MemberService {

    /**
     *@description: 提供我们发布的接口
     *@param:
     *@return:
     */
    @GetMapping("/getUser")
    String getUser(@RequestParam("userId") Integer userId);
}