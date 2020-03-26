package com.pjmike.common.lb;

import cn.hutool.core.util.ClassLoaderUtil;
import com.pjmike.common.lb.LoadBalance;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.common.extension.ExtensionLoader;

import java.util.ServiceLoader;

/**
 * @description: 工厂类
 *
 * @author: pjmike
 * @create: 2020/03/26
 */
public class LoadBalanceFactory {
    public static LoadBalance getLoadBalance(String name) {
        ExtensionLoader<LoadBalance> extensionLoader = ExtensionLoader.getExtensionLoader(LoadBalance.class);
        if (StringUtils.isEmpty(name)) {
            return extensionLoader.getDefaultExtension();
        } else {
            return extensionLoader.getExtension(name);
        }
    }

    public static LoadBalance getLoadBalance() {
        ExtensionLoader<LoadBalance> extensionLoader = ExtensionLoader.getExtensionLoader(LoadBalance.class);
        return extensionLoader.getDefaultExtension();
    }
}
