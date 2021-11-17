package com.imooc.api;

import com.imooc.utils.RedisOperator;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseController {
    @Resource
    public RedisOperator redis;
    public static final String MOBILE_SMSCODE = "mobile:smscode";
    public static final String REDIS_USER_TOKEN = "redis_user_token";
    public static final String REDIS_USER_INFO = "redis_user_info";
    public static final String REDIS_ADMIN_TOKEN = "redis_admin_token";

    public static final String REDIS_ALL_CATEGORY = "redis_all_category";

    public static final String REDIS_WRITER_FANS_COUNTS = "redis_writer_fans_counts";
    public static final String REDIS_MY_FOLLOW_COUNTS = "redis_my_follow_counts";

    public static final String REDIS_ARTICLE_READ_COUNTS = "redis_article_read_counts";
    public static final String REDIS_ALREADY_READ = "redis_already_read";

    public static final String REDIS_ARTICLE_COMMENT_COUNTS = "redis_article_comment_counts";

    public static final Integer COOKIE_MONTH = 30 * 24 * 60 * 60;
    public static final Integer COOKIE_DELETE = 0;
    public static final Integer COMMON_START_PAGE = 1;
    public static final Integer COMMON_PAGE_SIZE = 10;
    public static final List<String> COOKIENAMES = Arrays.asList("atoken","aid","aname");
    @Value("${website.domain-name}")
    public String DOMAIN_NAME;

    public void setCookie(HttpServletRequest request,
                          HttpServletResponse response,
                          String cookieName,
                          String cookieValue,
                          Integer maxAge) {
        try {
            cookieValue = URLEncoder.encode(cookieValue, "utf-8");
            setCookieValue(request, response, cookieName, cookieValue, maxAge);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private void setCookieValue(HttpServletRequest request,
                                HttpServletResponse response,
                                String cookieName,
                                String cookieValue,
                                Integer maxAge) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setMaxAge(maxAge);
        cookie.setDomain(DOMAIN_NAME);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public void deleteCookie(HttpServletRequest request,
                             HttpServletResponse response,
                             String cookieName) {
        try {
            String deleteValue = URLEncoder.encode("", "UTF-8");
            setCookieValue(request, response, cookieName, deleteValue, COOKIE_DELETE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void deleteCookie(HttpServletRequest request,
                             HttpServletResponse response,
                             List<String> cookieNames) {
        try {
            String deleteValue = URLEncoder.encode("", "UTF-8");
            for (String cookieName : cookieNames) {
                setCookieValue(request, response, cookieName, deleteValue, COOKIE_DELETE);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
