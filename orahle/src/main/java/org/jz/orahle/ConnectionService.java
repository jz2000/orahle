package org.jz.orahle;

import java.util.List;

public interface ConnectionService 
{
    List<DbConnection> findAll();

    List<DbConnection> search(String keyword);
}
