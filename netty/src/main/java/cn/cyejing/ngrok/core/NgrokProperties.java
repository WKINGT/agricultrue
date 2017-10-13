package cn.cyejing.ngrok.core;


public class NgrokProperties {

    /**
     * 服务器地址
     */
    private String serverAddress = "free.ngrok.cc";    //"b.cyejing.cn";
    /**
     * 服务器端口
     */
    private int serverPort = 38866;         //4443;
    /**
     * 协议
     */
    private String proto = "tcp";     //"http";
    /**
     * 自定义子域名
     */
    private String subdomain;
    /**
     * 自定义域名
     */
    private String hostname = "yourdomain.com";
    private int remotePort;
    private String httpAuth;

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getProto() {
        return proto;
    }

    public void setProto(String proto) {
        this.proto = proto;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(String subdomain) {
        this.subdomain = subdomain;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public String getHttpAuth() {
        return httpAuth;
    }

    public void setHttpAuth(String httpAuth) {
        this.httpAuth = httpAuth;
    }
}
