package com.imooc.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

@Component
public class MyFilter extends ZuulFilter {
    /**
     * 定义过滤器的类型
     * pre:在请求被路由之前执行
     * route:在路由请求之后执行
     * post:请求路由以后执行
     * error:处理请求发生错误以后执行
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 顺序，就是spring的order
     * @return
     */
    @Override
    public int filterOrder() {
        return 1;
    }

    /**
     * 是否开启过滤器
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        System.out.println("display pre zuul filter");
        return null;
    }
}
