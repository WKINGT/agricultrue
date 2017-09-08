package net.xgs.init;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.druid.DruidPlugin;


public interface InitDb {
	final Prop prop = PropKit.use("jdbc.properties");
	DruidPlugin init();
}
