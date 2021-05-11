package io.github.kimmking.gateway.outbound.netty4.client;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import lombok.extern.slf4j.Slf4j;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;


@Slf4j
public class NettyHttpClientOutboundHandler extends ChannelInboundHandlerAdapter {
    private ChannelHandlerContext channelHandlerContext;

    NettyHttpClientOutboundHandler(ChannelHandlerContext ctx) {
        this.channelHandlerContext = ctx;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            ByteBuf byteBuf = content.content();

            String responseContent = byteBuf.toString(StandardCharsets.UTF_8);
            System.out.println("内部请求响应content:" + responseContent);

            FullHttpResponse fullHttpResponse = null;
            try {
                fullHttpResponse = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(responseContent.getBytes()));
                fullHttpResponse.headers().set("Content-Type", "application/json");
                fullHttpResponse.headers().set("Content-Length", responseContent.getBytes().length);
            } catch (Exception e) {
                e.printStackTrace();
                fullHttpResponse = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
                exceptionCaught(channelHandlerContext, e);
            } finally {
                if (channelHandlerContext.channel().isActive()) {
                    fullHttpResponse.headers().set(CONNECTION, KEEP_ALIVE);
                    channelHandlerContext.writeAndFlush(fullHttpResponse);
                } else {
                    channelHandlerContext.writeAndFlush(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
                }
            }
        }
    }
}
