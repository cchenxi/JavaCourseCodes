package io.kimmking.rpcfx.client.proxy;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端生成接口代理
 * Date: 2020-12-17
 *
 * @author chenxi
 */
@Slf4j
public final class ProxyFactory {
    // cache
    private static Map<Class, Object> map = new ConcurrentHashMap<>();

    public static <T> T createProxy(final Class<T> serviceClass, final String url) {
        try {
            Object o = map.get(serviceClass);
            if (o != null) {
                return (T) o;
            }
            o = new ByteBuddy()
                    .subclass(Object.class)
                    .implement(serviceClass)
                    .intercept(InvocationHandlerAdapter.of(new RpcfxInvocationHandler(serviceClass, url)))
                    .make()
                    .load(serviceClass.getClassLoader())
                    .getLoaded()
                    .newInstance();
            map.put(serviceClass, o);
            return (T) o;
        } catch (Exception e) {
            throw new RuntimeException("can't create proxy");
        }
    }
}
