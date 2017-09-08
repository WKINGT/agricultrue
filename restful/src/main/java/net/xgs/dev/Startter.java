package net.xgs.dev;

import javax.servlet.ServletException;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

public interface Startter {
	Prop prop = PropKit.use("app.properties");

	void start() throws ServletException;
}
