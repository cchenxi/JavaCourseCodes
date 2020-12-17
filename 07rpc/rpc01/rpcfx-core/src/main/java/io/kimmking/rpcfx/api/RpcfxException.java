package io.kimmking.rpcfx.api;

/**
 * Rpc异常
 * Date: 2020-12-15
 *
 * @author chenxi
 */
public class RpcfxException extends Throwable {
    public RpcfxException() {
    }

    public RpcfxException(String message) {
        super(message);
    }

    public RpcfxException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcfxException(Throwable cause) {
        super(cause);
    }

    public RpcfxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
