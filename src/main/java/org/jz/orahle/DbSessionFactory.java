package org.jz.orahle;



/**
 *
 * @author sergey.zheznyakovskiy
 */
public class DbSessionFactory
{

    public static DbSession produceDbSession(DbConnection connection, String userName, String password)
    {
        DbSession result = System.getProperty("dry", "true").equals("true") ? 
                new DebuggingDbSession(connection.getAlias(), connection.getUrl(), userName, password) :
                new OracleDbSession(connection.getAlias(), connection.getUrl(), userName, password);
        return result;
    }
    
}
