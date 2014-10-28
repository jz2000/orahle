package org.jz.orahle;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author sergey.zheznyakovskiy
 */
public class DbFunction extends SessionResolver {
    private String functionName;

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getFunctionBody() {
        try 
        {
            List<String> source = this.getDbSession().getFunctionBody(this.getDbOwner(), this.functionName);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<pre>");
            for (String line : source) 
            {
                stringBuilder.append(line);
            }
            stringBuilder.append("</pre>");
            return stringBuilder.toString();
        } 
        catch(SQLException ex) 
        {
            return "<font color='red'>Error reading function body</font>";
        }
    }

}
