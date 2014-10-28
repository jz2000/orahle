package org.jz.orahle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jz
 */
public class SessionResolver
{
    private String dbAlias;
    private String dbOwner;
    private String dbObjectType;
    private String dbObjectNameFilter;
    private Map<String, DbSession> sessions = new HashMap<>();

    public String getDbAlias()
    {
        return dbAlias;
    }

    public void setDbAlias(String dbAlias)
    {
        this.dbAlias = dbAlias;
    }

    public Map<String, DbSession> getSessions()
    {
        return sessions;
    }

    public void setSessions(Map<String, DbSession> sessions)
    {
        this.sessions = sessions;
    }
    
    public DbSession getDbSession() 
    {
        DbSession dbSession = sessions.get(dbAlias);
        return dbSession;
    }

    public String getDbOwner() {
        return dbOwner;
    }

    public void setDbOwner(String dbOwner) {
        this.dbOwner = dbOwner;
    }

    public String getDbObjectType() {
        return dbObjectType;
    }

    public void setDbObjectType(String dbObjectType) {
        this.dbObjectType = dbObjectType;
    }

    public String getDbObjectNameFilter() {
        return dbObjectNameFilter;
    }

    public void setDbObjectNameFilter(String dbObjectNameFilter) {
        this.dbObjectNameFilter = dbObjectNameFilter;
    }
    
    public List<DbObject> getTables() throws Exception {
        return getDbSession().searchObjects(this.getDbObjectType(), this.getDbOwner(), this.getDbObjectNameFilter());
    }

}
