package io.github.kimmking.gateway;


import io.github.kimmking.gateway.inbound.HttpInboundServer;

public class NettyServerApplication {
    // 网关名称
    public static final String GATEWAY_NAME = "NIOGateway";

    // 网关版本
    public static final String GATEWAY_VERSION = "1.0.0";

    public static final String GATEWAY_PORT = "8888";
    
    public static void main(String[] args) {
        // http://localhost:8888/api/hello  ==> gateway API
        // http://localhost:8088/api/hello  ==> backend service

        // 需要代理的实际应用地址
        String proxyServers = System.getProperty("proxyServers","http://localhost:8088");
        // 网关
        String proxyPort = System.getProperty("proxyPort",GATEWAY_PORT);
        int port = Integer.parseInt(proxyPort);

        System.out.println(GATEWAY_NAME + " " + GATEWAY_VERSION +" starting...");
        HttpInboundServer server = new HttpInboundServer(port, proxyServers);
        System.out.println(GATEWAY_NAME + " " + GATEWAY_VERSION +" started at http://localhost:" + port + " for backend server:" + proxyServers);
        try {
            server.run();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
