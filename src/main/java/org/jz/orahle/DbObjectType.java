package org.jz.orahle;



/**
 *
 * @author sergey.zheznyakovskiy - 2013-11-05
 */
public class DbObjectType 
{
    private String typeLabel;
    private String typeName;
    private String pageName;
    

    public DbObjectType(String typeLabel, String typeName, String pageName)
    {
        this.typeLabel = typeLabel;
        this.typeName = typeName;
        this.pageName = pageName;
    }
    
    public String getTypeLabel()
    {
        return typeLabel;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public String getPageName()
    {
        return pageName;
    }
}
