package io.kimmking.rpcfx.client.proxy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.client.OkHttpTools;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Date: 2020-12-17
 *
 * @author chenxi
 */
@Slf4j
public class RpcfxInvocationHandler implements InvocationHandler {
    static {
        ParserConfig.getGlobalInstance().addAccept("io.kimmking");
    }

    private final Class<?> serviceClass;
    private final String url;

    public <T> RpcfxInvocationHandler(Class<T> serviceClass, String url) {
        this.serviceClass = serviceClass;
        this.url = url;
    }

    // 可以尝试，自己去写对象序列化，二进制还是文本的，，，rpcfx是xml自定义序列化、反序列化，json: code.google.com/p/rpcfx
    // int byte char float double long bool
    // [], data class

    @Override
    public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
        // build request
        RpcfxRequest request = buildRequest(method, params);

        // do post
        RpcfxResponse response = post(request, url);

        // handle response
        if (response.isStatus()) {
            return response.getResult();
        } else {
            throw response.getException();
        }
    }
    private RpcfxRequest buildRequest(final Method method, final Object[] params) {
        RpcfxRequest request = new RpcfxRequest();
        request.setServiceClass(this.serviceClass.getName());
        request.setMethod(method.getName());
        request.setParams(params);
        return request;
    }

    /**
     * 执行远程调用
     * 参考 soul 项目 {@link OkHttpTools}
     *
     * @param req
     * @param url
     * @return
     * @throws IOException
     */
    private RpcfxResponse post(RpcfxRequest req, String url) throws IOException {
        String reqJson = JSON.toJSONString(req);
        log.info("req json: {}", reqJson);

        // 1.可以复用client
        String respJson = OkHttpTools.getInstance().post(url, reqJson);
        // 2.尝试使用httpclient或者netty client

        log.info("resp json: {}", respJson);
        return JSON.parseObject(respJson, RpcfxResponse.class);
    }
}
