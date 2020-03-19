package com.pjmike.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;

/**
 * @description: 泛化调用工具类
 * @author: pjmike
 * @create: 2020/02/15
 */
public class DubboUtil {
    public static JSONObject sendRequest(String className) {
        ApplicationConfig application = new ApplicationConfig();
        application.setName("api-generic-consumer");
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("zookeeper://127.0.0.1:2181");
        application.setRegistry(registryConfig);

        ReferenceConfig<GenericService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(className);
        referenceConfig.setVersion("1.0.0");
        //声明为泛化接口
        referenceConfig.setGeneric(true);
        referenceConfig.setApplication(application);

        GenericService genericService = referenceConfig.get();
        String[] types = new String[]{"java.lang.String"};
        Object result = genericService.$invoke("sayHello", types, new String[]{"world"});
        System.err.println(result);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 200);
        jsonObject.put("result", result);
        System.err.println(JSON.toJSONString(jsonObject));
        return jsonObject;
    }
}
