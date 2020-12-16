package io.kimmking.rpcfx.api;

public interface RpcfxResolver {

    // 查找实现类
    Object resolve(String serviceClass);

}
