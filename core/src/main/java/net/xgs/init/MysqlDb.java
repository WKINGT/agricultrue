package net.xgs.init;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.plugin.druid.DruidPlugin;


public class MysqlDb implements InitDb{

	@Override
	public DruidPlugin init() {
		DruidPlugin druidPlugin = new DruidPlugin(prop.get("jdbc.mysql.uri"),
				prop.get("jdbc.mysql.user"), prop.get("jdbc.mysql.password"));
		druidPlugin.setValidationQuery(prop.get("jdbc.mysql.validation_query"));
		druidPlugin.addFilter(new StatFilter());
		WallFilter wall = new WallFilter();
		wall.setDbType("mysql");
		druidPlugin.addFilter(wall);
		druidPlugin.addFilter(new Slf4jLogFilter());
		return druidPlugin;
	}
	
}
