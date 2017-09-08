package net.xgs.commons.session;

public interface SessionManager {
    /**

     * 获取Session

     * @param sessionId sessionId

     * @return {Session}

     */
    Session get(String sessionId);
    
    /**

     * 保存 Session

     * @param session Session

     */
    void save(Session session);
    
    /**

     * 更新 Session

     * @param session Session

     */
    void update(Session session);
}