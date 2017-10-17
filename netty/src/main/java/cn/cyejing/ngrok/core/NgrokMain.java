package cn.cyejing.ngrok.core;

public class NgrokMain {

    private static final String serverAddress = "free.ngrok.cc";    //"b.cyejing.cn";
    private static final int serverPort = 38866;//4443;

    public static void main(String[] args) throws Exception {
        Tunnel tunnel = new Tunnel.TunnelBuild()
                .setPort(8888).setProto("tcp").setSubDomain("test").build();
        new NgrokClient(serverAddress, serverPort)
                .addTunnel(tunnel).start();

        /*
        //启动ngrok
		int port = event.getSource().getPort();
		String serverAddress = ngrokProperties.getServerAddress();
		int serverPort = ngrokProperties.getServerPort();
		String subdomain = ngrokProperties.getSubdomain();
		String hostname = ngrokProperties.getHostname();
		String proto = ngrokProperties.getProto();
		int remotePort = ngrokProperties.getRemotePort();
		String httpAuth = ngrokProperties.getHttpAuth();

		Tunnel tunnel = new Tunnel.TunnelBuild()
				.setPort(port)
				.setProto(proto)
				.setSubDomain(subdomain)
				.setHostname(hostname)
				.setRemotePort(remotePort)
				.setHttpAuth(httpAuth)
				.build();
		new NgrokClient(serverAddress, serverPort)
				.addTunnel(tunnel).start();
        */
    }

}
