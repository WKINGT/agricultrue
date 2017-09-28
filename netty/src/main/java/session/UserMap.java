package session;

import java.util.concurrent.ConcurrentHashMap;
/**
 * 用户id和TerminalMmap映射
 * argument1:用户ID
 * argument2: {@link Terminal}
 *
 * @author TianW
 *
 */
public class UserMap extends ConcurrentHashMap<String,Terminal> {
    /**
     *
     * @param userId
     * @return
     */
    public Terminal getTerminal(String userId){
        return  this.get(userId);
    }
}
