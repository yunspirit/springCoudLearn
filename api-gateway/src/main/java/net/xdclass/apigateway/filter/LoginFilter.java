package net.xdclass.apigateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;

@Component
public class LoginFilter extends ZuulFilter {

    /**
     * 过滤器类型  前置
     * @return
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /**
     *   order值越小，越先执行
     *   稍微放后一点，不能排到servlet之前，
     *   因为得排在编码servlet
     * @return
     */
    @Override
    public int filterOrder() {
         //return 0;
         return 4;
    }

    /**
     *过滤器是否生效
     * @return
     */
    @Override
    public boolean shouldFilter() {
        //有些接口不用过滤 就可以忽略

        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        System.out.println(request.getRequestURI());
        System.out.println(request.getRequestURL());

        if("/apigateway/order/api/v1/order/save".equalsIgnoreCase(request.getRequestURI())){
            return true;
        }

        return false;
    }

    /**
     * 业务逻辑
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {

        //上面shouldFilter()返回true，表示已经拦截，会执行下面的方法
        System.out.println("拦截啦");

        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        String token = request.getHeader("token");
        String cookie = request.getHeader("cookie");

        if(StringUtils.isBlank(token)) {
            token = request.getParameter("token");
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);
        }
//        }else {
//
//        }

        return null;
    }
}
