package io.github.kimmking.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * {@link HttpRequestHeaderFilter}
 * Date: 2020-11-03
 *
 * @author chenxi
 */
public class HttpRequestHeaderFilter implements HttpRequestFilter {
    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        System.out.println("header filter set header.nio");
        fullRequest.headers().set("nio", "chenxi");
    }
}
