package org.jz.orahle;

import java.util.List;

/**
 *
 * @author sergey.zheznyakovskiy
 */
public class QueryRunner extends SessionResolver
{
    private String queryText;

    public String getQueryText() 
    {
        return queryText;
    }

    public void setQueryText(
            String queryText) 
    {
        this.queryText = queryText;
        System.out.println("QUERY: " + queryText);
    }
    
    public String getQueryResult() throws Exception
    {
        if (this.queryText == null || "".equals(this.queryText)) 
        {
            return "No Query";
        }
        QueryResult result = this.getDbSession().executeQuery(this.queryText);
        if (result.getType() == QueryResult.Type.ROWSET) 
        {
        StringBuilder sb = new StringBuilder();
        sb.append("<table border>");
        sb.append("<tr>");
        for (String colName : result.getNames()) 
        {
            sb.append("<th>");
            sb.append(colName);
            sb.append("</th>");
        }
        sb.append("</tr>");
        for (List<Object> row : result.getRows()) 
        {
            sb.append("<tr>");
            for (Object colValue : row) 
            {
                sb.append("<td>");
                sb.append(colValue);
                sb.append("</td>");
            }
            sb.append("</tr>");
        }
        sb.append("</table>");
        return sb.toString();
        }
        else if (result.getType() == QueryResult.Type.COUNT) 
        {
            return "RESULT: " + result.getTextResult();
        } 
        else if (result.getType() == QueryResult.Type.ERROR) 
        {
            return "<font color='red'>ERROR: " + result.getTextResult() + "</font>";
        } 
        else 
        {
            return "<font color='gray'>NO QUERY</font>";
        }
    }
    
}
