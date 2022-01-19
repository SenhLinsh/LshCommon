package com.linsh.common.entity.db;

import com.linsh.utilseverywhere.StringUtils;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2022/01/19
 *    desc   :
 * </pre>
 */
public class RealmString /*extends RealmObject*/ {

    private String value;

    public RealmString() {
    }

    public RealmString(String value) {
        this.value = value;
    }

    public String getValue() {
        return StringUtils.nullToEmpty(value);
    }

    public void setValue(String value) {
        this.value = value;
    }
}
