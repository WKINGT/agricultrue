package test;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.redis.RedisPlugin;

import net.xgs.init.InitDb;
import net.xgs.model._MappingKit;
import net.xgs.model._MappingViewKit;

public class StartServer {


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
        
		new Thread(new serverStart(new AgriProServer(8888))).start();
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
}