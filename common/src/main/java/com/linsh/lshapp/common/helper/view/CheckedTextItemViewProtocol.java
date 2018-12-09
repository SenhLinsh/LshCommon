package com.linsh.lshapp.common.helper.view;

import com.linsh.protocol.ui.view.ViewProtocol;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2018/12/09
 *    desc   :
 * </pre>
 */
public interface CheckedTextItemViewProtocol extends ViewProtocol {

    void setChecked(boolean checked);

    boolean isChecked();

    void setText(CharSequence text);
}
