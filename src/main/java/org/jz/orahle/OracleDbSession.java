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
        String QUERY = "select * from ALL_TABLES where (owner = ?) order by TABLE_NAME";
        List<DbObject> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) 
        {
            try (CallableStatement stmt = connection.prepareCall(QUERY)) 
            {
                stmt.setString(1, owner.toUpperCase());
                try (ResultSet rs = stmt.executeQuery())
                {
                    while (rs.next()) 
                    {
                        DbObject object = new DbObject();
                        object.setType("TABLE");
                        object.setOwner(rs.getString("owner"));
                        object.setName(rs.getString("table_name"));
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
            String QUERY = "select * from ALL_TABLES where (owner = ?) and (table_name like ?) order by TABLE_NAME";
            List<DbObject> result = new ArrayList<>();
            try (Connection connection = dataSource.getConnection()) 
            {
                try (CallableStatement stmt = connection.prepareCall(QUERY)) 
                {
                    stmt.setString(1, owner.toUpperCase());
                    stmt.setString(2, keyword.toUpperCase());
                    try (ResultSet rs = stmt.executeQuery())
                    {
                        while (rs.next()) 
                        {
                            DbObject object = new DbObject();
                            object.setType("TABLE");
                            object.setOwner(rs.getString("owner"));
                            object.setName(rs.getString("table_name"));
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
                            result.setType("ROWSET");
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
                        result.setType("UPDATE");
                        result.setTextResult(updateCount + " row(s) updated");
                        return result;
                    }
                }
            }
    }

}
