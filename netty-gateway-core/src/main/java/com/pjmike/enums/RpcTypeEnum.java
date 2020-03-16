package com.pjmike.enums;

import com.pjmike.exception.GatewayException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: RpcType Enum
 * @author: pjmike
 * @create: 2020/03/16
 */
@RequiredArgsConstructor
@Getter
public enum RpcTypeEnum {
    /**
     * Http rpc type enum
     */
    HTTP("http", true),
    /**
     * dubbo rpc type enum
     */
    DUBBO("dubbo", true);
    private final String name;
    private final Boolean support;

    public static List<RpcTypeEnum> acquireSupports() {
        return Arrays.stream(RpcTypeEnum.values())
                .filter(RpcTypeEnum::getSupport).collect(Collectors.toList());
    }

    public static RpcTypeEnum acquireByName(String name) throws GatewayException {
        return Arrays.stream(RpcTypeEnum.values())
                .filter(r -> r.support && r.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new GatewayException("this rpc type cannot support"));
    }
}
