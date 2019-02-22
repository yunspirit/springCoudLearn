package net.xdclass.order_service.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 商品服务客户端   feign的名称
 */
//被调用的接口名称
@FeignClient(name = "product-service")
public interface ProductClient {


    @GetMapping("/api/v1/product/find")
    String findById(@RequestParam(value = "id") int id);


}
