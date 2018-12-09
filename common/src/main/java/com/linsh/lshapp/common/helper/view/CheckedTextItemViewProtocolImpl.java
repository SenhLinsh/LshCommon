package com.linsh.lshapp.common.helper.view;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linsh.protocol.impl.ui.view.JsonLayoutInflater;
import com.linsh.utilseverywhere.ResourceUtils;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2018/12/09
 *    desc   :
 * </pre>
 */
public class CheckedTextItemViewProtocolImpl implements CheckedTextItemViewProtocol {

    private final LinearLayout view;
    private final TextView textView;
    private final CheckBox checkBox;

    public CheckedTextItemViewProtocolImpl(Context context) {
        view = (LinearLayout) JsonLayoutInflater.from(context)
                .inflate(ResourceUtils.getTextFromAssets("CheckedTextItem.info"), null);
        textView = (TextView) view.getChildAt(0);
        checkBox = (CheckBox) view.getChildAt(1);
    }


    @Override
    public void setChecked(boolean checked) {
        checkBox.setChecked(checked);
    }

    @Override
    public boolean isChecked() {
        return checkBox.isChecked();
    }

    @Override
    public void setText(CharSequence text) {
        textView.setText(text);
    }

    @Override
    public View getView() {
        return view;
    }
}
