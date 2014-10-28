package org.jz.orahle;

import java.io.Serializable;



public class DbConnection implements Serializable
{
    private static final long serialVersionUID = 01L;

    private String user;
    private String alias;
    private String url;
    private String description;

    public DbConnection()
    {
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getAlias()
    {
        return alias;
    }

    public void setAlias(String alias)
    {
        this.alias = alias;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    @Override
    public String toString() {
        return alias;
    }
    
}
