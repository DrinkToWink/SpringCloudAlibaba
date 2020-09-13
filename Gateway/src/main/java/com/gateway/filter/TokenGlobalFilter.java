package com.gateway.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 *定义全局的filter，进行响相应的请求处理，该类要加入到容器中
 */
@Component
public class TokenGlobalFilter implements GlobalFilter, Ordered {

    @Value("${server.port}")
    private String serverPort;

    /**
     *@param exchange 封装了请求和响应的内容
     *@param chain 进行请求和响应的转发
     *@return Mono<Void> chain.filter(exchange)
     */
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //key名：url后面拼接的那个地址，判断是否为空
        String token = exchange.getRequest().getQueryParams().getFirst("token");
        if (StringUtils.isEmpty(token)) {
            //获取响应，并设置响应状态
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            String msg = "token is null ";
            //将字符串编码成字节数组，放在DataBuffer对象中，封装在response中
            DataBuffer buffer = response.bufferFactory().wrap(msg.getBytes());
            //如果token为空，将Mono类型的信息，进行返回
            return response.writeWith(Mono.just(buffer));
        }
        //可以将网关的端口号封装到请求中，发送出去
//        ServerHttpRequest request = exchange.getRequest().mutate().header("serverPort", serverPort)
//                .build();
//        return chain.filter(exchange.mutate().request(request).build());
        // 如果token不为空，直接转发到我们真实服务
        return chain.filter(exchange);
    }

    /**
     *设置该拦截器，执行的顺序，设置为0，应该是第一个执行
     */
    public int getOrder() {
        return 0;
    }
}
