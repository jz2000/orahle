package org.jz.orahle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sergey.zheznyakovskiy - 2013-11-07
 */
public class DebuggingDbSession implements DbSession
{
    private final String alias;
    private final String userName;
    private final String password;
    
    private final List<DbObject> objects = new ArrayList<>();
    private final String url;
    
    public DebuggingDbSession(
            String alias, 
            String url, 
            String userName, 
            String password)
    {
        this.alias = alias;
        this.url = url;
        this.userName = userName;
        this.password = password;
    }

    @Override
    public void validate() throws Exception
    {
        if (!userName.equals("jzvm")) 
        {
            throw new Exception("Invalid username or password");
        }
        if (password.isEmpty()) 
        {
            throw new Exception("password is empty");
        }
    }

    {
        putDbObject(makeDbObject("TABLE", "EMPLOYEE", "Employee"));
        putDbObject(makeDbObject("TABLE", "DEPT", "Department"));
        putDbObject(makeDbObject("TABLE", "MANAGER", "Manager"));
        putDbObject(makeDbObject("TABLE", "SALARY", "Salary"));
        putDbObject(makeDbObject("TABLE", "CONTRACT", "Contract"));
        putDbObject(makeDbObject("VIEW", "EMPLOYEE_TO_MANAGER_LIST", "Employee to Manager"));
        putDbObject(makeDbObject("VIEW", "EMPLOYEE_TO_DEPT_LIST", "Employee to Department"));
        putDbObject(makeDbObject("VIEW", "MANAGER_TO_DEPT", "Manager to department"));
        putDbObject(makeDbObject("VIEW", "SALARY_TO_DEPT_LIST", "Salary to department"));
        putDbObject(makeDbObject("SEQUENCE", "CONTRACT_ID", "Contract id generator"));
        putDbObject(makeDbObject("PACKAGE", "EMPLOYEE_OPERATIONS", "Employee operations"));
        putDbObject(makeDbObject("PACKAGE", "DEPT_OPERATIONS", "Department operations"));
        putDbObject(makeDbObject("PACKAGE_BODY", "EMPLOYEE_OPERATIONS", "Employee operations"));
        putDbObject(makeDbObject("PACKAGE_BODY", "DEPT_OPERATIONS", "Department operations"));
        putDbObject(makeDbObject("PROCEDURE", "CONTRACT_REVERT", "Contract revertation"));
     }
    
    public void putDbObject(
            DbObject object)
    {
        objects.add(object);
    }
    
    public static DbObject makeDbObject(
            String type,
            String name,
            String comment)
    {
        DbObject result = new DbObject();
        result.setType(type);
        result.setName(name);
        result.setComment(comment);
        return result;
    }
    
    @Override
    public List<DbObject> getObjectsForType(
            String typeName,
            String owner) throws Exception
    {
        List<DbObject> result = new ArrayList<>();
        for (DbObject object : objects)
        {
            if (typeName.equals("%"))
            {
                result.add(object);
            }
            else if (object.getType().equals(typeName))
            {
                result.add(object);
            }
        }
        return result;
    }

    @Override
    public List<DbObject> searchObjects(
            String typeName,
            String owner,
            String keyword) throws Exception
    {
        List<DbObject> result = new ArrayList<>();
        for (DbObject object : objects)
        {
            if (keyword.isEmpty() || object.getName().contains(keyword) || object.getComment().contains(keyword))
            {
                if (typeName.equals("%"))
                {
                    result.add(object);
                }
                else if (object.getType().equals(typeName))
                {
                    result.add(object);
                }
            }
        }
        return result;
    }

    @Override
    public List<DbObjectType> getObjectTypes()
    {
        return new DatabaseServiceDebugImpl().searchObjectTypes(null);
    }

    @Override
    public List<DbObject> getTables() 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public QueryResult executeQuery(String queryText) throws Exception 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<String> getViewBody(String owner, String viewName) throws SQLException 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public List<String> getProcedureBody(String owner, String packageName) throws SQLException 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<String> getFunctionBody(String owner, String packageName) throws SQLException 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<String> getTriggerBody(String owner, String packageName) throws SQLException 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<String> getTypeDefinition(String owner, String packageName) throws SQLException 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<String> getTypeBody(String owner, String packageName) throws SQLException 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public List<String> getPackageDefinition(
            String owner,
            String packageName) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public List<String> getPackageBody(
            String owner,
            String packageName) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DbTableDefinition getTableDefinition(String owner, String tableName) throws SQLException 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<DbTableColumn> getTableColumns(String owner, String tableName) throws SQLException 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<DbTableIndex> getTableIndexes(String owner, String tableName) throws SQLException 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<DbTableConstraint> getTableConstraints(String owner, String tableName) throws SQLException 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<String> getTableSql(String owner, String tableName) throws SQLException 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getAlias() 
    {
        return alias;
    }

    public String getUrl() 
    {
        return url;
    }
}
