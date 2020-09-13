package com.gateway.service;

import com.gateway.entity.GateWayEntity;
import com.gateway.mapper.MayiktGatewayMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 感觉使用查询数据库的方式来做网关转发的配置，就是把要转发的ip写在数据库中
 *没有直接把配置写在配置中心里面方便
 */
@Service
public class GatewayService implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;

    @Autowired
    private MayiktGatewayMapper mayiktGatewayMapper;

    private static final Logger logger = (Logger) LoggerFactory.getLogger(GatewayService.class);

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    /**
     *@describe 从数据库中查询出配置信息，放入gateWayEntities集合中
     *@return
     *@param
     */
    public String loadAllLoadRoute() {
        List<GateWayEntity> gateWayEntities = mayiktGatewayMapper.gateWayAll();
        for (GateWayEntity gb : gateWayEntities) {
            //将查询出来的配置信息传入loadRoute中，由gateway做相应处理
            loadRoute(gb);
        }
        return "success";
    }

   /**
    *@describe gateway根据拿到的ip和端口的信息，做路由转发的策略
    *@param
    *@return
    */
    public String loadRoute(GateWayEntity gateWayEntity) {
        RouteDefinition definition = new RouteDefinition();
        Map<String, String> predicateParams = new HashMap<>(8);
        PredicateDefinition predicate = new PredicateDefinition();
        FilterDefinition filterDefinition = new FilterDefinition();
        Map<String, String> filterParams = new HashMap<>(8);
        URI uri = null;
        if ("0".equals(gateWayEntity.getRouteType())) {
            // 如果配置路由type为0的话 则从注册中心获取服务地址
            uri = UriComponentsBuilder.fromUriString("lb://" + gateWayEntity.getRouteUrl() + "/").build().toUri();
        } else {
            uri = UriComponentsBuilder.fromHttpUrl(gateWayEntity.getRouteUrl()).build().toUri();
        }

        // 定义的路由唯一的id
        definition.setId(gateWayEntity.getRouteId());
        predicate.setName("Path");
        //路由转发地址
        predicateParams.put("pattern", gateWayEntity.getRoutePattern());
        predicate.setArgs(predicateParams);

        // 名称是固定的, 路径去前缀
        filterDefinition.setName("StripPrefix");
        filterParams.put("_genkey_0", "1");
        filterDefinition.setArgs(filterParams);
        definition.setPredicates(Arrays.asList(predicate));
        definition.setFilters(Arrays.asList(filterDefinition));
        definition.setUri(uri);
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        return "success";
    }

}
