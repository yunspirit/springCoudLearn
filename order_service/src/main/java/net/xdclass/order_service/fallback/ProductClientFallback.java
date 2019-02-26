package net.xdclass.order_service.fallback;

import net.xdclass.order_service.service.ProductClient;
import org.springframework.stereotype.Component;

//针对商品服务做降级处理
@Component
public class ProductClientFallback implements ProductClient {


    @Override
    public String findById(int id) {
        System.out.println("feign调用product service异常");
        return null;
    }
}
