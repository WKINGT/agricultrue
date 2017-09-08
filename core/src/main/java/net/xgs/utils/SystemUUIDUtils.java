package net.xgs.utils;

import java.util.UUID;

/**
 * Created by duai on 2017-08-08.
 */
public class SystemUUIDUtils {
    /**
     * 自动生成32位的UUid，对应数据库的主键id进行插入用。
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
