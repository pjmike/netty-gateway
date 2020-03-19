package com.pjmike.gateway.controller;


import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: pjmike
 * @create: 2020/03/18 17:08
 */
@RestController
public class TestController {

    @GetMapping("/")
    public String proxyTest(@RequestParam(required = false) Long t) {
        if (t < 0) {
            System.err.println("s < 0");
            throw new IllegalArgumentException("invalid arg");
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello world";
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> params) throws InterruptedException {
        String name = params.get("name");
        String age = params.get("age");
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("age", age);
        return map;
    }
}
