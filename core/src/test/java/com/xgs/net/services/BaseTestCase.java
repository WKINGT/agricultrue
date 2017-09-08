package com.xgs.net.services;

import org.junit.After;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.druid.DruidPlugin;

import net.xgs.init.InitDb;
import net.xgs.model._MappingKit;

public class BaseTestCase{

	Logger logger = LoggerFactory.getLogger(getClass());
	protected static DruidPlugin druidPlugin;
    protected static ActiveRecordPlugin arp;
    
    private static long startTime;
    private static long endTime;
    public BaseTestCase(){
    	this.before();
    }
    
    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

    	Prop prop = PropKit.use("cfg.properties");
    	
    	InitDb db = (InitDb) Class.forName(prop.get("system.db")).newInstance();
		druidPlugin = db.init();
		druidPlugin.setMaxPoolPreparedStatementPerConnectionSize(InitDb.prop.getInt("jdbc.min_poll", 10));
		druidPlugin.setMaxActive(InitDb.prop.getInt("jdbc.max_poll", 100));
		druidPlugin.start();
		arp = new ActiveRecordPlugin(druidPlugin);
		arp.setShowSql(InitDb.prop.getBoolean("jdbc.show_sql", false));
		arp.setContainerFactory(new CaseInsensitiveContainerFactory(true));
		_MappingKit.mapping(arp);
		arp.start();
        startTime = System.currentTimeMillis();
    	System.out.println(startTime);
    }
 
    public void before(){
    	
    }
    
    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    	endTime = System.currentTimeMillis();
    	System.out.println(endTime);
    	System.out.println(endTime - startTime);
        arp.stop();
        druidPlugin.stop();
    }
 
}