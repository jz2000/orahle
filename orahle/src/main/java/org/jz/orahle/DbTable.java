package org.jz.orahle;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author sergey.zheznyakovskiy
 */
public class DbTable extends SessionResolver 
{
    private String tableName;

    public String getTableName() 
    {
        return tableName;
    }

    public void setTableName(String tableName) 
    {
        this.tableName = tableName;
    }
    
    public DbTableDefinition getDefinition()  throws SQLException
    {
        DbTableDefinition definition = this.getDbSession().getTableDefinition(this.getDbOwner(), this.tableName);
        return definition;
    }

    public List<DbTableColumn> getColumns()  throws SQLException
    {
        List<DbTableColumn> tableColumns = this.getDbSession().getTableColumns(this.getDbOwner(), this.tableName);
        return tableColumns;
    }

    public List<DbTableIndex> getIndexes()  throws SQLException
    {
        List<DbTableIndex> tableIndexes = this.getDbSession().getTableIndexes(this.getDbOwner(), this.tableName);
        return tableIndexes;
    }

    public List<DbTableConstraint> getConstraints() throws SQLException
    {
        List<DbTableConstraint> tableConstraints = this.getDbSession().getTableConstraints(
                this.getDbOwner(), 
                this.tableName);
        return tableConstraints;
    }

    public String getTableSql() 
    {
        try 
        {
            List<String> source = this.getDbSession().getTableSql(this.getDbOwner(), this.tableName);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<pre>");
            for (String line : source) 
            {
                stringBuilder.append(line);
            }
            stringBuilder.append("</pre>");
            return stringBuilder.toString();
        } 
        catch (SQLException ex) 
        {
            return "<font color='red'>Error reading tableSql</font>";
        }
    }

}
