package org.jz.orahle;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author sergey.zheznyakovskiy
 */
public class DbPackage extends SessionResolver 
{
    private String packageName;

    public String getPackageName() 
    {
        return packageName;
    }

    public void setPackageName(String packageName) 
    {
        this.packageName = packageName;
    }

    public String getPackageDefinition() 
    {
        try 
        {
            List<String> source = this.getDbSession().getPackageDefinition(this.getDbOwner(), this.packageName);
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
            return "<font color='red'>Error reading package definition</font>";
        }
    }

    public String getPackageBody() 
    {
        try 
        {
            List<String> source = this.getDbSession().getPackageBody(this.getDbOwner(), this.packageName);
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
            return "<font color='red'>Error reading package body</font>";
        }
    }

}
