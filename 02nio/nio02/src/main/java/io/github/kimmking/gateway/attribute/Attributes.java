package io.github.kimmking.gateway.attribute;

import io.netty.util.AttributeKey;

/**
 * Date: 2020-11-04
 *
 * @author chenxi
 */
public interface Attributes {
    // 代理地址
    AttributeKey<String> PROXY_SERVER = AttributeKey.newInstance("proxyServer");
}
