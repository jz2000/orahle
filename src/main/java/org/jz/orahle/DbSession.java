package org.jz.orahle;



import java.util.List;
import java.util.Map;

/**
 *
 * @author sergey.zheznyakovskiy - 2013-11-07
 */
public interface DbSession
{

    public void validate() throws Exception;
    
    public List<DbObject> getObjectsForType(
            String typeName,
            String owner) throws Exception;

    public List<DbObject> searchObjects(
            String typeName, 
            String owner, 
            String keyword) throws Exception;

    public List<DbObjectType> getObjectTypes();
    
    public List<DbObject> getTables() throws Exception;
    
    public QueryResult executeQuery(
            String queryText) throws Exception;
}
