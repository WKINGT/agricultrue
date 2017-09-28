package socket;

import com.jfinal.aop.Enhancer;
import com.jfinal.config.Routes;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.redis.RedisPlugin;
import net.xgs.commons.plugin.IocPlugin;
import net.xgs.init.InitDb;
import net.xgs.model.ViewMachineBlockType;
import net.xgs.model._MappingKit;
import net.xgs.model._MappingViewKit;
import net.xgs.services.AlarmMsgService;
import net.xgs.services.MachineBlockTypeService;
import websocket.WebsocketChatServer;

import java.util.List;

public class StartServer {

	MachineBlockTypeService machineService = Enhancer.enhance(MachineBlockTypeService.class);
	private Prop prop = PropKit.use("cfg.properties");
	protected static DruidPlugin dp;
    protected static ActiveRecordPlugin activeRecord;
	public static void main(String[] args) throws Exception {
		StartServer startServer = new StartServer();
		startServer.start();
	}
	
	public void start(){
		InitDb init = null;
		try {
			init = (InitDb) Class.forName(prop.get("system.db")).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		DruidPlugin druidPlugin = init.init();
		druidPlugin.setMaxPoolPreparedStatementPerConnectionSize(InitDb.prop.getInt("jdbc.min_poll", 10));
		druidPlugin.setMaxActive(InitDb.prop.getInt("jdbc.max_poll", 100));
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
		arp.setShowSql(false);
		arp.setContainerFactory(new CaseInsensitiveContainerFactory(true));
		_MappingKit.mapping(arp);
		_MappingViewKit.mapping(arp);
		if(prop.getBoolean("cache.enable",false)) {
			RedisPlugin redis = new RedisPlugin(prop.get("cache.redis.netty.name"), prop.get("cache.redis.host"),prop.getInt("cache.redis.port"));
			redis.start();
		}
		
		druidPlugin.start();
		arp.start();
        System.out.println("数据库已启动");
		if (prop.getBoolean("cache.enable", false)) {
			new RedisPlugin(prop.get("cache.redis.share.name"), prop.get("cache.redis.host"), prop.getInt("cache.redis.port")).start();
		}

		Prop cfg = PropKit.use("cfg.properties");
		Routes routes = new Routes() {
			@Override
			public void config() {

			}
		};
		String[] pkgs = cfg.get("system.annotation.scan").split(",");
		IocPlugin ioc = new IocPlugin(routes, pkgs);
		ioc.start();


        AlarmMsgService service = new AlarmMsgService();
        String code = PropKit.use("deviceError.txt").get("dev.offline");
		
        List<ViewMachineBlockType> machineBlockType = machineService.findMachineByMain();
        for(ViewMachineBlockType type : machineBlockType) {
        	String systemId = type.getStr("system_id");
        	service.save(systemId, "00-80-00", code);
        	
        }
        System.out.println("设备状态初始化成功");
        
        
        
		new Thread(new serverStart(new AgriProServer(8888))).start();
		new Thread(new WebServerStart(new WebsocketChatServer(8686))).start();

	}
	
	class serverStart implements Runnable{
		private AgriProServer server;
		public serverStart(AgriProServer server){
			this.server = server;
		}
		public void run() {
			try {
				this.server.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	class WebServerStart implements Runnable{
		private WebsocketChatServer websocketServer;
		public WebServerStart(WebsocketChatServer websocketServer){
			this.websocketServer = websocketServer;
		}
		public void run() {
			try {
				this.websocketServer.run();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}