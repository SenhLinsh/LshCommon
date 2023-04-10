package com.linsh.common.entity;

import com.linsh.base.entity.IProperties;
import com.linsh.base.entity.IStatus;

import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2023/03/01
 *    desc   :
 * </pre>
 */
public class Status implements IStatus {

    private final IProperties header;
    private final IProperties template;
    private final List<IProperties> contents;

    public Status(IProperties header, IProperties template, List<IProperties> contents) {
        this.header = header;
        this.template = template;
        this.contents = contents;
    }

    @Override
    public IProperties getHeader() {
        return header;
    }

    @Override
    public IProperties getTemplate() {
        return template;
    }

    @Override
    public List<IProperties> getContents() {
        return contents;
    }
}
