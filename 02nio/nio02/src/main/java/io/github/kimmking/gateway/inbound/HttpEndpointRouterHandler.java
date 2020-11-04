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
        RandomEndpointRouter randomEndpointRouter = new RandomEndpointRouter();
        String endpoint = randomEndpointRouter.route(Arrays.asList(
                "http://localhost:8088",
                "http://localhost:8088",
                "http://localhost:8088",
                "http://localhost:8088"
        ));
        ctx.channel().attr(Attributes.PROXY_SERVER).set(endpoint);
        super.channelRead(ctx, msg);
    }
}
