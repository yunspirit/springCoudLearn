package net.xdclass.order_service.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import net.xdclass.order_service.domain.ProductOrder;
import net.xdclass.order_service.service.ProductClient;
import net.xdclass.order_service.service.ProductOrderService;
import net.xdclass.order_service.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class ProductOrderServiceImpl implements ProductOrderService {


    //feign使用
    @Autowired
    private ProductClient productClient;


    //ribbon使用
    @Autowired
    private RestTemplate restTemplate;


    private final Logger logger = LoggerFactory.getLogger(getClass());


    //ribbon方式  使用RestTemplate直接访问URL进行调用
    @Override
    public ProductOrder save2(int userId, int productId){



        /**
         * //订单服务调用商品服务
         * //Object obj = restTemplate.getForObject("http://product-service/api/v1/product/find?id="+productId,Object.class);
         */
        Map<String,Object> productMap = restTemplate.getForObject("http://product-service/api/v1/product/find?id="+productId,Map.class);
        //System.out.println(obj);

        ProductOrder productOrder = new ProductOrder();
        productOrder.setCreateTime(new Date());
        productOrder.setUserId(userId);
        productOrder.setTradeNo(UUID.randomUUID().toString());

        productOrder.setProductName(productMap.get("name").toString());
        productOrder.setPrice(Integer.parseInt(productMap.get("price").toString()));

        return productOrder;
    }



    //feign方式调用
    @Override
    public ProductOrder save(int userId, int productId) {

        logger.info("service save order");

        if(userId == 1){
            return null;
        }

        String response = productClient.findById(productId);

        JsonNode  jsonNode = JsonUtils.str2JsonNode(response);

        ProductOrder productOrder = new ProductOrder();
        productOrder.setCreateTime(new Date());
        productOrder.setUserId(userId);
        productOrder.setTradeNo(UUID.randomUUID().toString());
        productOrder.setProductName(jsonNode.get("name").toString());
        productOrder.setPrice(Integer.parseInt(jsonNode.get("price").toString()));
        return productOrder;
    }











}
