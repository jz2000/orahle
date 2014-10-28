package org.jz.orahle;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author sergey.zheznyakovskiy
 */
public class DbView extends SessionResolver {
    private String viewName;

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getViewBody() {
        try 
        {
            List<String> source = this.getDbSession().getViewBody(this.getDbOwner(), this.viewName);
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
            return "<font color='red'>Error reading view body</font>";
        }
    }

}
