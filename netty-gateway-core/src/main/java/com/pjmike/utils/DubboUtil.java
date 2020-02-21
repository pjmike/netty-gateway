package com.pjmike.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.rpc.service.GenericService;

/**
 * @description: 泛化调用工具类
 * @author: pjmike
 * @create: 2020/02/15
 */
public class DubboUtil {
    public static JSONObject sendRequest(String className) {
        ApplicationConfig application = new ApplicationConfig();
        ReferenceConfig<GenericService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(className);
        //声明为泛化接口
        referenceConfig.setGeneric(true);
        referenceConfig.setApplication(application);

        GenericService genericService = referenceConfig.get();
        String[] types = new String[]{"java.lang.String"};
        Object result = genericService.$invoke("test", types, new String[]{"hello world"});
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 200);
        jsonObject.put("result", result);
        return jsonObject;
    }
}
