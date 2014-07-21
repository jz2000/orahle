package org.jz.orahle;

/**
 *
 * @author sergey.zheznyakovskiy
 */
public class DbLogin {
    
    private String dbAlias;
    private ConnectionServiceImpl connectionList;

    public String getDbAlias() {
        return dbAlias;
    }

    public void setDbAlias(String dbAlias) {
        this.dbAlias = dbAlias;
    }

    public ConnectionServiceImpl getConnectionList() {
        return connectionList;
    }

    public void setConnectionList(ConnectionServiceImpl connectionList) {
        this.connectionList = connectionList;
    }
    
    public String getDbUserName() {
        if (connectionList == null) {
            return "";
        } else {
            return connectionList.getConnections(dbAlias).getUser();
        }

    }
    
}
