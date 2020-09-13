package com.orderservice.impl;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.orderservice.openfeign.MemberServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 每特教育独创第五期互联网架构课程
 */
@RestController
public class OrderService {

//    @Autowired
//    private MemberServiceFeign memberServiceFeign;

    // 限流规则名称
    private static final String GETORDER_KEY = "orderToMember";

    /**
     *@description: 基于我们的fegin客户端形式实现rpc远程调用
     *@param:
     *@return:
     */
//    @RequestMapping("/orderFeignToMember")
//    public String orderFeignToMember() {
//        String result = memberServiceFeign.getUser(1);
//        return "我是订单服务调用会员服务的接口,返回结果" + result;
//    }

    @RequestMapping("/")
    public String order() {
        return "订单服务";
    }

    /**
     *@description: 使用代码实现服务的降级、熔断、限流，规则的配置在SentinelApplicationRunner类中
     * 为了保证程序运行时执行SentinelApplicationRunner类中的方法，它需要实现ApplicationRunner接口
     *@param:
     *@return:
     */
    @RequestMapping("/orderToMember")
    public String orderToMember() {
        Entry entry = null;
        try {
            //这个GETORDER_KEY要跟SentinelApplicationRunner中的GETORDER_KEY相同
            //应该就是根据这个做的规则与服务的匹配，即哪个规则对应哪个服务
            entry = SphU.entry(GETORDER_KEY);
            return "orderToMember接口";
        } catch (Exception e) {
            // 限流的情况就会进入到Exception
            return "当前访问人数过多，请稍后重试!";
        } finally {
            // SphU.entry(xxx) 需要与 entry.exit() 成对出现,否则会导致调用链记录异常
            if (entry != null) {
                entry.exit();
            }
        }
    }

   /**
    *@description: fallback:服务降级执行本本地方法,blockHandler:限流/熔断出现异常执行的方法
    *value:指定资源的名称，这个值要跟SentinelApplicationRunner类封装到FlowRule中的GETORDER_KEY一样
    * 这是使用注解的方式来控制服务的限流、熔断，规则的配置也是在自定义的SentinelApplicationRunner类中
    *@param:
    *@return:
    */
    @SentinelResource(value = GETORDER_KEY, blockHandler = "getOrderQpsException")
    @RequestMapping("/orderToMemberSentinelResource")
    public String orderToMemberSentinelResource() {
        return "orderToMemberSentinelResource";
    }

    /**
     *@description: blockHandler:限流/熔断出现异常执行的方法
     *@param:
     *@return:
     */
    public String getOrderQpsException(BlockException e) {
        e.printStackTrace();
        return "该接口已经被限流啦!";
    }

    /**
     * 基于我们的控制创建规则实现限流
     * @return
     */
    @SentinelResource(value = "getOrderConsole", blockHandler = "getOrderQpsException")
    @RequestMapping("/getOrderConsole")
    public String getOrderConsole() {
        return "getOrderConsole";
    }


    @SentinelResource(value = "getOrderSemaphore", blockHandler = "getOrderQpsException")
    @RequestMapping("/getOrderSemaphore")
    public String getOrderSemaphore() {
        try {//当使用控制台配置线程数的时候，把当前需要处理的线程进行睡眠，不然测试不出来效果
            //因为手动点击浏览器刷新的速度肯定比不上线程执行业务逻辑的速度，线程睡眠才能测试出效果
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName());
        return "getOrderSemaphore";
    }
    /**
     * 注意：如果没有使用 @SentinelResource注解的情况下 默认的资源名称为接口路径地址
     */


}
