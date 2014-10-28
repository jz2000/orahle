package org.jz.orahle;

import java.util.List;

/**
 *
 * @author sergey.zheznyakovskiy
 */
public class QueryResult 
{
    
    public enum Type 
    {
        ROWSET,
        COUNT,
        ERROR,
    }
    
    private Type type;
    private String textResult;
    private List<String> names;
    private List<String> types;
    private List<List<Object>> rows;

    public void setType(Type type)
    {
        this.type = type;
    }

    public void setTypes(List<String> types) 
    {
        this.types = types;
    }

    public void setNames(List<String> names)
    {
        this.names = names;
    }

    public void setRows(List<List<Object>> rows) 
    {
        this.rows = rows;
    }

    public void setTextResult(String textResult) 
    {
        this.textResult = textResult;
    }

    public Type getType() 
    {
        return type;
    }

    public List<String> getTypes() 
    {
        return types;
    }

    public List<String> getNames()
    {
        return names;
    }

    public List<List<Object>> getRows() 
    {
        return rows;
    }

    public String getTextResult()
    {
        return textResult;
    }

}
