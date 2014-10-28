package org.jz.orahle;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author sergey.zheznyakovskiy
 */
public class DbProcedure extends SessionResolver {
    private String procedureName;

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public String getProcedureBody() {
        try 
        {
            List<String> source = this.getDbSession().getProcedureBody(this.getDbOwner(), this.procedureName);
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
            return "<font color='red'>Error reading procedure body</font>";
        }
    }

}
