package com.pjmike.filter.route;

import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.pjmike.context.ChannelContextUtil;
import com.pjmike.enums.RpcTypeEnum;
import com.pjmike.filter.GlobalFilter;
import com.pjmike.utils.DubboUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.concurrent.*;


/**
 * @description: Dubbo 泛化调用
 * @author: pjmike
 * @create: 2020/02/15
 */
@Slf4j
public class DubboExecutor {
    private ExecutorService threadPoolExecutor = new ThreadPoolExecutor(5, 100, 30L, TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<>(), new ThreadFactoryBuilder().setNameFormat("Route-%d-thread").build());

    private static final DubboExecutor INSTANCE = new DubboExecutor();

    public static DubboExecutor getInstance() {
        return INSTANCE;
    }

    public void doExecute(String interfaceClassName) throws Exception{
        Future<JSONObject> future = threadPoolExecutor.submit(() -> DubboUtil.sendRequest(interfaceClassName));
    }
    public void execute(Channel channel) throws Exception {
        FullHttpRequest request = ChannelContextUtil.getRequest(channel);
        URI routeUri = ChannelContextUtil.getRoute(channel).getUri();
        String url = routeUri.toASCIIString();
        // 1. 解析url -> RPC接口name
        // 2. 解析出url中包含的body
//        HttpRequestDecompose httpRequestDecompose = new HttpRequestDecompose(request);
//        Map<String, List<String>> params = httpRequestDecompose.getParams();
        String interfaceClassName = url.substring(24);
        Future<JSONObject> future = threadPoolExecutor.submit(() -> DubboUtil.sendRequest(interfaceClassName));
        JSONObject result = future.get();
        channel.writeAndFlush(result).addListener(ChannelFutureListener.CLOSE);
    }

}
