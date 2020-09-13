package com.memberservice.impl;

import com.sentinel.service.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

/**
 * @author 蚂蚁课堂创始人-余胜军QQ644064779
 * @title: MemberServiceImpl
 * @description: 每特教育独创第五期互联网架构课程
 * @date 2020/1/921:33
 */
@RestController
public class MemberServiceImpl implements MemberService {


    @Value("${server.port}")
    private String serverPort;

    public String getUser(Integer userId) {
        return "我是会员服务" + serverPort;
    }

    @RequestMapping("/")
    public String member(HttpServletRequest request) {
        String serverPort = request.getHeader("serverPort");
        return "this is member,网关端口号:" + serverPort;
    }
}
