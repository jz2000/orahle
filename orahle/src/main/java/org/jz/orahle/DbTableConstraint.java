package org.jz.orahle;

/**
 *
 * @author sergey.zheznyakovskiy
 */
public class DbTableConstraint {

    private String name;
    private String constraintType;
    private String searchCondition;
    private String refOwner;
    private String refConstrantName;
    private String deleteRule;
    private String status;
    private String generated;
    private String idxOwner;
    private String idxName;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConstraintType() {
        return constraintType;
    }

    public void setConstraintType(String constraintType) {
        this.constraintType = constraintType;
    }

    public String getSearchCondition() {
        return searchCondition;
    }

    public void setSearchCondition(String searchCondition) {
        this.searchCondition = searchCondition;
    }

    public String getRefOwner() {
        return refOwner;
    }

    public void setRefOwner(String refOwner) {
        this.refOwner = refOwner;
    }

    public String getRefConstrantName() {
        return refConstrantName;
    }

    public void setRefConstrantName(String refConstrantName) {
        this.refConstrantName = refConstrantName;
    }

    public String getDeleteRule() {
        return deleteRule;
    }

    public void setDeleteRule(String deleteRule) {
        this.deleteRule = deleteRule;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGenerated() {
        return generated;
    }

    public void setGenerated(String generated) {
        this.generated = generated;
    }

    public String getIdxOwner() {
        return idxOwner;
    }

    public void setIdxOwner(String idxOwner) {
        this.idxOwner = idxOwner;
    }

    public String getIdxName() {
        return idxName;
    }

    public void setIdxName(String idxName) {
        this.idxName = idxName;
    }
    
}
