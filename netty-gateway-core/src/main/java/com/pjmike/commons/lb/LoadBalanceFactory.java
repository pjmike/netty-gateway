package com.pjmike.commons.lb;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.common.extension.ExtensionLoader;

/**
 * @description: 工厂类
 *
 * @author: pjmike
 * @create: 2020/03/26
 */
public class LoadBalanceFactory {
    public static ILoadBalance getLoadBalance(String name) {
        ExtensionLoader<ILoadBalance> extensionLoader = ExtensionLoader.getExtensionLoader(ILoadBalance.class);
        if (StringUtils.isEmpty(name)) {
            return extensionLoader.getDefaultExtension();
        } else {
            return extensionLoader.getExtension(name);
        }
    }

    public static ILoadBalance getLoadBalance() {
        ExtensionLoader<ILoadBalance> extensionLoader = ExtensionLoader.getExtensionLoader(ILoadBalance.class);
        return extensionLoader.getDefaultExtension();
    }
}
