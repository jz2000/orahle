package org.jz.orahle;



import java.sql.SQLException;
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

    public List<String> getViewBody(
            String owner,
            String viewName) throws SQLException ;

    public List<String> getProcedureBody(
            String owner,
            String procedureName) throws SQLException ;

    public List<String> getFunctionBody(
            String owner,
            String functionName) throws SQLException ;

    public List<String> getTriggerBody(
            String owner,
            String triggerName) throws SQLException ;

    public List<String> getTypeDefinition(
            String owner,
            String typeName) throws SQLException ;

    public List<String> getTypeBody(
            String owner,
            String typeName) throws SQLException ;

    public List<String> getPackageDefinition(
            String owner,
            String packageName) throws SQLException ;

    public List<String> getPackageBody(
            String owner,
            String packageName) throws SQLException ;

    public DbTableDefinition getTableDefinition(
            String owner, 
            String tableName) throws SQLException;

    public List<DbTableColumn> getTableColumns(
            String owner, 
            String tableName) throws SQLException;

    public List<DbTableIndex> getTableIndexes(
            String owner, 
            String tableName) throws SQLException;

    public List<DbTableConstraint> getTableConstraints(
            String owner, 
            String tableName) throws SQLException;

    public List<String> getTableSql(
            String owner, 
            String tableName) throws SQLException;
}
