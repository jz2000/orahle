package org.jz.orahle;

/**
 *
 * @author sergey.zheznyakovskiy
 */
public class DbTableColumn 
{

    private long id;
    private String name;
    private String comment;
    private String dataType;
    private int dataLength;
    private int dataPrecision;
    private int dataScale;
    private String nullable;
    private String dataDefault;

    public long getId() 
    {
        return id;
    }

    public void setId(long id) 
    {
        this.id = id;
    }
    
    public String getName() 
    {
        return name;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getComment() 
    {
        return comment;
    }

    public void setComment(String comment) 
    {
        this.comment = comment;
    }

    public String getDataType() 
    {
        return dataType;
    }

    public void setDataType(String dataType) 
    {
        this.dataType = dataType;
    }

    public int getDataLength() 
    {
        return dataLength;
    }

    public void setDataLength(int dataLength) 
    {
        this.dataLength = dataLength;
    }

    public int getDataPrecision() 
    {
        return dataPrecision;
    }

    public void setDataPrecision(int dataPrecision) 
    {
        this.dataPrecision = dataPrecision;
    }

    public int getDataScale() 
    {
        return dataScale;
    }

    public void setDataScale(int dataScale) 
    {
        this.dataScale = dataScale;
    }

    public String getNullable() 
    {
        return nullable;
    }

    public void setNullable(String nullable) 
    {
        this.nullable = nullable;
    }

    public String getDataDefault() 
    {
        return dataDefault;
    }

    public void setDataDefault(String dataDefault) 
    {
        this.dataDefault = dataDefault;
    }
    
    public String getSize() 
    {
        if (dataScale != 0) 
        {
            return dataPrecision + "," + dataScale;
        } 
        else if (dataPrecision != 0) 
        {
            return dataPrecision + "";
        } 
        else 
        {
            return dataLength + "";
        }
    }
    
}
