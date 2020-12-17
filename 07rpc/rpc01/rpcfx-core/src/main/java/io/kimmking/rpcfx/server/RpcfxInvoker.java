package io.kimmking.rpcfx.server;

import io.kimmking.rpcfx.api.RpcfxException;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResolver;
import io.kimmking.rpcfx.api.RpcfxResponse;

import java.lang.reflect.Method;
import java.util.Arrays;

public class RpcfxInvoker {

    private RpcfxResolver resolver;

    public RpcfxInvoker(RpcfxResolver resolver){
        this.resolver = resolver;
    }

    /**
     * - 作业：两次json序列化合并成一次
     *
     * - 作业：封装统一的异常类
     *
     * @param request
     * @return
     */
    public RpcfxResponse invoke(RpcfxRequest request) {
        RpcfxResponse response;
        String serviceClass = request.getServiceClass();

        // 作业1：改成泛型和反射
        Object service = resolver.resolve(serviceClass);

        try {
            Method method = resolveMethodFromClass(service.getClass(), request.getMethod());
            Object result = method.invoke(service, request.getParams());
            response = new RpcfxResponse();
            response.setResult(result);
            response.setStatus(true);
        } catch (Exception e) {
            response = new RpcfxResponse();
            response.setException(new RpcfxException(e));
            response.setStatus(false);
        }
        return response;
    }

    private Method resolveMethodFromClass(Class<?> klass, String methodName) {
        return Arrays.stream(klass.getMethods()).filter(m -> methodName.equals(m.getName())).findFirst().get();
    }
}
