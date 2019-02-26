package net.xdclass.order_service.service;

import net.xdclass.order_service.fallback.ProductClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 商品服务客户端   feign的名称
 */
//被调用的接口名称
//FeignClient本身就整合了hystrix
@FeignClient(name = "product-service",fallback = ProductClientFallback.class)
public interface ProductClient {

    //如果调用商品服务失败怎么办？
    @GetMapping("/api/v1/product/find")
    String findById(@RequestParam(value = "id") int id);


}
