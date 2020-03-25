package com.pjmike.filter.route;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.pjmike.common.context.ChannelContext;
import com.pjmike.http.NettyHttpResponseUtil;
import com.pjmike.utils.DubboGenericUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpResponse;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


/**
 * @description: Dubbo 泛化调用
 * @author: pjmike
 * @create: 2020/02/15
 */
@Slf4j
public class DubboExecutor {
    private ExecutorService threadPoolExecutor = new ThreadPoolExecutor(5, 100, 30L, TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<>(), new ThreadFactoryBuilder().setNameFormat("Dubbo-Generic-Invoke-%d-thread").build());

    private static final DubboExecutor INSTANCE = new DubboExecutor();

    public static DubboExecutor getInstance() {
        return INSTANCE;
    }

    public void execute(Channel channel) throws Exception {
        Future<Object> future = threadPoolExecutor.submit(() ->
                DubboGenericUtil.getInstance().genericInvoke(
                        getInterfaceClass(channel),getMethodName(),getParameters()));
        Object result = future.get();
        FullHttpResponse response = NettyHttpResponseUtil.buildResponse(String.valueOf(result));
        channel.writeAndFlush(response);
    }

    private String getInterfaceClass(Channel channel) {
        // demo test
        URI routeUri = ChannelContext.getRoute(channel).getUri();
        String url = routeUri.toASCIIString();
        return url.substring(24);
    }

    private String getMethodName() {
        // demo test
        return "sayHello";
    }

    private List<Map<String, Object>> getParameters() {
        // demo test
        Map<String, Object> map = new HashMap<>(16);
        map.put("ParameterType", "java.lang.String");
        map.put("args", "world");
        List<Map<String, Object>> parameters = new ArrayList<>();
        parameters.add(map);
        return parameters;
    }
}
