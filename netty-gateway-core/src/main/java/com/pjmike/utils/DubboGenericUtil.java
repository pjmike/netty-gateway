package com.pjmike.utils;


import com.pjmike.common.constants.CommonConstants;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.List;
import java.util.Map;

/**
 * @description: 泛化调用工具类
 * @author: pjmike
 * @create: 2020/02/15
 */
public class DubboGenericUtil {
    private static PropertiesUtil propertiesUtil = PropertiesUtil.getInstance(CommonConstants.DUBBO_PROPERTIES_PATH);
    private ApplicationConfig application;
    private RegistryConfig registryConfig;
    private String version;
    private static DubboGenericUtil INSTANCE = new DubboGenericUtil();
    public DubboGenericUtil() {
        this.application = new ApplicationConfig();
        this.application.setName(propertiesUtil.getString(CommonConstants.DUBBO_APPLICATION_NAME));
        this.registryConfig = new RegistryConfig();
        this.registryConfig.setAddress(propertiesUtil.getString(CommonConstants.DUBBO_REGISTRY_ADDRESS));
        this.version = propertiesUtil.getString(CommonConstants.DUBBO_SERVICE_VERSION);

        this.application.setRegistry(this.registryConfig);
    }

    public static DubboGenericUtil getInstance() {
        return INSTANCE;
    }

    /**
     * 泛化调用
     *
     * @param interfaceClass
     * @param methodName
     * @param parameters
     * @return
     */
    public Object genericInvoke(String interfaceClass, String methodName, List<Map<String,Object>> parameters) {
        ReferenceConfig<GenericService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(interfaceClass);
        referenceConfig.setVersion(version);
        //声明为泛化接口
        referenceConfig.setGeneric(true);
        referenceConfig.setApplication(application);
        // 使用dubbo提供的缓存
        ReferenceConfigCache configCache = ReferenceConfigCache.getCache();
        GenericService genericService = configCache.get(referenceConfig);

        String[] parameterTypes = new String[parameters.size()];
        Object[] args = new Object[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            parameterTypes[i] = (String)parameters.get(i).get("ParameterType");
            args[i] = parameters.get(i).get("args");
        }
        // invoke
        return genericService.$invoke(methodName, parameterTypes, args);
    }
}
