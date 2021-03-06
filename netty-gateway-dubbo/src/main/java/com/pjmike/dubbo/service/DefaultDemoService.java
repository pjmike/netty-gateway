package com.pjmike.dubbo.service;

import com.pjmike.dubbo.api.DemoService;
import org.apache.dubbo.config.annotation.Service;

/**
 * @description:
 * @author: pjmike
 * @create: 2020/03/18
 */
@Service(version = "1.0.0")
public class DefaultDemoService implements DemoService {
    @Override
    public String sayHello(String name) {
        return "hello, " + name;
    }
}
