package com.linsh.common.entity;

import com.linsh.base.entity.IRemark;
import com.linsh.base.entity.IType;

import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2022/02/21
 *    desc   :
 * </pre>
 */
public class Type implements IType {

    private final IType parent;
    private final int level;
    private final String name;
    private final String content;
    private List<IRemark> remarks;
    private List<IType> subTypes;
    private final boolean isHidden;
    private final boolean isImportant;
    private final boolean isEncrypted;

    public Type(IType parent, int level, String name, String content, boolean isHidden, boolean isImportant, boolean isEncrypted) {
        this.parent = parent;
        this.level = level;
        this.name = name;
        this.content = content;
        this.isHidden = isHidden;
        this.isImportant = isImportant;
        this.isEncrypted = isEncrypted;
    }

    @Override
    public IType getParent() {
        return parent;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public List<IRemark> getRemarks() {
        return remarks;
    }

    @Override
    public List<IType> getSubTypes() {
        return subTypes;
    }

    @Override
    public boolean isEncrypted() {
        return isEncrypted;
    }

    @Override
    public boolean isHidden() {
        return isHidden;
    }

    @Override
    public boolean isImportant() {
        return isImportant;
    }

    public void setRemarks(List<IRemark> remarks) {
        this.remarks = remarks;
    }

    public void setSubTypes(List<IType> subTypes) {
        this.subTypes = subTypes;
    }
}
