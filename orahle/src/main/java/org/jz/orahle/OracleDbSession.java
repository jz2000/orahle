package org.jz.orahle;



import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.dbcp.BasicDataSource;
/**
 *
 * @author sergey.zheznyakovskiy - 2013-11-07
 */
public class OracleDbSession implements DbSession
{
    private static final String VALIDATION_SQL = "select 1 from dual";
    private final BasicDataSource dataSource = new BasicDataSource();
    private final String alias;

    public OracleDbSession(
            String alias, 
            String url, 
            String userName, 
            String password)
    {
        this.alias = alias;
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
    }

    @Override
    public void validate() throws Exception
    {
        try (Connection connection = dataSource.getConnection()) 
        {
            try (CallableStatement stmt = connection.prepareCall(VALIDATION_SQL)) 
            {
                try (ResultSet rs = stmt.executeQuery())
                {
                    rs.next();
                }
            }
        }
    }

    @Override
    public List<DbObject> getObjectsForType(
            String typeName, 
            String owner) throws SQLException
    {
        String QUERY = "select * from ALL_OBJECTS where (OBJECT_TYPE = ?) and (OWNER = ?) order by OBJECT_NAME";
        List<DbObject> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) 
        {
            try (CallableStatement stmt = connection.prepareCall(QUERY)) 
            {
                stmt.setString(1, typeName.toUpperCase());
                stmt.setString(2, owner.toUpperCase());
                try (ResultSet rs = stmt.executeQuery())
                {
                    while (rs.next()) 
                    {
                        DbObject object = new DbObject();
                        object.setType(typeName);
                        object.setOwner(rs.getString("owner"));
                        object.setName(rs.getString("object_name"));
                        object.setComment("Just read");
                        result.add(object);
                    }
                }
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
        if (keyword == null || keyword.isEmpty()) 
        {
            return getObjectsForType(typeName, owner);
        }
        else 
        {
            String QUERY = "select * from ALL_OBJECTS where (OBJECT_TYPE = ?) and (OWNER = ?) and (OBJECT_NAME like ?) order by OBJECT_NAME";
            List<DbObject> result = new ArrayList<>();
            try (Connection connection = dataSource.getConnection()) 
            {
                try (CallableStatement stmt = connection.prepareCall(QUERY)) 
                {
                    stmt.setString(1, typeName.toUpperCase());
                    stmt.setString(2, owner.toUpperCase());
                    stmt.setString(3, keyword.toUpperCase());
                    try (ResultSet rs = stmt.executeQuery())
                    {
                        while (rs.next()) 
                        {
                            DbObject object = new DbObject();
                            object.setType(typeName);
                            object.setOwner(rs.getString("owner"));
                            object.setName(rs.getString("object_name"));
                            object.setComment("Just read");
                            result.add(object);
                        }
                    }
                }
            }
            return result;
        }
    }

    @Override
    public List<DbObjectType> getObjectTypes()
    {
        return new DatabaseServiceDebugImpl().searchObjectTypes(null);
    }

    @Override
    public List<DbObject> getTables() throws Exception {
        return searchObjects("TABLE", "", null);
    }

    @Override
    public QueryResult executeQuery(String queryText) throws Exception {
            try (Connection connection = dataSource.getConnection()) 
            {
                try (CallableStatement stmt = connection.prepareCall(queryText)) 
                {
                    boolean resultType = stmt.execute();
                    if (resultType) 
                    {
                        try (ResultSet rs = stmt.getResultSet())
                        {
                            QueryResult result = new QueryResult();
                            result.setType(QueryResult.Type.ROWSET);
                            List<String> names = new ArrayList<>();
                            List<String> types = new ArrayList<>();
                            List<List<Object>> data = new ArrayList<>();
                            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) 
                            {
                                names.add(rs.getMetaData().getColumnName(i + 1));
                                types.add(rs.getMetaData().getColumnTypeName(i + 1));
                            }
                            result.setNames(names);
                            result.setTypes(types);
                            while (rs.next()) 
                            {
                                List<Object> row = new ArrayList<>();
                                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) 
                                {
                                    row.add(rs.getObject(i + 1));
                                }
                                data.add(row);
                            }
                            result.setRows(data);
                            return result;
                        }
                    }
                    else
                    {
                        int updateCount = stmt.getUpdateCount();
                        QueryResult result = new QueryResult();
                        result.setType(QueryResult.Type.COUNT);
                        result.setTextResult(updateCount + " row(s) updated");
                        return result;
                    }
                } 
                catch(SQLException ex) 
                {
                    QueryResult result = new QueryResult();
                    result.setType(QueryResult.Type.ERROR);
                    result.setTextResult(ex.getErrorCode() + " " + ex.getSQLState() + " " + ex.getMessage());
                    return result;
                }
            }
    }

    @Override
    public List<String> getPackageBody(
            String owner,
            String packageName
    ) throws SQLException {
        List<String> result = new ArrayList<>();
        String sql = "select * from ALL_SOURCE where (TYPE = 'PACKAGE BODY') and (OWNER = ?) and (NAME = ?) order by LINE";
        try (Connection connection = dataSource.getConnection()) 
        {
            try (CallableStatement stmt = connection.prepareCall(sql)) 
            {
                stmt.setString(1, owner.toUpperCase());
                stmt.setString(2, packageName.toUpperCase());
                try (ResultSet rs = stmt.executeQuery())
                {
                    while (rs.next()) 
                    {
                        String sourceLine = rs.getString("TEXT");
                        result.add(sourceLine);
                    }
                    return result;
                }
            } 
        }
    }

    @Override
    public List<String> getPackageDefinition(
            String owner,
            String packageName
    ) throws SQLException {
        List<String> result = new ArrayList<>();
        String sql = "select * from ALL_SOURCE where (TYPE = 'PACKAGE') and (OWNER = ?) and (NAME = ?) order by LINE";
        try (Connection connection = dataSource.getConnection()) 
        {
            try (CallableStatement stmt = connection.prepareCall(sql)) 
            {
                stmt.setString(1, owner.toUpperCase());
                stmt.setString(2, packageName.toUpperCase());
                try (ResultSet rs = stmt.executeQuery())
                {
                    while (rs.next()) 
                    {
                        String sourceLine = rs.getString("TEXT");
                        result.add(sourceLine);
                    }
                    return result;
                }
            } 
        }
    }

}
