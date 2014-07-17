package org.jz.orahle;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author sergey.zheznyakovskiy - 2013-11-05
 */
public class DatabaseServiceDebugImpl implements DatabaseService
{
    
    private List<DbObjectType> knownObjectTypes = Arrays.asList(
            new DbObjectType("Query", "QUERY", "db-queries"),
            new DbObjectType("Table", "TABLE", "db-tables"),
            new DbObjectType("View", "VIEW", "db-views"),
            new DbObjectType("Sequence", "SEQUENCE", "db-sequences"),
            new DbObjectType("Procedure", "PROCEDURE", "db-procedures"),
            new DbObjectType("Function", "FUNCTION", "db-functions"),
            new DbObjectType("Package", "PACKAGE", "db-packages"),
            new DbObjectType("Package Body", "PACKAGE_BODY", "db-package-bodies"),
            new DbObjectType("Index", "INDEX", "db-indexes"),
            new DbObjectType("Constraint", "CONSTRAINT", "db-constraints"),
            new DbObjectType("Any", "%", "db-objects"));

    public DatabaseServiceDebugImpl()
    {
    }

    @Override
    public List<DbObjectType> searchObjectTypes(String keyword)
    {
        if ((keyword == null) || keyword.trim().isEmpty())
        {
            return new ArrayList<DbObjectType>(knownObjectTypes);
        } 
        else
        {
            List<DbObjectType> result = new ArrayList<DbObjectType>();
            for (DbObjectType objectType : knownObjectTypes) 
            {
                if (objectType.getTypeLabel().toLowerCase().contains(keyword.toLowerCase()))
                {
                    result.add(objectType);
                }
            }
            return result;
        }
    }
    
}
