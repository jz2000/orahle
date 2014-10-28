package org.jz.orahle;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author sergey.zheznyakovskiy - 2013-11-07
 */
public interface DbSession
{

    void validate() throws Exception;
    
    List<DbObject> getObjectsForType(
            String typeName,
            String owner) throws Exception;

    List<DbObject> searchObjects(
            String typeName, 
            String owner, 
            String keyword) throws Exception;

    List<DbObjectType> getObjectTypes();
    
    List<DbObject> getTables() throws Exception;
    
    QueryResult executeQuery(
            String queryText) throws Exception;

    List<String> getViewBody(
            String owner,
            String viewName) throws SQLException;

    List<String> getProcedureBody(
            String owner,
            String procedureName) throws SQLException;

    List<String> getFunctionBody(
            String owner,
            String functionName) throws SQLException;

    List<String> getTriggerBody(
            String owner,
            String triggerName) throws SQLException;

    List<String> getTypeDefinition(
            String owner,
            String typeName) throws SQLException;

    List<String> getTypeBody(
            String owner,
            String typeName) throws SQLException;

    List<String> getPackageDefinition(
            String owner,
            String packageName) throws SQLException;

    List<String> getPackageBody(
            String owner,
            String packageName) throws SQLException;

    DbTableDefinition getTableDefinition(
            String owner, 
            String tableName) throws SQLException;

    List<DbTableColumn> getTableColumns(
            String owner, 
            String tableName) throws SQLException;

    List<DbTableIndex> getTableIndexes(
            String owner, 
            String tableName) throws SQLException;

    List<DbTableConstraint> getTableConstraints(
            String owner, 
            String tableName) throws SQLException;

    List<String> getTableSql(
            String owner, 
            String tableName) throws SQLException;
}
