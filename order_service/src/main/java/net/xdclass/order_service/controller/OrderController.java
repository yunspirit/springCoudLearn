package net.xdclass.order_service.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import net.xdclass.order_service.service.ProductOrderService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("api/v1/order")
public class OrderController {


    @Autowired
    private ProductOrderService productOrderService;

    @Autowired
    private RedisTemplate redisTemplate;


    @RequestMapping("save")
    @HystrixCommand(fallbackMethod = "saveOrderFail")
    public Object save(@RequestParam("user_id")int userId, @RequestParam("product_id") int productId , HttpServletRequest request) {

        String token = request.getHeader("token");
        String cookie = request.getHeader("cookie");

        Map<String,Object> msg = new HashMap<>();
        msg.put("code",0);
        msg.put("msg",productOrderService.save(userId, productId));
        return msg;

    }

    //product-client失败之后，hystrix检测到失败，就会调用下面方法
    // 方法签名要和api方法签名一致,就是和上面的签名参数一致
    private Object saveOrderFail(int userId, int productId ,HttpServletRequest request){

        //监控报警
        String saveOrderKey = "save-order";
        String  sendValue = (String)redisTemplate.opsForValue().get(saveOrderKey);

        final String ip = request.getRemoteAddr();
        //组装成异步过程  向redis中存取数据
        new Thread(()->{
            if(StringUtils.isBlank(sendValue)){
                System.out.println("紧急短信，用户下单失败，请查找原因");

                System.out.println("紧急短信，用户下单失败，请离开查找原因,ip地址是="+ip);

                //发送一个http请求，调用短信服务 TODO

                redisTemplate.opsForValue().set(saveOrderKey,"save-order-fail",20, TimeUnit.SECONDS);
            }else {
                System.out.println("已经发送过短信，20s内不重复发送");
            }
        }).start();


        Map<String,Object> msg = new HashMap<>();
        msg.put("code",-1);
        msg.put("msg","抢购人数太多");
        return msg;
    }


}
