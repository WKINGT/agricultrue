package net.xgs.commons.logs;
import com.jfinal.log.ILogFactory;
import com.jfinal.log.Log;

public class SLF4JLogFactory implements ILogFactory {

    @Override
    public Log getLog(Class<?> clazz) {
        return new SLF4JLog(clazz);
    }

    @Override
    public Log getLog(String name) {
        return new SLF4JLog(name);
    }
}