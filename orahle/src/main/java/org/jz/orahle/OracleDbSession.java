package org.jz.orahle;



import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
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
                    if (!rs.next()) {
                        throw new Exception("Cannot execute the validation query");
                    }
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
                stmt.setString(1, typeName.toUpperCase(Locale.getDefault()));
                stmt.setString(2, owner.toUpperCase(Locale.getDefault()));
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
                    stmt.setString(1, typeName.toUpperCase(Locale.getDefault()));
                    stmt.setString(2, owner.toUpperCase(Locale.getDefault()));
                    stmt.setString(3, keyword.toUpperCase(Locale.getDefault()));
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
    public List<String> getViewBody(
            String owner,
            String viewName
    ) throws SQLException {
        List<String> result = new ArrayList<>();
        String sql = "select * from ALL_VIEWS where (OWNER = ?) and (VIEW_NAME = ?)";
        try (Connection connection = dataSource.getConnection()) 
        {
            try (CallableStatement stmt = connection.prepareCall(sql)) 
            {
                stmt.setString(1, owner.toUpperCase(Locale.getDefault()));
                stmt.setString(2, viewName.toUpperCase(Locale.getDefault()));
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
    public List<String> getProcedureBody(
            String owner,
            String procedureName
    ) throws SQLException {
        List<String> result = new ArrayList<>();
        String sql = "select * from ALL_SOURCE where (TYPE = 'PROCEDURE') and (OWNER = ?) and (NAME = ?) order by LINE";
        try (Connection connection = dataSource.getConnection()) 
        {
            try (CallableStatement stmt = connection.prepareCall(sql)) 
            {
                stmt.setString(1, owner.toUpperCase(Locale.getDefault()));
                stmt.setString(2, procedureName.toUpperCase(Locale.getDefault()));
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
    public List<String> getFunctionBody(
            String owner,
            String functionName
    ) throws SQLException {
        List<String> result = new ArrayList<>();
        String sql = "select * from ALL_SOURCE where (TYPE = 'FUNCTION') and (OWNER = ?) and (NAME = ?) order by LINE";
        try (Connection connection = dataSource.getConnection()) 
        {
            try (CallableStatement stmt = connection.prepareCall(sql)) 
            {
                stmt.setString(1, owner.toUpperCase(Locale.getDefault()));
                stmt.setString(2, functionName.toUpperCase(Locale.getDefault()));
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
    public List<String> getTriggerBody(
            String owner,
            String triggerName
    ) throws SQLException {
        List<String> result = new ArrayList<>();
        String sql = "select * from ALL_SOURCE where (TYPE = 'TRIGGER') and (OWNER = ?) and (NAME = ?) order by LINE";
        try (Connection connection = dataSource.getConnection()) 
        {
            try (CallableStatement stmt = connection.prepareCall(sql)) 
            {
                stmt.setString(1, owner.toUpperCase(Locale.getDefault()));
                stmt.setString(2, triggerName.toUpperCase(Locale.getDefault()));
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
    public List<String> getTypeDefinition(
            String owner,
            String typeName
    ) throws SQLException {
        List<String> result = new ArrayList<>();
        String sql = "select * from ALL_SOURCE where (TYPE = 'TYPE') and (OWNER = ?) and (NAME = ?) order by LINE";
        try (Connection connection = dataSource.getConnection()) 
        {
            try (CallableStatement stmt = connection.prepareCall(sql)) 
            {
                stmt.setString(1, owner.toUpperCase(Locale.getDefault()));
                stmt.setString(2, typeName.toUpperCase(Locale.getDefault()));
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
    public List<String> getTypeBody(
            String owner,
            String packageName
    ) throws SQLException {
        List<String> result = new ArrayList<>();
        String sql = "select * from ALL_SOURCE where (TYPE = 'TYPE BODY') and (OWNER = ?) and (NAME = ?) order by LINE";
        try (Connection connection = dataSource.getConnection()) 
        {
            try (CallableStatement stmt = connection.prepareCall(sql)) 
            {
                stmt.setString(1, owner.toUpperCase(Locale.getDefault()));
                stmt.setString(2, packageName.toUpperCase(Locale.getDefault()));
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
                stmt.setString(1, owner.toUpperCase(Locale.getDefault()));
                stmt.setString(2, packageName.toUpperCase(Locale.getDefault()));
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
                stmt.setString(1, owner.toUpperCase(Locale.getDefault()));
                stmt.setString(2, packageName.toUpperCase(Locale.getDefault()));
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
    public DbTableDefinition getTableDefinition(
            String owner, 
            String tableName) throws SQLException 
    {
        String sql = "select * "
                + "from ALL_TABLES at "
                + "inner join ALL_TAB_COMMENTS atc on ((atc.OWNER = at.OWNER) and (atc.TABLE_NAME = at.TABLE_NAME)) "
                + "where "
                + "(at.OWNER = ?) "
                + "and "
                + "(at.TABLE_NAME = ?)";
        try (Connection connection = dataSource.getConnection()) 
        {
            try (CallableStatement stmt = connection.prepareCall(sql)) 
            {
                stmt.setString(1, owner.toUpperCase(Locale.getDefault()));
                stmt.setString(2, tableName.toUpperCase(Locale.getDefault()));
                try (ResultSet rs = stmt.executeQuery())
                {
                    if (rs.next()) 
                    {
                        DbTableDefinition result = new DbTableDefinition();
                        result.setRowCount(rs.getInt("NUM_ROWS"));
                        return result;
                    } else {
                        throw new SQLException("Cannot read the table definition");
                    }
                    
                }
            } 
        }
    }

    @Override
    public List<DbTableColumn> getTableColumns(
            String owner, 
            String tableName) throws SQLException 
    {
        List<DbTableColumn> result = new ArrayList<>();
        String sql = "select * "
                + "from ALL_TAB_COLS atc "
                + "inner join ALL_COL_COMMENTS acc on ((atc.OWNER = acc.OWNER) and (atc.TABLE_NAME = acc.TABLE_NAME) and (atc.COLUMN_NAME = acc.COLUMN_NAME))"
                + " where "
                + "(atc.OWNER = ?) "
                + "and "
                + "(atc.TABLE_NAME = ?) "
                + "order by "
                + "atc.COLUMN_ID";
        try (Connection connection = dataSource.getConnection()) 
        {
            try (CallableStatement stmt = connection.prepareCall(sql)) 
            {
                stmt.setString(1, owner.toUpperCase(Locale.getDefault()));
                stmt.setString(2, tableName.toUpperCase(Locale.getDefault()));
                try (ResultSet rs = stmt.executeQuery())
                {
                    while (rs.next()) 
                    {
                        DbTableColumn column = new DbTableColumn();
                        column.setId(rs.getLong("COLUMN_ID"));
                        column.setName(rs.getString("COLUMN_NAME"));
                        column.setComment(rs.getString("COMMENTS"));
                        column.setDataType(rs.getString("DATA_TYPE"));
                        column.setDataLength(rs.getInt("DATA_LENGTH"));
                        column.setDataPrecision(rs.getInt("DATA_PRECISION"));
                        column.setDataScale(rs.getInt("DATA_SCALE"));
                        column.setNullable(rs.getString("NULLABLE"));
                        //column.setDataDefault(rs.getObject("DATA_DEFAULT") + "");
                        result.add(column);
                    }
                    return result;
                }
            } 
        }
    }

    @Override
    public List<DbTableIndex> getTableIndexes(
            String owner, 
            String tableName) throws SQLException 
    {
        List<DbTableIndex> result = new ArrayList<>();
        String sql = "select * from ALL_INDEXES where (OWNER = ?) and (TABLE_NAME = ?)";
        try (Connection connection = dataSource.getConnection()) 
        {
            try (CallableStatement stmt = connection.prepareCall(sql)) 
            {
                stmt.setString(1, owner.toUpperCase(Locale.getDefault()));
                stmt.setString(2, tableName.toUpperCase(Locale.getDefault()));
                try (ResultSet rs = stmt.executeQuery())
                {
                    while (rs.next()) 
                    {
                        DbTableIndex index = new DbTableIndex();
                        index.setName(rs.getString("INDEX_NAME"));
                        index.setIndexType(rs.getString("INDEX_TYPE"));
                        index.setUniqueness(rs.getString("UNIQUENESS"));
                        result.add(index);
                    }
                    return result;
                }
            } 
        }
    }

    @Override
    public List<DbTableConstraint> getTableConstraints(
            String owner, 
            String tableName) throws SQLException 
    {
        List<DbTableConstraint> result = new ArrayList<>();
        String sql = "select * from ALL_CONSTRAINTS where (OWNER = ?) and (TABLE_NAME = ?)";
        try (Connection connection = dataSource.getConnection()) 
        {
            try (CallableStatement stmt = connection.prepareCall(sql)) 
            {
                stmt.setString(1, owner.toUpperCase(Locale.getDefault()));
                stmt.setString(2, tableName.toUpperCase(Locale.getDefault()));
                try (ResultSet rs = stmt.executeQuery())
                {
                    while (rs.next()) 
                    {
                        DbTableConstraint constraint = new DbTableConstraint();
                        constraint.setName(rs.getString("CONSTRAINT_NAME"));
                        constraint.setConstraintType(rs.getString("CONSTRAINT_TYPE"));
                        constraint.setSearchCondition(rs.getString("SEARCH_CONDITION"));
                        constraint.setRefOwner(rs.getString("R_OWNER"));
                        constraint.setRefConstrantName(rs.getString("R_CONSTRAINT_NAME"));
                        constraint.setDeleteRule(rs.getString("DELETE_RULE"));
                        constraint.setStatus(rs.getString("STATUS"));
                        constraint.setGenerated(rs.getString("GENERATED"));
                        constraint.setIdxOwner(rs.getString("INDEX_OWNER"));
                        constraint.setIdxName(rs.getString("INDEX_NAME"));
                        result.add(constraint);
                    }
                    return result;
                }
            } 
        }
    }

    @Override
    public List<String> getTableSql(
            String owner, 
            String tableName) throws SQLException 
    {
        return Collections.emptyList();
    }

    public String getAlias() {
        return alias;
    }
}
