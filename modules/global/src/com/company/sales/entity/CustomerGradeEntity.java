package com.company.sales.entity;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import javax.validation.constraints.NotNull;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.chile.core.annotations.NamePattern;

@NamePattern("%s|grade")
@MetaClass(name = "sales$CustomerGradeEntity")
public class CustomerGradeEntity extends BaseUuidEntity {
    private static final long serialVersionUID = 8584376640850405048L;

    @NotNull
    @MetaProperty(mandatory = true)
    protected Integer grade;

    @MetaProperty
    protected Integer parent;

    public void setGrade(CustomerGrade grade) {
        this.grade = grade == null ? null : grade.getId();
    }

    public CustomerGrade getGrade() {
        return grade == null ? null : CustomerGrade.fromId(grade);
    }

    public void setParent(CustomerGrade parent) {
        this.parent = parent == null ? null : parent.getId();
    }

    public CustomerGrade getParent() {
        return parent == null ? null : CustomerGrade.fromId(parent);
    }


}