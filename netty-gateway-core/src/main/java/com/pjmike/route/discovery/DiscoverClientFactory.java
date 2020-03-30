package com.pjmike.route.discovery;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.common.extension.ExtensionLoader;

/**
 * @description:
 * @author: pjmike
 * @create: 2020/03/30
 */
public class DiscoverClientFactory {
    public static DiscoverClient getDiscoverClient(String name) {
        ExtensionLoader<DiscoverClient> extensionLoader = ExtensionLoader.getExtensionLoader(DiscoverClient.class);
        if (StringUtils.isEmpty(name)) {
            return extensionLoader.getDefaultExtension();
        } else {
            return extensionLoader.getExtension(name);
        }
    }
}
