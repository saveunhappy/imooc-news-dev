package com.imooc.api.controller.user;

import org.springframework.web.bind.annotation.GetMapping;

public interface HelloControllerApi {
    @GetMapping("/hello")
    Object hello();
}
