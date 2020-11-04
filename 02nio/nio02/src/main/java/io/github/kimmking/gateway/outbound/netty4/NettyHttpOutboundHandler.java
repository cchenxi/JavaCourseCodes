package io.github.kimmking.gateway.outbound.netty4;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.github.kimmking.gateway.outbound.httpclient4.NamedThreadFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.Setter;

/**
 * Date: 2020-11-03
 *
 * @author chenxi
 */
public class NettyHttpOutboundHandler {

    @Setter
    private String backendUrl;
    private ExecutorService proxyService;

    public NettyHttpOutboundHandler() {
        int cores = Runtime.getRuntime().availableProcessors() * 2;
        long keepAliveTime = 1000;
        int queueSize = 2048;
        proxyService = new ThreadPoolExecutor(cores, cores, keepAliveTime, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(queueSize),
                new NamedThreadFactory("proxyService"),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) {
        proxyService.submit(() -> {
            NettyHttpClient nettyHttpClient = new NettyHttpClient(backendUrl);
            nettyHttpClient.fetchGet(fullRequest, ctx);
        });
    }
}
