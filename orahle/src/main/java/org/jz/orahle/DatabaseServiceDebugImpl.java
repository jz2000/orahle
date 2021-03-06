package org.jz.orahle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author sergey.zheznyakovskiy - 2013-11-05
 */
public class DatabaseServiceDebugImpl implements DatabaseService
{
    
    private List<DbObjectType> knownObjectTypes = Arrays.asList(
            new DbObjectType("Tables", "TABLE", "db-tables"),
            new DbObjectType("Views", "VIEW", "db-views"),
            new DbObjectType("Sequences", "SEQUENCE", "db-sequences"),
            new DbObjectType("Procedures", "PROCEDURE", "db-procedures"),
            new DbObjectType("Functions", "FUNCTION", "db-functions"),
            new DbObjectType("Triggers", "TRIGGER", "db-triggers"),
            new DbObjectType("Packages", "PACKAGE", "db-packages"),
            new DbObjectType("Package Bodies", "PACKAGE_BODY", "db-package-bodies"),
            new DbObjectType("Types", "TYPE", "db-types"),
            new DbObjectType("Type Bodies", "TYPE_BODY", "db-type-bodies"),
            new DbObjectType("Indexes", "INDEX", "db-indexes")
    );

    public DatabaseServiceDebugImpl()
    {
    }

    @Override
    public List<DbObjectType> searchObjectTypes(String keyword)
    {
        if ((keyword == null) || keyword.trim().isEmpty())
        {
            return new ArrayList<>(knownObjectTypes);
        } 
        else
        {
            List<DbObjectType> result = new ArrayList<>();
            for (DbObjectType objectType : knownObjectTypes) 
            {
                String locTypeLabel = objectType.getTypeLabel().toLowerCase(Locale.getDefault());
                String locKeyword = keyword.toLowerCase(Locale.getDefault());
                if (locTypeLabel.contains(locKeyword))
                {
                    result.add(objectType);
                }
            }
            return result;
        }
    }
    
}
