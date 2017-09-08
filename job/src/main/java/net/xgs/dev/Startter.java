package net.xgs.dev;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

import javax.servlet.ServletException;

public interface Startter {
	Prop prop = PropKit.use("app.properties");

	void start() throws ServletException;
}
