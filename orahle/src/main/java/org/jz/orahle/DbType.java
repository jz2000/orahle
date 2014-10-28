package org.jz.orahle;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author sergey.zheznyakovskiy
 */
public class DbType extends SessionResolver 
{
    private String typeName;

    public String getTypeName() 
    {
        return typeName;
    }

    public void setTypeName(String typeName) 
    {
        this.typeName = typeName;
    }

    public String getTypeDefinition() 
    {
        try 
        {
            List<String> source = this.getDbSession().getTypeDefinition(this.getDbOwner(), this.typeName);
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
            return "<font color='red'>Error reading type definition</font>";
        }
    }

    public String getTypeBody() 
    {
        try 
        {
            List<String> source = this.getDbSession().getTypeBody(this.getDbOwner(), this.typeName);
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
            return "<font color='red'>Error reading type body</font>";
        }
    }

}
