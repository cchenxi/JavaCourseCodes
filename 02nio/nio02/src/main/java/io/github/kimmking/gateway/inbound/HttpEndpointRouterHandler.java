package io.github.kimmking.gateway.inbound;

import java.util.Arrays;

import io.github.kimmking.gateway.attribute.Attributes;
import io.github.kimmking.gateway.router.RandomEndpointRouter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * {@link HttpEndpointRouterHandler}
 * Date: 2020-11-04
 *
 * @author chenxi
 */
public class HttpEndpointRouterHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 从channel的attr中获取网关代理的实际应用地址
        String proxyServers = ctx.channel().attr(Attributes.PROXY_SERVERS).get();
        String[] proxyServerArray = proxyServers.split(",");

        // 使用路由算法计算最终请求将达到的服务器地址
        RandomEndpointRouter randomEndpointRouter = new RandomEndpointRouter();
        String endpoint = randomEndpointRouter.route(Arrays.asList(proxyServerArray));

        // 使用channel的attr传递服务请求的最终地址
        ctx.channel().attr(Attributes.PROXY_SERVER).set(endpoint);
        super.channelRead(ctx, msg);
    }
}
