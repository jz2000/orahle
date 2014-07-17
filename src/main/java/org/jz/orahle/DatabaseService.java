package org.jz.orahle;



import java.util.List;

/**
 *
 * @author sergey.zheznyakovskiy - 2013-11-05
 */
public interface DatabaseService
{

    public List<DbObjectType> searchObjectTypes(String keyword);
    
}
