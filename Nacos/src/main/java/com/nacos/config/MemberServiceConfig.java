package com.nacos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @RefreshScope 监听刷新，如果配置中心的配置文件发生了改变（本地的配置文件是不行的）
 * 不用重启程序直接能获取最新值
 * 使用的位置：哪个类引用了配置文件的属性，就放在哪个类上面
 * */
@RestController
@RefreshScope
public class MemberServiceConfig {

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/getUser")
    public String getUser(){
        return "hello"+serverPort;
    }

    @Value("${youza.name}")
    private String userName;

    @GetMapping("/getUserName")
    public String testConfig(){
        return userName;
    }
}
