package com.linsh.common.entity;

import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2022/02/21
 *    desc   :
 * </pre>
 */
public class Remark implements IRemark {

    private final int level;
    private final String content;
    private List<IRemark> subRemarks;
    private boolean isEncrypted;

    public Remark(int level, String content, boolean isEncrypted) {
        this.level = level;
        this.content = content;
        this.isEncrypted = isEncrypted;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public List<IRemark> getSubRemarks() {
        return subRemarks;
    }

    @Override
    public boolean isEncrypted() {
        return isEncrypted;
    }

    public void setSubRemarks(List<IRemark> subRemarks) {
        this.subRemarks = subRemarks;
    }
}
