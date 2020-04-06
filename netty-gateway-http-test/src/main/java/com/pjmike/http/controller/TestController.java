package com.pjmike.http.controller;


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
    public Map<String, Object> login(@RequestBody Map<String, String> params) throws InterruptedException {
        String name = params.get("name");
        String age = params.get("age");
        System.out.println("name : " + name + ", age: " + age);
        Map<String, Object> map = new HashMap<>();
        map.put("result", "ok");
        map.put("code",200);
        return map;
    }
}
