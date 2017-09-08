package net.xgs.session;

public interface RestfulSessionManager {
    /**

     * 获取Session

     * @param sessionId sessionId

     * @return {Session}

     */
    RestfulSession get(String sessionId);

    /**

     * 保存 Session

     * @param session Session

     */
    void save(RestfulSession session);

    /**

     * 更新 Session

     * @param session Session

     */
    void update(RestfulSession session);

    void remove(String sessionId);
}