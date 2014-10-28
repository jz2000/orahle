package org.jz.orahle;

/**
 *
 * @author sergey.zheznyakovskiy
 */
public class DbTableIndex 
{
 
    private String name;
    private String indexType;
    private String uniqueness;

    public String getName() 
    {
        return name;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getIndexType() 
    {
        return indexType;
    }

    public void setIndexType(String indexType) 
    {
        this.indexType = indexType;
    }

    public String getUniqueness() 
    {
        return uniqueness;
    }

    public void setUniqueness(String uniqueness) 
    {
        this.uniqueness = uniqueness;
    }
    
}
