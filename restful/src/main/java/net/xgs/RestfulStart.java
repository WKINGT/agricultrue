package net.xgs;

import net.xgs.dev.Startter;

public class RestfulStart {
	
	public static void main(final String[] args) throws Exception {
		Startter startter = (Startter)Class.forName(Startter.prop.get("system.start.clazz")).newInstance();
		startter.start();
	}

}
